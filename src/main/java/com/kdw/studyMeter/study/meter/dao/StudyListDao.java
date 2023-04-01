package com.kdw.studyMeter.study.meter.dao;

import java.util.List;

import com.kdw.studyMeter.study.meter.vo.StudyListVo;

public interface StudyListDao {
	public List<StudyListVo> selectList();
	public StudyListVo selectOne(StudyListVo vo);
	public int insert(StudyListVo vo);
	public int update(StudyListVo vo);
	public List<StudyListVo> selectTypeList(StudyListVo vo);
}
