package com.kdw.studyMeter.planner.frame;

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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.kdw.studyMeter.file.service.FileService;
import com.kdw.studyMeter.planner.service.PlannerService;
import com.kdw.studyMeter.planner.vo.PlannerVo;

public class PlannerListFrame extends JFrame{
	
	private JPanel panel1;
	private JPanel panel11;
	private JPanel panel2;
	
	private JScrollPane scrollPane1;
	
	private JButton button1;
	private JButton button2;
	
	private PlannerService plannerService;
	private FileService fileService;
	
	private int num = 1;
	
	public PlannerListFrame(final PlannerService plannerService, final FileService fileService) {
		this.setTitle("계획 목록");
		this.setSize(500, 440);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.plannerService = plannerService;
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
					PlannerListItem panel = (PlannerListItem)comps[i];
					PlannerVo vo = panel.getPlannerVo();
					if(vo.getName() == null || "".equals(vo.getName().trim())) {
						JOptionPane.showMessageDialog(null, "계획 제목을 입력하세요.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(vo.getType() == null || "".equals(vo.getType().trim())) {
						JOptionPane.showMessageDialog(null, "계획 종류를 입력하세요.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
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
				PlannerVo vo = new PlannerVo();
				vo.setSeq(-1);
				vo.setParentSeq(-1);
				
				JPanel panel = new PlannerListItem(num++, vo);
				panel11.add(panel);
				
				panel1.revalidate();
				panel1.repaint();
			}
		});
		panel2.add(button2);
		this.add(panel2, BorderLayout.SOUTH);
	}
	
	public void init() {
		num = 1;
		panel11.removeAll();

		PlannerVo vo = new PlannerVo();
		vo.setSeq(-1);
		vo.setParentSeq(-1);
		List<PlannerVo> itemList = plannerService.selectList(vo);
		
		if(itemList != null && itemList.size() > 0) {
			for(PlannerVo item : itemList) {
				PlannerListItem plannerListItem = new PlannerListItem(num++, item);
				panel11.add(plannerListItem);
			}
		}else {
			PlannerListItem plannerListItem = new PlannerListItem(num++, vo);
			panel11.add(plannerListItem);
		}
		
		panel11.revalidate();
		panel11.repaint();
	}
	
	private class PlannerListItem extends JPanel{
		private JLabel label1;
		private JTextField textField1;
		private JComboBox<String> comboBox1;
		private JButton button1;
		
		private PlannerVo plannerVo;
		
		private PlannerDetailFrame plannerDetailFrame;
		
		public PlannerListItem(int num, PlannerVo vo) {
			this.plannerVo = vo;
			
			label1 = new JLabel(String.valueOf(num));
			this.add(label1);
			
			textField1 = new JTextField(25);
			textField1.setText(vo.getName());
			this.add(textField1);
			
			comboBox1 = new JComboBox<String>(new String[] {"선택", "만다라트 양식"});
			for(int i=0; i<comboBox1.getItemCount(); i++) {
				if(comboBox1.getItemAt(i).equals(vo.getType())) {
					comboBox1.setSelectedIndex(i);
				}
			}
			this.add(comboBox1);

			plannerDetailFrame = new PlannerDetailFrame(this.plannerVo.getSeq(), textField1.getText(), plannerService, fileService);
			button1 = new JButton("상세");
			button1.setBorder(BorderFactory.createLineBorder(Color.black));
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(plannerVo != null && plannerVo.getSeq() > -1) {
						plannerDetailFrame.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(null, "빈 항목의 상세입력은 할 수 없습니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			this.add(button1);
		}
		
		public void save() {
			if(this.plannerVo != null) {
				this.plannerVo.setName(textField1.getText());
				
				String type = comboBox1.getSelectedItem().toString();
				type = ("선택".equals(type))? null : type;
				this.plannerVo.setType(type);
				
				//기존항목은 update, 신규항목은 insert
				if(this.plannerVo.getSeq() != -1) {
					plannerService.update(this.plannerVo);
				}else {
					plannerService.insert(this.plannerVo);
				}
			}
		}
		
		public PlannerVo getPlannerVo() {
			if(this.plannerVo != null) {
				this.plannerVo.setName(textField1.getText());
				
				String type = comboBox1.getSelectedItem().toString();
				type = ("선택".equals(type))? null : type;
				this.plannerVo.setType(type);
			}
			return this.plannerVo;
		}
	}
}
