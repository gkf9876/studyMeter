package com.kdw.studyMeter.study.memorize.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.kdw.studyMeter.study.memorize.vo.StudyMemorizeDetailVo;

public class StudyMemorizeDetailDaoImpl implements StudyMemorizeDetailDao{
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public List<StudyMemorizeDetailVo> selectList(StudyMemorizeDetailVo vo) {
		return sqlSessionTemplate.selectList("studyMemorizeDetail.selectList", vo);
	}

	public int insert(StudyMemorizeDetailVo vo) {
		sqlSessionTemplate.insert("studyMemorizeDetail.insert", vo);
		return vo.getSeq();
	}

	public int update(StudyMemorizeDetailVo vo) {
		return sqlSessionTemplate.update("studyMemorizeDetail.update", vo);
	}
}
