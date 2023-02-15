package com.kdw.studyMeter.calendar.service;

import java.sql.Connection;
import java.util.List;

import com.kdw.studyMeter.calendar.dao.CalendarDao;
import com.kdw.studyMeter.calendar.dao.CalendarDaoImpl;
import com.kdw.studyMeter.calendar.vo.CalendarVo;

public class CalendarService {
	private CalendarDao dao;

	public CalendarService(Connection conn){
		dao = new CalendarDaoImpl(conn);
	}
	
	public List<CalendarVo> select(CalendarVo vo){
		return dao.select(vo);
	}
}
