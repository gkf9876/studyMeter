package com.kdw.studyMeter.study.memorize.vo;

public class StudyMemorizeDetailVo {
	private int seq;
	private int parentSeq;
	private String date;
	private String contents;
	private String useYn;
	private String createDate;
	private String fileSeqs;
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
}
