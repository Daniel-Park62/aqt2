package aqtclient.part;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityManager;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PopupList;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.TpacketDTO;
import aqtclient.model.Ttcppacket;


/* 실제 데이터로 확인이 필요함 */

public class AqtDetail extends Dialog {
	protected Shell shell;
	protected Object result;
	private Ttcppacket tpacket;  // testcode1 의 tpacket
	private Text txtSlen;
	private StyledText txtSendMsg;
	private Text txtRlen;
	private StyledText txtReceiveMsg;
	private Text txtPkey;
	private Text txtCmpid;
	private Text txtTestCode;
	private Text txtUri;
	private Text txtCol1;
	private Text txtCol2;
	private Text txtOStime;
	private Text txtStime;
	private Text txtRtime;
	private Text txtElapsed;
	private Text txtSvcTime;
	private StyledText txtRhead;
	private Text txtRcode;
	private Text txtCdate;
	protected String sv_select = "UTF-8";
	private Table tbl ;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
//	public AqtDetail(Composite parent, int style) {
//		create(parent, style);
//	}
	public AqtDetail(Shell parent, int style) {
		super(parent, style);
		setText(IAqtVar.titnm);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.setLocation(10, 10);
		shell.open();
		
		Display display = getParent().getDisplay();
		
		fillScreen();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
//	private void create (Composite parent, int style) {

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		
		shell = new Shell(getParent(), getStyle() | SWT.RESIZE | SWT.MAX);
		shell.setSize(1600, 1000);
		shell.setText(getText());
		shell.setBackground(SWTResourceManager.getColor(225,230,246));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
//		parent.setLayout(new FillLayout());
		shell.setLayout(new FillLayout());
	    
//	    sashForm = new SashForm(shell, SWT.VERTICAL);	
//	    sashForm.setBackground(SWTResourceManager.getColor(225,230,246));
		Composite compHeader = new Composite(shell, SWT.NONE);
		GridLayout gl_compHeader = new GridLayout();
		gl_compHeader.verticalSpacing = 10;
		gl_compHeader.marginHeight = 20;
		gl_compHeader.marginWidth = 20;
		gl_compHeader.marginBottom = 5;
		gl_compHeader.numColumns = 1;
		compHeader.setLayout(gl_compHeader);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(compHeader);
//		compHeader.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Composite compTitle = new Composite(compHeader, SWT.LINE_DASH);
		
		GridData gd_compTitle = new GridData(SWT.FILL , SWT.TOP, true, false);
//		gd_compTitle.horizontalSpan = 6;
		compTitle.setLayoutData(gd_compTitle);
		compTitle.setLayout(new GridLayout(5, false));
//		compTitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Label ltitle = new Label(compTitle, SWT.NONE);
		
    	ltitle.setText("전문상세보기" ) ;
    	ltitle.setFont( IAqtVar.title_font );
    	GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(ltitle);
    	
    	Button bt_next = new Button(compTitle, SWT.PUSH);
    	bt_next.setText("다음전문");
    	bt_next.setToolTipText("송신시간순 다음전문");
    	GridDataFactory.fillDefaults().grab(false, false).align(SWT.END, SWT.BOTTOM).applyTo(bt_next);
    	bt_next.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			getNext();
    			super.widgetSelected(e);
    		}
		});
    	bt_next = new Button(compTitle, SWT.PUSH);
    	bt_next.setText("이전전문");
    	bt_next.setToolTipText("송신시간순 이전전문");
    	GridDataFactory.fillDefaults().grab(false, false).align(SWT.END, SWT.BOTTOM).applyTo(bt_next);
    	bt_next.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			getPrev();
    			super.widgetSelected(e);
    		}
		});

    	Button bt_cmp = new Button(compTitle, SWT.PUSH);
    	bt_cmp.setText("원본비교");
    	GridDataFactory.fillDefaults().grab(false, false).align(SWT.END, SWT.BOTTOM).applyTo(bt_cmp);
    	bt_cmp.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			viewCompare();
    			super.widgetSelected(e);
    		}
		});

    	bt_cmp = new Button(compTitle, SWT.PUSH);
    	bt_cmp.setText("새로고침");
    	GridDataFactory.fillDefaults().grab(false, false).align(SWT.END, SWT.BOTTOM).applyTo(bt_cmp);
    	bt_cmp.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			EntityManager em = AqtMain.emf.createEntityManager();
    			tpacket = em.find(tpacket.getClass(), tpacket.getPkey());
    			fillScreen();
    			super.widgetSelected(e);
    			em.close();
    		}
		});

		Composite compDetail = new Composite(compHeader, SWT.BORDER);
		compDetail.setLayoutData(new GridData(SWT.FILL , SWT.TOP, true, false));
		compDetail.setLayout(new GridLayout(8, false));
		compDetail.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));


		Label lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("테스트ID");
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.TOP,false, false) );

		txtTestCode = new Text(compDetail, SWT.BORDER);
		txtTestCode.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtTestCode.setEditable(false);
		txtTestCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtTestCode.setFont( IAqtVar.font1);
		
		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("ID");
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.TOP,false, false) );
		
		txtPkey = new Text(compDetail, SWT.BORDER);
		txtPkey.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtPkey.setEditable(false);
		txtPkey.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtPkey.setFont( IAqtVar.font1);


		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("패킷ID");
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.TOP,false, false) );
		
		txtCmpid = new Text(compDetail, SWT.BORDER);
		txtCmpid.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtCmpid.setEditable(false);
		txtCmpid.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtCmpid.setFont( IAqtVar.font1);

		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("작업일시");
