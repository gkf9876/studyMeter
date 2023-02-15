package com.kdw.studyMeter.todo.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class TodoCalendarFrame extends JFrame{
	private JPanel panel1;
	private JPanel panel2;
	
	private JLabel label1;
	
	private Calendar cal;
	private int year;
	private int month;
	private int maxDay;
	
	private List<JPanel> days;
	
	public TodoCalendarFrame() {
		this.setTitle("캘린더");
		this.setSize(600, 500);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		//this.setResizable(false);
		
		cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		
		panel1 = new JPanel();
		label1 = new JLabel(year + "년 " + (month + 1) + "월");
		label1.setFont(label1.getFont().deriveFont(20.0f));
		panel1.add(label1);
		this.add(panel1, BorderLayout.NORTH);
		
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(6, 7, 2, 2));
		cal.set(year, month, 1);
		maxDay = cal.getActualMaximum(cal.DATE);
		
		panel2.add(new JLabel("SUN"));
		panel2.add(new JLabel("MON"));
		panel2.add(new JLabel("TUE"));
		panel2.add(new JLabel("WED"));
		panel2.add(new JLabel("THU"));
		panel2.add(new JLabel("FRI"));
		panel2.add(new JLabel("SAT"));
		
		int startDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int cnt = 1;
		
		days = new ArrayList<JPanel>();
		for(int i=0; i<35; i++) {
			if(startDayOfWeek <= i + 1 && maxDay >= i + 1) {
				days.add(new CellCal(String.valueOf(cnt++)));
			}else {
				days.add(new JPanel());
			}
		}
		
		cnt = 0;
		for(int i=0; i<5; i++) {
			for(int j=0; j<7; j++) {
				panel2.add(days.get(cnt++));
			}
		}
		this.add(panel2, BorderLayout.CENTER);
	}
	
	private class CellCal extends JPanel{
		
		private JLabel label1;
		private DefaultListModel<String> model;
		private JList<String> list1;
		private JScrollPane scrollPane1;
		
		public CellCal(String val) {
			this.setLayout(new BorderLayout());
			Border border = new LineBorder(Color.black);
			this.setBorder(border);
			
			this.add(new JLabel(val), BorderLayout.NORTH);
			
			model = new DefaultListModel<String>();
			
			list1 = new JList<String>(model);
			scrollPane1 = new JScrollPane(list1);
			this.add(scrollPane1, BorderLayout.CENTER);
		}
		
		public void addElement(String str) {
			model.addElement(str);
		}
	}
}
