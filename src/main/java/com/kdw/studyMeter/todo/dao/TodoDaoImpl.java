package com.kdw.studyMeter.todo.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.kdw.studyMeter.todo.vo.TodoVo;

public class TodoDaoImpl implements TodoDao{
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public List<TodoVo> selectList(TodoVo vo) {
		return sqlSessionTemplate.selectList("todo.selectList", vo);
	}

	public TodoVo selectOne(TodoVo vo) {
		// TODO Auto-generated method stub
		return null;
	}

	public int insert(final TodoVo vo) {
		sqlSessionTemplate.insert("todo.insert", vo);
		return vo.getSeq();
	}

	public int update(TodoVo vo) {
		return sqlSessionTemplate.update("todo.update", vo);
	}
}
