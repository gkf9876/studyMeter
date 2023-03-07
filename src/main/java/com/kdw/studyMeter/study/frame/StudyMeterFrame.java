package com.kdw.studyMeter.study.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;

import com.kdw.studyMeter.calendar.frame.CalendarFrame;
import com.kdw.studyMeter.calendar.service.CalendarService;
import com.kdw.studyMeter.file.service.FileService;
import com.kdw.studyMeter.study.service.StudyListService;
import com.kdw.studyMeter.study.service.StudyService;
import com.kdw.studyMeter.study.vo.StudyListVo;
import com.kdw.studyMeter.study.vo.StudyVo;
import com.kdw.studyMeter.todo.dao.service.TodoDetailService;
import com.kdw.studyMeter.todo.dao.service.TodoService;
import com.kdw.studyMeter.todo.frame.TodoFrame;

public class StudyMeterFrame extends JFrame{
	
	private JFrame frame1;
	private TodoFrame todoFrame;
	private CalendarFrame calendarFrame;
	private StudyListFrame studyListFrame;
	
	private JMenuBar menuBar;
	private JMenu menu1;
	private JMenuItem menuItem1;
	private JMenuItem menuItem2;
	private JMenuItem menuItem3;
	private JMenuItem menuItem4;
	
	private JPanel panel1;
	private JPanel panel10;
	private JPanel panel11;
	private JPanel panel12;
	private JPanel panel13;
	
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	
	private JTextField textField1;
	
	private JComboBox comboBox1;
	private ComboBoxModel comboBoxModel;
	
	private JButton button1;
	private JButton button2;
	
	private DefaultListModel<String> model;
	private JList<String> list1;
	private JScrollPane scrollPane1;
	
	private Timer timer1;
	private int hour = 0, min = 0, second = 0;
	
	private int seq = 0;
	
	private StudyListService studyListService;
	
