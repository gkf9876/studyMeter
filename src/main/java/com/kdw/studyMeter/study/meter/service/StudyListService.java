package com.kdw.studyMeter.study.meter.service;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kdw.studyMeter.file.dao.FileDaoImpl;
import com.kdw.studyMeter.study.meter.dao.StudyListDao;
import com.kdw.studyMeter.study.meter.dao.StudyListDaoImpl;
import com.kdw.studyMeter.study.meter.vo.StudyListVo;

public class StudyListService {
	private StudyListDao dao;

	public StudyListService(){
		ApplicationContext context = new GenericXmlApplicationContext("classpath*:**/applicationContext.xml");
		this.dao = context.getBean("studyListDao", StudyListDaoImpl.class);
	}
	
	public List<StudyListVo> selectList(StudyListVo vo){
		return dao.selectList();
	}
	
	public StudyListVo selectOne(StudyListVo vo) {
		return dao.selectOne(vo);
	}
	
	public int insert(StudyListVo vo) {
		return dao.insert(vo);
	}
	
	public int update(StudyListVo vo) {
		return dao.update(vo);
	}
	
	public List<StudyListVo> selectTypeList(StudyListVo vo){
		return dao.selectTypeList(vo);
	}
}
