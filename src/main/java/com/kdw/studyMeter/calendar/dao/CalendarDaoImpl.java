package com.kdw.studyMeter.calendar.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.kdw.studyMeter.calendar.vo.CalendarVo;

public class CalendarDaoImpl implements CalendarDao{
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<CalendarVo> select(CalendarVo calendarVo) {
		String sql = ""
				+ "	SELECT"
				+ "		DATE"
				+ "		, CONTENTS"
				+ "	FROM ("
				+ "		SELECT"
				+ "			DATE"
				+ "			, '독서시간 : ' || SUM(STUDY_MIN) || '분' AS CONTENTS"
				+ "			, '1' AS ODR"
				+ "		FROM ("
				+ "			SELECT"
				+ "				(STRFTIME('%s', END_DATE) - STRFTIME('%s', START_DATE)) / 60 AS STUDY_MIN"
				+ "				, STRFTIME('%Y-%m-%d', START_DATE) DATE"
				+ "			FROM"
				+ "				TB_STUDY ts"
				+ "		) A"
				+ "		GROUP BY"
				+ "			DATE"
				+ "		UNION ALL"
				+ "		SELECT"
				+ "			DATE"
				+ "			, (SELECT SUBJECT FROM TB_TODO WHERE SEQ = A.PARENT_SEQ) CONTENTS"
				+ "			, '2' AS ODR"
				+ "		FROM"
				+ "			TB_TODO_DETAIL A"
				+ "	)"
				+ "	WHERE"
				+ "		1=1"
				+ "		AND STRFTIME('%Y-%m', DATE) = ?"
				+ "	ORDER BY"
				+ "		DATE DESC, ODR ASC"
				+ "";

		return this.jdbcTemplate.query(sql, new Object[] {calendarVo.getSchYearMonthDate()}, new RowMapper<CalendarVo>() {
			public CalendarVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CalendarVo vo = new CalendarVo();
				vo.setDate(rs.getString("DATE"));
				vo.setContents(rs.getString("CONTENTS"));
				return vo;
			}
		});
	}

}
