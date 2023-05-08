package aqtclient.part;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Texecjob;

public class AqtExec  {

	private TableViewer tv ;

	private Text txtjobno ;
	private Text txtInfile ;
	private AqtTcodeCombo cmbCode ;
	private Text txtdesc ;
	private Button type0, type1 ;
	private Button btnkind1, btnkind9 ;
	private List<Texecjob> execlst ;
	
	Button chkDbSkip ;
	Button btn0, btn1, btn2, btn3 ;
	AqtButton btnsave , btnNew;
	Spinner sptnum, spinterval, sprepnum ;
	Label lb_num ;
	Text txtetc ;
	Text txtlimits ;
	Text txtstart ;
	Text txtend ;
	CDateTime cdt ;
	Text txtreqdt ;
//	Combo cmbstatus ;
	StyledText txtmsg ;
	SimpleDateFormat dformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss") ;
	Texecjob texecjob ;
	
	final String[] stat =  {"미실행","작업중","작업완료","작업중단"} ;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AqtExec(Composite parent, int style) {
		create (parent, style);
		
		refreshScreen();
	}
	
	
	private void fillData(Texecjob tjob) {
		
		txtjobno.setText(tjob.getPkey() +"");
		cmbCode.findSelect(tjob.getTcode());
		txtdesc.setText(tjob.getTdesc());
		type0.setSelection(tjob.getExectype() == 0);
		type1.setSelection(!type0.getSelection());
		btnkind1.setSelection(tjob.getJobkind() == 1);
		btnkind9.setSelection(!btnkind1.getSelection());
		txtInfile.setEditable(btnkind9.getSelection());
		txtInfile.setText(tjob.getInfile());
		chkDbSkip.setSelection("1".equals(tjob.getDbskip()));
		sptnum.setSelection( tjob.getTnum() );
		lb_num.setText(tjob.getJobkind() == 3 ? "URI별건수 :" : "송신간격(밀리초) :");
		lb_num.requestLayout();
		spinterval.setSelection( tjob.getReqnum() );
		sprepnum.setSelection( tjob.getRepnum() );
		
		txtetc.setText(tjob.getEtc());
		txtlimits.setText(tjob.getLimits());
		txtstart.setText( (tjob.getStartDt() != null ? dformat.format(tjob.getStartDt()) : "")  + " ~ " +
						 (tjob.getEndDt() != null ? dformat.format(tjob.getEndDt() ) : "") );
//		txtreqdt.setText(dformat.format(tjob.getReqstartDt()));
		cdt.setSelection(tjob.getReqstartDt());
//		cmbstatus.select(tjob.getResultstat());
		btnsave.setEnabled(tjob.getResultstat() == 0 && tjob.getJobkind() == 9 && AqtMain.authtype == AuthType.TESTADM);
		btnNew.setEnabled( AqtMain.authtype == AuthType.TESTADM) ;
		txtmsg.setText(tjob.getMsg());
		
	}
	
	private RTN saveData(Texecjob tjob) throws ParseException {
		if ( cmbCode.getTmaster().getEndDate() != null ) {
			MessageDialog.openInformation( AqtMain.aqtmain.getShell() , "작업불가", cmbCode.getTcode() + " 는 종료되었습니다.") ;
			return RTN.FAIL;
		}
		
		tjob.setTcode(cmbCode.getTcode() );
		tjob.setTdesc(txtdesc.getText());
		tjob.setJobkind(btnkind1.getSelection() ? 1 : 9);
		tjob.setInfile(txtInfile.getText());
		tjob.setExectype(type0.getSelection() ? 0 : 1);
		tjob.setDbskip(chkDbSkip.getSelection() ? "1" : "0");
		tjob.setTnum(sptnum.getSelection());
		tjob.setReqnum(spinterval.getSelection());
		tjob.setRepnum(sprepnum.getSelection());
		tjob.setEtc(txtetc.getText());
		tjob.setLimits(txtlimits.getText());
//		tjob.setReqstartDt(dformat.parse( txtreqdt.getText()) );
		tjob.setReqstartDt(cdt.getSelection());

		EntityManager em = AqtMain.emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.merge(tjob) ;
			transaction.commit();
		} catch (Exception e) {
			System.out.println(e);
			transaction.rollback();
		} finally {
			em.close();
		}
		
