package com.kdw.studyMeter.study.meter.service;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kdw.studyMeter.study.meter.dao.StudyDao;
import com.kdw.studyMeter.study.meter.dao.StudyDaoImpl;
import com.kdw.studyMeter.study.meter.vo.StudyVo;

public class StudyService {
	private StudyDao dao;

	public StudyService(){
		ApplicationContext context = new GenericXmlApplicationContext("classpath*:**/applicationContext.xml");
		this.dao = context.getBean("studyDao", StudyDaoImpl.class);
	}
	
	public List<StudyVo> select() {
		return dao.select();
	}
	
	public StudyVo selectOne(StudyVo vo) {
		return dao.selectOne(vo);
	}
	
	public List<StudyVo> selectChart(StudyVo vo) {
		return dao.selectChart(vo);
	}

	public int insert(StudyVo vo) {
		return dao.insert(vo);
	}

	public void update(StudyVo vo) {
		dao.update(vo);
	}
}
