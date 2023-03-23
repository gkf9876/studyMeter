package com.kdw.studyMeter.calendar.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.kdw.studyMeter.calendar.vo.CalendarVo;

public class CalendarDaoImpl implements CalendarDao{
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	public List<CalendarVo> selectList(CalendarVo vo) {
		return sqlSessionTemplate.selectList("calendar.selectList", vo);
	}
}
