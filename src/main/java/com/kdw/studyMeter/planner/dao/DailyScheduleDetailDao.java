package com.kdw.studyMeter.planner.dao;

import java.util.List;

import com.kdw.studyMeter.planner.vo.DailyScheduleDetailVo;

public interface DailyScheduleDetailDao {
	public List<DailyScheduleDetailVo> selectList(DailyScheduleDetailVo vo);
}
