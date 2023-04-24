package com.kdw.studyMeter.study.memorize.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.kdw.studyMeter.file.service.FileService;
import com.kdw.studyMeter.file.vo.FileVo;
import com.kdw.studyMeter.study.memorize.service.StudyMemorizeDetailService;
import com.kdw.studyMeter.study.memorize.vo.StudyMemorizeDetailVo;
import com.kdw.studyMeter.todo.frame.TodoDetailImageFrame;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class StudyMemorizeDetailFrame extends JFrame{
	
	private JPanel panel1;
	private JPanel panel11;
	private JPanel panel2;
	private JPanel panel3;
	
	private JScrollPane scrollPane1;
	
	private JButton button1;
	private JButton button2;
	private JButton button3;
	
	//할일 상세내용 목록
	private StudyMemorizeDetailItem studyMemorizeDetailItem;
	
	private StudyMemorizeDetailService studyMemorizeDetailService;
	
	private FileService fileService;
	
	private int parentSeq;
	
	private StudyMemorizeTestFrame studyMemorizeTestFrame;
	
	public StudyMemorizeDetailFrame(int parentSeq, String title, final StudyMemorizeDetailService studyMemorizeDetailService, final FileService fileService) {
		this.setTitle(title);
		this.setSize(650, 550);
		this.setLayout(new BorderLayout());
		
		this.parentSeq = parentSeq;
		
		this.studyMemorizeDetailService = studyMemorizeDetailService;
		this.fileService = fileService;
		
		panel1 = new JPanel();
		panel11 = new JPanel();
		panel11.setLayout(new BoxLayout(panel11, BoxLayout.Y_AXIS));
		
		//할일 목록 초기화
		//init();
		
		//스크롤 생성
		scrollPane1 = new JScrollPane(panel11);
		scrollPane1.setPreferredSize(new Dimension(600, 450));
		
		panel1.add(scrollPane1);
		this.add(panel1);
		
		panel2 = new JPanel();
		
		button1 = new JButton("저장");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//화면데이터 할일목록에 저장.
				Component[] comps = panel11.getComponents();
				for(int i=0; i<comps.length; i++) {
					StudyMemorizeDetailItem panel = (StudyMemorizeDetailItem)comps[i];
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
				StudyMemorizeDetailVo vo = new StudyMemorizeDetailVo();
				vo.setSeq(-1);
				vo.setParentSeq(-1);
				
				JPanel panel = new StudyMemorizeDetailItem(vo);
				panel11.add(panel);
				
				revalidate();
				repaint();
				
				scrollPane1.getVerticalScrollBar().setValue(scrollPane1.getVerticalScrollBar().getMaximum());
			}
		});
		panel2.add(button2);
		
		studyMemorizeTestFrame = new StudyMemorizeTestFrame(parentSeq, title + " 암기하기", studyMemorizeDetailService);
		button3 = new JButton("암기하기");
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				studyMemorizeTestFrame.init();
				studyMemorizeTestFrame.setVisible(true);
			}
		});
		panel2.add(button3);
		
		this.add(panel2, BorderLayout.SOUTH);
	}
	
	//할일 목록 초기화
	public void init() {
		panel11.removeAll();

		StudyMemorizeDetailVo vo = new StudyMemorizeDetailVo();
		vo.setSeq(-1);
		vo.setParentSeq(parentSeq);
		List<StudyMemorizeDetailVo> itemList = studyMemorizeDetailService.selectList(vo);
		
		if(itemList != null && itemList.size() > 0) {
			for(StudyMemorizeDetailVo item : itemList) {
				studyMemorizeDetailItem = new StudyMemorizeDetailItem(item);
				panel11.add(studyMemorizeDetailItem);
			}
		}else {
			studyMemorizeDetailItem = new StudyMemorizeDetailItem(vo);
			panel11.add(studyMemorizeDetailItem);
		}
		
		panel11.revalidate();
		panel11.repaint();
	}
	
	private class StudyMemorizeDetailItem extends JPanel{
		private JPanel panel1;
		private JPanel panel2;
		private JPanel panel3;
		
		private JDatePickerImpl datePicker;
		private UtilDateModel model;
		private JTextArea textArea;
		private JScrollPane scrollPane;
		private JButton button1;
		private JButton button2;
		
		private StudyMemorizeDetailVo studyMemorizeDetailVo;
		private List<FileVo> fileList;
		
		public StudyMemorizeDetailItem(StudyMemorizeDetailVo vo) {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.studyMemorizeDetailVo = vo;
			
			panel1 = new JPanel();
			panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
			
	        model = new UtilDateModel();
	        if(vo.getSeq() > -1) {
		        String planeDTO = vo.getDate();
		        String[] date = planeDTO.split("-");
		        int dateY = Integer.parseInt(date[0]);
		        int dateM = Integer.parseInt(date[1]) - 1;
		        int dateD = Integer.parseInt(date[2]);
		        model.setDate(dateY, dateM, dateD);
		        model.setSelected(true);
	        }else {
	        	LocalDate now = LocalDate.now();
		        model.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
		        model.setSelected(true);
	        }
	        
	        JDatePanelImpl datePanel = new JDatePanelImpl(model);
	        datePicker = new JDatePickerImpl(datePanel);
	        panel1.add(datePicker);
			
			button1 = new JButton("삭제");
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int result = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "확인", JOptionPane.OK_CANCEL_OPTION);
					if(result == 0) {
						datePicker.setEnabled(false);
						textArea.setEnabled(false);
						button1.setEnabled(false);
						studyMemorizeDetailVo.setUseYn("N");
					}
				}
			});
			panel1.add(button1);
			
			button2 = new JButton("첨부");
			button2.addActionListener(new ActionListener() {
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
							
							int seq = fileService.insert(file);
							String fileSeqs = studyMemorizeDetailVo.getFileSeqs();
							if(fileSeqs != null) {
								fileSeqs += ("," + seq);
							}else {
								fileSeqs = String.valueOf(seq);
							}
							studyMemorizeDetailVo.setFileSeqs(fileSeqs);
							studyMemorizeDetailService.update(studyMemorizeDetailVo);
							
							if(fileList == null)
								fileList = new ArrayList<FileVo>();
							fileList.add(file);

							JLabel label = new JLabel(file.getFileName());
							label.setCursor(new Cursor(Cursor.HAND_CURSOR));
							label.addMouseListener(new MouseListener() {
				
								public void mouseClicked(MouseEvent e) {
									StudyMemorizeDetailImageFrame frame = new StudyMemorizeDetailImageFrame(file.getFileName(), System.getProperty("user.dir") + "\\" + file.getFilePath());
									frame.setVisible(true);
								}
				
								public void mousePressed(MouseEvent e) {
									// TODO Auto-generated method stub
									
								}
				
								public void mouseReleased(MouseEvent e) {
									// TODO Auto-generated method stub
									
								}
				
								public void mouseEntered(MouseEvent e) {
									// TODO Auto-generated method stub
									
								}
				
								public void mouseExited(MouseEvent e) {
									// TODO Auto-generated method stub
									
								}
								
							});
							panel3.add(label);
							
							panel3.revalidate();
							panel3.repaint();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			panel1.add(button2);
			
			this.add(panel1);
	        
			panel2 = new JPanel();
			panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
			textArea = new JTextArea((vo.getSeq() == -1)? "" : vo.getContents());
			textArea.setLineWrap(true);
			scrollPane = new JScrollPane(textArea);
			scrollPane.setPreferredSize(new Dimension(560, 300));
			panel2.add(scrollPane);
			
			this.add(panel2);
			
			panel3 = new JPanel();
			
			//첨부된 파일 목록 출력
			fileList = vo.getFileInfos();
			if(vo.getFileSeqs() != null && !"".equals(vo.getFileSeqs().trim())) {
				for(final FileVo fileVo : fileList) {
					JLabel label = new JLabel(fileVo.getFileName());
					label.setCursor(new Cursor(Cursor.HAND_CURSOR));
					label.addMouseListener(new MouseListener() {
		
						public void mouseClicked(MouseEvent e) {
							if(fileVo.getFileExtns() != null && fileVo.getFileExtns().equals("jpg")) {
								TodoDetailImageFrame frame = new TodoDetailImageFrame(fileVo.getFileName(), System.getProperty("user.dir") + "\\" + fileVo.getFilePath());
								frame.setVisible(true);
							}else {
								File file = new File(fileVo.getFilePath());
								if(file.exists()) {
								    try {
										Desktop.getDesktop().open(file);
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						}
		
						public void mousePressed(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
		
						public void mouseReleased(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
		
						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
		
						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
						
					});
					panel3.add(label);
				}
			}
			
			this.add(panel3);
		}
		
		public void save() {
			studyMemorizeDetailVo.setParentSeq(parentSeq);
			studyMemorizeDetailVo.setDate(String.format("%04d-%02d-%02d", model.getYear(), (model.getMonth() + 1), model.getDay()));
			studyMemorizeDetailVo.setContents(textArea.getText());
			
			List<String> fileSeqs = new ArrayList<String>();
				if(fileList != null) {
				for(FileVo vo : fileList) {
					fileSeqs.add(String.valueOf(vo.getSeq()));
				}
			}
			studyMemorizeDetailVo.setFileSeqs(String.join(",", fileSeqs));
			
			if(textArea.getText() != null && !textArea.getText().trim().equals("")) {
				if(studyMemorizeDetailVo.getSeq() == -1) {
					studyMemorizeDetailService.insert(studyMemorizeDetailVo);
				}else {
					studyMemorizeDetailService.update(studyMemorizeDetailVo);
				}
			}
		}
	}
}
