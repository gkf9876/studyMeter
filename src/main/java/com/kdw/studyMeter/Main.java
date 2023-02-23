package com.kdw.studyMeter;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JFrame;

import com.kdw.studyMeter.calendar.service.CalendarService;
import com.kdw.studyMeter.file.service.FileService;
import com.kdw.studyMeter.study.service.StudyService;
import com.kdw.studyMeter.todo.dao.service.TodoDetailService;
import com.kdw.studyMeter.todo.dao.service.TodoService;

public class Main {

	public static JFrame frame1;
	
	private static Connection conn = null;
	
	public static void main(String[] args) {

		try {
			Class.forName("org.sqlite.JDBC");
			URL resource = Main.class.getClassLoader().getResource("db");
			String filePath = resource.getFile();
			conn = DriverManager.getConnection("jdbc:sqlite:" + filePath);
			//conn = DriverManager.getConnection("jdbc:sqlite:db");
			
			frame1 = new Frame1(new StudyService(conn), new TodoService(conn), new TodoDetailService(conn)
					, new CalendarService(conn), new FileService(conn));
			frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame1.setVisible(true);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
