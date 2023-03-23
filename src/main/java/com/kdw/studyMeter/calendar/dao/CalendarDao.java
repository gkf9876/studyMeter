package com.kdw.studyMeter.calendar.dao;

import java.util.List;

import com.kdw.studyMeter.calendar.vo.CalendarVo;

public interface CalendarDao {
	public List<CalendarVo> selectList(CalendarVo vo);
}
