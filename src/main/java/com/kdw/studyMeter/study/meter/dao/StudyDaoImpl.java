package com.kdw.studyMeter.study.meter.dao;

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

import com.kdw.studyMeter.study.meter.vo.StudyVo;

public class StudyDaoImpl implements StudyDao{
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<StudyVo> select() {
		String sql = ""
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
				+ "		TB_STUDY A";

		return this.jdbcTemplate.query(sql, new RowMapper<StudyVo>() {
			public StudyVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				StudyVo vo = new StudyVo();
				vo.setSeq(rs.getInt("SEQ"));
				vo.setStudyNm(rs.getString("STUDY_NM"));
				vo.setStartDate(rs.getString("START_DATE"));
				vo.setEndDate(rs.getString("END_DATE"));
				vo.setMemo(rs.getString("MEMO"));
				vo.setStudySeq(rs.getInt("STUDY_SEQ"));
				vo.setStudyType(rs.getString("STUDY_TYPE"));
				return vo;
			}
		});
	}

	public StudyVo selectOne(StudyVo vo) {
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
		
		return this.jdbcTemplate.queryForObject(sql, new Object[] {vo.getSeq()}, new RowMapper<StudyVo>() {
			public StudyVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				StudyVo vo = new StudyVo();
				vo.setSeq(rs.getInt("SEQ"));
				vo.setStudySeq(rs.getInt("STUDY_SEQ"));
				vo.setStudyType(rs.getString("STUDY_TYPE"));
				vo.setStudyNm(rs.getString("STUDY_NM"));
				vo.setStartDate(rs.getString("START_DATE"));
				vo.setEndDate(rs.getString("END_DATE"));
				vo.setMemo(rs.getString("MEMO"));
				return vo;
			}
		});
	}
	
	public List<StudyVo> selectChart(int cnt) {
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
			
			return this.jdbcTemplate.query(sql, new Object[] {cnt}, new RowMapper<StudyVo>() {
				public StudyVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					StudyVo vo = new StudyVo();
					vo.setDate(rs.getString("DATE"));
					vo.setStudyMin(rs.getInt("STUDY_MIN"));
					return vo;
				}
			});
	}
	
	public int insert(final StudyVo vo) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = ""
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

		this.jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	            PreparedStatement pstmt = con.prepareStatement(sql);
	            pstmt.setInt(1, vo.getStudySeq());
	            pstmt.setString(2, vo.getMemo());
	            return pstmt;
			}
		}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}

	public int update(StudyVo vo) {
		String sql = ""
				+ "	UPDATE "
				+ "		TB_STUDY "
				+ "	SET "
				+ "		END_DATE = DATETIME('now', 'localtime')"
				+ "	WHERE "
				+ "		SEQ = ?";

		return this.jdbcTemplate.update(sql, vo.getSeq());
	}
}
