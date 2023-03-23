package com.kdw.studyMeter.study.memorize.vo;

public class StudyMemorizeVo {
	private int seq;
	private int parentSeq;
	private int level;
	private String subject;
	private String useYn;
	private String checkYn;
	private int odr;
	private String createDate;
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
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getCheckYn() {
		return checkYn;
	}
	public void setCheckYn(String checkYn) {
		this.checkYn = checkYn;
	}
	public int getOdr() {
		return odr;
	}
	public void setOdr(int odr) {
		this.odr = odr;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
