package com.kdw.studyMeter.study.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.kdw.studyMeter.study.vo.StudyListVo;

public class StudyListDaoImpl implements StudyListDao{
	private Connection conn = null;

	public StudyListDaoImpl(Connection conn){
		this.conn = conn;
	}
	
	public List<StudyListVo> select() {
		List<StudyListVo> result = new ArrayList<StudyListVo>();
		
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(""
					+ "	SELECT "
					+ "		SEQ"
					+ "		, STUDY_NM"
					+ "		, STUDY_TYPE"
					+ "		, CREATE_DATE"
					+ "		, USE_YN"
					+ "	FROM "
					+ "		TB_STUDY_LIST"
					+ "	WHERE"
					+ "		USE_YN = 'Y'"
					+ "	ORDER BY"
					+ "		CREATE_DATE ASC");
			while(rs.next()) {
				StudyListVo vo = new StudyListVo();
				vo.setSeq(rs.getInt("SEQ"));
				vo.setStudyNm(rs.getString("STUDY_NM"));
				vo.setStudyType(rs.getString("STUDY_TYPE"));
				vo.setCreateDate(rs.getString("CREATE_DATE"));
				vo.setUseYn(rs.getString("USE_YN"));
				result.add(vo);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public StudyListVo selectOne(StudyListVo vo) {
		StudyListVo result = new StudyListVo();

		try {
			String sql = ""
					+ "	SELECT "
					+ "		* "
					+ "	FROM "
					+ "		TB_STUDY_LIST "
					+ "	WHERE "
					+ "		SEQ = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, vo.getSeq());
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result.setSeq(rs.getInt("SEQ"));
				result.setStudyNm(rs.getString("STUDY_NM"));
				result.setStudyType(rs.getString("STUDY_TYPE"));
				result.setCreateDate(rs.getString("CREATE_DATE"));
				result.setUseYn(rs.getString("USE_YN"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int insert(StudyListVo vo) {
		int result = 0;
		try {
			String sql = ""
					+ "	INSERT INTO "
					+ "		TB_STUDY_LIST("
					+ "			STUDY_NM"
					+ "			, STUDY_TYPE"
					+ "		) VALUES("
					+ "			?"
					+ "			, ?"
					+ "		)";
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, vo.getStudyNm());
			pstmt.setString(2, vo.getStudyType());
			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			result = rs.getInt("last_insert_rowid()");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int update(StudyListVo vo) {
		int result = -1;
		try {
			String sql = ""
					+ "	UPDATE "
					+ "		TB_STUDY_LIST "
					+ "	SET "
					+ "		STUDY_NM = ?"
					+ "		, STUDY_TYPE = ?"
					+ "		, USE_YN = ?"
					+ "	WHERE "
					+ "		SEQ = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getStudyNm());
			pstmt.setString(2, vo.getStudyType());
			pstmt.setString(3, vo.getUseYn());
			pstmt.setInt(4, vo.getSeq());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
