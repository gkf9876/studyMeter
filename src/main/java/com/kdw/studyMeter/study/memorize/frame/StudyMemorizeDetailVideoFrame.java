package com.kdw.studyMeter.study.memorize.frame;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class StudyMemorizeDetailVideoFrame extends JFrame{
	private JFXPanel fxPanel;
	private JPanel panel1;
	private MediaPlayer mediaPlayer;
	
	private JButton button1;
	private JButton button2;
	private JButton button3;
	
	private JTextField textField1;
	
	public StudyMemorizeDetailVideoFrame(String title, String path) {
		this.setTitle(title);
		this.setSize(650, 550);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		
		try {
			fxPanel = new JFXPanel();
			StackPane layout = new StackPane();
			layout.setStyle("-fx-background-color: cornsilk; -fx-font-size: 20; -fx-padding: 20; -fx-alignment: center;");
			
			MediaView mediaView = new MediaView();
			
			Media media = new Media(new File("C:\\Users\\LGRnD\\Desktop\\20230816073453_30.mp4").toURI().toString());
			
			mediaPlayer = new MediaPlayer(media);
			
			mediaPlayer.setVolume(0.5);
			mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(javafx.util.Duration.ZERO));
			mediaPlayer.setOnReady(new Runnable() {

				@Override
				public void run() {
					//리스너를 사용 변화하는 값(현재 위치값)을 프로그레스 바에 적용하는 부분
					mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
						@Override
						public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
							double dCurTime = mediaPlayer.getCurrentTime().toSeconds();
							double dDurTime = mediaPlayer.getTotalDuration().toSeconds();
							
							double dDurTime2 = Math.floor(dDurTime);
							double dLastTime = dCurTime / dDurTime2;
							
							textField1.setText("현재 진행 : "+ dCurTime + " 초/ 총길이 : " + dDurTime2 + " 초");
						}
					});
				}
			});
	
			mediaView.setMediaPlayer(mediaPlayer);
			
			layout.getChildren().addAll(mediaView);
			Scene scene = new Scene(layout, 640, 480);
			
			fxPanel.setScene(scene);
			this.add(fxPanel, BorderLayout.CENTER);
			
			panel1 = new JPanel();
			button1 = new JButton("재생");
			button1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mediaPlayer.play();
					
					button1.setEnabled(false);
					button2.setEnabled(true);
					button3.setEnabled(true);
				}
			});
			panel1.add(button1);
			
			button2 = new JButton("중지");
			button2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mediaPlayer.stop();
					
					button1.setEnabled(true);
					button2.setEnabled(true);
					button3.setEnabled(true);
				}
			});
			panel1.add(button2);
			
			button3 = new JButton("일시정지");
			button3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mediaPlayer.pause();
					
					button1.setEnabled(true);
					button2.setEnabled(true);
					button3.setEnabled(false);
				}
			});
			panel1.add(button3);
			
			textField1 = new JTextField(30);
			panel1.add(textField1);
			
			this.add(panel1, BorderLayout.SOUTH);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.setVisible(true);
	}
}
