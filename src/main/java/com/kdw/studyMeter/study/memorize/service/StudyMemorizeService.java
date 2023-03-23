package com.kdw.studyMeter.study.memorize.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kdw.studyMeter.study.memorize.dao.StudyMemorizeDao;
import com.kdw.studyMeter.study.memorize.dao.StudyMemorizeDaoImpl;
import com.kdw.studyMeter.study.memorize.vo.StudyMemorizeVo;

public class StudyMemorizeService {
	private StudyMemorizeDao dao;
	
	public StudyMemorizeService(){
		ApplicationContext context = new GenericXmlApplicationContext("classpath*:**/applicationContext.xml");
		this.dao = context.getBean("studyMemorizeDao", StudyMemorizeDaoImpl.class);
	}

	public List<StudyMemorizeVo> select() {
		return dao.select();
	}

	public StudyMemorizeVo selectOne(StudyMemorizeVo vo) {
		return dao.selectOne(vo);
	}

	public int insert(StudyMemorizeVo vo) {
		return dao.insert(vo);
	}

	public int update(StudyMemorizeVo vo) {
		return dao.update(vo);
	}

	public List<StudyMemorizeVo> select(int parentSeq, int level) {
		return dao.select(parentSeq, level);
	}
}
