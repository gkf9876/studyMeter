package com.kdw.studyMeter.planner.frame;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.kdw.studyMeter.file.service.FileService;
import com.kdw.studyMeter.file.vo.FileVo;
import com.kdw.studyMeter.planner.service.DailyScheduleDetailService;
import com.kdw.studyMeter.planner.service.DailyScheduleService;
import com.kdw.studyMeter.planner.vo.DailyScheduleDetailVo;
import com.kdw.studyMeter.planner.vo.DailyScheduleVo;

public class DailyScheduleDetailFrame extends JFrame{
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	
	private JLabel label1;
	private JButton button1;
	
	private DailyScheduleVo dailyScheduleVo;
	private FileVo fileVo;

	private DailyScheduleService dailyScheduleService;
	private DailyScheduleDetailService dailyScheduleDetailService;
	private FileService fileService;
	
	private DailySchedulePanel dailySchedulePanel;
	
	public DailyScheduleDetailFrame(int seq, String title, final DailyScheduleService dailyScheduleService, final DailyScheduleDetailService dailyScheduleDetailService, final FileService fileService) {
		this.setTitle(title);
		this.setSize(650, 750);
		this.setLayout(new BorderLayout());
		this.setResizable(false);

		this.dailyScheduleService = dailyScheduleService;
		this.dailyScheduleDetailService = dailyScheduleDetailService;
		this.fileService = fileService;
		
		this.dailyScheduleVo = new DailyScheduleVo();
		this.dailyScheduleVo.setSeq(seq);
		if(seq > -1) {
			List<DailyScheduleVo> list = dailyScheduleService.selectOne(this.dailyScheduleVo);
			this.dailyScheduleVo = list.get(0);
		}
		
		int fileSeq = this.dailyScheduleVo.getFileSeq();
		
		if(fileSeq != 0) {
			this.fileVo = new FileVo();
			this.fileVo.setSeq(this.dailyScheduleVo.getFileSeq());
			this.fileVo = fileService.selectOne(this.fileVo);
		}else
			this.fileVo = null;
		
		panel1 = new JPanel();
		this.add(panel1, BorderLayout.CENTER);
		
		panel2 = new JPanel();
		panel2.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
		
		this.add(panel2, BorderLayout.SOUTH);
		
		panel3 = new JPanel();
		label1 = new JLabel("");
		panel3.add(label1);
		
		button1 = new JButton("파일첨부");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int ret = fileChooser.showOpenDialog(null);
				if(ret != JFileChooser.APPROVE_OPTION) {
					
				}else {
					try {
						final FileVo file = new FileVo();
						file.setFilePath(fileChooser.getSelectedFile().getPath());
						file.setFileName(fileChooser.getSelectedFile().getName());
						
						String path = fileChooser.getSelectedFile().getPath();
						
						//KB 단위로 저장
						file.setFileSize((int)Files.size(Paths.get(path)) / 1024);
						if(path.lastIndexOf(".") > -1)
							file.setFileExtns(path.substring(path.lastIndexOf(".") + 1));
						else
							file.setFileExtns("");
						
						int fileSeq = fileService.insert(file);
						dailyScheduleVo.setFileSeq(fileSeq);
						dailyScheduleService.update(dailyScheduleVo);

						if(fileVo != null) {
							fileVo = file;
						}else {
							fileVo = new FileVo();
							fileVo.setSeq(fileSeq);
							fileVo = fileService.selectOne(fileVo);
						}
						
						init();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		panel3.add(button1);
		
		this.add(panel3, BorderLayout.SOUTH);
    	
		init();
	}
	
	public void init() {
		panel1.removeAll();

		dailySchedulePanel = new DailySchedulePanel(this.dailyScheduleVo, this);
		panel1.add(dailySchedulePanel);
		this.add(panel1, BorderLayout.CENTER);
		
		panel1.revalidate();
		panel1.repaint();

		if(this.fileVo != null) {
			
			panel2.removeAll();
			
	        // 라벨 생성
	        JLabel imgLabel = new JLabel();
	        
	        // 아이콘 생성
	        ImageIcon icon = new ImageIcon(this.fileVo.getFilePath());
	        Image img = icon.getImage();
	        
	        //width : height = img.getWidth(null) : img.getHeight(null)
	        int height = 500;
	        int width = height * img.getWidth(null) / img.getHeight(null);
	        
	        Image updateImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	        ImageIcon updateIcon = new ImageIcon(updateImg);
	        		
	        // 라벨에 아이콘(이미지) 설정
	        imgLabel.setIcon(updateIcon);
	        
	        // 라벨 설정(크기, 정렬...)
	        imgLabel.setBounds(0, 0, width, height);
	        imgLabel.setHorizontalAlignment(JLabel.CENTER);
			
	        //프레임에 컴포넌트 추가
	        panel2.add(imgLabel);
	        
			panel2.revalidate();
			panel2.repaint();
			
			label1.setText(this.fileVo.getFileName());
		}
	}
	
	private class DailySchedulePanel extends JPanel{
		private JComboBox<String> comboBox1;
		private List<DailyScheduleDetailVo> list = null;
		private DailyScheduleDetailVo detailVo;
		
		public DailySchedulePanel(DailyScheduleVo vo, DailyScheduleDetailFrame owner) {
			this.setPreferredSize(new Dimension(600, 700));
			
			detailVo = new DailyScheduleDetailVo();
			detailVo.setParentSeq(vo.getSeq());
			detailVo.setWeek(1);
			
			list = dailyScheduleDetailService.selectList(detailVo);

			String[] week = {"월", "화", "수", "목", "금", "토", "일"};
			comboBox1 = new JComboBox<String>(week);
			comboBox1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox<String> cb = (JComboBox<String>)e.getSource();
					detailVo = new DailyScheduleDetailVo();
					detailVo.setParentSeq(vo.getSeq());
					
					if("월".equals((String)cb.getSelectedItem())) {
						detailVo.setWeek(1);
					}else if("화".equals((String)cb.getSelectedItem())) {
						detailVo.setWeek(2);
					}else if("수".equals((String)cb.getSelectedItem())) {
						detailVo.setWeek(3);
					}else if("목".equals((String)cb.getSelectedItem())) {
						detailVo.setWeek(4);
					}else if("금".equals((String)cb.getSelectedItem())) {
						detailVo.setWeek(5);
					}else if("토".equals((String)cb.getSelectedItem())) {
						detailVo.setWeek(6);
					}else if("일".equals((String)cb.getSelectedItem())) {
						detailVo.setWeek(0);
					}
					
					list = dailyScheduleDetailService.selectList(detailVo);
					repaint();
				}
			});
			this.add(comboBox1);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			int x = 50;			//차트 x축 위치
			int y = 100;		//차트 y축 위치
			int radius = 250;	//반지름
			int width = radius * 2;
			int height = radius * 2;
			
			Color[] color = {Color.yellow, Color.blue, Color.green, Color.orange};
			int index = 0;
			for(DailyScheduleDetailVo vo : list) {
				double startTime = Integer.parseInt(vo.getStartTime().split(":")[0]) + (double)Integer.parseInt(vo.getStartTime().split(":")[1]) / 60;
				double endTime = Integer.parseInt(vo.getEndTime().split(":")[0]) + (double)Integer.parseInt(vo.getEndTime().split(":")[1]) / 60;
				
				int startAngle = 90 - (int)(startTime * 360 / 24);
				int endAngle = 90 - (int)(endTime * 360 / 24);
				int angle = endAngle - startAngle;
				
				g.setColor(color[index++]);
				if(index > 3)
					index = 0;
				g.fillArc(x, y, width, height, startAngle, angle);
				System.out.println(String.format("startAngle : %d, endAngle : %d,  angle : %d", startAngle, endAngle, angle));

				int strX = (int)((x + radius) + (radius - 20) * Math.cos(Math.toRadians((startAngle + endAngle) / 2)));
				int strY = (int)((y + radius) - (radius - 20) * Math.sin(Math.toRadians((startAngle + endAngle) / 2)));
				
				drawFancyString((Graphics2D)g, vo.getContents(), strX, strY, 15, Color.WHITE, 1);
			}

			g.setColor(Color.black);
		    Graphics2D g2=(Graphics2D)g;
		    g2.setStroke(new BasicStroke(2,BasicStroke.CAP_SQUARE,0));
			g.drawOval(x, y, width, height);
			
			//시간 표시
			for(int i=0; i<24; i++) {
				int hourLength = 10;
				int startX = x + radius;
				int startY = y + radius;
				int endX = x + radius;
				int endY = y + radius;
				
				int strX = x + radius;
				int strY = y + radius;
				
				if(i == 0 || i == 6 || i == 12 || i == 18)
					hourLength = 20;
				
				startX = (int)((x + radius) + radius * Math.cos(Math.toRadians(90 - 360/24 * i)));
				startY = (int)((y + radius) - radius * Math.sin(Math.toRadians(90 - 360/24 * i)));
				endX = (int)((x + radius) + (radius + hourLength) * Math.cos(Math.toRadians(90 - 360/24 * i)));
				endY = (int)((y + radius) - (radius + hourLength) * Math.sin(Math.toRadians(90 - 360/24 * i)));
				
				strX = (int)((x + radius) + (radius + hourLength + 10) * Math.cos(Math.toRadians(90 - 360/24 * i)));
				strY = (int)((y + radius) - (radius + hourLength + 10) * Math.sin(Math.toRadians(90 - 360/24 * i)));

				g.setColor(Color.BLACK);
				g.drawLine(startX, startY, endX, endY);
				
				Font f = new Font("Aria", Font.ITALIC, 20);
				g.setFont(f);
				g.setColor(Color.BLACK);
				g.drawString(i + "시", strX, strY);
			}
		}
	}
	
	public void drawFancyString(Graphics2D g, String str, int x, int y, float size, Color internalColor, int outlineSize) {
		if (str.length() == 0)
			return;
		
		AffineTransform orig = g.getTransform();
		Font f = new Font("Aria", Font.ITALIC|Font.BOLD, 20);
		TextLayout tl = new TextLayout(str, f, g.getFontRenderContext());
		AffineTransform transform = g.getTransform();
		FontMetrics fm = g.getFontMetrics(f);
		Shape outline = tl.getOutline(null);
		transform.translate(x, y + fm.getAscent());
		
		g.setTransform(transform);
		g.setColor(internalColor);
		g.fill(outline);
		g.setStroke(new BasicStroke(outlineSize));
		g.setColor(Color.BLACK);
		g.draw(outline);

		g.setTransform(orig);
	}
}
