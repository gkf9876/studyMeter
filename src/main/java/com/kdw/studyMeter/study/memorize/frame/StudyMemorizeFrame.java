package com.kdw.studyMeter.study.memorize.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
	private JButton button3;
	
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
		
		button3 = new JButton("PDF 출력");
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		        String result = ""; // 초기값이 null이 들어가면 오류가 발생될수 있기 때문에 공백을 지정
		        
		        try {
		            Document document = new Document(); // pdf문서를 처리하는 객체
		 
		            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("d:/sample.pdf"));
		            // pdf파일의 저장경로를 d드라이브의 sample.pdf로 한다는 뜻
		 
		            document.open(); // 웹페이지에 접근하는 객체를 연다
		 
		            BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/malgun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		            // pdf가 기본적으로 한글처리가 안되기 때문에 한글폰트 처리를 따로 해주어야 한다.
		            // createFont메소드에 사용할 폰트의 경로 (malgun.ttf)파일의 경로를 지정해준다.
		            // 만약에 이 경로에 없을 경우엔 java파일로 만들어서 집어넣어야 한다.
		 
		            Font font = new Font(baseFont, 12); // 폰트의 사이즈를 12픽셀로 한다.
		 
		            PdfPTable table = new PdfPTable(5); // 4개의 셀을 가진 테이블 객체를 생성 (pdf파일에 나타날 테이블)
		            Chunk chunk = new Chunk("암기목록", font); // 타이틀 객체를 생성 (타이틀의 이름을 장바구니로 하고 위에 있는 font를 사용)
		            Paragraph ph = new Paragraph(chunk);
		            ph.setAlignment(Element.ALIGN_CENTER);
		            document.add(ph); // 문단을 만들어서 가운데 정렬 (타이틀의 이름을 가운데 정렬한다는 뜻)
		 
		            document.add(Chunk.NEWLINE);
		            document.add(Chunk.NEWLINE); // 줄바꿈 (왜냐하면 타이틀에서 두줄을 내린후에 셀(테이블)이 나오기 때문)
		 
		            PdfPCell cell1 = new PdfPCell(new Phrase("제목", font)); // 셀의 이름과 폰트를 지정해서 셀을 생성한다.
		            cell1.setHorizontalAlignment(Element.ALIGN_CENTER); // 셀의 정렬방식을 지정한다. (가운데정렬)
		 
		            PdfPCell cell2 = new PdfPCell(new Phrase("날짜", font));
		            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		 
		            PdfPCell cell3 = new PdfPCell(new Phrase("내용", font));
		            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		 
		            PdfPCell cell4 = new PdfPCell(new Phrase("등록일", font));
		            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		 
		            PdfPCell cell5 = new PdfPCell(new Phrase("첨부파일", font));
		            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		 
		            table.addCell(cell1); // 그리고 테이블에 위에서 생성시킨 셀을 넣는다.
		            table.addCell(cell2);
		            table.addCell(cell3);
		            table.addCell(cell4);
		            table.addCell(cell5);
		 
		            for (int i = 0; i < 8; i++) {
		                PdfPCell cellProductName = new PdfPCell(new Phrase("제목_" + i, font)); // 반복문을 사용해서 상품정보를 하나씩
		                                                                                                    // 출력해서 셀에 넣고 테이블에
		                                                                                                    // 저장한다.
		 
		                PdfPCell cellPrice = new PdfPCell(new Phrase("" + "날짜_" + i, font));
		                // Phrase타입은 숫자형(int형 같은타입)으로 하면 에러가 발생되기 때문에 dto앞에 공백("")주어서 String타입으로 변경한다.
		 
		                PdfPCell cellAmount = new PdfPCell(new Phrase("" + "내용_" + i, font));
		                // Phrase타입은 숫자형(int형 같은타입)으로 하면 에러가 발생되기 때문에 dto앞에 공백("")주어서 String타입으로 변경한다.
		 
		                PdfPCell cellMoney = new PdfPCell(new Phrase("" + "등록일_" + i, font));
		                // Phrase타입은 숫자형(int형 같은타입)으로 하면 에러가 발생되기 때문에 dto앞에 공백("")주어서 String타입으로 변경한다.
		 
		                PdfPCell cellMoney2 = new PdfPCell(new Phrase("" + "첨부파일_" + i, font));
		                // Phrase타입은 숫자형(int형 같은타입)으로 하면 에러가 발생되기 때문에 dto앞에 공백("")주어서 String타입으로 변경한다.
		                
		                table.addCell(cellProductName); // 셀의 데이터를 테이블에 저장한다. (장바구니안에 들어있는 갯수만큼 테이블이 만들어진다)
		                table.addCell(cellPrice);
		                table.addCell(cellAmount);
		                table.addCell(cellMoney);
		                table.addCell(cellMoney2);
		            }
		            document.add(table); // 웹접근 객체에 table를 저장한다.
		            document.close(); // 저장이 끝났으면 document객체를 닫는다.
		            result = "pdf 파일이 생성되었습니다.";
		 
		        } catch (Exception exe) {
		        	exe.printStackTrace();
		            result = "pdf 파일 생성 실패...";
		        }
		        System.out.println(result);
			}
		});
		panel2.add(button3);
		
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
