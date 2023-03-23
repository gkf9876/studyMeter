package com.kdw.studyMeter.study.memorize.dao;

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

import com.kdw.studyMeter.study.memorize.vo.StudyMemorizeVo;

public class StudyMemorizeDaoImpl implements StudyMemorizeDao{
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<StudyMemorizeVo> select() {
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
					+ "		TB_STUDY_MEMORIZE"
					+ "	WHERE"
					+ "		1=1"
					+ "		AND USE_YN = 'Y'"
					+ "	ORDER BY"
					+ "		CASE WHEN CHECK_YN = 'Y' THEN '2' WHEN CHECK_YN = 'N' THEN '1' ELSE '0' END ASC, ODR ASC"
					+ "";

			return this.jdbcTemplate.query(sql, new RowMapper<StudyMemorizeVo>() {
				public StudyMemorizeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					StudyMemorizeVo vo = new StudyMemorizeVo();
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

	public StudyMemorizeVo selectOne(StudyMemorizeVo vo) {
		// TODO Auto-generated method stub
		return null;
	}

	public int insert(final StudyMemorizeVo vo) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = ""
				+ "	INSERT INTO "
				+ "		TB_STUDY_MEMORIZE("
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

	public int update(StudyMemorizeVo vo) {
		String sql = ""
				+ "	UPDATE "
				+ "		TB_STUDY_MEMORIZE "
				+ "	SET "
				+ "		SUBJECT = ?"
				+ "		, CHECK_YN = ?"
				+ "		, USE_YN = ?"
				+ "		, ODR = ?"
				+ "	WHERE "
				+ "		SEQ = ?";

		return this.jdbcTemplate.update(sql, new Object[]{vo.getSubject(), vo.getCheckYn(), vo.getUseYn(), vo.getOdr(), vo.getSeq()});
	}

	public List<StudyMemorizeVo> select(int parentSeq, int level) {
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
				+ "		TB_STUDY_MEMORIZE"
				+ "	WHERE"
				+ "		1=1"
				+ "		AND PARENT_SEQ = ?"
				+ "		AND LEVEL = ?"
				+ "		AND USE_YN = 'Y'"
				+ "	ORDER BY"
				+ "		CASE WHEN CHECK_YN = 'Y' THEN '2' WHEN CHECK_YN = 'N' THEN '1' ELSE '0' END ASC, ODR ASC"
				+ "";

		return this.jdbcTemplate.query(sql, new Object[] {parentSeq, level}, new RowMapper<StudyMemorizeVo>() {
			public StudyMemorizeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				StudyMemorizeVo vo = new StudyMemorizeVo();
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
