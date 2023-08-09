package com.kdw.studyMeter.planner.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.kdw.studyMeter.planner.vo.PlannerVo;

public class PlannerDaoImpl implements PlannerDao{
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	public List<PlannerVo> selectList(PlannerVo vo) {
		return sqlSessionTemplate.selectList("planner.selectList", vo);
	}

	public List<PlannerVo> selectOne(PlannerVo vo) {
		return sqlSessionTemplate.selectList("planner.selectOne", vo);
	}
	
	public int insert(final PlannerVo vo) {
		sqlSessionTemplate.insert("planner.insert", vo);
		return vo.getSeq();
	}

	public int update(PlannerVo vo) {
		return sqlSessionTemplate.insert("planner.update", vo);
	}
}
