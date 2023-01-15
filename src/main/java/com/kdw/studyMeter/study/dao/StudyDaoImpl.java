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

	public StudyDaoImpl(){
		try {
			Class.forName("org.sqlite.JDBC");
			URL resource = getClass().getClassLoader().getResource("db");
			String filePath = resource.getFile();
			conn = DriverManager.getConnection("jdbc:sqlite:" + filePath);
			
			//conn = DriverManager.getConnection("jdbc:sqlite:D:\\Users\\gkf9876\\eclipse-workspace\\StudyMeter\\src\\main\\resources\\db");
			//conn = DriverManager.getConnection("jdbc:sqlite:db");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<StudyVo> select() {
		List<StudyVo> result = new ArrayList<StudyVo>();
		
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM TB_STUDY");
			while(rs.next()) {
				StudyVo vo = new StudyVo();
				vo.setSeq(rs.getInt("SEQ"));
				vo.setStudyNm(rs.getString("STUDY_NAME"));
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
			String sql = "SELECT * FROM TB_STUDY WHERE SEQ = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, vo.getSeq());
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result.setSeq(rs.getInt("SEQ"));
				result.setStudyNm(rs.getString("STUDY_NM"));
				result.setStartDate(rs.getString("START_DATE"));
				result.setEndDate(rs.getString("END_DATE"));
				result.setMemo(rs.getString("MEMO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int insert(StudyVo vo) {
		int result = 0;
		try {
			String sql = "INSERT INTO TB_STUDY(STUDY_NM, START_DATE, MEMO) VALUES(?, DATETIME('now', 'localtime'), ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, vo.getStudyNm());
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
		int result = 0;
		try {
			Statement stat = conn.createStatement();
			result = stat.executeUpdate("UPDATE TB_STUDY SET "
					+ "END_DATE = DATETIME('now', 'localtime')"
					+ "WHERE SEQ = "
					+ vo.getSeq() + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