//		lblcomm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.TOP,false, false) );

		txtCdate = new Text(compDetail, SWT.BORDER);
		txtCdate.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtCdate.setEditable(false);
		txtCdate.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtCdate.setFont( IAqtVar.font1);


		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("송신시간");
//		lblcomm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

		txtStime = new Text(compDetail, SWT.BORDER);
		txtStime.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtStime.setEditable(false);
		txtStime.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtStime.setFont( IAqtVar.font1);
		
		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("수신시간");
//		lblcomm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

		txtRtime = new Text(compDetail, SWT.BORDER);
		txtRtime.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtRtime.setEditable(false);
		txtRtime.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtRtime.setFont( IAqtVar.font1);
		
		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("총소요시간");
//		lblcomm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

		txtElapsed = new Text(compDetail, SWT.BORDER);
		txtElapsed.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtElapsed.setEditable(false);
		txtElapsed.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtElapsed.setFont( IAqtVar.font1);
		
		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("서비스소요시간");
//		lblcomm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

		txtSvcTime = new Text(compDetail, SWT.BORDER);
		txtSvcTime.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtSvcTime.setEditable(false);
		txtSvcTime.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtSvcTime.setFont( IAqtVar.font1);

		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("송신시간(원)");
//		lblcomm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

		txtOStime = new Text(compDetail, SWT.BORDER);
		txtOStime.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtOStime.setEditable(false);
		txtOStime.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtOStime.setFont( IAqtVar.font1);
		txtOStime.setTextLimit(26) ;
		txtStime.setTextLimit(26) ;
		txtRtime.setTextLimit(26) ;

		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("송수신Host");
//		lblcomm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.TOP,false, false) );

		txtRcode = new Text(compDetail, SWT.BORDER);
		txtRcode.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false, false)  );
		txtRcode.setEditable(false);
		txtRcode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtRcode.setFont( IAqtVar.font1);

		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("서비스(URI)");
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

		txtUri = new Text(compDetail, SWT.BORDER);
		txtUri.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false,3,1));
		txtUri.setEditable(false);
		txtUri.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtUri.setFont( IAqtVar.font1);
		
		if (AqtMain.tconfig.getCol1() != null ) {
			lblcomm = new Label(compDetail, SWT.NONE);
			lblcomm.setText(AqtMain.tconfig.getCol1());
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

			txtCol1 = new Text(compDetail, SWT.BORDER);
			txtCol1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			txtCol1.setEditable(false);
			txtCol1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtCol1.setFont( IAqtVar.font1);
		}
		if (AqtMain.tconfig.getCol2() != null ) {
			lblcomm = new Label(compDetail, SWT.NONE);
			lblcomm.setText(AqtMain.tconfig.getCol2());
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

			txtCol2 = new Text(compDetail, SWT.BORDER);
			txtCol2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			txtCol2.setEditable(false);
			txtCol2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtCol2.setFont( IAqtVar.font1);
		}
