package com.kdw.studyMeter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.kdw.studyMeter.todo.dao.service.TodoService;
import com.kdw.studyMeter.todo.vo.TodoVo;

public class Frame3 extends JFrame{
	
	private JPanel panel1;
	private JPanel panel11;
	private JPanel panel12;
	private JPanel panel;
	
	private List<JCheckBox> checkBoxList;
	private JCheckBox checkBox;
	
	private List<JTextField> textFieldList;
	private JTextField textField;
	
	private List<TodoVo> itmList;
	
	private JButton button1;
	private JButton button2;
	
	private JButton button;
	
	private JScrollPane scrollPane1;
	
	private TodoService todoService;
	
	private int cnt = 0;
	
	public Frame3(final TodoService todoService) {
		this.setTitle("할일 목록");
		this.setSize(540, 490);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		//this.setResizable(false);
		
		this.todoService = todoService;
		
		panel1 = new JPanel();
		panel11 = new JPanel();
		panel11.setLayout(new BoxLayout(panel11, BoxLayout.Y_AXIS));
		
		//할일 데이터 가져오기.
		checkBoxList = new ArrayList<JCheckBox>();
		textFieldList = new ArrayList<JTextField>();
		itmList = todoService.select();

		if(itmList != null && itmList.size() > 0) {
			for(TodoVo vo : itmList) {
				panel = new JPanel();
				checkBox = new JCheckBox();
				checkBox.setSelected(("Y".equals(vo.getCheckYn()))? true : false);
				panel.add(checkBox);
				checkBoxList.add(checkBox);

				textField = new JTextField(38);
				textField.setText(vo.getSubject());
				panel.add(textField);
				textFieldList.add(textField);
				
				button = new JButton("+");
				button.setBorder(BorderFactory.createLineBorder(Color.black));
				button.setPreferredSize(new Dimension(16, 16));
				panel.add(button);
				
				panel11.add(panel);
			}
		}else {
			panel = new JPanel();
			itmList = new ArrayList<TodoVo>();
			itmList.add(new TodoVo());
			
			checkBox = new JCheckBox();
			panel.add(checkBox);
			checkBoxList.add(checkBox);

			textField = new JTextField(38);
			panel.add(textField);
			textFieldList.add(textField);
			
			button = new JButton("+");
			button.setBorder(BorderFactory.createLineBorder(Color.black));
			button.setPreferredSize(new Dimension(16, 16));
			panel.add(button);
			
			panel11.add(panel);
		}

		scrollPane1 = new JScrollPane(panel11);
		scrollPane1.setPreferredSize(new Dimension(500, 400));
		
		panel1.add(scrollPane1);

		panel12 = new JPanel();
		button1 = new JButton("저장");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0; i<textFieldList.size(); i++) {
					if(textFieldList.get(i).getText() != null && !textFieldList.get(i).getText().trim().equals("")){
						TodoVo vo = itmList.get(i);
						vo.setSubject(textFieldList.get(i).getText());
						vo.setCheckYn((checkBoxList.get(i).isSelected())? "Y" : "N");
						
						if(vo.getSeq() == -1)
							todoService.insert(vo);
						else
							todoService.update(vo);
					}
				}
				
				//할일 목록 모두 지우고 다시 그리기
				panel11.removeAll();
				checkBoxList.clear();
				textFieldList.clear();
				
				panel11.revalidate();
				panel11.repaint();
				itmList = todoService.select();
				
				if(itmList != null && itmList.size() > 0) {
					for(TodoVo vo : itmList) {
						panel = new JPanel();
						checkBox = new JCheckBox();
						checkBox.setSelected(("Y".equals(vo.getCheckYn()))? true : false);
						panel.add(checkBox);
						checkBoxList.add(checkBox);

						textField = new JTextField(38);
						textField.setText(vo.getSubject());
						panel.add(textField);
						textFieldList.add(textField);
						
						button = new JButton("+");
						button.setBorder(BorderFactory.createLineBorder(Color.black));
						button.setPreferredSize(new Dimension(16, 16));
						panel.add(button);
						
						panel11.add(panel);
					}
				}else {
					panel = new JPanel();
					itmList = new ArrayList<TodoVo>();
					itmList.add(new TodoVo());
					
					checkBox = new JCheckBox();
					panel.add(checkBox);
					checkBoxList.add(checkBox);

					textField = new JTextField(38);
					panel.add(textField);
					textFieldList.add(textField);
					
					button = new JButton("+");
					button.setBorder(BorderFactory.createLineBorder(Color.black));
					button.setPreferredSize(new Dimension(16, 16));
					panel.add(button);
					
					panel11.add(panel);
				}
			}
		});
		panel12.add(button1);
		
		button2 = new JButton("추가");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel = new JPanel();
				
				TodoVo vo = new TodoVo();
				vo.setSeq(-1);
				itmList.add(vo);
				
				checkBox = new JCheckBox();
				panel.add(checkBox);
				checkBoxList.add(checkBox);

				textField = new JTextField(38);
				panel.add(textField);
				textFieldList.add(textField);
				
				button = new JButton("+");
				button.setBorder(BorderFactory.createLineBorder(Color.black));
				button.setPreferredSize(new Dimension(16, 16));
				panel.add(button);
				
				panel11.add(panel);
				
				panel11.revalidate();
				panel11.repaint();
			}
		});
		panel12.add(button2);
		panel1.add(panel12);
		
		this.add(panel1);
	}
}
