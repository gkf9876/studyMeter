package com.kdw.studyMeter.file.dao;

import org.mybatis.spring.SqlSessionTemplate;

import com.kdw.studyMeter.file.vo.FileVo;

public class FileDaoImpl implements FileDao{
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public FileVo selectOne(FileVo vo) {
		return sqlSessionTemplate.selectOne("file.selectOne", vo);
	}

	public int insert(final FileVo vo) {
		sqlSessionTemplate.insert("file.insert", vo);
		return vo.getSeq();
	}

	public int update(FileVo vo) {
		return sqlSessionTemplate.update("file.update", vo);
	}
}
