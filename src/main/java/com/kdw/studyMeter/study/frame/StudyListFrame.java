package com.kdw.studyMeter.study.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.kdw.studyMeter.file.service.FileService;
import com.kdw.studyMeter.study.service.StudyListService;
import com.kdw.studyMeter.study.vo.StudyListVo;

public class StudyListFrame extends JFrame{
	
	private JPanel panel1;
	private JPanel panel11;
	private JPanel panel2;
	
	private JScrollPane scrollPane1;
	
	private JButton button1;
	private JButton button2;
	
	private StudyListService studyListService;
	private FileService fileService;
	
	private int num = 1;
	
	public StudyListFrame(final StudyListService studyListService, final FileService fileService) {
		this.setTitle("공부 목록");
		this.setSize(500, 440);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.studyListService = studyListService;
		this.fileService = fileService;
		
		panel1 = new JPanel();
		panel11 = new JPanel();
		panel11.setLayout(new BoxLayout(panel11, BoxLayout.Y_AXIS));
		
		//할일 목록 초기화
		init();
		
		//스크롤 생성
		scrollPane1 = new JScrollPane(panel11);
		scrollPane1.setPreferredSize(new Dimension(480, 350));
		
		panel1.add(scrollPane1);
		this.add(panel1);
		
		panel2 = new JPanel();
		
		button1 = new JButton("저장");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//화면데이터 할일목록에 저장.
				Component[] comps = panel11.getComponents();
				for(int i=0; i<comps.length; i++) {
					StudyListItem panel = (StudyListItem)comps[i];
					StudyListVo vo = panel.getStudyListVo();
					if(vo.getStudyNm() == null || "".equals(vo.getStudyNm().trim())) {
						JOptionPane.showMessageDialog(null, "공부 제목을 입력하세요.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(vo.getStudyType() == null || "".equals(vo.getStudyType().trim())) {
						JOptionPane.showMessageDialog(null, "공부 종류를 입력하세요.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
						return;
					}
					panel.save();
				}
				
				//할일 목록으로 데이터 업데이트
				init();
			}
		});
		panel2.add(button1);
		
		button2 = new JButton("추가");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StudyListVo vo = new StudyListVo();
				vo.setSeq(-1);
				
				JPanel panel = new StudyListItem(num++, vo);
				panel11.add(panel);
				
				panel1.revalidate();
				panel1.repaint();
			}
		});
		panel2.add(button2);
		this.add(panel2, BorderLayout.SOUTH);
	}
	
	private void init() {
		num = 1;
		panel11.removeAll();

		StudyListVo vo = new StudyListVo();
		vo.setSeq(-1);
		List<StudyListVo> itemList = studyListService.select(vo);
		
		if(itemList != null && itemList.size() > 0) {
			for(StudyListVo item : itemList) {
				StudyListItem studyListItem = new StudyListItem(num++, item);
				panel11.add(studyListItem);
			}
		}else {
			StudyListItem studyListItem = new StudyListItem(num++, vo);
			panel11.add(studyListItem);
		}
		
		panel11.revalidate();
		panel11.repaint();
	}
	
	private class StudyListItem extends JPanel{
		private JLabel label1;
		private JTextField textField1;
		private JTextField textField2;
		private JButton button1;
		
		private StudyListVo studyListVo;
		
		private StudyDetailFrame studyDetailFrame;
		
		public StudyListItem(int num, StudyListVo vo) {
			this.studyListVo = vo;
			
			label1 = new JLabel(String.valueOf(num));
			this.add(label1);
			
			textField1 = new JTextField(25);
			textField1.setText(vo.getStudyNm());
			this.add(textField1);
			
			textField2 = new JTextField(10);
			textField2.setText(vo.getStudyType());
			this.add(textField2);

			studyDetailFrame = new StudyDetailFrame(this.studyListVo.getSeq(), textField1.getText(), studyListService, fileService);
			button1 = new JButton("상세");
			button1.setBorder(BorderFactory.createLineBorder(Color.black));
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					studyDetailFrame.setVisible(true);
				}
			});
			this.add(button1);
		}
		
		public void save() {
			if(this.studyListVo != null) {
				this.studyListVo.setStudyNm(textField1.getText());
				this.studyListVo.setStudyType(textField2.getText());
				
				//기존항목은 update, 신규항목은 insert
				if(this.studyListVo.getSeq() != -1) {
					studyListService.update(this.studyListVo);
				}else {
					studyListService.insert(this.studyListVo);
				}
			}
		}
		
		public StudyListVo getStudyListVo() {
			if(this.studyListVo != null) {
				this.studyListVo.setStudyNm(textField1.getText());
				this.studyListVo.setStudyType(textField2.getText());
			}
			return this.studyListVo;
		}
	}
}
