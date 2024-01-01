package com.kdw.studyMeter.planner.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.kdw.studyMeter.planner.vo.DailyScheduleDetailVo;

public class DailyScheduleDetailDaoImpl implements DailyScheduleDetailDao{
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	public List<DailyScheduleDetailVo> selectList(DailyScheduleDetailVo vo) {
		return sqlSessionTemplate.selectList("dailyScheduleDetail.selectList", vo);
	}
}
