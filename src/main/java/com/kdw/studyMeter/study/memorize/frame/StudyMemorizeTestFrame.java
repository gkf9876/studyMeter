package com.kdw.studyMeter.study.memorize.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.kdw.studyMeter.study.memorize.service.StudyMemorizeDetailService;
import com.kdw.studyMeter.study.memorize.vo.StudyMemorizeDetailVo;

public class StudyMemorizeTestFrame extends JFrame{
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel21;
	private JPanel panel22;
	private JPanel panel3;
	
	private JTextArea textArea1;
	private JTextArea textArea2;
	
	private JScrollPane scrollPane1;
	private JScrollPane scrollPane2;
	
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	
	private JLabel label1;
	private JLabel label2;
	
	private int parentSeq;
	
	private StudyMemorizeDetailService studyMemorizeDetailService;
	private List<StudyMemorizeDetailVo> itemList;
	
	private int stage = 0;
	
	public StudyMemorizeTestFrame(int parentSeq, String title, StudyMemorizeDetailService studyMemorizeDetailService) {
		this.setTitle(title);
		this.setSize(700, 600);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		
		this.parentSeq = parentSeq;
		this.studyMemorizeDetailService = studyMemorizeDetailService;
		
		panel1 = new JPanel();
		label1 = new JLabel("왼쪽에 암기 내용을 입력하고 오른쪽에서 확인하세요!");
		panel1.add(label1);
		this.add(panel1, BorderLayout.NORTH);
		
		panel2 = new JPanel();

		panel21 = new JPanel();
		textArea1 = new JTextArea();
		textArea1.setLineWrap(true);
		scrollPane1 = new JScrollPane(textArea1);
		scrollPane1.setPreferredSize(new Dimension(300, 450));
		panel21.add(scrollPane1);

		textArea2 = new JTextArea();
		textArea2.setLineWrap(true);
		scrollPane2 = new JScrollPane(textArea2);
		scrollPane2.setPreferredSize(new Dimension(300, 450));
		panel21.add(scrollPane2);
		panel2.add(panel21);
		
		panel22 = new JPanel();
		panel22.setLayout(new FlowLayout(FlowLayout.RIGHT));
		button3 = new JButton("확인하기");
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(itemList != null && itemList.size() > 0 && itemList.get(stage) != null) {
					textArea2.setText(itemList.get(stage).getContents());
				}
			}
		});
		panel22.add(button3);
		
		button4 = new JButton("지우기");
		button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea1.setText("");
				textArea2.setText("");
			}
		});
		panel22.add(button4);
		
		panel2.add(panel22);
		
		this.add(panel2, BorderLayout.CENTER);
		
		panel3 = new JPanel();
		button1 = new JButton("<");
		panel3.add(button1);
		
		button2 = new JButton(">");
		panel3.add(button2);
		
		label2 = new JLabel("");
		panel3.add(label2);
		
		this.add(panel3, BorderLayout.SOUTH);
		
		//init();
	}
	
	public void init() {
		textArea1.setText("");
		textArea2.setText("");
		stage = 0;
		
		StudyMemorizeDetailVo vo = new StudyMemorizeDetailVo();
		vo.setSeq(-1);
		vo.setParentSeq(parentSeq);
		itemList = studyMemorizeDetailService.selectList(vo);
		
		label2.setText("(" + (stage + 1) + "/" + itemList.size() + ")");
	}
}
