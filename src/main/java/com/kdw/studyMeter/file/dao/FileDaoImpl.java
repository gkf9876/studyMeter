package com.kdw.studyMeter.file.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.kdw.studyMeter.file.vo.FileVo;

public class FileDaoImpl implements FileDao{
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public FileVo selectOne(FileVo vo) {
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
		
		return this.jdbcTemplate.queryForObject(sql, new Object[] {vo.getSeq()}, new RowMapper<FileVo>() {
			public FileVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FileVo vo = new FileVo();
				vo.setSeq(rs.getInt("SEQ"));
				vo.setFilePath(rs.getString("FILE_PATH"));
				vo.setFileName(rs.getString("FILE_NAME"));
				vo.setFileSize(rs.getInt("FILE_SIZE"));
				vo.setFileExtns(rs.getString("FILE_EXTNS"));
				vo.setUseYn(rs.getString("USE_YN"));
				vo.setCreateDate(rs.getString("CREATE_DATE"));
				return vo;
			}
		});
	}

	public int insert(final FileVo vo) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = ""
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

		this.jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	            PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, vo.getFilePath());
				pstmt.setString(2, vo.getFileName());
				pstmt.setInt(3, vo.getFileSize());
				pstmt.setString(4, vo.getFileExtns());
	            return pstmt;
			}
		}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}

	public int update(FileVo vo) {
		String sql = ""
				+ "	UPDATE "
				+ "		TB_FILE "
				+ "	SET "
				+ "		FILE_PATH = ?"
				+ "		, USE_YN = ?"
				+ "	WHERE "
				+ "		SEQ = ?";

		return this.jdbcTemplate.update(sql, vo.getFilePath(), vo.getUseYn(), vo.getSeq());
	}

}
