package com.kdw.studyMeter.todo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.kdw.studyMeter.todo.vo.TodoDetailVo;

public class TodoDetailDaoImpl implements TodoDetailDao{
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<TodoDetailVo> select(TodoDetailVo todoDetailVo) {
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
				+ "		DATE DESC, CREATE_DATE DESC"
				+ "";

		return this.jdbcTemplate.query(sql, new Object[] {todoDetailVo.getParentSeq()}, new RowMapper<TodoDetailVo>() {
			public TodoDetailVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				TodoDetailVo vo = new TodoDetailVo();
				vo.setSeq(rs.getInt("SEQ"));
				vo.setParentSeq(rs.getInt("PARENT_SEQ"));
				vo.setDate(rs.getString("DATE"));
				vo.setContents(rs.getString("CONTENTS"));
				vo.setFileSeqs(rs.getString("FILE_SEQS"));
				vo.setUseYn(rs.getString("USE_YN"));
				vo.setCreateDate(rs.getString("CREATE_DATE"));
				return vo;
			}
		});
	}

	public int insert(final TodoDetailVo vo) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = ""
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

		this.jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	            PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, vo.getParentSeq());
				pstmt.setString(2, vo.getDate());
				pstmt.setString(3, vo.getContents());
				pstmt.setString(4, vo.getFileSeqs());
	            return pstmt;
			}
		}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}

	public int update(TodoDetailVo vo) {
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

		return this.jdbcTemplate.update(sql, new Object[]{vo.getDate(), vo.getContents(), vo.getFileSeqs(), vo.getUseYn(), vo.getSeq()});
	}

}
