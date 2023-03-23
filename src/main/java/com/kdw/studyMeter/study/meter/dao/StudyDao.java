package com.kdw.studyMeter.study.meter.dao;

import java.util.List;

import com.kdw.studyMeter.study.meter.vo.StudyVo;

public interface StudyDao {
	public List<StudyVo> select();
	public StudyVo selectOne(StudyVo vo);
	public List<StudyVo> selectChart(int cnt);
	public int insert(StudyVo vo);
	public int update(StudyVo vo);
}