		return RTN.OK ;
	}
	private void create (Composite parent, int style) {

//		parent.setLayout(new FillLayout());
	    
//		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);	
//	    sashForm.setBackground(parent.getBackground());
		Composite compHeader = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).extendedMargins(15, 15, 10, 0).applyTo(compHeader);
//		compHeader.setBackground(parent.getBackground());
		Label ltitle = new Label(compHeader, SWT.NONE);
		
    	ltitle.setImage(AqtMain.getMyimage("tit_exec.png"));
		
		Composite compTitle = new Composite(compHeader, SWT.LINE_SOLID);
		
		compTitle.setBackground(parent.getBackground());
		compTitle.setLayoutData(new GridData(SWT.FILL , SWT.FILL, true, false));
		GridLayout glin = new GridLayout(4, false) ;
//		glin.horizontalSpacing = 20 ;
		compTitle.setLayout(glin);
//		compTitle.setBackground(parent.getBackground());
		
		Label lbl = new Label(compTitle, SWT.NONE);
		lbl.setText("【 전문송신이력 】  ");
		lbl.setFont( IAqtVar.font13) ;
		
		lbl = new Label(compTitle, SWT.NONE);
		lbl.setText("◇ 보기선택");
		lbl.setFont( IAqtVar.font1) ;
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(false, true).applyTo(lbl);
		
		Composite compchk = new Composite(compTitle, SWT.BORDER ) ;
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(false, true).applyTo(compchk);
//		compchk.setLayout(new RowLayout(SWT.HORIZONTAL));
		RowLayoutFactory.fillDefaults().margins(10, 5).type(SWT.HORIZONTAL).spacing(10).applyTo(compchk);
		btn0 = new Button(compchk, SWT.RADIO);
		btn1 = new Button(compchk, SWT.RADIO);
		btn1.setForeground( SWTResourceManager.getColor(SWT.COLOR_RED) ) ;
		btn2 = new Button(compchk, SWT.RADIO);
		btn3 = new Button(compchk, SWT.RADIO);
		btn0.setText("미실행");
		btn1.setText("실행중");
		btn2.setText("실행완료");
		btn3.setText("모두보기");
		btn3.setSelection(true);
		btn0.setFont(IAqtVar.font1) ;
		btn1.setFont(IAqtVar.font1) ;
		btn2.setFont(IAqtVar.font1) ;
		btn3.setFont(IAqtVar.font1) ;
		
		btn0.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshScreen();
				super.widgetSelected(e);
			}
		});
		btn1.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshScreen();
				super.widgetSelected(e);
			}
		});
		btn2.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshScreen();
				super.widgetSelected(e);
			}
		});
		btn3.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshScreen();
				super.widgetSelected(e);
			}
		});

		AqtButton btnSearch = new AqtButton(compTitle, SWT.PUSH,"새로고침");
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(true, false).minSize(100, -1).applyTo(btnSearch);
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				refreshScreen();
			}
		});

		Composite comptv = new Composite(compHeader, SWT.BORDER);
