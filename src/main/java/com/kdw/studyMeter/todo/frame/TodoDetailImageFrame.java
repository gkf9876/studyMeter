package com.kdw.studyMeter.todo.frame;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TodoDetailImageFrame extends JFrame{
	private JPanel panel1;
	
	public TodoDetailImageFrame(String title, String path) {
		this.setTitle(title);
		this.setSize(650, 550);
		
		panel1 = new JPanel();

        // 라벨 생성
        JLabel imgLabel = new JLabel();
        
        // 아이콘 생성
        ImageIcon icon = new ImageIcon(path);
        
        // 라벨에 아이콘(이미지) 설정
        imgLabel.setIcon(icon);
        
        // 라벨 설정(크기, 정렬...)
        imgLabel.setBounds(210, 30, 165, 150);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
		
        //프레임에 컴포넌트 추가
    	getContentPane().add(imgLabel);
    	
		this.add(panel1);
	}
}
