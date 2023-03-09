package com.kdw.studyMeter.todo.dao;

import java.util.List;

import com.kdw.studyMeter.todo.vo.TodoDetailVo;

public interface TodoDetailDao {
	public List<TodoDetailVo> select(TodoDetailVo vo);
	public int insert(TodoDetailVo vo);
	public int update(TodoDetailVo vo);
}
