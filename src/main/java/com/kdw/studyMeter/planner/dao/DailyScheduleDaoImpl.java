package com.kdw.studyMeter.planner.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.kdw.studyMeter.planner.vo.DailyScheduleVo;

public class DailyScheduleDaoImpl implements DailyScheduleDao{
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	public List<DailyScheduleVo> selectList(DailyScheduleVo vo) {
		return sqlSessionTemplate.selectList("dailySchedule.selectList", vo);
	}

	public List<DailyScheduleVo> selectOne(DailyScheduleVo vo) {
		return sqlSessionTemplate.selectList("dailySchedule.selectOne", vo);
	}
	
	public int insert(final DailyScheduleVo vo) {
		sqlSessionTemplate.insert("dailySchedule.insert", vo);
		return vo.getSeq();
	}

	public int update(DailyScheduleVo vo) {
		return sqlSessionTemplate.insert("dailySchedule.update", vo);
	}
}