//		Composite compMessage = new Composite(compHeader, SWT.NONE);
		
		SashForm sash1 = new SashForm(compHeader, SWT.VERTICAL) ;
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(sash1);
		Composite compM1 = new Composite(sash1, SWT.NONE);
		Composite compM2 = new Composite(sash1, SWT.NONE);
		Composite compM3 = new Composite(sash1, SWT.NONE);
		sash1.setSashWidth(5);
		if (! tpacket.getProto().equals("0")) {
			GridLayoutFactory.fillDefaults().equalWidth(false).applyTo(compM2);
			sash1.setWeights(new int[] {3,3,4});
		} else {
			compM2.dispose();
			sash1.setWeights(new int[] {4,6});
		}

//		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(compM1);
		GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(compM1);
		
//		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(compM3);
		GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(compM3);
//		compMessage.setLayoutData(new GridData(SWT.FILL , SWT.FILL, true, true));
//		compMessage.setLayout(new GridLayout(3, false));
		/*
		Label lblm = new Label(compM1, SWT.NONE);
		lblm.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, SWT.CENTER, true, false));
		lblm.setText("송신Data");
		lblm.setFont( IAqtVar.font1) ;
		*/
		Button button = new Button(compM1, SWT.PUSH);
	    button.setText("송신Data");
	    button.setFont( IAqtVar.font1) ;
	    button.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	        PopupList list = new PopupList(shell, 5);

	        list.setItems(new String[]{"UTF-8","UTF-16","MS949","ISO-8859-1"});
	        list.select(sv_select );
	        Point pt = shell.getDisplay().getCursorLocation() ;

	        String selected = list.open(new Rectangle(pt.x, pt.y - 40, 80, 30));
	        if (selected == null) return ;
	        sv_select = selected ;
	        txtSendMsg.setText(tpacket.getSdataENCODE(selected));
	      }
	    });
	    GridDataFactory.fillDefaults().grab(true, false).align(SWT.LEFT,SWT.CENTER).applyTo(button);

		
	    Label lblm = new Label(compM1, SWT.NONE);
		lblm.setText("송신Data길이");
//		lblm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblm.setFont( IAqtVar.font1);
		
		txtSlen = new Text(compM1, SWT.BORDER | SWT.RIGHT);
		txtSlen.setEditable(false);
		txtSlen.setFont( IAqtVar.font1);
		txtSlen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		
		txtSendMsg = new StyledText(compM1, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).hint(-1, 180).span(3, 1).applyTo(txtSendMsg);
		txtSendMsg.setFont( IAqtVar.font1);
		txtSendMsg.setEditable(false);
		txtSendMsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		if (AuthType.TESTADM == AqtMain.authtype)
			txtSendMsg.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.stateMask == SWT.CTRL && (e.keyCode == 'e' || e.keyCode == 'E')) {
						txtSendMsg.setEditable( ! txtSendMsg.getEditable() );
					}
					if (e.stateMask == SWT.CTRL && (e.keyCode == 's' || e.keyCode == 'S')) {
					    EntityManager em = AqtMain.emf.createEntityManager();
					    try{
	//				    	System.out.println("update sdata "+ tpacket.getPkey());
					        em.getTransaction().begin();
					        tpacket = em.find(Ttcppacket.class, tpacket.getPkey());
					        tpacket.setSdata(txtSendMsg.getText());
					        em.getTransaction().commit();
					        MessageDialog.openInformation(shell , "Info", "저장되었습니다.");
					    }finally{
					        em.close();
					    }					
					}
					if (e.keyCode == SWT.F5 ) {
					    EntityManager em = AqtMain.emf.createEntityManager();
					    try{
					        tpacket = em.find(Ttcppacket.class, tpacket.getPkey());
					        fillScreen() ;
					    }finally{
					        em.close();
					    }					
					}
				}
			});

		if (! tpacket.getProto().equals("0")) {
//			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(compM2);

			lblcomm = new Label(compM2, SWT.NONE);
			lblcomm.setText("수신Header");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,false, false) );
			txtRhead = new StyledText(compM2, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(txtRhead);
			txtRhead.setEditable(false);
			txtRhead.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtRhead.setFont( IAqtVar.font1);
		}

		
