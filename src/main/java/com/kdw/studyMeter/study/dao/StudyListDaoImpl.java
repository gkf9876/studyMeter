package com.kdw.studyMeter.study.dao;

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

import com.kdw.studyMeter.study.vo.StudyListVo;

public class StudyListDaoImpl implements StudyListDao{
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<StudyListVo> select() {
		String sql = ""
				+ "	SELECT "
				+ "		SEQ"
				+ "		, STUDY_NM"
				+ "		, STUDY_TYPE"
				+ "		, CREATE_DATE"
				+ "		, USE_YN"
				+ "		, FILE_SEQ"
				+ "	FROM "
				+ "		TB_STUDY_LIST"
				+ "	WHERE"
				+ "		USE_YN = 'Y'"
				+ "	ORDER BY"
				+ "		CREATE_DATE ASC";

		return this.jdbcTemplate.query(sql, new RowMapper<StudyListVo>() {
			public StudyListVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				StudyListVo vo = new StudyListVo();
				vo.setSeq(rs.getInt("SEQ"));
				vo.setStudyNm(rs.getString("STUDY_NM"));
				vo.setStudyType(rs.getString("STUDY_TYPE"));
				vo.setCreateDate(rs.getString("CREATE_DATE"));
				vo.setUseYn(rs.getString("USE_YN"));
				vo.setFileSeq(rs.getInt("FILE_SEQ"));
				return vo;
			}
		});
	}

	public StudyListVo selectOne(StudyListVo vo) {
		String sql = ""
				+ "	SELECT "
				+ "		* "
				+ "	FROM "
				+ "		TB_STUDY_LIST "
				+ "	WHERE "
				+ "		SEQ = ?";
		
		return this.jdbcTemplate.queryForObject(sql, new Object[] {vo.getSeq()}, new RowMapper<StudyListVo>() {
			public StudyListVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				StudyListVo vo = new StudyListVo();
				vo.setSeq(rs.getInt("SEQ"));
				vo.setStudyNm(rs.getString("STUDY_NM"));
				vo.setStudyType(rs.getString("STUDY_TYPE"));
				vo.setCreateDate(rs.getString("CREATE_DATE"));
				vo.setUseYn(rs.getString("USE_YN"));
				vo.setFileSeq(rs.getInt("FILE_SEQ"));
				return vo;
			}
		});
	}
	
	public int insert(final StudyListVo vo) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = ""
				+ "	INSERT INTO "
				+ "		TB_STUDY_LIST("
				+ "			STUDY_NM"
				+ "			, STUDY_TYPE"
				+ "		) VALUES("
				+ "			?"
				+ "			, ?"
				+ "		)";

		this.jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	            PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, vo.getStudyNm());
				pstmt.setString(2, vo.getStudyType());
	            return pstmt;
			}
		}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}

	public int update(StudyListVo vo) {
		String sql = ""
				+ "	UPDATE "
				+ "		TB_STUDY_LIST "
				+ "	SET "
				+ "		STUDY_NM = ?"
				+ "		, STUDY_TYPE = ?"
				+ "		, USE_YN = ?"
				+ "		, FILE_SEQ = ?"
				+ "	WHERE "
				+ "		SEQ = ?";

		return this.jdbcTemplate.update(sql, new Object[] {vo.getStudyNm(), vo.getStudyType(),  vo.getUseYn(), vo.getFileSeq(), vo.getSeq()});
	}
}
