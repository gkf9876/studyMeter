package com.kdw.studyMeter.study.meter.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.kdw.studyMeter.study.meter.vo.StudyVo;

public class StudyDaoImpl implements StudyDao{
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	public List<StudyVo> select() {
		return sqlSessionTemplate.selectList("study.selectList");
	}

	public StudyVo selectOne(StudyVo vo) {
		return sqlSessionTemplate.selectOne("study.selectOne", vo);
	}
	
	public List<StudyVo> selectChart(int cnt) {
		return sqlSessionTemplate.selectList("study.selectChart", cnt);
	}
	
	public int insert(final StudyVo vo) {
		sqlSessionTemplate.insert("study.insert", vo);
		return vo.getSeq();
	}

	public int update(StudyVo vo) {
		return sqlSessionTemplate.insert("study.update", vo);
	}
}
