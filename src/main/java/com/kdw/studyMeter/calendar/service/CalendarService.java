package com.kdw.studyMeter.calendar.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kdw.studyMeter.calendar.dao.CalendarDao;
import com.kdw.studyMeter.calendar.dao.CalendarDaoImpl;
import com.kdw.studyMeter.calendar.vo.CalendarVo;

public class CalendarService {
	private CalendarDao dao;

	public CalendarService(){
		ApplicationContext context = new GenericXmlApplicationContext("classpath*:**/applicationContext.xml");
		this.dao = context.getBean("calendarDao", CalendarDaoImpl.class);
	}
	
	public List<CalendarVo> select(CalendarVo vo){
		return dao.select(vo);
	}
}
