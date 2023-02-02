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
					+ "		, PARENT_SEQ"
					+ "		, LEVEL"
					+ "		, SUBJECT"
					+ "		, USE_YN"
					+ "		, CHECK_YN"
					+ "		, ODR"
					+ "		, CREATE_DATE"
					+ "	FROM "
					+ "		TB_TODO"
					+ "	WHERE"
					+ "		1=1"
					+ "		AND USE_YN = 'Y'"
					+ "	ORDER BY"
					+ "		ODR ASC"
					+ "");
			while(rs.next()) {
				TodoVo vo = new TodoVo();
				vo.setSeq(rs.getInt("SEQ"));
				vo.setParentSeq(rs.getInt("PARENT_SEQ"));
				vo.setLevel(rs.getInt("LEVEL"));
				vo.setSubject(rs.getString("SUBJECT"));
				vo.setUseYn(rs.getString("USE_YN"));
				vo.setCheckYn(rs.getString("CHECK_YN"));
				vo.setOdr(rs.getInt("ODR"));
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
			String sql = ""
					+ "	INSERT INTO "
					+ "		TB_TODO("
					+ "			PARENT_SEQ"
					+ "			, LEVEL"
					+ "			, SUBJECT"
					+ "			, USE_YN"
					+ "			, CHECK_YN"
					+ "			, ODR"
					+ "			, CREATE_DATE"
					+ "		)VALUES("
					+ "			?"
					+ "			, ?"
					+ "			, ?"
					+ "			, 'Y'"
					+ "			, ?"
					+ "			, ?"
					+ "			, DATETIME('now', 'localtime')"
					+ "		)";
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, vo.getParentSeq());
			pstmt.setInt(2, vo.getLevel());
			pstmt.setString(3, vo.getSubject());
			pstmt.setString(4, vo.getCheckYn());
			pstmt.setInt(5, vo.getOdr());
			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			vo.setSeq(rs.getInt("last_insert_rowid()"));
			result = vo.getSeq();
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
					+ "		, ODR = ?"
					+ "	WHERE "
					+ "		SEQ = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getSubject());
			pstmt.setString(2, vo.getCheckYn());
			pstmt.setString(3, vo.getUseYn());
			pstmt.setInt(4, vo.getOdr());
			pstmt.setInt(5, vo.getSeq());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public List<TodoVo> select(int parentSeq, int level) {
		List<TodoVo> result = new ArrayList<TodoVo>();
		
		try {
			String sql = ""
					+ "	SELECT "
					+ "		SEQ"
					+ "		, PARENT_SEQ"
					+ "		, LEVEL"
					+ "		, SUBJECT"
					+ "		, USE_YN"
					+ "		, CHECK_YN"
					+ "		, ODR"
					+ "		, CREATE_DATE"
					+ "	FROM "
					+ "		TB_TODO"
					+ "	WHERE"
					+ "		1=1"
					+ "		AND PARENT_SEQ = ?"
					+ "		AND LEVEL = ?"
					+ "		AND USE_YN = 'Y'"
					+ "	ORDER BY"
					+ "		ODR ASC"
					+ "";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, parentSeq);
			pstmt.setInt(2, level);
			
			pstmt.execute();
			ResultSet rs = pstmt.getResultSet();
			
			while(rs.next()) {
				TodoVo vo = new TodoVo();
				vo.setSeq(rs.getInt("SEQ"));
				vo.setParentSeq(rs.getInt("PARENT_SEQ"));
				vo.setLevel(rs.getInt("LEVEL"));
				vo.setSubject(rs.getString("SUBJECT"));
				vo.setUseYn(rs.getString("USE_YN"));
				vo.setCheckYn(rs.getString("CHECK_YN"));
				vo.setOdr(rs.getInt("ODR"));
				vo.setCreateDate(rs.getString("CREATE_DATE"));
				result.add(vo);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
