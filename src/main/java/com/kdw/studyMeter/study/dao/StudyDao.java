package com.kdw.studyMeter.study.dao;

import java.util.List;

import com.kdw.studyMeter.study.vo.StudyVo;

public interface StudyDao {
	public List<StudyVo> select();
	public StudyVo selectOne(StudyVo vo);
	public int insert(StudyVo vo);
	public int update(StudyVo vo);
}
