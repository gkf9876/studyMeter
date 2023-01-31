package com.kdw.studyMeter.todo.frame;

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

public class TodoFrame extends JFrame{
	
	private JPanel panel1;
	private JPanel panel11;
	private JPanel panel2;
	
	private JScrollPane scrollPane1;
	
	private JButton button1;
	private JButton button2;
	
	//할일 목록
	private List<TodoVo> todoList;
	
	private TodoService todoService;
	
	private int cnt = 0;
	
	public TodoFrame(final TodoService todoService) {
		this.setTitle("할일 목록");
		this.setSize(540, 490);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		//this.setResizable(false);
		
		this.todoService = todoService;
		
		//할일 목록 출력
		panel1 = new JPanel();
		panel11 = new JPanel();
		panel11.setLayout(new BoxLayout(panel11, BoxLayout.Y_AXIS));
		
		//할일 목록 초기화
		init();
		
		//스크롤 생성
		scrollPane1 = new JScrollPane(panel11);
		scrollPane1.setPreferredSize(new Dimension(550, 400));
		
		panel1.add(scrollPane1);
		this.add(panel1, BorderLayout.CENTER);
		
		//저장, 삭제, 추가 버튼 구역
		panel2 = new JPanel();
		button1 = new JButton("저장");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//화면데이터 할일목록에 저장.
				Component[] comps = panel11.getComponents();
				for(int i=0; i<comps.length; i++) {
					JPanel panel = (JPanel)comps[i];
					JCheckBox checkBox = (JCheckBox)panel.getComponents()[0];
					JTextField textField = (JTextField)panel.getComponents()[1];
					JButton button1 = (JButton)panel.getComponents()[2];
					
					TodoVo vo = todoList.get(i);
					vo.setCheckYn((checkBox.isSelected())? "Y" : "N");
					vo.setSubject(textField.getText());
					vo.setOdr(i);
					
					//현재 항목이 이전 항목의 하위항목일때
					for(int j=i-1; j>=0; j--) {
						if(todoList.get(j).getLevel() < vo.getLevel()) {
							vo.setParentSeq(todoList.get(j).getSeq());
							break;
						}
					}
					
					//기존항목은 update, 신규항목은 insert
					if(vo.getSeq() != -1) {
						todoService.update(vo);
					}else {
						todoService.insert(vo);
					}
				}
				
				//할일 목록으로 데이터 업데이트
				init();
			}
		});
		panel2.add(button1);
		
		button2 = new JButton("추가");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TodoVo vo = new TodoVo();
				vo.setSeq(-1);
				vo.setParentSeq(-1);
				vo.setLevel(0);
				todoList.add(vo);
				panel11.add(new TodoItem(vo), panel11.getComponentCount());
				panel11.revalidate();
				panel11.repaint();
			}
		});
		panel2.add(button2);
		
		this.add(panel2, BorderLayout.SOUTH);
	}
	
	//할일 목록 초기화
	public void init() {
		panel11.removeAll();

		todoList = todoService.select();
		
		if(todoList.size() > 0) {
			for(TodoVo vo : todoList) {
				TodoItem itm = new TodoItem(vo);
				panel11.add(itm);
			}
		}else {
			TodoVo vo = new TodoVo();
			vo.setSeq(-1);
			vo.setParentSeq(-1);
			vo.setLevel(0);
			todoList.add(vo);
			panel11.add(new TodoItem(vo));
		}
		
		panel11.revalidate();
		panel11.repaint();
	}
	
	private class TodoItem extends JPanel{
		
		private JCheckBox checkBox;
		private JTextField textField;
		private JButton button1;
		
		private TodoVo todoVo;
		
		public TodoItem(final TodoVo vo) {
			todoVo = vo;
			
			checkBox = new JCheckBox();
			checkBox.setSelected(("Y".equals(vo.getCheckYn()))? true : false);
			checkBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//하위 항목까지 모두 체크/해제
					Component[] comps = panel11.getComponents();
					for(int i=0; i<comps.length; i++) {
						JPanel panel = (JPanel)comps[i];
						JCheckBox checkBox = (JCheckBox)panel.getComponents()[0];
						JTextField textField = (JTextField)panel.getComponents()[1];
						JButton button1 = (JButton)panel.getComponents()[2];
						TodoVo vo = todoList.get(i);
						
						if(e.getSource() == checkBox) {
							//신규 항목 위치 확인하여 추가
							for(int j=i+1; j<todoList.size(); j++) {
								if(vo.getLevel() < todoList.get(j).getLevel()) {
									JPanel imsiPanel = (JPanel)panel11.getComponents()[j];
									JCheckBox imsiCheckBox = (JCheckBox)imsiPanel.getComponents()[0];
									JTextField imsiTextField = (JTextField)imsiPanel.getComponents()[1];
									JButton imsiButton1 = (JButton)imsiPanel.getComponents()[2];
									TodoVo imsiVo = todoList.get(j);
									
									imsiCheckBox.setSelected(checkBox.isSelected());
								}else if(vo.getLevel() >= todoList.get(j).getLevel()) {
									break;
								}
							}
							
							panel11.revalidate();
							panel11.repaint();
							break;
						}
					}
				}
			});
			this.add(checkBox);
			
			textField = new JTextField(40 - vo.getLevel() * 2);
			textField.setText(vo.getSubject());
			this.add(textField);
			
			button1 = new JButton("+");
			button1.setBorder(BorderFactory.createLineBorder(Color.black));
			button1.setPreferredSize(new Dimension(16, 16));
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					//현재 항목의 하위항목을 추가
					Component[] comps = panel11.getComponents();
					for(int i=0; i<comps.length; i++) {
						JPanel panel = (JPanel)comps[i];
						JCheckBox checkBox = (JCheckBox)panel.getComponents()[0];
						JTextField textField = (JTextField)panel.getComponents()[1];
						JButton button1 = (JButton)panel.getComponents()[2];
						TodoVo vo = todoList.get(i);
						
						if(e.getSource() == button1) {
							TodoVo sonVo = new TodoVo();
							sonVo.setSeq(-1);
							sonVo.setParentSeq(vo.getSeq());
							sonVo.setLevel(vo.getLevel() + 1);
							
							//신규 항목 위치 확인하여 추가
							if(i < comps.length - 1) {
								for(int j=i+1; j<todoList.size(); j++) {
									if(vo.getLevel() >= todoList.get(j).getLevel()) {
										todoList.add(j, sonVo);
										panel11.add(new TodoItem(sonVo), j);
										break;
									}else if(j == todoList.size() - 1) {
										todoList.add(j+1, sonVo);
										panel11.add(new TodoItem(sonVo), j+1);
										break;
									}
								}
							}else {
								//마지막 항목일때
								todoList.add(i + 1, sonVo);
								panel11.add(new TodoItem(sonVo), i + 1);
							}
							
							panel11.revalidate();
							panel11.repaint();
							break;
						}
					}
				}
			});
			this.add(button1);
		}
	}
}
