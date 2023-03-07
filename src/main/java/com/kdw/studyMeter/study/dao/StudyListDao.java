package com.kdw.studyMeter.study.dao;

import java.util.List;

import com.kdw.studyMeter.study.vo.StudyListVo;

public interface StudyListDao {
	public List<StudyListVo> select();
	public StudyListVo selectOne(StudyListVo vo);
	public int insert(StudyListVo vo);
	public int update(StudyListVo vo);
}
