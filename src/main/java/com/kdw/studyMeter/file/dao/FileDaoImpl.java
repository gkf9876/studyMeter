package com.kdw.studyMeter.file.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.kdw.studyMeter.file.vo.FileVo;
import com.kdw.studyMeter.study.vo.StudyVo;

public class FileDaoImpl implements FileDao{
	private Connection conn = null;

	public FileDaoImpl(Connection conn){
		this.conn = conn;
	}

	public FileVo selectOne(FileVo vo) {
		FileVo result = new FileVo();

		try {
			String sql = ""
					+ "	SELECT "
					+ "		SEQ"
					+ "		, FILE_PATH"
					+ "		, FILE_NAME"
					+ "		, FILE_SIZE"
					+ "		, FILE_EXTNS"
					+ "		, USE_YN"
					+ "		, CREATE_DATE "
					+ "	FROM "
					+ "		TB_FILE "
					+ "	WHERE "
					+ "		1=1"
					+ "		AND USE_YN = 'Y'"
					+ "		AND SEQ = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, vo.getSeq());
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result.setSeq(rs.getInt("SEQ"));
				result.setFilePath(rs.getString("FILE_PATH"));
				result.setFileName(rs.getString("FILE_NAME"));
				result.setFileSize(rs.getInt("FILE_SIZE"));
				result.setFileExtns(rs.getString("FILE_EXTNS"));
				result.setUseYn(rs.getString("USE_YN"));
				result.setCreateDate(rs.getString("CREATE_DATE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int insert(FileVo vo) {
		int result = 0;
		try {
			String sql = ""
					+ "	INSERT "
					+ "		INTO "
					+ "		TB_FILE("
					+ "			FILE_PATH"
					+ "			, FILE_NAME"
					+ "			, FILE_SIZE"
					+ "			, FILE_EXTNS"
					+ "		) VALUES("
					+ "			?"
					+ "			, ?"
					+ "			, ?"
					+ "			, ?"
					+ "		)";
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, vo.getFilePath());
			pstmt.setString(2, vo.getFileName());
			pstmt.setInt(3, vo.getFileSize());
			pstmt.setString(4, vo.getFileExtns());
			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			result = rs.getInt("last_insert_rowid()");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int update(FileVo vo) {
		int result = -1;
		try {
			String sql = ""
					+ "	UPDATE "
					+ "		TB_FILE "
					+ "	SET "
					+ "		FILE_PATH = ?"
					+ "		, USE_YN = ?"
					+ "	WHERE "
					+ "		SEQ = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getFilePath());
			pstmt.setString(2, vo.getUseYn());
			pstmt.setInt(3, vo.getSeq());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
