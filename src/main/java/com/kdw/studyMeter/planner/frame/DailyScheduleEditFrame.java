package com.kdw.studyMeter.planner.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.kdw.studyMeter.planner.service.DailyScheduleService;
import com.kdw.studyMeter.planner.vo.DailyScheduleVo;

public class DailyScheduleEditFrame extends JFrame{

	private JPanel panel1;
	private JPanel panel2;
	
	private JTextField textField1;
	private JScrollPane scrollPane;
	private JTextArea textArea1;
	private JButton button1;
	
	private DailyScheduleVo dailyScheduleVo;
	
	private DailyScheduleService dailyScheduleService;
	
	public DailyScheduleEditFrame(final DailyScheduleVo vo, final DailyScheduleService dailyScheduleService, final DailyScheduleDetailFrame owner) {
		this.setTitle(vo.getName());
		this.setSize(400, 350);
		this.setLayout(new BorderLayout());
		//this.setResizable(false);
		
		this.dailyScheduleVo = vo;
		
		this.dailyScheduleService = dailyScheduleService;

		panel1 = new JPanel();
		textField1 = new JTextField(25);
		textField1.setText(vo.getName());
		panel1.add(textField1);

		textArea1 = new JTextArea(vo.getContents());
		textArea1.setLineWrap(true);
		scrollPane = new JScrollPane(textArea1);
		scrollPane.setPreferredSize(new Dimension(300, 200));
		panel1.add(scrollPane);
		
		this.add(panel1, BorderLayout.CENTER);

		panel2 = new JPanel();
		button1 = new JButton("저장");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(vo != null) {
					vo.setName(textField1.getText());
					vo.setContents(textArea1.getText());
					
					//기존항목은 update, 신규항목은 insert
					if(vo.getSeq() != -1) {
						dailyScheduleService.update(vo);
					}else {
						dailyScheduleService.insert(vo);
					}
					owner.init();
				}
				dispose();
			}
		});
		panel2.add(button1);
		
		this.add(panel2, BorderLayout.SOUTH);
	}
}
