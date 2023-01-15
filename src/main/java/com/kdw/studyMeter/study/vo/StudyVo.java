package com.kdw.studyMeter.study.vo;

public class StudyVo {
	private int seq;
	private String studyNm;
	private String startDate;
	private String endDate;
	private String memo;
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getStudyNm() {
		return studyNm;
	}
	public void setStudyNm(String studyNm) {
		this.studyNm = studyNm;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
