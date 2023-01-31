package com.kdw.studyMeter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;

import com.kdw.studyMeter.study.frame.Frame2;
import com.kdw.studyMeter.study.service.StudyService;
import com.kdw.studyMeter.study.vo.StudyVo;
import com.kdw.studyMeter.todo.dao.service.TodoService;
import com.kdw.studyMeter.todo.frame.TodoCalendarFrame;
import com.kdw.studyMeter.todo.frame.TodoFrame;

public class Frame1 extends JFrame{
	
	private JFrame frame1;
	private TodoFrame frame2;
	private JFrame todoCalendarFrame;
	
	private JPanel panel1;
	private JPanel panel11;
	private JPanel panel12;
	private JPanel panel13;
	private JPanel panel14;
	
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	
	private JTextField textField1;
	private JTextField textField2;
	
	private JTextField textField3;
	private JTextField textField4;
	
	private JTextField textField5;
	private JTextField textField6;
	
	private JTextField textField7;
	private JTextField textField8;
	
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JButton button5;
	
	private DefaultListModel<String> model;
	private JList<String> list1;
	private JScrollPane scrollPane1;
	
	private Timer timer1;
	private int hour = 0, min = 0, second = 0;
	
	private int seq = 0;
	
	public Frame1(final StudyService studyService, final TodoService todoService) {
		this.setTitle("공부량 측정기");
		this.setSize(500, 380);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		try {
			panel1 = new JPanel();
			panel11 = new JPanel();
			label1 = new JLabel();
			label1.setText("공부제목");
			panel11.add(label1);
			
			textField1 = new JTextField(10);
			panel11.add(textField1);
			
			label2 = new JLabel();
			label2.setText("메모");
			panel11.add(label2);
			
			textField2 = new JTextField(10);
			panel11.add(textField2);
			
			button1 = new JButton();
			button1.setText("시작");
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(textField1.getText() != null && !textField1.getText().trim().equals("")) {
						StudyVo vo = new StudyVo();
						vo.setStudyNm(textField1.getText());
						vo.setMemo(textField2.getText());
						seq = studyService.insert(vo);
						button1.setEnabled(false);
						button2.setEnabled(true);
						textField1.setEnabled(false);
						textField2.setEnabled(false);
						
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
						textField1.requestFocus();
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
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					
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
			textField3 = new JTextField(2);
			textField3.setHorizontalAlignment(JTextField.CENTER);
			panel13.add(textField3);
			textField4 = new JTextField(2);
			textField4.setHorizontalAlignment(JTextField.CENTER);
			panel13.add(textField4);
			
			label3 = new JLabel();
			label3.setText(":");
			panel13.add(label3);

			textField5 = new JTextField(2);
			textField5.setHorizontalAlignment(JTextField.CENTER);
			panel13.add(textField5);
			textField6 = new JTextField(2);
			textField6.setHorizontalAlignment(JTextField.CENTER);
			panel13.add(textField6);
			
			label4 = new JLabel();
			label4.setText(":");
			panel13.add(label4);

			textField7 = new JTextField(2);
			textField7.setHorizontalAlignment(JTextField.CENTER);
			panel13.add(textField7);
			textField8 = new JTextField(2);
			textField8.setHorizontalAlignment(JTextField.CENTER);
			panel13.add(textField8);
			
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
						
						textField8.setText(String.valueOf(second % 10));
						textField7.setText(String.valueOf(second / 10));
	
						textField6.setText(String.valueOf(min % 10));
						textField5.setText(String.valueOf(min / 10));
	
						textField4.setText(String.valueOf(hour % 10));
						textField3.setText(String.valueOf(hour / 10));
						
						System.out.println(String.format("%02d:%02d:%02d", hour, min, second));
					
					}catch(Exception exc) {
						exc.printStackTrace();
					}
				}
			});

			panel1.add(panel13);
			
			panel14 = new JPanel();
			frame1 = new Frame2(studyService);
			button3 = new JButton("통계");
			button3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frame1.setVisible(true);
				}
			});
			panel14.add(button3);
			
			frame2 = new TodoFrame(todoService);
			button4 = new JButton("할일");
			button4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frame2.init();
					frame2.setVisible(true);
				}
			});
			panel14.add(button4);

			todoCalendarFrame = new TodoCalendarFrame();
			button5 = new JButton("캘린더");
			button5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					todoCalendarFrame.setVisible(true);
				}
			});
			panel14.add(button5);
			
			panel1.add(panel14);
			
			this.add(panel1, BorderLayout.CENTER);
			
			this.setVisible(false);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