//		Label lblReceiveMsg = new Label(compMessage, SWT.NONE);
//		lblReceiveMsg.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, SWT.CENTER, true, false));
//		lblReceiveMsg.setText("수신Data");
//		lblReceiveMsg.setFont( IAqtVar.font1) ;

		 button = new Button(compM3, SWT.PUSH);
	    button.setText("수신Data");
	    button.setFont( IAqtVar.font1) ;
	    button.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	        PopupList list = new PopupList(shell, 5);

	        list.setItems(new String[]{"UTF-8","UTF-16","MS949","ISO-8859-1"});
	        list.select(sv_select );
	        Point pt = shell.getDisplay().getCursorLocation() ;

	        String selected = list.open(new Rectangle(pt.x, pt.y - 40, 80, 30));
	        if (selected == null) return ;
	        sv_select = selected ;
	        txtReceiveMsg.setText(tpacket.getRdataENCODE(selected));
	      }
	    });
	    GridDataFactory.fillDefaults().grab(true, false).align(SWT.LEFT,SWT.CENTER).applyTo(button);
	    
		Label lblRlen = new Label(compM3, SWT.NONE);
		lblRlen.setText("수신Data길이");
//		lblRlen.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblRlen.setFont( IAqtVar.font1);
		
		txtRlen = new Text(compM3, SWT.BORDER | SWT.RIGHT);
		txtRlen.setEditable(false);
		txtRlen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtRlen.setFont( IAqtVar.font1);
		
		txtReceiveMsg = new StyledText(compM3, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
//		GridData gd_txtReceiveMsg = new GridData(GridData.FILL_BOTH);
//		gd_txtReceiveMsg.horizontalSpan = 3;
//		gd_txtReceiveMsg.verticalSpan = 40;
//		txtReceiveMsg.setLayoutData(gd_txtReceiveMsg);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL,SWT.FILL).span(3, 1).hint(-1, 220).applyTo(txtReceiveMsg);
		txtReceiveMsg.setFont( IAqtVar.font1);
		txtReceiveMsg.setEditable(false);
		txtReceiveMsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

//		sashForm.pack();
		compTitle.pack();
		new Label(compTitle, SWT.NONE);
		new Label(compTitle, SWT.NONE);
//		sash1.setWeights(new int[] {4,6});
		sash1.requestLayout();
		compDetail.pack();
		compHeader.requestLayout();
	}

	private void fillScreen() {
		if (tpacket == null)
			return;
		SimpleDateFormat dformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S") ;
		txtPkey.setText(tpacket.getPkey()+"");
		txtSlen.setText(String.format("%,d",tpacket.getSlen()));
		txtRlen.setText(String.format("%,d", tpacket.getRlen()));
		if (AqtMain.tconfig.getEncval() != null )
			sv_select = AqtMain.tconfig.getEncval() ;
		else if (! tpacket.getTmaster().getLvl().equals("0") && tpacket.getRhead().contains("EUC-KR") )  
			sv_select = "MS949";
		else
			sv_select = "UTF-8";
		
		txtSendMsg.setText(tpacket.getSdataENCODE(sv_select));
		txtReceiveMsg.setText(tpacket.getRdataENCODE(sv_select));
		txtCmpid.setText(tpacket.getCmpid()+"");
		txtTestCode.setText(tpacket.getTcode()); 
		txtUri.setText(tpacket.getUri() + " [" + tpacket.getTservice().getSvckor() + "]" );
//		txtScrno.setText(tpacket.getScrno()); 
		txtOStime.setText(tpacket.getOStime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.n")));
		txtStime.setText(tpacket.getStime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.n")));
		txtRtime.setText(tpacket.getRtime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.n")));
		txtElapsed.setText(String.format("%.3f",tpacket.getElapsed()));
		txtSvcTime.setText(String.format("%.3f",tpacket.getSvctime()));
		txtRcode.setText( tpacket.getSrcip()+" ->"+tpacket.getDstip()+":"+tpacket.getDstport() );
		if (txtRhead != null) {
			txtRhead.setText(tpacket.getRhead());
		}
		txtCdate.setText(dformat.format(tpacket.getCdate()));
		txtRlen.requestLayout();
		txtSlen.requestLayout();
		txtSlen.setRedraw(true);
		if (txtCol1 != null) txtCol1.setText( tpacket.getCol1() ) ;
		if (txtCol2 != null) txtCol2.setText( tpacket.getCol2() ) ;
	}
	
	public void setTrxList(Ttcppacket tpacket) {
		this.tpacket = tpacket;
	}

	public void setTrx(long ipkey, Table tbl) {
		this.tbl = tbl ;
		EntityManager em = AqtMain.emf.createEntityManager();
		this.tpacket = em.find(Ttcppacket.class, ipkey);
		em.close();
	}
	
	private void getPrev() {
		EntityManager em = AqtMain.emf.createEntityManager();
		try {
			int ix = tbl.getSelectionIndex() - 1 ;
			tbl.setSelection( ix );
			Ttcppacket t = (Ttcppacket)(tbl.getItem(ix ).getData()) ;
//			Ttcppacket t = (Ttcppacket) em.createNativeQuery("select t.* from Ttcppacket t where t.tcode = ? and t.o_stime < ? and pkey != ?	order by t.o_stime desc limit 1",Ttcppacket.class  )
//					.setParameter(1, tpacket.getTcode()).setParameter(2, tpacket.getOStime()).setParameter(3, tpacket.getPkey())
//					.getSingleResult() ;
			this.tpacket = t;
			fillScreen();
		} catch (Exception e) {
			MessageDialog.openInformation(this.getParent(), "알림", "이전전문이 없습니다.") ;
		}finally{
	        em.close();
	    }				
	}
	private void getNext() {
		EntityManager em = AqtMain.emf.createEntityManager();
		
		try {
			int ix = tbl.getSelectionIndex() + 1 ;
			tbl.setSelection( ix );
			Ttcppacket t = (Ttcppacket)(tbl.getItem(ix ).getData()) ;
//			Ttcppacket t = (Ttcppacket) em.createNativeQuery("select t.* from Ttcppacket t where t.tcode = ? and t.o_stime > ? and pkey != ?	order by t.o_stime limit 1",Ttcppacket.class  )
//					.setParameter(1, tpacket.getTcode()).setParameter(2, tpacket.getOStime()).setParameter(3, tpacket.getPkey())
//					.getSingleResult() ;
			this.tpacket = t;
			fillScreen();
		} catch (Exception e) {
			MessageDialog.openInformation(this.getParent(), "알림", "다음전문이 없습니다.") ;
		}finally{
	        em.close();
	    }				
	}
	private void viewCompare() {
		EntityManager em = AqtMain.emf.createEntityManager();
		
		try {
			TpacketDTO t2 = new TpacketDTO( tpacket.getTloaddata() );
//			Tloaddata tl = em.find(Tloaddata.class, tpacket.getCmpid() ) ;
//			if (tl != null) {
//				t2 = new TpacketDTO( tl ) ;
//			} else {
//				t2 = new TpacketDTO( em.createQuery("select t from Ttcppacket t  where t.p = :cmpid and t.oStime = :ostime and t.tmaster.lvl = '0'", Ttcppacket.class  )
//						.setParameter("ostime", tpacket.getOStime() ).setParameter("cmpid", tpacket.getCmpid() )
//						.getResultList().get(0) ) ;
//			}
		
			AqtDetailComp2 aqtDetail = new AqtDetailComp2(this.getParent(),
					SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.CLOSE |SWT.CENTER,
					new TpacketDTO(tpacket)  , t2 ) ;

			aqtDetail.open();
		} catch (Exception e) {
//			System.out.println(e.getMessage() + tpacket.getTmaster().getCmpCode() + " " + tpacket.getCmpid() );
			MessageDialog.openInformation(this.getParent(), "알림", "원본을 찾을 수 없습니다.\n" + e.getMessage()) ;
		}finally{
	        em.close();
	    }				

	}

}
