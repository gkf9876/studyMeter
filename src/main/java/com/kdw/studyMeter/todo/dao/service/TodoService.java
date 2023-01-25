package com.kdw.studyMeter.todo.dao.service;

import java.sql.Connection;
import java.util.List;

import com.kdw.studyMeter.todo.dao.TodoDao;
import com.kdw.studyMeter.todo.dao.TodoDaoImpl;
import com.kdw.studyMeter.todo.vo.TodoVo;

public class TodoService {
	private TodoDao dao;
	
	public TodoService(Connection conn){
		dao = new TodoDaoImpl(conn);
	}

	public List<TodoVo> select() {
		return dao.select();
	}

	public TodoVo selectOne(TodoVo vo) {
		return dao.selectOne(vo);
	}

	public int insert(TodoVo vo) {
		return dao.insert(vo);
	}

	public int update(TodoVo vo) {
		return dao.update(vo);
	}
}
