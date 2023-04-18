package com.kdw.studyMeter.todo.vo;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdw.studyMeter.file.vo.FileVo;

public class TodoDetailVo {
	private int seq;
	private int parentSeq;
	private String date;
	private String contents;
	private String useYn;
	private String createDate;
	private String fileSeqs;
	private List<FileVo> fileInfos;
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getParentSeq() {
		return parentSeq;
	}
	public void setParentSeq(int parentSeq) {
		this.parentSeq = parentSeq;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getFileSeqs() {
		return fileSeqs;
	}
	public void setFileSeqs(String fileSeqs) {
		this.fileSeqs = fileSeqs;
	}
	public List<FileVo> getFileInfos() {
		return fileInfos;
	}
	public void setFileInfos(String fileInfos) {
		this.fileInfos = new ArrayList<FileVo>();
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonStr = fileInfos ;
	
			JSONParser parser = new JSONParser();
			JSONArray jsonarray = (JSONArray)parser.parse(jsonStr);
			for(int i=0; i<jsonarray.size(); i++) {
				JSONObject obj = (JSONObject)jsonarray.get(i);
				FileVo fileVo = mapper.readValue(obj.toString(), FileVo.class);
				this.fileInfos.add(fileVo);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
