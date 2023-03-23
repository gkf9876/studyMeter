package com.kdw.studyMeter.study.memorize.frame;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StudyMemorizeDetailImageFrame extends JFrame{
	private JPanel panel1;
	
	public StudyMemorizeDetailImageFrame(String title, String path) {
		this.setTitle(title);
		this.setSize(650, 550);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		
		panel1 = new JPanel();
		panel1.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // 라벨 생성
        JLabel imgLabel = new JLabel();
        
        // 아이콘 생성
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        
        int width = 600;
        int height = width * img.getHeight(null) / img.getWidth(null);
        
        Image updateImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon updateIcon = new ImageIcon(updateImg);
        		
        // 라벨에 아이콘(이미지) 설정
        imgLabel.setIcon(updateIcon);
        
        // 라벨 설정(크기, 정렬...)
        imgLabel.setBounds(0, 0, width, height);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
		
        //프레임에 컴포넌트 추가
        panel1.add(imgLabel);
    	
		this.add(panel1, BorderLayout.CENTER);
	}
}
