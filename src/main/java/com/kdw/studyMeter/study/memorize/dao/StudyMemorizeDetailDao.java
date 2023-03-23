package com.kdw.studyMeter.study.memorize.dao;

import java.util.List;

import com.kdw.studyMeter.study.memorize.vo.StudyMemorizeDetailVo;

public interface StudyMemorizeDetailDao {
	public List<StudyMemorizeDetailVo> selectList(StudyMemorizeDetailVo vo);
	public int insert(StudyMemorizeDetailVo vo);
	public int update(StudyMemorizeDetailVo vo);
}
