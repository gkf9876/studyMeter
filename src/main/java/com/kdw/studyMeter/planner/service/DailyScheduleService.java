package com.kdw.studyMeter.planner.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kdw.studyMeter.planner.dao.DailyScheduleDao;
import com.kdw.studyMeter.planner.dao.DailyScheduleDaoImpl;
import com.kdw.studyMeter.planner.vo.DailyScheduleVo;

public class DailyScheduleService {
	private DailyScheduleDao dao;

	public DailyScheduleService(){
		ApplicationContext context = new GenericXmlApplicationContext("classpath*:**/applicationContext.xml");
		this.dao = context.getBean("dailyScheduleDao", DailyScheduleDaoImpl.class);
	}
	
	public List<DailyScheduleVo> selectList(DailyScheduleVo vo) {
		return dao.selectList(vo);
	}
	
	public List<DailyScheduleVo> selectOne(DailyScheduleVo vo) {
		return dao.selectOne(vo);
	}

	public int insert(DailyScheduleVo vo) {
		return dao.insert(vo);
	}

	public void update(DailyScheduleVo vo) {
		dao.update(vo);
	}
}
