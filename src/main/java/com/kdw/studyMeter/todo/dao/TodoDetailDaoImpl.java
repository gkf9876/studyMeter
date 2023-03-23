package com.kdw.studyMeter.todo.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.kdw.studyMeter.todo.vo.TodoDetailVo;

public class TodoDetailDaoImpl implements TodoDetailDao{
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public List<TodoDetailVo> selectList(TodoDetailVo vo) {
		return sqlSessionTemplate.selectList("todoDetail.selectList", vo);
	}

	public int insert(TodoDetailVo vo) {
		sqlSessionTemplate.insert("todoDetail.insert", vo);
		return vo.getSeq();
	}

	public int update(TodoDetailVo vo) {
		return sqlSessionTemplate.update("todoDetail.update", vo);
	}
}
