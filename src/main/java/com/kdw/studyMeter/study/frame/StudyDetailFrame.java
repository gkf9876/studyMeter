package com.kdw.studyMeter.study.frame;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.kdw.studyMeter.file.service.FileService;
import com.kdw.studyMeter.file.vo.FileVo;
import com.kdw.studyMeter.study.service.StudyListService;
import com.kdw.studyMeter.study.vo.StudyListVo;

public class StudyDetailFrame extends JFrame{
	private JPanel panel1;
	private JPanel panel2;
	
	private JLabel label1;
	private JButton button1;
	
	private StudyListVo studyListVo;
	private FileVo fileVo;
	
	private StudyListService studyListService;
	private FileService fileService;
	
	public StudyDetailFrame(int seq, String title, final StudyListService studyListService, final FileService fileService) {
		this.setTitle(title);
		this.setSize(650, 550);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		
		this.studyListService = studyListService;
		this.fileService = fileService;
		
		this.studyListVo = new StudyListVo();
		this.studyListVo.setSeq(seq);
		if(seq > -1)
			this.studyListVo = studyListService.selectOne(this.studyListVo);
		
		int fileSeq = this.studyListVo.getFileSeq();
		
		if(fileSeq != 0) {
			this.fileVo = new FileVo();
			this.fileVo.setSeq(this.studyListVo.getFileSeq());
			this.fileVo = fileService.selectOne(this.fileVo);
		}else
			this.fileVo = null;
		
		panel1 = new JPanel();
		panel1.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
		
		this.add(panel1, BorderLayout.CENTER);
		
		panel2 = new JPanel();
		label1 = new JLabel("");
		panel2.add(label1);
		
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
						studyListVo.setFileSeq(fileSeq);
						studyListService.update(studyListVo);

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
		panel2.add(button1);
		
		this.add(panel2, BorderLayout.SOUTH);
    	
		init();
	}
	
	public void init() {

		if(this.fileVo != null) {
			panel1.removeAll();
			
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
	        panel1.add(imgLabel);
	        
			panel1.revalidate();
			panel1.repaint();
			
			label1.setText(this.fileVo.getFileName());
		}
	}
}
