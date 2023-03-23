package com.kdw.studyMeter.study.meter.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.kdw.studyMeter.study.meter.vo.StudyListVo;

public class StudyListDaoImpl implements StudyListDao{
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	public List<StudyListVo> selectList() {
		return sqlSessionTemplate.selectList("studyList.selectList");
	}

	public StudyListVo selectOne(StudyListVo vo) {
		return sqlSessionTemplate.selectOne("studyList.selectOne", vo);
	}
	
	public int insert(final StudyListVo vo) {
		sqlSessionTemplate.insert("studyList.insert", vo);
		return vo.getSeq();
	}

	public int update(StudyListVo vo) {
		return sqlSessionTemplate.update("studyList.update", vo);
	}
}
