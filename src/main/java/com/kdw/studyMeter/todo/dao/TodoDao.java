package com.kdw.studyMeter.todo.dao;

import java.util.List;

import com.kdw.studyMeter.todo.vo.TodoVo;

public interface TodoDao {
	public List<TodoVo> select();
	public TodoVo selectOne(TodoVo vo);
	public int insert(TodoVo vo);
	public int update(TodoVo vo);
	public List<TodoVo> select(int parentSeq, int level);
}
