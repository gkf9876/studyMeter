package com.kdw.studyMeter.study.dao;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.kdw.studyMeter.study.vo.StudyVo;

public class StudyDaoImpl implements StudyDao{
	private Connection conn = null;

	public StudyDaoImpl(Connection conn){
		this.conn = conn;
	}
	
	public List<StudyVo> select() {
		List<StudyVo> result = new ArrayList<StudyVo>();
		
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(""
					+ "	SELECT "
					+ "		SEQ"
					+ "		, STUDY_SEQ"
					+ "		, (SELECT STUDY_TYPE FROM TB_STUDY_LIST WHERE SEQ = A.STUDY_SEQ) STUDY_TYPE"
					+ "		, (SELECT STUDY_NM FROM TB_STUDY_LIST WHERE SEQ = A.STUDY_SEQ) STUDY_NM"
					+ "		, START_DATE"
					+ "		, END_DATE"
					+ "		, MEMO"
					+ "		, ((STRFTIME('%s', END_DATE) - STRFTIME('%s', START_DATE)) / 60) AS STUDY_TIME"
					+ "	FROM "
					+ "		TB_STUDY A");
			while(rs.next()) {
				StudyVo vo = new StudyVo();
				vo.setSeq(rs.getInt("SEQ"));
				vo.setStudySeq(rs.getInt("STUDY_SEQ"));
				vo.setStudyNm(rs.getString("STUDY_NAME"));
				vo.setStudyType(rs.getString("STUDY_TYPE"));
				vo.setStartDate(rs.getString("START_DATE"));
				vo.setEndDate(rs.getString("END_DATE"));
				vo.setMemo(rs.getString("MEMO"));
				result.add(vo);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public StudyVo selectOne(StudyVo vo) {
		StudyVo result = new StudyVo();

		try {
			String sql = ""
					+ "	SELECT "
					+ "		SEQ"
					+ "		, STUDY_SEQ"
					+ "		, (SELECT STUDY_TYPE FROM TB_STUDY_LIST WHERE SEQ = A.STUDY_SEQ) STUDY_TYPE"
					+ "		, (SELECT STUDY_NM FROM TB_STUDY_LIST WHERE SEQ = A.STUDY_SEQ) STUDY_NM"
					+ "		, START_DATE"
					+ "		, END_DATE"
					+ "		, MEMO"
					+ "	FROM "
					+ "		TB_STUDY A"
					+ "	WHERE "
					+ "		SEQ = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, vo.getSeq());
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result.setSeq(rs.getInt("SEQ"));
				result.setStudySeq(rs.getInt("STUDY_SEQ"));
				result.setStudyNm(rs.getString("STUDY_NM"));
				result.setStudyType(rs.getString("STUDY_TYPE"));
				result.setStartDate(rs.getString("START_DATE"));
				result.setEndDate(rs.getString("END_DATE"));
				result.setMemo(rs.getString("MEMO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<StudyVo> selectChart(int cnt) {
		List<StudyVo> result = new ArrayList<StudyVo>();
		
		try {
			Statement stat = conn.createStatement();
			String sql = ""
					+ "	SELECT"
					+ "		DATE"
					+ "		, STUDY_MIN"
					+ "		, ROWNUMBER"
					+ "	FROM ("
					+ "		SELECT"
					+ "			DATE"
					+ "			, SUM(STUDY_MIN) AS STUDY_MIN"
					+ "			, ROW_NUMBER() OVER(ORDER BY DATE DESC) ROWNUMBER"
					+ "		FROM ("
					+ "			SELECT"
					+ "				ts.*"
					+ "				, (STRFTIME('%s', END_DATE) - STRFTIME('%s', START_DATE)) / 60 AS STUDY_MIN"
					+ "				, STRFTIME('%Y.%m.%d', START_DATE) DATE"
					+ "			FROM"
					+ "				TB_STUDY ts"
					+ "		) A"
					+ "		GROUP BY"
					+ "			DATE"
					+ "		ORDER BY"
					+ "			DATE ASC"
					+ "	)"
					+ "	WHERE"
					+ "		ROWNUMBER <= ?"
					+ "";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cnt);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				StudyVo vo = new StudyVo();
				vo.setDate(rs.getString("DATE"));
				vo.setStudyMin(rs.getInt("STUDY_MIN"));
				result.add(vo);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int insert(StudyVo vo) {
		int result = 0;
		try {
			String sql = ""
					+ "	INSERT INTO "
					+ "		TB_STUDY("
					+ "			STUDY_SEQ"
					+ "			, START_DATE"
					+ "			, END_DATE"
					+ "			, MEMO"
					+ "		) VALUES("
					+ "			?"
					+ "			, DATETIME('now', 'localtime')"
					+ "			, DATETIME('now', 'localtime')"
					+ "			, ?"
					+ "		)";
			
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, vo.getStudySeq());
			pstmt.setString(2, vo.getMemo());
			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			result = rs.getInt("last_insert_rowid()");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int update(StudyVo vo) {
		int result = -1;
		try {
			String sql = ""
					+ "	UPDATE "
					+ "		TB_STUDY "
					+ "	SET "
					+ "		END_DATE = DATETIME('now', 'localtime')"
					+ "	WHERE "
					+ "		SEQ = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, vo.getSeq());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