//		GridData gd_comptv = new GridData(SWT.FILL, SWT.TOP, true, true,2,1);
//		gd_comptv.minimumHeight = 300;
//		gd_comptv.heightHint = 400;
//		comptv.setLayoutData(gd_comptv);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(comptv);
		GridLayoutFactory.fillDefaults().equalWidth(true).numColumns(1).applyTo(comptv);

		tv = new TableViewer(comptv, SWT.NONE | SWT.FULL_SELECTION) ;
		tv.setComparator(new ViewerComparator() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				return compareElements( e1, e2);
			}
		});
		
		Table tbl = tv.getTable() ;

		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(tbl);
	    Menu popupMenu = new Menu(tbl);

	    MenuItem copymi = new MenuItem(popupMenu, SWT.NONE);
	    copymi.setText("작업복사");
	    copymi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int i = tbl.getSelectionIndex() ;
				if ( i >= 0) {
					TableItem item = tbl.getItem(i) ;
					Texecjob te = ((Texecjob)item.getData())  ;

					try {
						texecjob = te.copy() ;
						texecjob.setPkey(0);
						texecjob.setResultstat(0);
						texecjob.setJobkind(9);
						texecjob.setStartDt(null);
						texecjob.setEndDt(null);
						texecjob.setMsg("");
						texecjob.setReqstartDt(new Date());
					} catch (CloneNotSupportedException  e) {
						// TODO: handle exception
					}
					fillData(texecjob);
					cmbCode.getControl().setFocus() ;
				}
				
			}
		});

	    
	    MenuItem delmi = new MenuItem(popupMenu, SWT.NONE);
	    delmi.setText("작업삭제");
	    delmi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int i = tbl.getSelectionIndex() ;
				if ( i >= 0) {
					EntityManager em = AqtMain.emf.createEntityManager();
					em.setFlushMode(FlushModeType.AUTO);
					TableItem item = tbl.getItem(i) ;
					Texecjob te = ((Texecjob)item.getData())  ;
					if ( te.getResultstat() == 1 ) {
						MessageDialog.openInformation(parent.getShell(), "삭제불가", "이 작업은 삭제 할 수 없습니다.") ;
						return ;
					}

					boolean result = MessageDialog.openConfirm(parent.getShell(), "작업삭제",
							"["+ te.getTdesc() + "] 삭제하시겠습니까?" ) ;
					if (  result ) {
						te = em.find(Texecjob.class, te.getPkey()) ;
						em.getTransaction().begin();
						em.remove(te);
						em.getTransaction().commit();
					}
					em.close();
					refreshScreen();
				}
				
			}
		});
	    MenuItem pm_reexec = new MenuItem(popupMenu, SWT.NONE);
	    pm_reexec.setText("작업재시작");
	    pm_reexec.setToolTipText("이 작업을 다시 요청합니다.");
	    pm_reexec.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int i = tbl.getSelectionIndex() ;
				if ( i >= 0) {
					TableItem item = tbl.getItem(i) ;
					Texecjob te = ((Texecjob)item.getData())  ;
//					if ( te.getJobkind() == 1 ) {
//						EntityManager em = AqtMain.emf.createEntityManager();
//						try {
//							em.getTransaction().begin();
//							te.setResultstat(0);
//							te.setReqstartDt(new Date()) ;
//							em.merge(te);
//							em.getTransaction().commit();
//							MessageDialog.openInformation(parent.getShell(), "작업재요청", "재요청 되었습니다.") ;
//						} catch (Exception e) {
//							em.getTransaction().rollback();
//							MessageDialog.openInformation(parent.getShell(), "작업재요청", e.getMessage()) ;
//						}
//						em.close();
//						return ;
//					}
					
					if ( te.getJobkind() == 3 ) {
						MessageDialog.openInformation(parent.getShell(), "재요청불가", "전문복제는 재작업 할 수 없습니다.") ;
						return ;
					}
					te.setResultstat(0);
					te.setEndDt(null);
					fillData(te);
					btnsave.setEnabled(true);
					btnNew.setEnabled(false);
					MessageDialog.openInformation(parent.getShell(), "작업재시작", "데이터 수정후 저장버튼을 누르면 재시작됩니다.") ;
				}
				
			}
		});
		MenuItem pm_list = new MenuItem(popupMenu, SWT.NONE);
		pm_list.setText("전문목록");
		pm_list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				int i = tbl.getSelectionIndex() ;
				if (  i >= 0 ) {
					Texecjob te = (Texecjob) tbl.getItem(i).getData() ;
					AqtMain.openTrList("t.tcode = '"+ te.getTcode() + "' "); 
				}
				
			}
		});
	    
		delmi.setEnabled(AqtMain.authtype == AuthType.TESTADM);
		copymi.setEnabled(AqtMain.authtype == AuthType.TESTADM);
		pm_reexec.setEnabled(AqtMain.authtype == AuthType.TESTADM);
		pm_list.setEnabled(true);

	    tbl.setMenu(popupMenu);

		tbl.setHeaderVisible(true);
		tbl.setLinesVisible(true);
		tbl.setFont(IAqtVar.font1b);
		tbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tbl.setHeaderBackground(AqtMain.htcol);
		tbl.setHeaderForeground(AqtMain.forecol);
		tbl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem item = tbl.getItem(tbl.getSelectionIndex()) ;
				texecjob = ((Texecjob)item.getData())  ;
				fillData(texecjob);
				super.widgetSelected(e);
			}
		});
		tv.setUseHashlookup(true);
		
		TableViewerColumn tvc;

		SimpleDateFormat smdfmt = new SimpleDateFormat("MM/dd HH.mm.ss");

		tvc = createTableViewerColumn("Job No", 70, 1);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getPkey() + "";
			}
		});
		
		tvc = createTableViewerColumn("작업종류", 100, 1);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getJobkindNm();
			}
		});

		tvc = createTableViewerColumn("테스트ID", 100, 1);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getTcode() ;
			}
		});
		tvc = createTableViewerColumn("테스트내용", 240, 1);
		tvc.getColumn().setAlignment(SWT.LEFT);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getTdesc() ;
			}
		});
		tvc = createTableViewerColumn("작업갯수", 80, 1);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getTnum() + "" ;
			}
		});

		tvc = createTableViewerColumn("작업시작요청일시", 160, 1);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return smdfmt.format(tj.getReqstartDt())  ;
			}
		});

