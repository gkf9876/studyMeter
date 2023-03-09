package com.kdw.studyMeter.todo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.kdw.studyMeter.study.vo.StudyVo;
import com.kdw.studyMeter.todo.vo.TodoVo;

public class TodoDaoImpl implements TodoDao{
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<TodoVo> select() {
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
					+ "		AND USE_YN = 'Y'"
					+ "	ORDER BY"
					+ "		CASE WHEN CHECK_YN = 'Y' THEN '2' WHEN CHECK_YN = 'N' THEN '1' ELSE '0' END ASC, ODR ASC"
					+ "";

			return this.jdbcTemplate.query(sql, new RowMapper<TodoVo>() {
				public TodoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					TodoVo vo = new TodoVo();
					vo.setSeq(rs.getInt("SEQ"));
					vo.setParentSeq(rs.getInt("PARENT_SEQ"));
					vo.setLevel(rs.getInt("LEVEL"));
					vo.setSubject(rs.getString("SUBJECT"));
					vo.setUseYn(rs.getString("USE_YN"));
					vo.setCheckYn(rs.getString("CHECK_YN"));
					vo.setOdr(rs.getInt("ODR"));
					vo.setCreateDate(rs.getString("CREATE_DATE"));
					return vo;
				}
			});
	}

	public TodoVo selectOne(TodoVo vo) {
		// TODO Auto-generated method stub
		return null;
	}

	public int insert(final TodoVo vo) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = ""
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

		this.jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	            PreparedStatement pstmt = con.prepareStatement(sql);
	            pstmt.setInt(1, vo.getParentSeq());
	            pstmt.setInt(2, vo.getLevel());
	            pstmt.setString(3, vo.getSubject());
	            pstmt.setString(4, vo.getCheckYn());
	            pstmt.setInt(5, vo.getOdr());
	            return pstmt;
			}
		}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}

	public int update(TodoVo vo) {
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

		return this.jdbcTemplate.update(sql, new Object[]{vo.getSubject(), vo.getCheckYn(), vo.getUseYn(), vo.getOdr(), vo.getSeq()});
	}

	public List<TodoVo> select(int parentSeq, int level) {
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
				+ "		CASE WHEN CHECK_YN = 'Y' THEN '2' WHEN CHECK_YN = 'N' THEN '1' ELSE '0' END ASC, ODR ASC"
				+ "";

		return this.jdbcTemplate.query(sql, new Object[] {parentSeq, level}, new RowMapper<TodoVo>() {
			public TodoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				TodoVo vo = new TodoVo();
				vo.setSeq(rs.getInt("SEQ"));
				vo.setParentSeq(rs.getInt("PARENT_SEQ"));
				vo.setLevel(rs.getInt("LEVEL"));
				vo.setSubject(rs.getString("SUBJECT"));
				vo.setUseYn(rs.getString("USE_YN"));
				vo.setCheckYn(rs.getString("CHECK_YN"));
				vo.setOdr(rs.getInt("ODR"));
				vo.setCreateDate(rs.getString("CREATE_DATE"));
				return vo;
			}
		});
	}
}
