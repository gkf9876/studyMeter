package com.kdw.studyMeter.calendar.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.kdw.studyMeter.calendar.vo.CalendarVo;
import com.kdw.studyMeter.todo.vo.TodoDetailVo;

public class CalendarDaoImpl implements CalendarDao{
	private Connection conn = null;
	
	public CalendarDaoImpl(Connection conn) {
		this.conn = conn;
	}
	
	public List<CalendarVo> select(CalendarVo calendarVo) {
		List<CalendarVo> result = new ArrayList<CalendarVo>();
		
		try {
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
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, calendarVo.getSchYearMonthDate());
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				CalendarVo vo = new CalendarVo();
				vo.setDate(rs.getString("DATE"));
				vo.setContents(rs.getString("CONTENTS"));
				result.add(vo);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
