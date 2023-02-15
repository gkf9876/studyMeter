package com.kdw.studyMeter.todo.dao.service;

import java.sql.Connection;
import java.util.List;

import com.kdw.studyMeter.todo.dao.TodoDao;
import com.kdw.studyMeter.todo.dao.TodoDetailDao;
import com.kdw.studyMeter.todo.dao.TodoDetailDaoImpl;
import com.kdw.studyMeter.todo.vo.TodoDetailVo;

public class TodoDetailService {
	private TodoDetailDao dao;
	
	public TodoDetailService(Connection conn){
		dao = new TodoDetailDaoImpl(conn);
	}

	public List<TodoDetailVo> select(TodoDetailVo vo) {
		return dao.select(vo);
	}

	public TodoDetailVo selectOne(TodoDetailVo vo) {
		return dao.selectOne(vo);
	}

	public int insert(TodoDetailVo vo) {
		return dao.insert(vo);
	}

	public int update(TodoDetailVo vo) {
		return dao.update(vo);
	}
}
