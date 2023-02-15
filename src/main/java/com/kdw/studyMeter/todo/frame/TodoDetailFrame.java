package com.kdw.studyMeter.todo.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.kdw.studyMeter.todo.dao.service.TodoDetailService;
import com.kdw.studyMeter.todo.vo.TodoDetailVo;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class TodoDetailFrame extends JFrame{
	
	private JPanel panel1;
	private JPanel panel11;
	private JPanel panel2;
	
	private JScrollPane scrollPane1;
	
	private JButton button1;
	private JButton button2;
	
	//할일 상세내용 목록
	private TodoDetailItem todoDetailItem;
	
	private TodoDetailService todoDetailService;
	
	private int parentSeq;
	
	public TodoDetailFrame(int parentSeq, String title, final TodoDetailService service) {
		this.setTitle(title);
		this.setSize(650, 550);
		this.setLayout(new BorderLayout());
		
		this.parentSeq = parentSeq;
		
		this.todoDetailService = service;
		
		panel1 = new JPanel();
		panel11 = new JPanel();
		panel11.setLayout(new BoxLayout(panel11, BoxLayout.Y_AXIS));
		
		//할일 목록 초기화
		init();
		
		//스크롤 생성
		scrollPane1 = new JScrollPane(panel11);
		scrollPane1.setPreferredSize(new Dimension(600, 450));
		
		panel1.add(scrollPane1);
		this.add(panel1);
		
		panel2 = new JPanel();
		
		button1 = new JButton("저장");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//화면데이터 할일목록에 저장.
				Component[] comps = panel11.getComponents();
				for(int i=0; i<comps.length; i++) {
					TodoDetailItem panel = (TodoDetailItem)comps[i];
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
				TodoDetailVo vo = new TodoDetailVo();
				vo.setSeq(-1);
				vo.setParentSeq(-1);
				
				JPanel panel = new TodoDetailItem(vo);
				panel11.add(panel);
				
				panel1.revalidate();
				panel1.repaint();
			}
		});
		panel2.add(button2);
		this.add(panel2, BorderLayout.SOUTH);
	}
	
	//할일 목록 초기화
	public void init() {
		panel11.removeAll();

		TodoDetailVo vo = new TodoDetailVo();
		vo.setSeq(-1);
		vo.setParentSeq(parentSeq);
		List<TodoDetailVo> itemList = todoDetailService.select(vo);
		
		if(itemList != null && itemList.size() > 0) {
			for(TodoDetailVo item : itemList) {
				todoDetailItem = new TodoDetailItem(item);
				panel11.add(todoDetailItem);
			}
		}else {
			todoDetailItem = new TodoDetailItem(vo);
			panel11.add(todoDetailItem);
		}
		
		panel11.revalidate();
		panel11.repaint();
	}
	
	private class TodoDetailItem extends JPanel{
		private JPanel panel1;
		private JPanel panel2;
		
		private JDatePickerImpl datePicker;
		private UtilDateModel model;
		private JTextArea textArea;
		private JScrollPane scrollPane;
		private JButton button;
		
		private TodoDetailVo todoDetailVo;
		
		public TodoDetailItem(TodoDetailVo vo) {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.todoDetailVo = vo;
			
			panel1 = new JPanel();
			panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
			
	        model = new UtilDateModel();
	        if(vo.getSeq() > -1) {
		        String planeDTO = vo.getDate();
		        String[] date = planeDTO.split("-");
		        int dateY = Integer.parseInt(date[0]);
		        int dateM = Integer.parseInt(date[1]) - 1;
		        int dateD = Integer.parseInt(date[2]);
		        model.setDate(dateY, dateM, dateD);
		        model.setSelected(true);
	        }else {
	        	LocalDate now = LocalDate.now();
		        model.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
		        model.setSelected(true);
	        }
	        
	        JDatePanelImpl datePanel = new JDatePanelImpl(model);
	        datePicker = new JDatePickerImpl(datePanel);
	        panel1.add(datePicker);
			
			button = new JButton("삭제");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int result = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "확인", JOptionPane.OK_CANCEL_OPTION);
					if(result == 0) {
						datePicker.setEnabled(false);
						textArea.setEnabled(false);
						button.setEnabled(false);
						todoDetailVo.setUseYn("N");
					}
				}
			});
			panel1.add(button);
			
			this.add(panel1);
	        
			panel2 = new JPanel();
			panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
			textArea = new JTextArea((vo.getSeq() == -1)? "" : vo.getContents());
			scrollPane = new JScrollPane(textArea);
			scrollPane.setPreferredSize(new Dimension(560, 100));
			panel2.add(scrollPane);
			
			this.add(panel2);
		}
		
		public void save() {
			todoDetailVo.setParentSeq(parentSeq);
			todoDetailVo.setDate(String.format("%04d-%02d-%02d", model.getYear(), (model.getMonth() + 1), model.getDay()));
			todoDetailVo.setContents(textArea.getText());
			
			if(textArea.getText() != null && !textArea.getText().trim().equals("")) {
				if(todoDetailVo.getSeq() == -1) {
					todoDetailService.insert(todoDetailVo);
				}else {
					todoDetailService.update(todoDetailVo);
				}
			}
		}
	}
}