//		tvc = createTableViewerColumn("작업방법", 120, 0);
//		tvc.setLabelProvider(new myColumnProvider() {
//			public String getText(Object element) {
//				if (element == null)
//					return super.getText(element);
//				Texecjob tj = (Texecjob) element;
//				return tj.getExectype() == 0 ? type0.getText() : type1.getText() ;
//			}
//		});

		tvc = createTableViewerColumn("상태", 80, 0);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return stat[tj.getResultstat()] ;
			}
		});
		
//		tvc = createTableViewerColumn("DB Skip", 120, 7);
//		tvc.setLabelProvider(new myColumnProvider() {
//			public String getText(Object element) {
//				if (element == null)
//					return super.getText(element);
//				Texecjob tj = (Texecjob) element;
//				return tj.getDbskip() == "1" ? "Skip" : "";
//			}
//		});

		tvc = createTableViewerColumn("작업시작시간", 160, 1);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getStartDt() == null ? "" : smdfmt.format(tj.getStartDt())  ;
			}
		});

		tvc = createTableViewerColumn("작업종료시간", 160, 1);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getEndDt() != null ? smdfmt.format(tj.getEndDt()) : "" ;
			}
		});
		tvc = createTableViewerColumn("입력파일명", 180, 0);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getInfile() != null ? tj.getInfile() : "" ;
			}
		});
		
		tv.setContentProvider(new ContentProvider());
		
		Composite composite = new Composite(compHeader, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(4).equalWidth(false).applyTo(composite);

//		GridLayout compLayout = new GridLayout(3, false);
//		compLayout.verticalSpacing = 10;
//		compLayout.marginTop = 20;
//		compLayout.marginWidth = 0;
//		compLayout.marginBottom = 0;
//		composite.setLayout(compLayout);

//		composite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BOTTOM).grab(true, false).applyTo(composite);
		Label lblTrans = new Label(composite, SWT.NONE);

//		lblTrans.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.TOP).grab(true, false).applyTo(lblTrans);
		lblTrans.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblTrans.setText("테스트 상세내역 ▽");
		lblTrans.setFont( IAqtVar.font13b) ;

		AqtButton btnCopy = new AqtButton(composite, SWT.PUSH,"전문생성");
		btnCopy.setToolTipText("다른 테스트 정보로 부터 데이터를 복제하여 새로운 테스트 데이터를 생성합니다.");
		GridDataFactory.fillDefaults().align(SWT.END, SWT.TOP).grab(false, false).minSize(100, -1).applyTo(btnCopy);
		btnCopy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				AqtCopyTdata aqtcopy = new AqtCopyTdata(parent.getShell(), AqtMain.aqtmain.getGtcode() );
				aqtcopy.open() ;
				refreshScreen();
			}
		});

		btnNew = new AqtButton(composite, SWT.PUSH, "신규작업입력") ;
//		btnNew.setText("신규작업입력");
//		btnNew.setLayoutData(new GridData(SWT.END, SWT.TOP, false, false));
		GridDataFactory.fillDefaults().align(SWT.END, SWT.TOP).grab(false, false).applyTo(btnNew);
