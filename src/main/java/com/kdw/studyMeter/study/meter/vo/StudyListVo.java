package com.kdw.studyMeter.study.meter.vo;

public class StudyListVo {
	private int seq;
	private String studyNm;
	private String studyType;
	private String createDate;
	private String useYn;
	private int fileSeq;
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
	public String getStudyType() {
		return studyType;
	}
	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public int getFileSeq() {
		return fileSeq;
	}
	public void setFileSeq(int fileSeq) {
		this.fileSeq = fileSeq;
	}
}
