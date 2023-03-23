package com.kdw.studyMeter.study.memorize.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.kdw.studyMeter.study.memorize.vo.StudyMemorizeVo;

public class StudyMemorizeDaoImpl implements StudyMemorizeDao{
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public List<StudyMemorizeVo> selectList(StudyMemorizeVo vo) {
		return sqlSessionTemplate.selectList("studyMemorize.selectList", vo);
	}

	public StudyMemorizeVo selectOne(StudyMemorizeVo vo) {
		// TODO Auto-generated method stub
		return null;
	}

	public int insert(final StudyMemorizeVo vo) {
		sqlSessionTemplate.insert("studyMemorize.insert", vo);
		return vo.getSeq();
	}

	public int update(StudyMemorizeVo vo) {
		return sqlSessionTemplate.update("studyMemorize.update", vo);
	}
}
