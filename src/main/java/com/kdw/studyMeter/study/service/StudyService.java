package com.kdw.studyMeter.study.service;

import java.util.List;

import com.kdw.studyMeter.study.dao.StudyDao;
import com.kdw.studyMeter.study.dao.StudyDaoImpl;
import com.kdw.studyMeter.study.vo.StudyVo;

public class StudyService {
	private StudyDao dao;

	public StudyService(){
		dao = new StudyDaoImpl();
	}
	
	public List<StudyVo> select() {
		return dao.select();
	}
	
	public StudyVo selectOne(StudyVo vo) {
		return dao.selectOne(vo);
	}

	public int insert(StudyVo vo) {
		return dao.insert(vo);
	}

	public void update(StudyVo vo) {
		dao.update(vo);
	}
}
