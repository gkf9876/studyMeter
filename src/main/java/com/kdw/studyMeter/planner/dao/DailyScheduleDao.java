package com.kdw.studyMeter.planner.dao;

import java.util.List;

import com.kdw.studyMeter.planner.vo.DailyScheduleVo;

public interface DailyScheduleDao {
	public List<DailyScheduleVo> selectList(DailyScheduleVo vo);
	public List<DailyScheduleVo> selectOne(DailyScheduleVo vo);
	public int insert(DailyScheduleVo vo);
	public int update(DailyScheduleVo vo);
}
