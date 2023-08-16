package com.kdw.studyMeter.planner.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.kdw.studyMeter.file.service.FileService;
import com.kdw.studyMeter.file.vo.FileVo;
import com.kdw.studyMeter.planner.service.PlannerService;
import com.kdw.studyMeter.planner.vo.PlannerVo;

public class PlannerDetailFrame extends JFrame{
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	
	private JLabel label1;
	private JButton button1;
	
	private PlannerVo plannerVo;
	private FileVo fileVo;
	
	private PlannerService plannerService;
	private FileService fileService;
	
	private MandalArtPanel mandalArtPanel;
	
	public PlannerDetailFrame(int seq, String title, final PlannerService plannerService, final FileService fileService) {
		this.setTitle(title);
		this.setSize(650, 750);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		
		this.plannerService = plannerService;
		this.fileService = fileService;
		
		this.plannerVo = new PlannerVo();
		this.plannerVo.setSeq(seq);
		if(seq > -1) {
			List<PlannerVo> list = plannerService.selectOne(this.plannerVo);
			this.plannerVo = list.get(0);
		}
		
		int fileSeq = this.plannerVo.getFileSeq();
		
		if(fileSeq != 0) {
			this.fileVo = new FileVo();
			this.fileVo.setSeq(this.plannerVo.getFileSeq());
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
						plannerVo.setFileSeq(fileSeq);
						plannerService.update(plannerVo);

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

		if("만다라트 양식".equals(this.plannerVo.getType())) {
			mandalArtPanel = new MandalArtPanel(this.plannerVo, this);
			panel1.add(mandalArtPanel);
		}
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
	
	private class MandalArtItem extends JPanel{
		JButton[][] buttons;
		PlannerVo[][] plannerVos;
		
		public MandalArtItem(PlannerVo vo, final PlannerDetailFrame owner) {
			this.setLayout(new GridLayout(3, 3));
			this.setPreferredSize(new Dimension(200, 200));
			
			buttons = new JButton[3][3];
			plannerVos = new PlannerVo[3][3];

			List<PlannerVo> list = new ArrayList<PlannerVo>();
			if(vo.getSeq() != -1) {
				list = plannerService.selectOne(vo);
			}
			
			String name = (list.size() >= 2)? list.get(1).getName() : "";
			buttons[0][0] = new JButton(setHtml(name));
			buttons[0][0].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
			buttons[0][0].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(plannerVos[0][0] != null && (plannerVos[0][0].getSeq() != -1 || plannerVos[0][0].getParentSeq() != -1)) {
						PlannerEditFrame editFrame = new PlannerEditFrame(plannerVos[0][0], plannerService, owner);
						editFrame.setVisible(true);
					}
				}
			});
			
			if(list.size() >= 2)
				plannerVos[0][0] = list.get(1);
			else {
				plannerVos[0][0] = new PlannerVo();
				plannerVos[0][0].setSeq(-1);
				plannerVos[0][0].setParentSeq(vo.getSeq());
				plannerVos[0][0].setType(vo.getType());
			}
			this.add(buttons[0][0], 0, 0);

			name = (list.size() >= 3)? list.get(2).getName() : "";
			buttons[0][1] = new JButton(setHtml(name));
			buttons[0][1].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
			buttons[0][1].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(plannerVos[0][1] != null && (plannerVos[0][1].getSeq() != -1 || plannerVos[0][1].getParentSeq() != -1)) {
						PlannerEditFrame editFrame = new PlannerEditFrame(plannerVos[0][1], plannerService, owner);
						editFrame.setVisible(true);
					}
				}
			});
			
			if(list.size() >= 3)
				plannerVos[0][1] = list.get(2);
			else {
				plannerVos[0][1] = new PlannerVo();
				plannerVos[0][1].setSeq(-1);
				plannerVos[0][1].setParentSeq(vo.getSeq());
				plannerVos[0][1].setType(vo.getType());
			}
			this.add(buttons[0][1], 0, 1);

			name = (list.size() >= 4)? list.get(3).getName() : "";
			buttons[0][2] = new JButton(setHtml(name));
			buttons[0][2].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
			buttons[0][2].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(plannerVos[0][2] != null && (plannerVos[0][2].getSeq() != -1 || plannerVos[0][2].getParentSeq() != -1)) {
						PlannerEditFrame editFrame = new PlannerEditFrame(plannerVos[0][2], plannerService, owner);
						editFrame.setVisible(true);
					}
				}
			});
			
			if(list.size() >= 4)
				plannerVos[0][2] = list.get(3);
			else {
				plannerVos[0][2] = new PlannerVo();
				plannerVos[0][2].setSeq(-1);
				plannerVos[0][2].setParentSeq(vo.getSeq());
				plannerVos[0][2].setType(vo.getType());
			}
			this.add(buttons[0][2], 0, 2);

			name = (list.size() >= 9)? list.get(8).getName() : "";
			buttons[1][0] = new JButton(setHtml(name));
			buttons[1][0].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
			buttons[1][0].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(plannerVos[1][0] != null && (plannerVos[1][0].getSeq() != -1 || plannerVos[1][0].getParentSeq() != -1)) {
						PlannerEditFrame editFrame = new PlannerEditFrame(plannerVos[1][0], plannerService, owner);
						editFrame.setVisible(true);
					}
				}
			});
			
			if(list.size() >= 9)
				plannerVos[1][0] = list.get(8);
			else {
				plannerVos[1][0] = new PlannerVo();
				plannerVos[1][0].setSeq(-1);
				plannerVos[1][0].setParentSeq(vo.getSeq());
				plannerVos[1][0].setType(vo.getType());
			}
			this.add(buttons[1][0], 1, 0);

			name = (list.size() >= 1)? list.get(0).getName() : "";
			buttons[1][1] = new JButton(setHtml(vo.getName()));
			buttons[1][1].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 1), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
			buttons[1][1].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(plannerVos[1][1] != null && (plannerVos[1][1].getSeq() != -1 || plannerVos[1][1].getParentSeq() != -1)) {
						PlannerEditFrame editFrame = new PlannerEditFrame(plannerVos[1][1], plannerService, owner);
						editFrame.setVisible(true);
					}
				}
			});
			
			if(list.size() >= 1)
				plannerVos[1][1] = list.get(0);
			else {
				plannerVos[1][1] = new PlannerVo();
				plannerVos[1][1].setSeq(-1);
				plannerVos[1][1].setParentSeq(vo.getSeq());
				plannerVos[1][1].setType(vo.getType());
				plannerVos[1][1].setName(vo.getName());
				plannerVos[1][1].setContents(vo.getContents());
			}
			this.add(buttons[1][1], 1, 1);

			name = (list.size() >= 5)? list.get(4).getName() : "";
			buttons[1][2] = new JButton(setHtml(name));
			buttons[1][2].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
			buttons[1][2].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(plannerVos[1][2] != null && (plannerVos[1][2].getSeq() != -1 || plannerVos[1][2].getParentSeq() != -1)) {
						PlannerEditFrame editFrame = new PlannerEditFrame(plannerVos[1][2], plannerService, owner);
						editFrame.setVisible(true);
					}
				}
			});
			
			if(list.size() >= 5)
				plannerVos[1][2] = list.get(4);
			else {
				plannerVos[1][2] = new PlannerVo();
				plannerVos[1][2].setSeq(-1);
				plannerVos[1][2].setParentSeq(vo.getSeq());
				plannerVos[1][2].setType(vo.getType());
			}
			this.add(buttons[1][2], 1, 2);

			name = (list.size() >= 8)? list.get(7).getName() : "";
			buttons[2][0] = new JButton(setHtml(name));
			buttons[2][0].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
			buttons[2][0].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(plannerVos[2][0] != null && (plannerVos[2][0].getSeq() != -1 || plannerVos[2][0].getParentSeq() != -1)) {
						PlannerEditFrame editFrame = new PlannerEditFrame(plannerVos[2][0], plannerService, owner);
						editFrame.setVisible(true);
					}
				}
			});
			
			if(list.size() >= 8)
				plannerVos[2][0] = list.get(7);
			else {
				plannerVos[2][0] = new PlannerVo();
				plannerVos[2][0].setSeq(-1);
				plannerVos[2][0].setParentSeq(vo.getSeq());
				plannerVos[2][0].setType(vo.getType());
			}
			this.add(buttons[2][0], 2, 0);

			name = (list.size() >= 7)? list.get(6).getName() : "";
			buttons[2][1] = new JButton(setHtml(name));
			buttons[2][1].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
			buttons[2][1].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(plannerVos[2][1] != null && (plannerVos[2][1].getSeq() != -1 || plannerVos[2][1].getParentSeq() != -1)) {
						PlannerEditFrame editFrame = new PlannerEditFrame(plannerVos[2][1], plannerService, owner);
						editFrame.setVisible(true);
					}
				}
			});
			
			if(list.size() >= 7)
				plannerVos[2][1] = list.get(6);
			else {
				plannerVos[2][1] = new PlannerVo();
				plannerVos[2][1].setSeq(-1);
				plannerVos[2][1].setParentSeq(vo.getSeq());
				plannerVos[2][1].setType(vo.getType());
			}
			this.add(buttons[2][1], 2, 1);

			name = (list.size() >= 6)? list.get(5).getName() : "";
			buttons[2][2] = new JButton(setHtml(name));
			buttons[2][2].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
			buttons[2][2].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(plannerVos[2][2] != null && (plannerVos[2][2].getSeq() != -1 || plannerVos[2][2].getParentSeq() != -1)) {
						PlannerEditFrame editFrame = new PlannerEditFrame(plannerVos[2][2], plannerService, owner);
						editFrame.setVisible(true);
					}
				}
			});
			
			if(list.size() >= 6)
				plannerVos[2][2] = list.get(5);
			else {
				plannerVos[2][2] = new PlannerVo();
				plannerVos[2][2].setSeq(-1);
				plannerVos[2][2].setParentSeq(vo.getSeq());
				plannerVos[2][2].setType(vo.getType());
			}
			this.add(buttons[2][2], 2, 2);
		}
	}
	
	private class MandalArtPanel extends JPanel{
		MandalArtItem[][] items;
		PlannerVo[][] plannerVos;
		
		public MandalArtPanel(PlannerVo vo, PlannerDetailFrame owner) {
			this.setLayout(new GridLayout(3, 3));
			this.setPreferredSize(new Dimension(600, 600));
			
			items = new MandalArtItem[3][3];
			
			List<PlannerVo> list = plannerService.selectOne(vo);
			
			PlannerVo itemVo;
			if(list.size() >= 2)
				itemVo = list.get(1);
			else {
				itemVo = new PlannerVo();
				itemVo.setSeq(-1);
				itemVo.setParentSeq(vo.getSeq());
				itemVo.setType(vo.getType());
			}
			items[0][0] = new MandalArtItem(itemVo, owner);
			this.add(items[0][0], 0, 0);

			if(list.size() >= 3)
				itemVo = list.get(2);
			else {
				itemVo = new PlannerVo();
				itemVo.setSeq(-1);
				itemVo.setParentSeq(vo.getSeq());
				itemVo.setType(vo.getType());
			}
			items[0][1] = new MandalArtItem(itemVo, owner);
			this.add(items[0][1], 0, 1);

			if(list.size() >= 4)
				itemVo = list.get(3);
			else {
				itemVo = new PlannerVo();
				itemVo.setSeq(-1);
				itemVo.setParentSeq(vo.getSeq());
				itemVo.setType(vo.getType());
			}
			items[0][2] = new MandalArtItem(itemVo, owner);
			this.add(items[0][2], 0, 2);

			if(list.size() >= 9)
				itemVo = list.get(8);
			else {
				itemVo = new PlannerVo();
				itemVo.setSeq(-1);
				itemVo.setParentSeq(vo.getSeq());
				itemVo.setType(vo.getType());
			}
			items[1][0] = new MandalArtItem(itemVo, owner);
			this.add(items[1][0], 1, 0);

			if(list.size() >= 1)
				itemVo = list.get(0);
			else {
				itemVo = new PlannerVo();
				itemVo.setSeq(-1);
				itemVo.setParentSeq(vo.getSeq());
				itemVo.setType(vo.getType());
			}
			items[1][1] = new MandalArtItem(itemVo, owner);
			this.add(items[1][1], 1, 1);

			if(list.size() >= 5)
				itemVo = list.get(4);
			else {
				itemVo = new PlannerVo();
				itemVo.setSeq(-1);
				itemVo.setParentSeq(vo.getSeq());
				itemVo.setType(vo.getType());
			}
			items[1][2] = new MandalArtItem(itemVo, owner);
			this.add(items[1][2], 1, 2);

			if(list.size() >= 8)
				itemVo = list.get(7);
			else {
				itemVo = new PlannerVo();
				itemVo.setSeq(-1);
				itemVo.setParentSeq(vo.getSeq());
				itemVo.setType(vo.getType());
			}
			items[2][0] = new MandalArtItem(itemVo, owner);
			this.add(items[2][0], 2, 0);

			if(list.size() >= 7)
				itemVo = list.get(6);
			else {
				itemVo = new PlannerVo();
				itemVo.setSeq(-1);
				itemVo.setParentSeq(vo.getSeq());
				itemVo.setType(vo.getType());
			}
			items[2][1] = new MandalArtItem(itemVo, owner);
			this.add(items[2][1], 2, 1);

			if(list.size() >= 6)
				itemVo = list.get(5);
			else {
				itemVo = new PlannerVo();
				itemVo.setSeq(-1);
				itemVo.setParentSeq(vo.getSeq());
				itemVo.setType(vo.getType());
			}
			items[2][2] = new MandalArtItem(itemVo, owner);
			this.add(items[2][2], 2, 2);
		}
	}
	
	private String setHtml(String str) {
		String result = "";
		result += "<html>";
		result += "<body style='text-align:center;'>";
		result += (str != null)? str : "";
		result += "</body>";
		result += "</html>";
		return result;
	}
}
