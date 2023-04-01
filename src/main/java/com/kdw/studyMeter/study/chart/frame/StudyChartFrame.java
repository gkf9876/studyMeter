package com.kdw.studyMeter.study.chart.frame;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.kdw.studyMeter.study.meter.service.StudyService;
import com.kdw.studyMeter.study.meter.vo.StudyListVo;
import com.kdw.studyMeter.study.meter.vo.StudyVo;

public class StudyChartFrame extends JFrame{
	
	private JPanel panel1;
	private JPanel panel2;
	
	private StudyService studyService;
	
	private StudyVo studyVo = new StudyVo();
	private StudyListVo studyListVo = null;
	
	public StudyChartFrame(final StudyService studyService) {
		this.setTitle("공부량 통계");
		this.setSize(500, 380);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.studyService = studyService;
		
		panel1 = new JPanel();
		this.add(panel1, BorderLayout.NORTH);
		
		panel2 = new ChartPanel();
		this.add(panel2, BorderLayout.CENTER);
	}
	
	public void init(StudyListVo studyListVo) {
		this.studyListVo = studyListVo;
		this.setTitle(studyListVo.getStudyNm() + " 공부량 통계");
	}
	
	private class ChartPanel extends JPanel{
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			int x = 40;			//차트 x축 위치
			int y = 30;			//차트 y축 위치
			int width = 400;	//너비
			int height = 250;	//높이
			int xCnt = 5;		//세로줄 갯수
			int yCnt = 5;		//가로즐 갯수
			int xXis = -25;		//범례 위치조정값
			int yXis = -5;		//범례 위치조정값
			int varWidth = 15;	//막대 너비
			int maxY = 200;		//y축 최대값
			
			List<String> xStr = new ArrayList<String>();		//x축 범례
			List<Integer> yStr = new ArrayList<Integer>();		//y축 범례
			
			studyVo.setRownumber(xCnt);
			if(studyListVo != null)
				studyVo.setStudySeq(studyListVo.getSeq());
			else
				studyVo.setStudySeq(-1);
			
			List<StudyVo> chartData = studyService.selectChart(studyVo);
			Map<String, Integer> val = new HashMap<String, Integer>();
			for(int i=0; i<chartData.size(); i++) {
				StudyVo vo = chartData.get(i);
				xStr.add(vo.getDate());
				val.put(vo.getDate(), vo.getStudyMin());
			}
			
			//가로줄 갯수에 맞게 범례입력
			for(int i=0; i<yCnt; i++) {
				yStr.add((maxY / yCnt) * (i + 1));
			}
			
		    Graphics2D g2=(Graphics2D)g;
		    g2.setStroke(new BasicStroke(2,BasicStroke.CAP_SQUARE,0));
		    
			//가로축
			g.drawLine(x, y + height, x + width, y + height);
			
			//세로축
			g.drawLine(x, y, x, y + height);

		    g2.setStroke(new BasicStroke(1,BasicStroke.CAP_SQUARE,0));
		    
			//가로줄
			for(int i=0; i<yCnt; i++) {
				g.drawString(String.valueOf(yStr.get(i)), x - 20, y + height - (height / yCnt) * (i + 1) - yXis);	//Y축 범례 입력
				g.drawLine(x, y + height - (height / yCnt) * (i + 1), x + width, y + height - (height / yCnt) * (i + 1));
			}
			
			//세로줄
			for(int i=0; i<xCnt; i++) {
				if(i < xStr.size()) {
					g.setColor(Color.black);
					g.drawString(xStr.get(i), x + (width / xCnt) * (i + 1) + xXis, y + height + 20);	//X축 범례 입력
					g.drawLine(x + (width / xCnt) * (i + 1), y + height, x + (width / xCnt) * (i + 1), y);
					
					if(val.get(xStr.get(i)) != null) {
						int barX = x + (width / xCnt) * (i + 1) - varWidth / 2;
						//Height : maxY = x : val.get(xStr.get(i))
						int barHeight = height * val.get(xStr.get(i)) / maxY;			//스케일 조정
						int barY = y + height - barHeight;
						int barWidth = varWidth;

						g.setColor(Color.red);
						g.fillRect(barX, barY, barWidth, barHeight);	//막대 그리기
						
						g.setColor(Color.black);
						g.drawString(String.valueOf(val.get(xStr.get(i))), barX, barY - 5);//막대위에 수치 표시
					}
				}
			}
		}
	}
}
