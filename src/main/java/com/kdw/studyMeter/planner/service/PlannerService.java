package com.kdw.studyMeter.planner.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kdw.studyMeter.planner.dao.PlannerDao;
import com.kdw.studyMeter.planner.dao.PlannerDaoImpl;
import com.kdw.studyMeter.planner.vo.PlannerVo;

public class PlannerService {
	private PlannerDao dao;

	public PlannerService(){
		ApplicationContext context = new GenericXmlApplicationContext("classpath*:**/applicationContext.xml");
		this.dao = context.getBean("plannerDao", PlannerDaoImpl.class);
	}
	
	public List<PlannerVo> selectList(PlannerVo vo) {
		return dao.selectList(vo);
	}
	
	public List<PlannerVo> selectOne(PlannerVo vo) {
		return dao.selectOne(vo);
	}

	public int insert(PlannerVo vo) {
		return dao.insert(vo);
	}

	public void update(PlannerVo vo) {
		dao.update(vo);
	}
}
