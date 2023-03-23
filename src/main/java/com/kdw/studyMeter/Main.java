package com.kdw.studyMeter;

import javax.swing.JFrame;

import com.kdw.studyMeter.calendar.service.CalendarService;
import com.kdw.studyMeter.file.service.FileService;
import com.kdw.studyMeter.study.memorize.service.StudyMemorizeDetailService;
import com.kdw.studyMeter.study.memorize.service.StudyMemorizeService;
import com.kdw.studyMeter.study.meter.frame.StudyMeterFrame;
import com.kdw.studyMeter.study.meter.service.StudyListService;
import com.kdw.studyMeter.study.meter.service.StudyService;
import com.kdw.studyMeter.todo.service.TodoDetailService;
import com.kdw.studyMeter.todo.service.TodoService;

public class Main {

	public static JFrame frame1;
	
	public static void main(String[] args) {
		try {
			frame1 = new StudyMeterFrame(new StudyService(), new TodoService(), new TodoDetailService(), new CalendarService(), new FileService(), new StudyListService(), new StudyMemorizeService(), new StudyMemorizeDetailService());
			frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame1.setVisible(true);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
