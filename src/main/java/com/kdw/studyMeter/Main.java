package com.kdw.studyMeter;

import javax.swing.JFrame;

import com.kdw.studyMeter.calendar.service.CalendarService;
import com.kdw.studyMeter.file.service.FileService;
import com.kdw.studyMeter.study.frame.StudyMeterFrame;
import com.kdw.studyMeter.study.service.StudyListService;
import com.kdw.studyMeter.study.service.StudyService;
import com.kdw.studyMeter.todo.dao.service.TodoDetailService;
import com.kdw.studyMeter.todo.dao.service.TodoService;

public class Main {

	public static JFrame frame1;
	
	public static void main(String[] args) {
		try {
			frame1 = new StudyMeterFrame(new StudyService(), new TodoService(), new TodoDetailService(), new CalendarService(), new FileService(), new StudyListService());
			frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame1.setVisible(true);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
