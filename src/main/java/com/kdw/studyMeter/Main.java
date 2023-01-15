package com.kdw.studyMeter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import com.kdw.studyMeter.study.service.StudyService;
import com.kdw.studyMeter.study.vo.StudyVo;

public class Main {

	private static StudyService service;
	
	private static JFrame frame;
	private static JPanel panel1;
	private static JPanel panel2;
	
	private static JLabel label1;
	private static JLabel label2;
	private static JLabel label3;
	private static JLabel label4;
	
	private static JTextField textField1;
	private static JTextField textField2;
	
	private static JTextField textField3;
	private static JTextField textField4;
	
	private static JTextField textField5;
	private static JTextField textField6;
	
	private static JTextField textField7;
	private static JTextField textField8;
	
	private static JButton button1;
	private static JButton button2;
	
	private static DefaultListModel<String> model;
	private static JList<String> list1;
	private static JScrollPane scrollPane1;
	
	private static Timer timer1;
	private static int hour = 0, min = 0, second = 0;
	
	private static int seq = 0;
	
	public static void main(String[] args) {
		frame = new JFrame();
		frame.setTitle("공부량 측정기");
		frame.setSize(500, 380);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		try {
			service = new StudyService();

			panel1 = new JPanel();
			label1 = new JLabel();
			label1.setText("공부제목");
			panel1.add(label1);
			
			textField1 = new JTextField(10);
			panel1.add(textField1);
			
			label2 = new JLabel();
			label2.setText("메모");
			panel1.add(label2);
			
			textField2 = new JTextField(10);
			panel1.add(textField2);
			
			button1 = new JButton();
			button1.setText("시작");
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(textField1.getText() != null && !textField1.getText().trim().equals("")) {
						StudyVo vo = new StudyVo();
						vo.setStudyNm(textField1.getText());
						vo.setMemo(textField2.getText());
						seq = service.insert(vo);
						button1.setEnabled(false);
						button2.setEnabled(true);
						textField1.setEnabled(false);
						textField2.setEnabled(false);
						
						vo = new StudyVo();
						vo.setSeq(seq);
						vo = service.selectOne(vo);

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
			panel1.add(button1);
			
			button2 = new JButton();
			button2.setText("종료");
			button2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					StudyVo vo = new StudyVo();
					vo.setSeq(seq);
					service.update(vo);
					button1.setEnabled(true);
					button2.setEnabled(false);
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					
					vo = new StudyVo();
					vo.setSeq(seq);
					vo = service.selectOne(vo);
					
					model.addElement("종료시간 : " + vo.getEndDate());
					model.addElement("공부시간 : " + hour + "시간 " + min + "분 " + second + "초");
					timer1.stop();
				}
			});
			button2.setEnabled(false);
			panel1.add(button2);
			
			model = new DefaultListModel<String>();
			list1 = new JList<String>(model);
			scrollPane1 = new JScrollPane(list1);
			scrollPane1.setPreferredSize(new Dimension(450, 250));
			panel1.add(scrollPane1);
			
			frame.add(panel1, BorderLayout.CENTER);
			
			panel2 = new JPanel();
			textField3 = new JTextField(2);
			textField3.setHorizontalAlignment(JTextField.CENTER);
			panel2.add(textField3);
			textField4 = new JTextField(2);
			textField4.setHorizontalAlignment(JTextField.CENTER);
			panel2.add(textField4);
			
			label3 = new JLabel();
			label3.setText(":");
			panel2.add(label3);

			textField5 = new JTextField(2);
			textField5.setHorizontalAlignment(JTextField.CENTER);
			panel2.add(textField5);
			textField6 = new JTextField(2);
			textField6.setHorizontalAlignment(JTextField.CENTER);
			panel2.add(textField6);
			
			label4 = new JLabel();
			label4.setText(":");
			panel2.add(label4);

			textField7 = new JTextField(2);
			textField7.setHorizontalAlignment(JTextField.CENTER);
			panel2.add(textField7);
			textField8 = new JTextField(2);
			textField8.setHorizontalAlignment(JTextField.CENTER);
			panel2.add(textField8);
			
			timer1 = new Timer(1000, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(second < 59)
						second++;
					else {
						second = 0;
						
						if(min < 59)
							min++;
						else {
							min = 0;
							hour++;
						}
					}
					
					textField8.setText(String.valueOf(second % 10));
					textField7.setText(String.valueOf(second / 10));

					textField6.setText(String.valueOf(min % 10));
					textField5.setText(String.valueOf(min / 10));

					textField4.setText(String.valueOf(hour % 10));
					textField3.setText(String.valueOf(hour / 10));
					
					System.out.println(String.format("%02d:%02d:%02d", hour, min, second));
				}
			});
			
			frame.add(panel2, BorderLayout.SOUTH);
			
			frame.setVisible(true);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