//		btnNew.setFont( IAqtVar.font1) ;
		btnNew.setEnabled( AqtMain.authtype == AuthType.TESTADM) ;
		btnNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				texecjob = new Texecjob() ;
				texecjob.setTcode(cmbCode.getTcode());
				fillData(texecjob);
				cmbCode.getControl().setFocus() ;
				btnNew.setEnabled(false) ;
			}
		});
		btnsave = new AqtButton(composite, SWT.PUSH, " 저장 ") ;
		btnsave.setLayoutData(new GridData(SWT.END, SWT.TOP, false, false));
		btnsave.setEnabled( AqtMain.authtype == AuthType.TESTADM ) ; 
		btnsave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean result = MessageDialog.openConfirm(parent.getShell(), "테스트작업요청",
						"테스트작업요청을 등록하시겠습니까?" ) ;
				if (result) {
					try {
						if ( saveData(texecjob) == RTN.OK) 	refreshScreen();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		Composite form1  = new Composite(composite, SWT.BORDER) ;
		GridData gd_form1 = new GridData(SWT.FILL, SWT.TOP, true, false,3,1);
//		gd_form1.minimumHeight = 200;
//		gd_form1.heightHint = 300 ;
		form1.setLayoutData(gd_form1);
		form1.setLayout(new GridLayout(6, false) );
		
		Label lbl1 = new Label(form1,SWT.LEFT) ;
		lbl1.setText("Job No :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		txtjobno = new Text(form1,SWT.CENTER | SWT.BORDER | SWT.READ_ONLY) ;
		txtjobno.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		lbl1 = new Label(form1,SWT.LEFT) ;
		lbl1.setText("테스트ID :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		cmbCode = new AqtTcodeCombo(form1, SWT.READ_ONLY);
		cmbCode.getControl().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		lbl1 = new Label(form1,SWT.LEFT) ;
		lbl1.setText("테스트내용 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		txtdesc = new Text(form1,SWT.LEFT | SWT.BORDER) ;
		txtdesc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtdesc.setTextLimit(20);
		txtdesc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		lbl1 = new Label(form1,SWT.LEFT) ;
		lbl1.setText("작업종류 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		Composite compo_exectype = new Composite(form1, SWT.BORDER ) ;

		RowLayoutFactory.fillDefaults().margins(10, 5).type(SWT.HORIZONTAL).spacing(10).applyTo(compo_exectype);
		btnkind1 = new Button(compo_exectype, SWT.RADIO);
		btnkind9 = new Button(compo_exectype, SWT.RADIO);

		btnkind1.setText("Import 파일");
		btnkind9.setText("전문송신");
		btnkind9.setSelection(true);
		
		btnkind1.setFont(IAqtVar.font1) ;
		btnkind9.setFont(IAqtVar.font1) ;

		lbl1 = new Label(form1,SWT.LEFT) ;
		lbl1.setText("입력파일명 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		txtInfile = new Text(form1,SWT.LEFT | SWT.BORDER ) ;
		txtInfile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtInfile.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtInfile.setCursor(IAqtVar.handc);
		txtInfile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
		    	String[] ext = {  "*.pcap", "*" }  ;
		    	final FileDialog dlg = new FileDialog ( Display.getDefault().getActiveShell() , SWT.APPLICATION_MODAL | SWT.OPEN );
		    	dlg.setFilterExtensions ( ext );
		    	dlg.setText ( "cap 파일 선택" );

		    	String fileName = dlg.open ();
		    	if ( fileName != null ) txtInfile.setText(fileName);
				// TODO Auto-generated method stub
				super.mouseDoubleClick(e);
			}
		});

		lbl1 = new Label(form1,SWT.LEFT) ;
		lbl1.setText("작업방법 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		compo_exectype = new Composite(form1, SWT.BORDER ) ;

		RowLayoutFactory.fillDefaults().margins(10, 5).type(SWT.HORIZONTAL).spacing(10).applyTo(compo_exectype);
		type0 = new Button(compo_exectype, SWT.RADIO);
		type1 = new Button(compo_exectype, SWT.RADIO);

		type0.setText("즉시실행");
		type1.setText("원본시간간격 송신");
		type0.setSelection(true);
		
		type0.setSelection(true);
		type1.setFont(IAqtVar.font1) ;
		type0.setFont(IAqtVar.font1) ;
//		compo_exectype.setEnabled(false);

		lbl1 = new Label(form1,SWT.LEFT) ;
		lbl1.setText("작업갯수 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		lbl1.pack();

		sptnum  = new Spinner( form1, SWT.BORDER | SWT.CENTER) ;
		sptnum.setMaximum(400);
		sptnum.setSelection(1);
		sptnum.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		lb_num = new Label(form1,SWT.LEFT) ;
		lb_num.setText("송신간격(밀리초) :");
		lb_num.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		lb_num.pack();
		spinterval  = new Spinner( form1, SWT.BORDER | SWT.CENTER) ;
		spinterval.setMaximum(30000);
		spinterval.setSelection(1);
		spinterval.setToolTipText("전문송신시 건당 대기 시간(밀리초)을 선택합니다.");
		spinterval.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		lbl1 = new Label(form1,SWT.LEFT) ;
		lbl1.setText("작업반복회수 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		sprepnum  = new Spinner( form1, SWT.BORDER | SWT.CENTER) ;
		sprepnum.setMaximum(30000);
		sprepnum.setSelection(1);
		sprepnum.setToolTipText("전문송신시 작업반복횟수");
		sprepnum.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		lbl1 = new Label(form1,SWT.LEFT) ;
		lbl1.setText(" 기타 대상선택조건 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		txtetc = new Text(form1, SWT.BORDER) ;
		txtetc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,3,1));
		txtetc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtetc.setToolTipText("** t.칼럼명 의 형식으로 입력 **\n* method: POST, GET 등 \n* rcode: 응답코드(200 등)\n* sflag: 0.미수행 1.성공 2.실패 \n* srcip,srcport: 소스ip,port\n* dstip,dstport: 목적지ip,port\n* svctime: 응답소요시간");

		lbl1 = new Label(form1,SWT.LEFT) ;
		lbl1.setText(" DB Update 여부 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		chkDbSkip = new Button(form1, SWT.CHECK) ;
		chkDbSkip.setSelection(false);
		chkDbSkip.setText("DB Update Skip");

		lbl1 = new Label(form1,SWT.LEFT) ;
		lbl1.setText(" 처리건수 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		txtlimits = new Text(form1, SWT.BORDER) ;
		txtlimits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtlimits.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtlimits.setToolTipText("10 <- 10건\n10,20 <- 10번째부터 20건");
		txtlimits.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent arg0) {
				arg0.doit = arg0.text.matches("[0-9,]*")  ;
			}
		});

		lbl1 = new Label(form1,SWT.LEFT) ;
		lbl1.setText("작업시작 요청일 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		cdt = new CDateTime(form1, CDT.BORDER |  CDT.DROP_DOWN | CDT.DATE_LONG | CDT.TIME_MEDIUM);
		cdt.setSelection(new Date());
		cdt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		cdt.setPattern("yyyy/MM/dd hh:mm:ss a");
		cdt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		/*
		 * lbl1 = new Label(form1,SWT.LEFT ) ; lbl1.setText("작업상태 :");
		 * lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		 * 
		 * cmbstatus = new Combo(form1, SWT.BORDER | SWT.READ_ONLY ) ;
		 * cmbstatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		 * cmbstatus.setItems( stat ); cmbstatus.setEnabled(false);
		 */
		lbl1 = new Label(form1,SWT.LEFT) ;
		lbl1.setText("작업기간 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		txtstart = new Text(form1, SWT.BORDER | SWT.READ_ONLY) ;
		txtstart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

//		lbl1 = new Label(form1,SWT.LEFT) ;
//		lbl1.setText("작업종료일시 :");
//		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
//
//		txtend = new Text(form1, SWT.BORDER | SWT.READ_ONLY) ;
//		txtend.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Composite compMessage = new Composite(compHeader, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BOTTOM).span(2, 3).grab(true, false).applyTo(compMessage);
		compMessage.setLayout(new GridLayout(1, false));
		lbl1 = new Label(compMessage,SWT.NONE) ;
		lbl1.setText("작업메세지");
		
		txtmsg = new StyledText(compMessage, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);

//		txtmsg.setLayoutData(new GridData(SWT.FILL , SWT.FILL, true, true));
		txtmsg.setEditable(false);
		txtmsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BOTTOM).hint(-1, 100).grab(true, false).applyTo(txtmsg);
		
		IAqtVar.setAllFont(form1, IAqtVar.font1 );
		new Label(form1, SWT.NONE);
		form1.pack();
		
//		sashForm.setWeights(new int[] {40, 60});
//		sashForm.pack();
//		comptv.pack();
	}
	
    private TableViewerColumn createTableViewerColumn(String header, int width, int idx) 
    {
        TableViewerColumn column = new TableViewerColumn(tv, SWT.CENTER );
        TableColumn tcol = column.getColumn() ;
        tcol.setText(header);
        tcol.setWidth(width);
        tcol.setResizable(true);
        tcol.setMoveable(true);
        
        if (idx == 1)
        	tcol.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					Table table = tcol.getParent() ;
					if (tcol.equals(table.getSortColumn())) {
						int dire = table.getSortDirection() ;
						table.setSortDirection(dire == SWT.UP ? SWT.DOWN : dire == SWT.NONE ? SWT.UP : SWT.NONE );
					} else {
						table.setSortColumn(tcol);
						table.setSortDirection(SWT.UP);
					}
					tv.refresh();
				}
			});

        return column;
    }
	private class myColumnProvider extends ColumnLabelProvider {
		@Override
		public Color getForeground(Object element) {
			if (element == null)
				return super.getForeground(element);
			
			switch (((Texecjob) element).getResultstat()) {
			case 0:
				return SWTResourceManager.getColor(SWT.COLOR_BLUE);
			case 1:
				return SWTResourceManager.getColor(SWT.COLOR_RED);
			case 2:
				return SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY);
			default:
				return SWTResourceManager.getColor(SWT.COLOR_DARK_RED);
			}
		}
//		@Override
//		public Font getFont(Object element) {
//			// TODO Auto-generated method stub
//			if (element == null)
//				return super.getFont(element);
//			return ((Texecjob) element).getResultstat() != 2 ? IAqtVar.font1b :  super.getFont(element) ;
//
//		}
	}

	private void refreshScreen () {
	    EntityManager em = AqtMain.emf.createEntityManager();
	    
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		String cond = btn3.getSelection() ? "" : "where resultstat " + (btn0.getSelection() ? "=0" : btn1.getSelection() ? "=1" :  "> 1")  ;
		
		String qstmt = "select * from Texecjob  " + cond 
    			+ " order by resultstat,startdt desc, pkey desc" ;
//		System.out.println(qstmt);
        execlst = em.createNativeQuery(qstmt, Texecjob.class)
        		.getResultList();
        em.close();		
        
        tv.setInput(execlst);
        tv.getTable().getParent().requestLayout();
        if (execlst.size() > 0) {
        	tv.getTable().setSelection(0);
        	tv.getTable().notifyListeners(SWT.Selection, null);
        }
        AqtMain.aqtmain.setStatus(String.format(">> 조회건수 %,d 건" ,execlst.size() ) );        
	}
	
    protected int compareElements(Object e1, Object e2) {
		Table table = tv.getTable();
		int index = Arrays.asList(table.getColumns()).indexOf(table.getSortColumn());
		int result = 0;
		Date d1, d2 ;
		Integer l1, l2 ;
		String s1,s2 ;
		if (index != -1) {
			switch (index) {
			case 0:
				l1 = ((Texecjob)e1).getPkey() ;
				l2 = ((Texecjob)e2).getPkey() ;
				result = l1.compareTo(l2);
				break;
			case 1:
				l1 = ((Texecjob)e1).getJobkind() ;
				l2 = ((Texecjob)e2).getJobkind() ;
				result = l1.compareTo(l2);
				break;
			case 2:
				s1 = ((Texecjob)e1).getTcode() ;
				s2 = ((Texecjob)e2).getTcode() ;
				result = s1.compareTo(s2);
				break;
			case 3:
				s1 = ((Texecjob)e1).getTdesc() ;
				s2 = ((Texecjob)e2).getTdesc() ;
				result = s1.compareTo(s2);
				break;
			case 4:
				l1 = ((Texecjob)e1).getTnum() ;
				l2 = ((Texecjob)e2).getTnum() ;
				result = l1.compareTo(l2);
				break;
			case 5:
				d1 = ((Texecjob)e1).getReqstartDt() ;
				d2 = ((Texecjob)e2).getReqstartDt() ;
				result = d1.compareTo(d2);
				break;
			case 7:
				d1 = ((Texecjob)e1).getStartDt() ;
				d2 = ((Texecjob)e2).getStartDt() ;
				result = d1.compareTo(d2);
				break;
			case 8:
				d1 = ((Texecjob)e1).getEndDt() ;
				d2 = ((Texecjob)e2).getEndDt() ;
				result = d1.compareTo(d2);
				break;
			default:
				result = 0;
			}
		}
		return table.getSortDirection() == SWT.UP ? result : -result;
	}

	private class ContentProvider implements IStructuredContentProvider {
		/**
		 * 
		 */
		@Override
		public Object[] getElements(Object input) {
			// return new Object[0];
			List<Texecjob> arrayList = (List<Texecjob>) input;
			return arrayList.toArray();
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

}
