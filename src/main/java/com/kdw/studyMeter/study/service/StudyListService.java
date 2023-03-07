package com.kdw.studyMeter.study.service;

import java.sql.Connection;
import java.util.List;

import com.kdw.studyMeter.study.dao.StudyListDao;
import com.kdw.studyMeter.study.dao.StudyListDaoImpl;
import com.kdw.studyMeter.study.vo.StudyListVo;

public class StudyListService {
	private StudyListDao dao;

	public StudyListService(Connection conn){
		dao = new StudyListDaoImpl(conn);
	}
	
	public List<StudyListVo> select(StudyListVo vo){
		return dao.select();
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
}
