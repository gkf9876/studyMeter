package com.kdw.studyMeter.study.memorize.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kdw.studyMeter.study.memorize.dao.StudyMemorizeDetailDao;
import com.kdw.studyMeter.study.memorize.dao.StudyMemorizeDetailDaoImpl;
import com.kdw.studyMeter.study.memorize.vo.StudyMemorizeDetailVo;

public class StudyMemorizeDetailService {
	private StudyMemorizeDetailDao dao;
	
	public StudyMemorizeDetailService(){
		ApplicationContext context = new GenericXmlApplicationContext("classpath*:**/applicationContext.xml");
		this.dao = context.getBean("studyMemorizeDetailDao", StudyMemorizeDetailDaoImpl.class);
	}

	public List<StudyMemorizeDetailVo> selectList(StudyMemorizeDetailVo vo) {
		return dao.selectList(vo);
	}

	public int insert(StudyMemorizeDetailVo vo) {
		return dao.insert(vo);
	}

	public int update(StudyMemorizeDetailVo vo) {
		return dao.update(vo);
	}
}
