package com.kdw.studyMeter.todo.service;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kdw.studyMeter.todo.dao.TodoDaoImpl;
import com.kdw.studyMeter.todo.dao.TodoDetailDao;
import com.kdw.studyMeter.todo.dao.TodoDetailDaoImpl;
import com.kdw.studyMeter.todo.vo.TodoDetailVo;

public class TodoDetailService {
	private TodoDetailDao dao;
	
	public TodoDetailService(){
		ApplicationContext context = new GenericXmlApplicationContext("classpath*:**/applicationContext.xml");
		this.dao = context.getBean("todoDetailDao", TodoDetailDaoImpl.class);
	}

	public List<TodoDetailVo> select(TodoDetailVo vo) {
		return dao.select(vo);
	}

	public int insert(TodoDetailVo vo) {
		return dao.insert(vo);
	}

	public int update(TodoDetailVo vo) {
		return dao.update(vo);
	}
}
