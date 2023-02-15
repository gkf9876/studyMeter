package com.kdw.studyMeter.calendar.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.kdw.studyMeter.calendar.service.CalendarService;
import com.kdw.studyMeter.calendar.vo.CalendarVo;

public class CalendarFrame extends JFrame{
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel21;

	private JLabel label2;
	
	private JButton button1;
	private JButton button2;
	
	private Calendar cal;
	private int year;
	private int month;
	private int maxDay;
	
	private List<JPanel> days;
	
	CalendarService calendarService;
	
	public CalendarFrame(CalendarService service) {
		this.setTitle("캘린더");
		this.setSize(600, 500);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		//this.setResizable(false);
		
		this.calendarService = service;
		
		cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		
		panel1 = new JPanel();
		button1 = new JButton("◀");
		button1.setFont(button1.getFont().deriveFont(20.0f));
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cal.add(Calendar.MONTH, -1);
				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH);
				
				label2.setText(year + "년 " + (month + 1) + "월");
				panel2.removeAll();
				init();
			}
		});
		panel1.add(button1);
		
		label2 = new JLabel(year + "년 " + (month + 1) + "월");
		label2.setFont(label2.getFont().deriveFont(20.0f));
		panel1.add(label2);
		
		button2 = new JButton("▶");
		button2.setFont(button2.getFont().deriveFont(20.0f));
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cal.add(Calendar.MONTH, 1);
				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH);
				
				label2.setText(year + "년 " + (month + 1) + "월");
				panel2.removeAll();
				init();
			}
		});
		panel1.add(button2);
		this.add(panel1, BorderLayout.NORTH);
		
		panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		
		init();
		
		this.add(panel2, BorderLayout.CENTER);
	}
	
	public void init() {
		panel21 = new JPanel();
		panel21.setLayout(new GridLayout(7, 7, 2, 2));
		cal.set(year, month, 1);
		maxDay = cal.getActualMaximum(cal.DATE);
		
		//요일명 출력
		String[] weekName = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
		for(String week : weekName) {
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			JLabel label = new JLabel(week);
			label.setHorizontalAlignment(JLabel.CENTER);
			panel.add(label, BorderLayout.CENTER);
			panel21.add(panel);
		}
		
		//캘린더 틀 만들기
		int startDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int cnt = 1;
		
		//공부시간, 할일 데이터 가져오기
		CalendarVo calendarVo = new CalendarVo();
		calendarVo.setSchYearMonthDate(String.format("%04d-%02d", year, (month + 1)));
		List<CalendarVo> list = calendarService.select(calendarVo);
		Map<String, List<CalendarVo>> map = new HashMap<String, List<CalendarVo>>();
		
		for(CalendarVo vo : list) {
			List<CalendarVo> imsi = map.get(vo.getDate());
					
			if(imsi == null) {
				imsi = new ArrayList<CalendarVo>();
				imsi.add(vo);
				map.put(vo.getDate(), imsi);
			}else {
				imsi.add(vo);
				map.put(vo.getDate(), imsi);
			}
		}
		
		days = new ArrayList<JPanel>();
		for(int i=0; i<42; i++) {
			if(startDayOfWeek <= i + 1 && maxDay >= cnt) {
				List<CalendarVo> dayTodoList = map.get(String.format("%04d-%02d-%02d", year, (month + 1), cnt));
				CellCal cellCal = new CellCal(String.valueOf(cnt++));
				if(dayTodoList != null) {
					for(CalendarVo vo : dayTodoList) {
						cellCal.addElement(vo.getContents());
					}
				}
				days.add(cellCal);
			}else {
				days.add(new JPanel());
			}
		}
		
		cnt = 0;
		for(int i=0; i<6; i++) {
			for(int j=0; j<7; j++) {
				panel21.add(days.get(cnt++));
			}
		}
		panel2.add(panel21, BorderLayout.CENTER);
		
		this.revalidate();
		this.repaint();
	}
	
	private class CellCal extends JPanel{
		
		private JLabel label1;
		private DefaultListModel<String> model;
		private JList<String> list1;
		private JScrollPane scrollPane1;
		
		public CellCal(String day) {
			this.setLayout(new BorderLayout());
			Border border = new LineBorder(Color.black);
			this.setBorder(border);
			
			this.add(new JLabel(day), BorderLayout.NORTH);
			
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
