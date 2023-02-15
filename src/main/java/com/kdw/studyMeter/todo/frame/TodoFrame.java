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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.kdw.studyMeter.todo.dao.service.TodoDetailService;
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
	private TodoItem todoItem;
	
	private TodoService todoService;
	private TodoDetailService todoDetailService;
	
	public TodoFrame(final TodoService todoService, final TodoDetailService todoDetailService) {
		this.setTitle("할일 목록");
		this.setSize(650, 490);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.todoService = todoService;
		this.todoDetailService = todoDetailService;
		
		//할일 목록 출력
		panel1 = new JPanel();
		panel11 = new JPanel();
		panel11.setLayout(new BoxLayout(panel11, BoxLayout.Y_AXIS));
		
		//할일 목록 초기화
		init();
		
		//스크롤 생성
		scrollPane1 = new JScrollPane(panel11);
		scrollPane1.setPreferredSize(new Dimension(600, 400));
		
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
					TodoItem panel = (TodoItem)comps[i];
					panel.save();
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
				//vo.setLevel(1);
				//vo.setOdr(panel11.getComponentCount());
				//TodoItem item = new TodoItem(vo);
				//todoItemList.add(item);
				todoItem.add(vo);
				//panel11.add(item);
				
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
		
		todoItem = new TodoItem();
		panel11.add(todoItem);
		
		panel11.revalidate();
		panel11.repaint();
	}
	
	private class TodoItem extends JPanel{
		
		private TodoDetailFrame todoDetailFrame;
		private JPanel panel;
		private JCheckBox checkBox;
		private JTextField textField;
		private JButton button1;
		private JButton button2;
		private JButton button3;
		private JButton button4;
		
		private TodoVo todoVo;
		
		//할일 목록
		private List<TodoItem> sonItemList;

		//최상위 아이템 추가할때 사용
		public TodoItem() {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.todoVo = null;
			
			panel = new JPanel();
			this.add(panel);
			
			//자식 항목 출력하기.
			sonItemList = new ArrayList<TodoItem>();
			List<TodoVo> sonItem = todoService.select(-1, 1);
			if(sonItem.size() > 0) {
				for(TodoVo vo : sonItem) {
					TodoItem item = new TodoItem(vo);
					sonItemList.add(item);
					this.add(item);
				}
			}else {
				TodoVo vo = new TodoVo();
				vo.setSeq(-1);
				vo.setParentSeq(-1);
				vo.setLevel(1);
				TodoItem item = new TodoItem(vo);
				sonItemList.add(item);
				this.add(item);
			}
		}
		
		public TodoItem(TodoVo vo) {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.todoVo = vo;
			
			panel = new JPanel();
			checkBox = new JCheckBox();
			checkBox.setSelected(("Y".equals(this.todoVo.getCheckYn()))? true : false);
			checkBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//현재 항목의 하위항목을 추가
					setCheck((checkBox.isSelected())? true : false);
				}
			});
			panel.add(checkBox);
			
			textField = new JTextField(40 - this.todoVo.getLevel() * 2);
			textField.setText(this.todoVo.getSubject());
			panel.add(textField);
			
			button1 = new JButton("+");
			button1.setBorder(BorderFactory.createLineBorder(Color.black));
			button1.setPreferredSize(new Dimension(16, 16));
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//현재 항목의 하위항목을 추가
					TodoItem item = (TodoItem)panel.getParent();
					TodoVo vo = new TodoVo();
					vo.setSeq(-1);
					item.add(vo);
					
					item.revalidate();
					item.repaint();
				}
			});
			panel.add(button1);
			
			button2 = new JButton("▲");
			button2.setBorder(BorderFactory.createLineBorder(Color.black));
			button2.setPreferredSize(new Dimension(16, 16));
			button2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//해당 항목의 위에 있는 항목 불러오기
					TodoItem currentItem = (TodoItem)panel.getParent();
					TodoItem parentItem = (TodoItem)currentItem.getParent();
					List<TodoItem> sonItemList = parentItem.getSonItemList();
					for(int i=0; i<sonItemList.size(); i++) {
						TodoItem regionItem = (i-1 >= 0)? sonItemList.get(i-1) : null;
						TodoItem item = sonItemList.get(i);
						TodoItem nextItem = (i+1 < sonItemList.size())? sonItemList.get(i+1) : null;
						
						if(item == currentItem) {
							System.out.println("=================== UP MOVE!! ======================");
							System.out.println("regionItem : " + ((regionItem != null)? regionItem.getTodoVo().getSubject() + ", odr : " + regionItem.getTodoVo().getOdr() : "null"));
							System.out.println("item : " + item.getTodoVo().getSubject() + ", odr : " + item.getTodoVo().getOdr());
							System.out.println("nextItem : " + ((nextItem != null)? nextItem.getTodoVo().getSubject() + ", odr : " + nextItem.getTodoVo().getOdr() : "null"));
							System.out.println("=================== RESULT!! =======================");
							
							if(regionItem != null) {
								TodoVo regionVo = regionItem.getTodoVo();
								TodoVo vo = item.getTodoVo();
								
								int tempOdr = regionVo.getOdr();
								regionVo.setOdr(vo.getOdr());
								vo.setOdr(tempOdr);
								
								regionItem.setTodoVo(regionVo);
								item.setTodoVo(vo);
								
								parentItem.remove(item);
								parentItem.add(item, i);
								
								sonItemList.remove(i);
								sonItemList.add(i-1, item);
								parentItem.setSonItemList(sonItemList);
								
								TodoItem imsiRegionItem = (i-1 >= 0)? sonItemList.get(i-1) : null;
								TodoItem imsiItem = sonItemList.get(i);
								TodoItem imsiNextItem = (i+1 < sonItemList.size())? sonItemList.get(i+1) : null;
								
								System.out.println("regionItem : " + ((imsiRegionItem != null)? imsiRegionItem.getTodoVo().getSubject() + ", odr : " + imsiRegionItem.getTodoVo().getOdr() : "null"));
								System.out.println("item : " + imsiItem.getTodoVo().getSubject() + ", odr : " + imsiItem.getTodoVo().getOdr());
								System.out.println("nextItem : " + ((imsiNextItem != null)? imsiNextItem.getTodoVo().getSubject() + ", odr : " + imsiNextItem.getTodoVo().getOdr() : "null"));
							}
							
							System.out.println("====================================================\n\n\n");
							
							parentItem.revalidate();
							parentItem.repaint();
							break;
						}
					}
				}
			});
			panel.add(button2);
			
			button3 = new JButton("▼");
			button3.setBorder(BorderFactory.createLineBorder(Color.black));
			button3.setPreferredSize(new Dimension(16, 16));
			button3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//해당 항목의 위에 있는 항목 불러오기
					TodoItem currentItem = (TodoItem)panel.getParent();
					TodoItem parentItem = (TodoItem)currentItem.getParent();
					List<TodoItem> sonItemList = parentItem.getSonItemList();
					for(int i=0; i<sonItemList.size(); i++) {
						TodoItem regionItem = (i-1 >= 0)? sonItemList.get(i-1) : null;
						TodoItem item = sonItemList.get(i);
						TodoItem nextItem = (i+1 < sonItemList.size())? sonItemList.get(i+1) : null;
						
						if(item == currentItem) {
							System.out.println("=================== DOWN MOVE!! ======================");
							System.out.println("regionItem : " + ((regionItem != null)? regionItem.getTodoVo().getSubject() + ", odr : " + regionItem.getTodoVo().getOdr() : "null"));
							System.out.println("item : " + item.getTodoVo().getSubject() + ", odr : " + item.getTodoVo().getOdr());
							System.out.println("nextItem : " + ((nextItem != null)? nextItem.getTodoVo().getSubject() + ", odr : " + nextItem.getTodoVo().getOdr() : "null"));
							System.out.println("=================== RESULT!! =======================");
							
							if(nextItem != null) {
								TodoVo nextVo = nextItem.getTodoVo();
								TodoVo vo = item.getTodoVo();
								
								int tempOdr = nextVo.getOdr();
								nextVo.setOdr(vo.getOdr());
								vo.setOdr(tempOdr);
								
								nextItem.setTodoVo(nextVo);
								item.setTodoVo(vo);
								
								parentItem.remove(item);
								parentItem.add(item, i+2);
								
								sonItemList.remove(i);
								sonItemList.add(i+1, item);
								parentItem.setSonItemList(sonItemList);
								
								TodoItem imsiRegionItem = (i-1 >= 0)? sonItemList.get(i-1) : null;
								TodoItem imsiItem = sonItemList.get(i);
								TodoItem imsiNextItem = (i+1 < sonItemList.size())? sonItemList.get(i+1) : null;
								
								System.out.println("regionItem : " + ((imsiRegionItem != null)? imsiRegionItem.getTodoVo().getSubject() + ", odr : " + imsiRegionItem.getTodoVo().getOdr() : "null"));
								System.out.println("item : " + imsiItem.getTodoVo().getSubject() + ", odr : " + imsiItem.getTodoVo().getOdr());
								System.out.println("nextItem : " + ((imsiNextItem != null)? imsiNextItem.getTodoVo().getSubject() + ", odr : " + imsiNextItem.getTodoVo().getOdr() : "null"));
							}
							
							System.out.println("====================================================\n\n\n");
							
							parentItem.revalidate();
							parentItem.repaint();
							break;
						}
					}
				}
			});
			panel.add(button3);
			
			button4 = new JButton("상세");
			todoDetailFrame = new TodoDetailFrame(todoVo.getSeq(), textField.getText(), todoDetailService);
			button4.setBorder(BorderFactory.createLineBorder(Color.black));
			button4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(todoVo != null && todoVo.getSeq() > -1) {
						todoDetailFrame.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(null, "빈 항목의 상세입력은 할 수 없습니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			panel.add(button4);
			
			this.add(panel);
			
			//자식 항목 출력하기.
			List<TodoVo> sonItem = todoService.select(todoVo.getSeq(), todoVo.getLevel() + 1);
			sonItemList = new ArrayList<TodoItem>();
			for(TodoVo sonvo : sonItem) {
				TodoItem item = new TodoItem(sonvo);
				sonItemList.add(item);
				this.add(item);
			}
		}
		
		public void save() {
			if(this.todoVo != null) {
				this.todoVo.setCheckYn((checkBox.isSelected())? "Y" : "N");
				this.todoVo.setSubject(textField.getText());
				
				//기존항목은 update, 신규항목은 insert
				if(this.todoVo.getSeq() != -1) {
					todoService.update(this.todoVo);
				}else {
					todoService.insert(this.todoVo);
				}
			}
			
			//자식 항목 모두 저장처리
			for(TodoItem item : sonItemList) {
				//부모 아이템 시퀀스값 가져와서 저장.
				if(this.todoVo != null) {
					TodoVo vo = item.getTodoVo();
					vo.setParentSeq(this.todoVo.getSeq());
					item.setTodoVo(vo);
				}
				item.save();
			}
		}
		
		public void add(TodoVo vo) {
			if(this.todoVo != null) {
				vo.setParentSeq(this.todoVo.getSeq());
				vo.setLevel(this.todoVo.getLevel() + 1);
				vo.setOdr(this.getComponentCount());
			}else {
				vo.setParentSeq(-1);
				vo.setLevel(1);
				vo.setOdr(this.getComponentCount());
			}
			
			TodoItem item = new TodoItem(vo);
			sonItemList.add(item);
			this.add(item);
		}
		
		public void setCheck(boolean checkYn) {
			checkBox.setSelected(checkYn);
			
			for(TodoItem item : sonItemList) {
				item.setCheck(checkYn);
			}
		}

		public TodoVo getTodoVo() {
			return todoVo;
		}

		public void setTodoVo(TodoVo todoVo) {
			this.todoVo = todoVo;
		}

		public List<TodoItem> getSonItemList() {
			return sonItemList;
		}

		public void setSonItemList(List<TodoItem> sonItemList) {
			this.sonItemList = sonItemList;
		}
	}
}
