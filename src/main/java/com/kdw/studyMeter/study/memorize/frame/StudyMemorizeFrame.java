package com.kdw.studyMeter.study.memorize.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.kdw.studyMeter.file.service.FileService;
import com.kdw.studyMeter.study.memorize.service.StudyMemorizeDetailService;
import com.kdw.studyMeter.study.memorize.service.StudyMemorizeService;
import com.kdw.studyMeter.study.memorize.vo.StudyMemorizeVo;

public class StudyMemorizeFrame extends JFrame{
	
	private JPanel panel1;
	private JPanel panel11;
	private JPanel panel2;
	
	private JScrollPane scrollPane1;
	
	private JButton button1;
	private JButton button2;
	
	//할일 목록
	private StudyMemorizeItem studyMemorizeItem;
	
	private StudyMemorizeService studyMemorizeService;
	private StudyMemorizeDetailService studyMemorizeDetailService;
	private FileService fileService;
	
	public StudyMemorizeFrame(final StudyMemorizeService studyMemorizeService, final StudyMemorizeDetailService studyMemorizeDetailService, final FileService fileService) {
		this.setTitle("암기 목록");
		this.setSize(650, 490);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.studyMemorizeService = studyMemorizeService;
		this.studyMemorizeDetailService = studyMemorizeDetailService;
		this.fileService = fileService;
		
		//할일 목록 출력
		panel1 = new JPanel();
		panel11 = new JPanel();
		panel11.setLayout(new BoxLayout(panel11, BoxLayout.Y_AXIS));
		
		//할일 목록 초기화
		//init();
		
		//스크롤 생성
		scrollPane1 = new JScrollPane(panel11);
		scrollPane1.setPreferredSize(new Dimension(600, 400));
		
		panel1.add(scrollPane1);
		this.add(panel1, BorderLayout.CENTER);
		
		//저장, 삭제, 추가 버튼 구역
		panel2 = new JPanel();
		button1 = new JButton("저장");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//화면데이터 할일목록에 저장.
				Component[] comps = panel11.getComponents();
				for(int i=0; i<comps.length; i++) {
					StudyMemorizeItem panel = (StudyMemorizeItem)comps[i];
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
				StudyMemorizeVo vo = new StudyMemorizeVo();
				vo.setSeq(-1);
				vo.setParentSeq(-1);
				//vo.setLevel(1);
				//vo.setOdr(panel11.getComponentCount());
				//StudyMemorizeItem item = new StudyMemorizeItem(vo);
				//studyMemorizeItemList.add(item);
				studyMemorizeItem.add(vo);
				//panel11.add(item);
				
				revalidate();
				repaint();
				
				scrollPane1.getVerticalScrollBar().setValue(scrollPane1.getVerticalScrollBar().getMaximum());
			}
		});
		panel2.add(button2);
		
		this.add(panel2, BorderLayout.SOUTH);
	}
	
	//할일 목록 초기화
	public void init() {
		panel11.removeAll();
		
		studyMemorizeItem = new StudyMemorizeItem();
		panel11.add(studyMemorizeItem);
		
		panel11.revalidate();
		panel11.repaint();
	}
	
	private class StudyMemorizeItem extends JPanel{
		
		private StudyMemorizeDetailFrame studyMemorizeDetailFrame;
		private JPanel panel;
		private JCheckBox checkBox;
		private JTextField textField;
		private JButton button1;
		private JButton button2;
		private JButton button3;
		private JButton button4;
		
		private StudyMemorizeVo studyMemorizeVo;
		
		//할일 목록
		private List<StudyMemorizeItem> sonItemList;

		//최상위 아이템 추가할때 사용
		public StudyMemorizeItem() {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.studyMemorizeVo = null;
			
			panel = new JPanel();
			this.add(panel);
			
			//자식 항목 출력하기.
			sonItemList = new ArrayList<StudyMemorizeItem>();
			StudyMemorizeVo param = new StudyMemorizeVo();
			param.setParentSeq(-1);
			param.setLevel(1);
			List<StudyMemorizeVo> sonItem = studyMemorizeService.selectList(param);
			if(sonItem.size() > 0) {
				for(StudyMemorizeVo vo : sonItem) {
					StudyMemorizeItem item = new StudyMemorizeItem(vo);
					sonItemList.add(item);
					this.add(item);
				}
			}else {
				StudyMemorizeVo vo = new StudyMemorizeVo();
				vo.setSeq(-1);
				vo.setParentSeq(-1);
				vo.setLevel(1);
				vo.setOdr(1);
				StudyMemorizeItem item = new StudyMemorizeItem(vo);
				sonItemList.add(item);
				this.add(item);
			}
		}
		
		public StudyMemorizeItem(StudyMemorizeVo vo) {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.studyMemorizeVo = vo;
			
			panel = new JPanel();
			checkBox = new JCheckBox();
			checkBox.setSelected(("Y".equals(this.studyMemorizeVo.getCheckYn()))? true : false);
			checkBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//현재 항목의 하위항목을 추가
					setCheck((checkBox.isSelected())? true : false);
				}
			});
			panel.add(checkBox);
			
			textField = new JTextField(40 - this.studyMemorizeVo.getLevel() * 2);
			textField.setText(this.studyMemorizeVo.getSubject());
			panel.add(textField);
			
			button1 = new JButton("+");
			button1.setBorder(BorderFactory.createLineBorder(Color.black));
			button1.setPreferredSize(new Dimension(16, 16));
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//현재 항목의 하위항목을 추가
					StudyMemorizeItem item = (StudyMemorizeItem)panel.getParent();
					StudyMemorizeVo vo = new StudyMemorizeVo();
					vo.setSeq(-1);
					item.add(vo);
					
					item.revalidate();
					item.repaint();
				}
			});
			panel.add(button1);
			
			button2 = new JButton("▲");
			button2.setBorder(BorderFactory.createLineBorder(Color.black));
			button2.setPreferredSize(new Dimension(16, 16));
			button2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//해당 항목의 위에 있는 항목 불러오기
					StudyMemorizeItem currentItem = (StudyMemorizeItem)panel.getParent();
					StudyMemorizeItem parentItem = (StudyMemorizeItem)currentItem.getParent();
					List<StudyMemorizeItem> sonItemList = parentItem.getSonItemList();
					for(int i=0; i<sonItemList.size(); i++) {
						StudyMemorizeItem regionItem = (i-1 >= 0)? sonItemList.get(i-1) : null;
						StudyMemorizeItem item = sonItemList.get(i);
						StudyMemorizeItem nextItem = (i+1 < sonItemList.size())? sonItemList.get(i+1) : null;
						
						if(item == currentItem) {
							System.out.println("=================== UP MOVE!! ======================");
							System.out.println("regionItem : " + ((regionItem != null)? regionItem.getStudyMemorizeVo().getSubject() + ", odr : " + regionItem.getStudyMemorizeVo().getOdr() : "null"));
							System.out.println("item : " + item.getStudyMemorizeVo().getSubject() + ", odr : " + item.getStudyMemorizeVo().getOdr());
							System.out.println("nextItem : " + ((nextItem != null)? nextItem.getStudyMemorizeVo().getSubject() + ", odr : " + nextItem.getStudyMemorizeVo().getOdr() : "null"));
							System.out.println("=================== RESULT!! =======================");
							
							if(regionItem != null) {
								StudyMemorizeVo regionVo = regionItem.getStudyMemorizeVo();
								StudyMemorizeVo vo = item.getStudyMemorizeVo();
								
								int tempOdr = regionVo.getOdr();
								regionVo.setOdr(vo.getOdr());
								vo.setOdr(tempOdr);
								
								regionItem.setStudyMemorizeVo(regionVo);
								item.setStudyMemorizeVo(vo);
								
								parentItem.remove(item);
								parentItem.add(item, i);
								
								sonItemList.remove(i);
								sonItemList.add(i-1, item);
								parentItem.setSonItemList(sonItemList);
								
								StudyMemorizeItem imsiRegionItem = (i-1 >= 0)? sonItemList.get(i-1) : null;
								StudyMemorizeItem imsiItem = sonItemList.get(i);
								StudyMemorizeItem imsiNextItem = (i+1 < sonItemList.size())? sonItemList.get(i+1) : null;
								
								System.out.println("regionItem : " + ((imsiRegionItem != null)? imsiRegionItem.getStudyMemorizeVo().getSubject() + ", odr : " + imsiRegionItem.getStudyMemorizeVo().getOdr() : "null"));
								System.out.println("item : " + imsiItem.getStudyMemorizeVo().getSubject() + ", odr : " + imsiItem.getStudyMemorizeVo().getOdr());
								System.out.println("nextItem : " + ((imsiNextItem != null)? imsiNextItem.getStudyMemorizeVo().getSubject() + ", odr : " + imsiNextItem.getStudyMemorizeVo().getOdr() : "null"));
							}
							
							System.out.println("====================================================\n\n\n");
							
							parentItem.revalidate();
							parentItem.repaint();
							break;
						}
					}
				}
			});
			panel.add(button2);
			
			button3 = new JButton("▼");
			button3.setBorder(BorderFactory.createLineBorder(Color.black));
			button3.setPreferredSize(new Dimension(16, 16));
			button3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//해당 항목의 위에 있는 항목 불러오기
					StudyMemorizeItem currentItem = (StudyMemorizeItem)panel.getParent();
					StudyMemorizeItem parentItem = (StudyMemorizeItem)currentItem.getParent();
					List<StudyMemorizeItem> sonItemList = parentItem.getSonItemList();
					for(int i=0; i<sonItemList.size(); i++) {
						StudyMemorizeItem regionItem = (i-1 >= 0)? sonItemList.get(i-1) : null;
						StudyMemorizeItem item = sonItemList.get(i);
						StudyMemorizeItem nextItem = (i+1 < sonItemList.size())? sonItemList.get(i+1) : null;
						
						if(item == currentItem) {
							System.out.println("=================== DOWN MOVE!! ======================");
							System.out.println("regionItem : " + ((regionItem != null)? regionItem.getStudyMemorizeVo().getSubject() + ", odr : " + regionItem.getStudyMemorizeVo().getOdr() : "null"));
							System.out.println("item : " + item.getStudyMemorizeVo().getSubject() + ", odr : " + item.getStudyMemorizeVo().getOdr());
							System.out.println("nextItem : " + ((nextItem != null)? nextItem.getStudyMemorizeVo().getSubject() + ", odr : " + nextItem.getStudyMemorizeVo().getOdr() : "null"));
							System.out.println("=================== RESULT!! =======================");
							
							if(nextItem != null) {
								StudyMemorizeVo nextVo = nextItem.getStudyMemorizeVo();
								StudyMemorizeVo vo = item.getStudyMemorizeVo();
								
								int tempOdr = nextVo.getOdr();
								nextVo.setOdr(vo.getOdr());
								vo.setOdr(tempOdr);
								
								nextItem.setStudyMemorizeVo(nextVo);
								item.setStudyMemorizeVo(vo);
								
								parentItem.remove(item);
								parentItem.add(item, i+2);
								
								sonItemList.remove(i);
								sonItemList.add(i+1, item);
								parentItem.setSonItemList(sonItemList);
								
								StudyMemorizeItem imsiRegionItem = (i-1 >= 0)? sonItemList.get(i-1) : null;
								StudyMemorizeItem imsiItem = sonItemList.get(i);
								StudyMemorizeItem imsiNextItem = (i+1 < sonItemList.size())? sonItemList.get(i+1) : null;
								
								System.out.println("regionItem : " + ((imsiRegionItem != null)? imsiRegionItem.getStudyMemorizeVo().getSubject() + ", odr : " + imsiRegionItem.getStudyMemorizeVo().getOdr() : "null"));
								System.out.println("item : " + imsiItem.getStudyMemorizeVo().getSubject() + ", odr : " + imsiItem.getStudyMemorizeVo().getOdr());
								System.out.println("nextItem : " + ((imsiNextItem != null)? imsiNextItem.getStudyMemorizeVo().getSubject() + ", odr : " + imsiNextItem.getStudyMemorizeVo().getOdr() : "null"));
							}
							
							System.out.println("====================================================\n\n\n");
							
							parentItem.revalidate();
							parentItem.repaint();
							break;
						}
					}
				}
			});
			panel.add(button3);
			
			button4 = new JButton("상세");
			studyMemorizeDetailFrame = new StudyMemorizeDetailFrame(studyMemorizeVo.getSeq(), textField.getText(), studyMemorizeDetailService, fileService);
			button4.setBorder(BorderFactory.createLineBorder(Color.black));
			button4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(studyMemorizeVo != null && studyMemorizeVo.getSeq() > -1) {
						studyMemorizeDetailFrame.init();
						studyMemorizeDetailFrame.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(null, "빈 항목의 상세입력은 할 수 없습니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			panel.add(button4);
			
			this.add(panel);
			
			//자식 항목 출력하기.
			StudyMemorizeVo param = new StudyMemorizeVo();
			param.setParentSeq(studyMemorizeVo.getSeq());
			param.setLevel(studyMemorizeVo.getLevel() + 1);
			List<StudyMemorizeVo> sonItem = studyMemorizeService.selectList(param);
			sonItemList = new ArrayList<StudyMemorizeItem>();
			for(StudyMemorizeVo sonvo : sonItem) {
				StudyMemorizeItem item = new StudyMemorizeItem(sonvo);
				sonItemList.add(item);
				this.add(item);
			}
		}
		
		public void save() {
			if(this.studyMemorizeVo != null && textField.getText() != null && !textField.getText().trim().equals("")) {
				this.studyMemorizeVo.setCheckYn((checkBox.isSelected())? "Y" : "N");
				this.studyMemorizeVo.setSubject(textField.getText());
				
				//기존항목은 update, 신규항목은 insert
				if(this.studyMemorizeVo.getSeq() != -1) {
					studyMemorizeService.update(this.studyMemorizeVo);
				}else {
					int seq = studyMemorizeService.insert(this.studyMemorizeVo);
					this.studyMemorizeVo.setSeq(seq);
				}
			}
			
			//자식 항목 모두 저장처리
			for(StudyMemorizeItem item : sonItemList) {
				//부모 아이템 시퀀스값 가져와서 저장.
				if(this.studyMemorizeVo != null) {
					StudyMemorizeVo vo = item.getStudyMemorizeVo();
					vo.setParentSeq(this.studyMemorizeVo.getSeq());
					item.setStudyMemorizeVo(vo);
				}
				item.save();
			}
		}
		
		public void add(StudyMemorizeVo vo) {
			if(this.studyMemorizeVo != null) {
				vo.setParentSeq(this.studyMemorizeVo.getSeq());
				vo.setLevel(this.studyMemorizeVo.getLevel() + 1);
				vo.setOdr(this.getComponentCount());
			}else {
				vo.setParentSeq(-1);
				vo.setLevel(1);
				vo.setOdr(this.getComponentCount());
			}
			
			StudyMemorizeItem item = new StudyMemorizeItem(vo);
			sonItemList.add(item);
			this.add(item);
		}
		
		public void setCheck(boolean checkYn) {
			checkBox.setSelected(checkYn);
			
			for(StudyMemorizeItem item : sonItemList) {
				item.setCheck(checkYn);
			}
		}

		public StudyMemorizeVo getStudyMemorizeVo() {
			return studyMemorizeVo;
		}

		public void setStudyMemorizeVo(StudyMemorizeVo studyMemorizeVo) {
			this.studyMemorizeVo = studyMemorizeVo;
		}

		public List<StudyMemorizeItem> getSonItemList() {
			return sonItemList;
		}

		public void setSonItemList(List<StudyMemorizeItem> sonItemList) {
			this.sonItemList = sonItemList;
		}
	}
}
