package com.kdw.studyMeter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

import com.kdw.studyMeter.todo.dao.service.TodoService;
import com.kdw.studyMeter.todo.vo.TodoVo;

public class Frame3 extends JFrame{
	
	private JPanel panel1;
	private JPanel panel11;
	private JPanel panel12;
	
	private List<JCheckBox> checkBoxList;
	
	private List<JTextField> textFieldList;
	
	private List<TodoVo> itmList;
	
	private JButton button1;
	private JButton button2;
	
	private JScrollPane scrollPane1;
	
	private TodoService todoService;
	
	//빈 항목 추가하기
	private JPanel addColumn(int parentSeq, final int level) {
		JPanel panel = new JPanel();
		TodoVo vo = new TodoVo();
		vo.setSeq(-1);
		vo.setParentSeq(parentSeq);
		vo.setLevel(level);
		itmList.add(vo);
		
		JCheckBox checkBox = new JCheckBox();
		panel.add(checkBox);
		checkBoxList.add(checkBox);

		JTextField textField = new JTextField(40 - level * 2);
		panel.add(textField);
		textFieldList.add(textField);
		
		JButton button = new JButton("+");
		button.setBorder(BorderFactory.createLineBorder(Color.black));
		button.setPreferredSize(new Dimension(16, 16));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Component[] comp = panel11.getComponents();
				
				for(int i=0; i<comp.length; i++) {
					JPanel itm = (JPanel)comp[i];
					JButton button = (JButton)itm.getComponents()[2];
					
					if(e.getSource() == button) {
						JPanel panel = new JPanel();
						JCheckBox checkBox = new JCheckBox();
						panel.add(checkBox);

						JTextField textField = new JTextField(40 - (level + 1) * 2);
						panel.add(textField);
						
						button = new JButton("+");
						button.setBorder(BorderFactory.createLineBorder(Color.black));
						button.setPreferredSize(new Dimension(16, 16));
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								
							}
						});
						panel.add(button);
						
						panel11.add(panel, i + 1);
						
						checkBoxList.add(i + 1, checkBox);
						textFieldList.add(i + 1, textField);
						TodoVo vo = new TodoVo();
						vo.setSeq(-1);
						vo.setParentSeq(itmList.get(i).getSeq());
						vo.setLevel(itmList.get(i).getLevel() + 1);
						itmList.add(i + 1, vo);
						break;
					}
				}
				panel11.revalidate();
				panel11.repaint();
			}
		});
		panel.add(button);
		
		return panel;
	}
	
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
				JPanel panel = new JPanel();
				JCheckBox checkBox = new JCheckBox();
				checkBox.setSelected(("Y".equals(vo.getCheckYn()))? true : false);
				panel.add(checkBox);
				checkBoxList.add(checkBox);

				JTextField textField = new JTextField(40 - vo.getLevel() * 2);
				textField.setText(vo.getSubject());
				panel.add(textField);
				textFieldList.add(textField);
				
				JButton button = new JButton("+");
				button.setBorder(BorderFactory.createLineBorder(Color.black));
				button.setPreferredSize(new Dimension(16, 16));
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Component[] comp = panel11.getComponents();
						
						for(int i=0; i<comp.length; i++) {
							JPanel itm = (JPanel)comp[i];
							JButton button = (JButton)itm.getComponents()[2];
							
							if(e.getSource() == button) {
								JPanel panel = new JPanel();
								JCheckBox checkBox = new JCheckBox();
								panel.add(checkBox);

								JTextField textField = new JTextField(40 - (itmList.get(i).getLevel() + 1) * 2);
								panel.add(textField);
								
								button = new JButton("+");
								button.setBorder(BorderFactory.createLineBorder(Color.black));
								button.setPreferredSize(new Dimension(16, 16));
								button.addActionListener(new ActionListener() {

									public void actionPerformed(ActionEvent e) {
										// TODO Auto-generated method stub
										
									}
									
								});
								panel.add(button);
								
								panel11.add(panel, i + 1);
								
								checkBoxList.add(i + 1, checkBox);
								textFieldList.add(i + 1, textField);
								TodoVo vo = new TodoVo();
								vo.setSeq(-1);
								vo.setParentSeq(itmList.get(i).getSeq());
								vo.setLevel(itmList.get(i).getLevel() + 1);
								itmList.add(i + 1, vo);
								break;
							}
						}
						panel11.revalidate();
						panel11.repaint();
					}
				});
				panel.add(button);
				
				panel11.add(panel);
			}
		}else {
			panel11.add(addColumn(-1, 1));
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
						vo.setOdr(i);
						
						//신규항목은 insert, 기존항목은 update
						if(vo.getSeq() == -1) {
							//하위항목이면 상위항목의 시퀀스값을 가져온다.
							if(vo.getLevel() > 1 && vo.getParentSeq() == -1) {
								for(int j=i-1; j>0; j--) {
									TodoVo imsiVo = itmList.get(j);
									if(imsiVo.getLevel() < vo.getLevel()) {
										vo.setParentSeq(imsiVo.getSeq());
										break;
									}
								}
							}
							int seq = todoService.insert(vo);
							vo.setSeq(seq);
						}else
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
						JPanel panel = new JPanel();
						JCheckBox checkBox = new JCheckBox();
						checkBox.setSelected(("Y".equals(vo.getCheckYn()))? true : false);
						panel.add(checkBox);
						checkBoxList.add(checkBox);

						JTextField textField = new JTextField(40 - vo.getLevel() * 2);
						textField.setText(vo.getSubject());
						panel.add(textField);
						textFieldList.add(textField);
						
						JButton button = new JButton("+");
						button.setBorder(BorderFactory.createLineBorder(Color.black));
						button.setPreferredSize(new Dimension(16, 16));
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Component[] comp = panel11.getComponents();
								
								for(int i=0; i<comp.length; i++) {
									JPanel itm = (JPanel)comp[i];
									JButton button = (JButton)itm.getComponents()[2];
									
									if(e.getSource() == button) {
										JPanel panel = new JPanel();
										JCheckBox checkBox = new JCheckBox();
										panel.add(checkBox);

										JTextField textField = new JTextField(40 - (itmList.get(i).getLevel() + 1) * 2);
										panel.add(textField);
										
										button = new JButton("+");
										button.setBorder(BorderFactory.createLineBorder(Color.black));
										button.setPreferredSize(new Dimension(16, 16));
										button.addActionListener(new ActionListener() {

											public void actionPerformed(ActionEvent e) {
												// TODO Auto-generated method stub
												
											}
											
										});
										panel.add(button);
										
										panel11.add(panel, i + 1);
										
										checkBoxList.add(i + 1, checkBox);
										textFieldList.add(i + 1, textField);
										TodoVo vo = new TodoVo();
										vo.setSeq(-1);
										vo.setParentSeq(itmList.get(i).getSeq());
										vo.setLevel(itmList.get(i).getLevel() + 1);
										itmList.add(i + 1, vo);
										break;
									}
								}
								panel11.revalidate();
								panel11.repaint();
							}
						});
						panel.add(button);
						
						panel11.add(panel);
					}
				}else
					panel11.add(addColumn(-1, 0));
			}
		});
		panel12.add(button1);
		
		button2 = new JButton("추가");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel11.add(addColumn(-1, 1));
				panel11.revalidate();
				panel11.repaint();
			}
		});
		panel12.add(button2);
		panel1.add(panel12);
		
		this.add(panel1);
	}
}
