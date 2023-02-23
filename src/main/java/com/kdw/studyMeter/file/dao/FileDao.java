package com.kdw.studyMeter.file.dao;

import com.kdw.studyMeter.file.vo.FileVo;

public interface FileDao {
	public FileVo selectOne(FileVo vo);
	public int insert(FileVo vo);
	public int update(FileVo vo);
}
