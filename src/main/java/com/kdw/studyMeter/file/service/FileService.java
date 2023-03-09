package com.kdw.studyMeter.file.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kdw.studyMeter.file.dao.FileDao;
import com.kdw.studyMeter.file.dao.FileDaoImpl;
import com.kdw.studyMeter.file.vo.FileVo;

public class FileService {
	private FileDao dao;

	public FileService(){
		ApplicationContext context = new GenericXmlApplicationContext("classpath*:**/applicationContext.xml");
		this.dao = context.getBean("fileDao", FileDaoImpl.class);
	}
	
	public FileVo selectOne(FileVo vo) {
		return dao.selectOne(vo);
	}
	
	public int insert(FileVo vo) {
		int result = -1;
		try {
			result = dao.insert(vo);
			
			Date now = new Date();
			SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			
			File inputFile = new File(vo.getFilePath());
			FileInputStream inputStream = new FileInputStream(inputFile);
			File outputFile = new File("data/" + dtFormat.format(now) + "_" + result);
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			
			Path path = Paths.get(vo.getFilePath());
			System.out.println("FilePath : " + vo.getFilePath() + ", size : " + Files.size(path));
			
			int c;
			double size = 0;
			byte[] bytes = new byte[1024];
			while((c = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, c);
				size += c;
			}
			outputStream.flush();
			inputStream.close();
			outputStream.close();
			
			vo.setSeq(result);
			vo.setFilePath("data/" + dtFormat.format(now) + "_" + result);
			vo.setUseYn("Y");
			dao.update(vo);
			System.out.println("get size : " + size);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int update(FileVo vo) {
		return dao.update(vo);
	}
}
