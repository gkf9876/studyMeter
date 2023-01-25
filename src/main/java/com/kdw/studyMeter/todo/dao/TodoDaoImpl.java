package com.kdw.studyMeter.todo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.kdw.studyMeter.study.vo.StudyVo;
import com.kdw.studyMeter.todo.vo.TodoVo;

public class TodoDaoImpl implements TodoDao{
	private Connection conn = null;
	
	public TodoDaoImpl(Connection conn) {
		this.conn = conn;
	}

	public List<TodoVo> select() {
		List<TodoVo> result = new ArrayList<TodoVo>();
		
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(""
					+ "	SELECT "
					+ "		SEQ"
					+ "		, SUBJECT"
					+ "		, USE_YN"
					+ "		, CHECK_YN"
					+ "		, CREATE_DATE"
					+ "	FROM "
					+ "		TB_TODO"
					+ "	WHERE"
					+ "		USE_YN = 'Y'"
					+ "	ORDER BY"
					+ "		CASE WHEN CHECK_YN = 'Y' THEN 1 ELSE 0 END ASC, SEQ ASC"
					+ "");
			while(rs.next()) {
				TodoVo vo = new TodoVo();
				vo.setSeq(rs.getInt("SEQ"));
				vo.setSubject(rs.getString("SUBJECT"));
				vo.setUseYn(rs.getString("USE_YN"));
				vo.setCheckYn(rs.getString("CHECK_YN"));
				vo.setCreateDate(rs.getString("CREATE_DATE"));
				result.add(vo);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public TodoVo selectOne(TodoVo vo) {
		// TODO Auto-generated method stub
		return null;
	}

	public int insert(TodoVo vo) {
		int result = 0;
		try {
			String sql = "INSERT INTO TB_TODO(SUBJECT, USE_YN, CHECK_YN, CREATE_DATE) VALUES(?, 'Y', ?, DATETIME('now', 'localtime'))";
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, vo.getSubject());
			pstmt.setString(2, vo.getCheckYn());
			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			result = rs.getInt("last_insert_rowid()");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int update(TodoVo vo) {
		int result = -1;
		try {
			String sql = ""
					+ "	UPDATE "
					+ "		TB_TODO "
					+ "	SET "
					+ "		SUBJECT = ?"
					+ "		, CHECK_YN = ?"
					+ "		, USE_YN = ?"
					+ "	WHERE "
					+ "		SEQ = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getSubject());
			pstmt.setString(2, vo.getCheckYn());
			pstmt.setString(3, vo.getUseYn());
			pstmt.setInt(4, vo.getSeq());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
