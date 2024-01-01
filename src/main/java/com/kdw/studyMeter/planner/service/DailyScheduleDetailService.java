package com.kdw.studyMeter.planner.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kdw.studyMeter.planner.dao.DailyScheduleDetailDao;
import com.kdw.studyMeter.planner.dao.DailyScheduleDetailDaoImpl;
import com.kdw.studyMeter.planner.vo.DailyScheduleDetailVo;

public class DailyScheduleDetailService {
	private DailyScheduleDetailDao dao;

	public DailyScheduleDetailService(){
		ApplicationContext context = new GenericXmlApplicationContext("classpath*:**/applicationContext.xml");
		this.dao = context.getBean("dailyScheduleDetailDao", DailyScheduleDetailDaoImpl.class);
	}
	
	public List<DailyScheduleDetailVo> selectList(DailyScheduleDetailVo vo) {
		return dao.selectList(vo);
	}
}