	public StudyMeterFrame(final StudyService studyService, final TodoService todoService, final TodoDetailService todoDetailService
			, final CalendarService calendarService, final FileService fileService, final StudyListService studyListService) {
		this.setTitle("공부량 측정기");
		this.setSize(500, 380);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.studyListService = studyListService;
		
		menuBar = new JMenuBar();
		menu1 = new JMenu("메뉴");
		menuBar.add(menu1);
		
		menuItem1 = new JMenuItem("통계");
		frame1 = new StudyChartFrame(studyService);
		menuItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame1.setVisible(true);
			}
		});
		menu1.add(menuItem1);

		todoFrame = new TodoFrame(todoService, todoDetailService, fileService);
		menuItem2 = new JMenuItem("할일");
		menuItem2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				todoFrame.init();
				todoFrame.setVisible(true);
			}
			
		});
		menu1.add(menuItem2);

		calendarFrame = new CalendarFrame(calendarService);
		menuItem3 = new JMenuItem("캘린더");
		menuItem3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				calendarFrame.setVisible(true);
			}
			
		});
		menu1.add(menuItem3);
		
		studyListFrame = new StudyListFrame(studyListService, fileService);
		menuItem4 = new JMenuItem("공부 목록");
		menuItem4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				studyListFrame.setVisible(true);
			}
		});
		menu1.add(menuItem4);
		
		this.setJMenuBar(menuBar);
		
		try {
			panel1 = new JPanel();
			
			panel10 = new JPanel();
			label1 = new JLabel();
			label1.setText("공부제목");
			panel10.add(label1);
			
			comboBox1 = new JComboBox();
			comboBoxModel = new ComboBoxModel();
			comboBox1.setModel(comboBoxModel);
			comboBox1.setSelectedIndex(comboBoxModel.getSize() - 1);
			panel10.add(comboBox1);
			panel1.add(panel10);

			panel11 = new JPanel();
			label2 = new JLabel();
			label2.setText("메모");
			panel11.add(label2);
			
			textField1 = new JTextField(24);
			panel11.add(textField1);
			
			button1 = new JButton();
			button1.setText("시작");
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(comboBox1.getSelectedItem().toString() != null && !comboBox1.getSelectedItem().toString().trim().equals("")) {
						StudyVo vo = new StudyVo();
						vo.setStudySeq(comboBoxModel.getSeq(comboBox1.getSelectedItem().toString()));
						vo.setMemo(textField1.getText());
						seq = studyService.insert(vo);
						button1.setEnabled(false);
						button2.setEnabled(true);
						comboBox1.setEnabled(false);
						textField1.setEnabled(false);
						
						vo = new StudyVo();
						vo.setSeq(seq);
						vo = studyService.selectOne(vo);

						model.addElement("공부시작 : " + vo.getStudyNm());
						model.addElement("시작시간 : " + vo.getStartDate());
						
						second = 0;
						min = 0;
						hour = 0;
						timer1.start();
					}else {
						JOptionPane.showMessageDialog(null, "공부 제목을 입력하세요.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
						comboBox1.requestFocus();
					}
				}
			});
			panel11.add(button1);
			
			button2 = new JButton();
			button2.setText("종료");
			button2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					StudyVo vo = new StudyVo();
					vo.setSeq(seq);
					studyService.update(vo);
					button1.setEnabled(true);
					button2.setEnabled(false);
					comboBox1.setEnabled(true);
					textField1.setEnabled(true);
					
					vo = new StudyVo();
					vo.setSeq(seq);
					vo = studyService.selectOne(vo);
					
					model.addElement("종료시간 : " + vo.getEndDate());
					model.addElement("공부시간 : " + hour + "시간 " + min + "분 " + second + "초");
					timer1.stop();
				}
			});
			button2.setEnabled(false);
			panel11.add(button2);
			panel1.add(panel11);

			panel12 = new JPanel();
			model = new DefaultListModel<String>();
			list1 = new JList<String>(model);
			scrollPane1 = new JScrollPane(list1);
			scrollPane1.setPreferredSize(new Dimension(450, 120));
			panel12.add(scrollPane1);
			
			panel1.add(panel12, BorderLayout.CENTER);

			panel13 = new JPanel();
			label3 = new JLabel();
			label3.setFont(new Font("Serif", Font.PLAIN, 50));
			label3.setHorizontalAlignment(JTextField.CENTER);
			panel13.add(label3);
			
			timer1 = new Timer(1000, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if(second >= 60) {
							StudyVo vo = new StudyVo();
							vo.setSeq(seq);
							studyService.update(vo);
							vo = studyService.selectOne(vo);
							
							SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							Date startDate = dtFormat.parse(vo.getStartDate());
							Date endDate = dtFormat.parse(vo.getEndDate());
							long diffSecond = (endDate.getTime() - startDate.getTime()) / 1000;
							hour = (int)(diffSecond / 3600);
							min = (int)((diffSecond - hour * 3600) / 60);
							second = (int)((diffSecond - hour * 3600) % 60);
						}else {
							if(second < 59)
								second++;
							else {
								StudyVo vo = new StudyVo();
								vo.setSeq(seq);
								studyService.update(vo);
								vo = studyService.selectOne(vo);
								
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								Date startDate = dtFormat.parse(vo.getStartDate());
								Date endDate = dtFormat.parse(vo.getEndDate());
								long diffSecond = (endDate.getTime() - startDate.getTime()) / 1000;
								hour = (int)(diffSecond / 3600);
								min = (int)((diffSecond - hour * 3600) / 60);
								second = (int)((diffSecond - hour * 3600) % 60);
							}
						}
						
						String timeValue = String.format("%02d:%02d:%02d", hour, min, second);
						label3.setText(timeValue);
						System.out.println(timeValue);
					
					}catch(Exception exc) {
						exc.printStackTrace();
					}
				}
			});

			panel1.add(panel13);
			
			this.add(panel1, BorderLayout.CENTER);
			
			this.setVisible(false);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private class ComboBoxModel extends DefaultComboBoxModel<Object>{
		
		private Map<String, Integer> data;
		private List<StudyListVo> studyList;
		private String[] dataArr;
		
		public ComboBoxModel() {
			data = new HashMap<String, Integer>();
			StudyListVo studyListVo = new StudyListVo();
			studyList = studyListService.select(studyListVo);
			dataArr = new String[studyList.size()];
			for(int i=0; i<studyList.size(); i++) {
				StudyListVo vo = studyList.get(i);
				dataArr[i] = vo.getStudyNm();
				data.put(vo.getStudyNm(), vo.getSeq());
			}
		}
		
		@Override
		public int getSize() {
			return dataArr.length;
		}
		
		@Override
		public Object getElementAt(int index) {
			return dataArr[index];
		}
		
		public int getSeq(String studyNm) {
			return data.get(studyNm);
		}
	}
}
