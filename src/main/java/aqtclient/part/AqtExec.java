package aqtclient.part;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Texecjob;

public class AqtExec  {

	private TableViewer tv ;

	private Text txtjobno ;
	private Text txtcode ;
	private Text txtdesc ;
	Button type0 ;
	Button type1 ;
	Button chkDbSkip ;
	Button btn1, btn2, btn3 ,btnsave ;
	Spinner sptnum ;
	Text txtetc ;
	Text txtstart ;
	Text txtend ;
	
	Text txtreqdt ;
	Combo cmbstatus ;
	StyledText txtmsg ;
	SimpleDateFormat dformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss") ;
	Texecjob texecjob ;
	
	final String[] stat =  {"미실행","작업중","작업완료","작업중단"} ;
	
	VerifyListener vnumCheck = new VerifyListener() {
		
        @Override
        public void verifyText(VerifyEvent e) {

            Text text = (Text)e.getSource();

            // get old text and create new text by using the VerifyEvent.text
            final String oldS = text.getText();
            String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);

            boolean isNum = true;
            try
            {
                Integer.parseInt(newS);
            }
            catch(NumberFormatException ex)
            {
            	isNum = false;
            }

             e.doit = isNum;
        }

    } ;
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
		txtcode.setText(tjob.getTcode());
		txtdesc.setText(tjob.getTdesc());
		type0.setSelection(tjob.getExectype() == 0);
		type1.setSelection(tjob.getExectype() == 1);
		chkDbSkip.setSelection("1".equals(tjob.getDbskip()));
		sptnum.setSelection( tjob.getTnum() );
		txtetc.setText(tjob.getEtc());
		txtstart.setText(tjob.getStartDt() != null ? dformat.format(tjob.getStartDt()) : "");
		txtend.setText( tjob.getEndDt() != null ? dformat.format(tjob.getEndDt() ) : "");
		txtreqdt.setText(dformat.format(tjob.getReqstartDt()));
		cmbstatus.select(tjob.getResultstat());
		btnsave.setEnabled(tjob.getResultstat() == 0);
		
		txtmsg.setText(tjob.getMsg());
		
	}
	
	private RTN saveData(Texecjob tjob) throws ParseException {

		tjob.setTcode(txtcode.getText().toUpperCase());
		tjob.setTdesc(txtdesc.getText());
		tjob.setExectype(type0.getSelection() ? 0 : 1);
		tjob.setDbskip(chkDbSkip.getSelection() ? "1" : "0");
		tjob.setTnum(sptnum.getSelection());
		tjob.setEtc(txtetc.getText());
		tjob.setReqstartDt(dformat.parse( txtreqdt.getText()) );

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

		parent.setLayout(new FillLayout());
	    
//		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);	
//	    sashForm.setBackground(parent.getBackground());
		Composite compHeader = new Composite(parent, SWT.NONE);
		GridLayout headerLayout = new GridLayout(1,true);
		headerLayout.verticalSpacing = 10;
		headerLayout.marginHeight = 10;
		headerLayout.marginWidth = 15;
		compHeader.setLayout(headerLayout);

//		compHeader.setBackground(parent.getBackground());
		Label ltitle = new Label(compHeader, SWT.NONE);
		
    	ltitle.setImage(SWTResourceManager.getImage("images/tit_exec.png"));
		
		Composite compTitle = new Composite(compHeader, SWT.LINE_DASH);
		
		compTitle.setBackground(parent.getBackground());
		compTitle.setLayoutData(new GridData(SWT.FILL , SWT.FILL, true, true));
		GridLayout glin = new GridLayout(2, false) ;
		glin.horizontalSpacing = 20 ;
		compTitle.setLayout(glin);
		compTitle.setBackground(parent.getBackground());
		
		Label lbl = new Label(compTitle, SWT.NONE);
		lbl.setText("보기선택");
		lbl.setFont( IAqtVar.font1) ;
		
		Composite compchk = new Composite(compTitle, SWT.BORDER ) ;
		compchk.setLayout(new RowLayout(SWT.HORIZONTAL));
		btn1 = new Button(compchk, SWT.RADIO);
		btn2 = new Button(compchk, SWT.RADIO);
		btn3 = new Button(compchk, SWT.RADIO);
		btn1.setText("미실행Job");
		btn2.setText("실행Job");
		btn3.setText("모두보기");
		btn3.setSelection(true);
		btn1.setFont(IAqtVar.font1) ;
		btn2.setFont(IAqtVar.font1) ;
		btn3.setFont(IAqtVar.font1) ;
		
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

		Composite comptv = new Composite(compTitle, SWT.BORDER);
//		GridData gd_comptv = new GridData(SWT.FILL, SWT.TOP, true, true,2,1);
//		gd_comptv.minimumHeight = 300;
//		gd_comptv.heightHint = 400;
//		comptv.setLayoutData(gd_comptv);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).span(2, 1).applyTo(comptv);
		GridLayoutFactory.fillDefaults().equalWidth(true).numColumns(1).applyTo(comptv);

		tv = new TableViewer(comptv, SWT.NONE | SWT.FULL_SELECTION) ;
		
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
						texecjob.setStartDt(null);
						texecjob.setEndDt(null);
						texecjob.setMsg(null);
						texecjob.setReqstartDt(new Date());
					} catch (CloneNotSupportedException  e) {
						// TODO: handle exception
					}
					fillData(texecjob);
					txtcode.setFocus() ;
				}
				
			}
		});

	    
	    MenuItem delmi = new MenuItem(popupMenu, SWT.NONE);
	    delmi.setText("작업삭제");
	    copymi.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    delmi.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    
	    delmi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int i = tbl.getSelectionIndex() ;
				if ( i >= 0) {
					EntityManager em = AqtMain.emf.createEntityManager();
					TableItem item = tbl.getItem(i) ;
					Texecjob te = ((Texecjob)item.getData())  ;
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
	    tbl.setMenu(popupMenu);

		tbl.setHeaderVisible(true);
		tbl.setLinesVisible(true);
		tbl.setFont(IAqtVar.font1);
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

		tvc = createTableViewerColumn("Job No", 80, 0);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getPkey() + "";
			}
		});

		tvc = createTableViewerColumn("테스트코드", 120, 1);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getTcode() ;
			}
		});
		tvc = createTableViewerColumn("테스트내용", 200, 2);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getTdesc() ;
			}
		});
		tvc = createTableViewerColumn("Proc갯수", 100, 3);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getTnum() + "" ;
			}
		});

		tvc = createTableViewerColumn("작업시작요청일시", 180, 4);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return smdfmt.format(tj.getReqstartDt())  ;
			}
		});

		tvc = createTableViewerColumn("작업방법", 100, 5);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getExectype() == 0 ? type0.getText() : type1.getText() ;
			}
		});

		tvc = createTableViewerColumn("status", 80, 6);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return stat[tj.getResultstat()] ;
			}
		});
		
		tvc = createTableViewerColumn("DB Skip", 120, 7);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getDbskip() == "1" ? "Skip" : "";
			}
		});

		tvc = createTableViewerColumn("작업시작시간", 160, 8);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getStartDt() == null ? "" : smdfmt.format(tj.getStartDt())  ;
			}
		});

		tvc = createTableViewerColumn("작업종료시간", 160, 9);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Texecjob tj = (Texecjob) element;
				return tj.getEndDt() != null ? smdfmt.format(tj.getEndDt()) : "" ;
			}
		});
		tv.setContentProvider(new ContentProvider());
		
		Composite composite = new Composite(compHeader, SWT.NONE);

		GridLayout compLayout = new GridLayout(3, false);
		compLayout.verticalSpacing = 10;
		compLayout.marginTop = 20;
		compLayout.marginWidth = 10;
		compLayout.marginBottom = 5;
		
		composite.setLayout(compLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		Label lblTrans = new Label(composite, SWT.NONE);

		lblTrans.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
		lblTrans.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblTrans.setText("테스트 상세내역");
		lblTrans.setFont( IAqtVar.font1b) ;

		Button btnNew = new Button(composite, SWT.PUSH) ;
		btnNew.setText("신규작업입력");
		btnNew.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
		btnNew.setFont( IAqtVar.font1) ;
		btnNew.setEnabled( AqtMain.authtype == AuthType.TESTADM) ;
		btnNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				texecjob = new Texecjob() ;
				texecjob.setTcode(AqtMain.tmaster.getCode());
				fillData(texecjob);
				txtcode.setFocus() ;
			}
		});
		btnsave = new Button(composite, SWT.PUSH) ;
		btnsave.setText("테스트요청등록");
		btnsave.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
		btnsave.setFont( IAqtVar.font1) ;
		btnsave.setEnabled( AqtMain.authtype == AuthType.TESTADM) ; 
		btnsave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean result = MessageDialog.openConfirm(parent.getShell(), "테스트작업요청",
						"테스트작업요청을 등록하시겠습니까?" ) ;
				if (result) {
					try {
						saveData(texecjob) ;
						refreshScreen();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
/*		
		lblTrans = new Label(composite, SWT.NONE);
		lblTrans.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		lblTrans.setImage(SWTResourceManager.getImage("images/execbtn.png"));
		lblTrans.setCursor(IAqtVar.handc);
		lblTrans.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				boolean result = MessageDialog.openConfirm(parent.getShell(), "테스트작업요청",
						"테스트작업요청을 등록하시겠습니까?" ) ;
				if (result) {
					try {
						saveData(texecjob) ;
						refreshScreen();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
		});
*/		
		Composite form1  = new Composite(composite, SWT.BORDER) ;
		GridData gd_form1 = new GridData(SWT.FILL, SWT.FILL, true, false,3,1);
//		gd_form1.minimumHeight = 200;
		gd_form1.heightHint = 250 ;
		form1.setLayoutData(gd_form1);
		form1.setLayout(new GridLayout(4, true) );
		
		Label lbl1 = new Label(form1,SWT.RIGHT) ;
		lbl1.setText("1. Job No :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		txtjobno = new Text(form1,SWT.CENTER | SWT.BORDER | SWT.READ_ONLY) ;
		txtjobno.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		lbl1 = new Label(form1,SWT.RIGHT) ;
		lbl1.setText("2. 테스트코드 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		txtcode = new Text(form1,SWT.LEFT | SWT.BORDER ) ;
		txtcode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		txtcode.setTextLimit(20);
		txtcode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		lbl1 = new Label(form1,SWT.RIGHT) ;
		lbl1.setText("3. 테스트내용 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		txtdesc = new Text(form1,SWT.LEFT | SWT.BORDER) ;
		txtdesc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		txtdesc.setTextLimit(20);
		txtdesc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		lbl1 = new Label(form1,SWT.RIGHT) ;
		lbl1.setText("4. 작업방법 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		Composite compo_exectype = new Composite(form1, SWT.BORDER ) ;

		compo_exectype.setLayout(new RowLayout(SWT.HORIZONTAL));
		type0 = new Button(compo_exectype, SWT.RADIO);
		type1 = new Button(compo_exectype, SWT.RADIO);

		type0.setText("즉시실행");
		type1.setText("ASIS송신시간에 맞추어 송신");
		type0.setSelection(true);
		
		type0.setSelection(true);

		lbl1 = new Label(form1,SWT.RIGHT) ;
		lbl1.setText("5. 작업갯수(최대 400) :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		sptnum  = new Spinner( form1, SWT.BORDER) ;
		sptnum.setMaximum(400);
		sptnum.setSelection(2);
		sptnum.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		lbl1 = new Label(form1,SWT.RIGHT) ;
		lbl1.setText("6. DB Update 여부 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		chkDbSkip = new Button(form1, SWT.CHECK) ;
		chkDbSkip.setSelection(false);
		chkDbSkip.setText("DB Update skip");


		lbl1 = new Label(form1,SWT.RIGHT) ;
		lbl1.setText("7. 기타 대상선택조건 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		txtetc = new Text(form1, SWT.BORDER) ;
		txtetc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,3,1));
		txtetc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));


		lbl1 = new Label(form1,SWT.RIGHT) ;
		lbl1.setText("8. 작업시작 요청일 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		txtreqdt = new Text(form1, SWT.BORDER) ;
		txtreqdt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		txtreqdt.setText(dformat.format( System.currentTimeMillis() ));
		txtreqdt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtreqdt.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseDoubleClick(MouseEvent e) {
	    		Point pt = AqtMain.aqtmain.getShell().getDisplay().getCursorLocation() ; 
	    		Date dt = null;
				try {
					dt = dformat.parse(txtreqdt.getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	CalDialog cd = new CalDialog(Display.getCurrent().getActiveShell() , pt.x, pt.y + 20 ,  dt );
	    		
                String s = (String)cd.open();
                if (s != null) {
                	txtreqdt.setText(s.replace('-', '/') + txtreqdt.getText(10, 19) ) ;
                }
	    		super.mouseDoubleClick(e);
	    	}

		});

		lbl1 = new Label(form1,SWT.RIGHT ) ;
		lbl1.setText("9. 작업상태 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		cmbstatus = new Combo(form1, SWT.BORDER | SWT.READ_ONLY) ;
		cmbstatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		cmbstatus.setItems( stat );
		cmbstatus.setEnabled(false);

		lbl1 = new Label(form1,SWT.RIGHT) ;
		lbl1.setText("10. 작업시작일시 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		txtstart = new Text(form1, SWT.BORDER | SWT.READ_ONLY) ;
		txtstart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		lbl1 = new Label(form1,SWT.RIGHT) ;
		lbl1.setText("11. 작업종료일시 :");
		lbl1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		txtend = new Text(form1, SWT.BORDER | SWT.READ_ONLY) ;
		txtend.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		Composite compMessage = new Composite(composite, SWT.NONE);
		GridData gd_txtMsg = new GridData(GridData.FILL_BOTH );
		gd_txtMsg.horizontalSpan = 3;
		gd_txtMsg.heightHint = 120 ;
//		gd_txtMsg.verticalSpan = 20;
		compMessage.setLayoutData(gd_txtMsg);
		compMessage.setLayout(new GridLayout(1, false));
		lbl1 = new Label(compMessage,SWT.NONE) ;
		lbl1.setText("작업메세지");
		
		txtmsg = new StyledText(compMessage, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);

		txtmsg.setLayoutData(new GridData(SWT.FILL , SWT.FILL, true, true));
		txtmsg.setEditable(false);
		txtmsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		IAqtVar.setAllFont(form1, IAqtVar.font1 );
		
//		sashForm.setWeights(new int[] {40, 60});
//		sashForm.pack();
//		comptv.pack();
	}
	
    private TableViewerColumn createTableViewerColumn(String header, int width, int idx) 
    {
        TableViewerColumn column = new TableViewerColumn(tv, SWT.CENTER, idx);
        column.getColumn().setText(header);
        column.getColumn().setWidth(width);
        column.getColumn().setResizable(true);
        column.getColumn().setMoveable(true);
        
        return column;
    }
	private class myColumnProvider extends ColumnLabelProvider {
		@Override
		public Color getForeground(Object element) {
			if (element == null)
				return super.getForeground(element);
			return ((Texecjob) element).getResultstat() > 1 ? SWTResourceManager.getColor(SWT.COLOR_BLACK) :
					((Texecjob) element).getResultstat() == 1 ? SWTResourceManager.getColor(SWT.COLOR_RED)
							: SWTResourceManager.getColor(SWT.COLOR_BLUE);
		}
		@Override
		public Font getFont(Object element) {
			// TODO Auto-generated method stub
			if (element == null)
				return super.getFont(element);
			return ((Texecjob) element).getResultstat() != 2 ? IAqtVar.font1b :  super.getFont(element) ;

		}
	}

	private void refreshScreen () {
	    EntityManager em = AqtMain.emf.createEntityManager();
	    
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		String cond = (btn1.getSelection() ? "<2" : btn2.getSelection() ? ">0" : ">=0") ;
		
        List<Texecjob> lst = em.createQuery("select e from Texecjob e where e.resultstat" + cond 
        			+ " order by e.resultstat,e.pkey desc", Texecjob.class)
        		.getResultList();
        tv.setInput(lst);
        
		em.close();		
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
