package com.kdw.studyMeter.todo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.kdw.studyMeter.todo.vo.TodoDetailVo;
import com.kdw.studyMeter.todo.vo.TodoVo;

public class TodoDetailDaoImpl implements TodoDetailDao{
	private Connection conn = null;
	
	public TodoDetailDaoImpl(Connection conn) {
		this.conn = conn;
	}

	public List<TodoDetailVo> select(TodoDetailVo todoDetailVo) {
		List<TodoDetailVo> result = new ArrayList<TodoDetailVo>();
		
		try {
			String sql = ""
					+ "	SELECT"
					+ "		SEQ"
					+ "		, PARENT_SEQ"
					+ "		, DATE"
					+ "		, CONTENTS"
					+ "		, FILE_SEQS"
					+ "		, USE_YN"
					+ "		, CREATE_DATE"
					+ "	FROM"
					+ "		TB_TODO_DETAIL"
					+ "	WHERE"
					+ "		1=1"
					+ "		AND USE_YN = 'Y'"
					+ "		AND PARENT_SEQ = ?"
					+ "	ORDER BY"
					+ "		DATE DESC"
					+ "";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, todoDetailVo.getParentSeq());
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				TodoDetailVo vo = new TodoDetailVo();
				vo.setSeq(rs.getInt("SEQ"));
				vo.setParentSeq(rs.getInt("PARENT_SEQ"));
				vo.setDate(rs.getString("DATE"));
				vo.setContents(rs.getString("CONTENTS"));
				vo.setFileSeqs(rs.getString("FILE_SEQS"));
				vo.setUseYn(rs.getString("USE_YN"));
				vo.setCreateDate(rs.getString("CREATE_DATE"));
				result.add(vo);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public TodoDetailVo selectOne(TodoDetailVo vo) {
		// TODO Auto-generated method stub
		return null;
	}

	public int insert(TodoDetailVo vo) {
		int result = 0;
		try {
			String sql = ""
					+ "	INSERT INTO "
					+ "		TB_TODO_DETAIL("
					+ "			PARENT_SEQ"
					+ "			, DATE"
					+ "			, CONTENTS"
					+ "			, FILE_SEQS"
					+ "			, USE_YN"
					+ "		)VALUES("
					+ "			?"
					+ "			, ?"
					+ "			, ?"
					+ "			, ?"
					+ "			, 'Y'"
					+ "		)";
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, vo.getParentSeq());
			pstmt.setString(2, vo.getDate());
			pstmt.setString(3, vo.getContents());
			pstmt.setString(4, vo.getFileSeqs());
			
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

	public int update(TodoDetailVo vo) {
		int result = -1;
		try {
			String sql = ""
					+ "	UPDATE "
					+ "		TB_TODO_DETAIL "
					+ "	SET "
					+ "		DATE = ?"
					+ "		, CONTENTS = ?"
					+ "		, FILE_SEQS = ?"
					+ "		, USE_YN = ?"
					+ "	WHERE "
					+ "		SEQ = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getDate());
			pstmt.setString(2, vo.getContents());
			pstmt.setString(3, vo.getFileSeqs());
			pstmt.setString(4, vo.getUseYn());
			pstmt.setInt(5, vo.getSeq());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
