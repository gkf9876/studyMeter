package com.kdw.studyMeter.planner.dao;

import java.util.List;

import com.kdw.studyMeter.planner.vo.PlannerVo;

public interface PlannerDao {
	public List<PlannerVo> selectList(PlannerVo vo);
	public List<PlannerVo> selectOne(PlannerVo vo);
	public int insert(PlannerVo vo);
	public int update(PlannerVo vo);
}
