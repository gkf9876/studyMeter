package com.kdw.studyMeter.study.memorize.dao;

import java.util.List;

import com.kdw.studyMeter.study.memorize.vo.StudyMemorizeVo;

public interface StudyMemorizeDao {
	public List<StudyMemorizeVo> selectList(StudyMemorizeVo vo);
	public StudyMemorizeVo selectOne(StudyMemorizeVo vo);
	public int insert(StudyMemorizeVo vo);
	public int update(StudyMemorizeVo vo);
}
