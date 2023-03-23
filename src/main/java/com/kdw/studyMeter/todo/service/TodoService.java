package com.kdw.studyMeter.todo.service;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kdw.studyMeter.study.meter.dao.StudyDaoImpl;
import com.kdw.studyMeter.todo.dao.TodoDao;
import com.kdw.studyMeter.todo.dao.TodoDaoImpl;
import com.kdw.studyMeter.todo.vo.TodoVo;

public class TodoService {
	private TodoDao dao;
	
	public TodoService(){
		ApplicationContext context = new GenericXmlApplicationContext("classpath*:**/applicationContext.xml");
		this.dao = context.getBean("todoDao", TodoDaoImpl.class);
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

	public List<TodoVo> select(int parentSeq, int level) {
		return dao.select(parentSeq, level);
	}
}
