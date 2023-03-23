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

import com.kdw.studyMeter.study.memorize.vo.StudyMemorizeDetailVo;

public class StudyMemorizeDetailDaoImpl implements StudyMemorizeDetailDao{
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<StudyMemorizeDetailVo> select(StudyMemorizeDetailVo todoDetailVo) {
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
				+ "		TB_STUDY_MEMORIZE_DETAIL"
				+ "	WHERE"
				+ "		1=1"
				+ "		AND USE_YN = 'Y'"
				+ "		AND PARENT_SEQ = ?"
				+ "	ORDER BY"
				+ "		DATE DESC, CREATE_DATE DESC"
				+ "";

		return this.jdbcTemplate.query(sql, new Object[] {todoDetailVo.getParentSeq()}, new RowMapper<StudyMemorizeDetailVo>() {
			public StudyMemorizeDetailVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				StudyMemorizeDetailVo vo = new StudyMemorizeDetailVo();
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

	public int insert(final StudyMemorizeDetailVo vo) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = ""
				+ "	INSERT INTO "
				+ "		TB_STUDY_MEMORIZE_DETAIL("
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

	public int update(StudyMemorizeDetailVo vo) {
		String sql = ""
				+ "	UPDATE "
				+ "		TB_STUDY_MEMORIZE_DETAIL "
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
