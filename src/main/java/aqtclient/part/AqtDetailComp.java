package aqtclient.part;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PopupList;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Ttcppacket;


/* 실제 데이터로 확인이 필요함 */

public class AqtDetailComp extends Dialog {
	protected Shell shell;
	protected Object result;
	private Ttcppacket tr1, tr2;  // testcode1 의 tr
	private TranInfo tran1, tran2 ;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
//	public AqtDetail(Composite parent, int style) {
//		create(parent, style);
//	}
	public AqtDetailComp(Shell parent, int style, long pkey1, long pkey2) {

		super(parent, style);

		EntityManager em = AqtMain.emf.createEntityManager();
		this.tr1 = em.find(Ttcppacket.class, pkey1);
		this.tr2 = em.find(Ttcppacket.class, pkey2);
		em.close();

	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();

		shell.setText(IAqtVar.titnm);
//		shell.setLocation(10, 10);
		shell.open();
		
		Display display = getParent().getDisplay();
		
//		fillScreen();
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
	 * @throws UnsupportedEncodingException 
	 */
	private void createContents()  {
		
		shell = new Shell(getParent(), getStyle() | SWT.RESIZE | SWT.MAX);
//		shell.setSize(1800, 1000);
//		shell.setBounds(10, 10, 1800, 1000);
		shell.setMaximized(true);
		shell.setBackground(SWTResourceManager.getColor(225,230,246));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setLayout( new FillLayout(SWT.VERTICAL) );

		Composite compTitle = new Composite(shell, SWT.NONE);
		
		GridData gd_compTitle = new GridData(SWT.FILL , SWT.TOP, true, false);
//		gd_compTitle.horizontalSpan = 1;
		compTitle.setLayoutData(gd_compTitle);
		compTitle.setLayout(new GridLayout(2, false));

		Label ltitle = new Label(compTitle, SWT.NONE);
    	ltitle.setText(" 전문상세비교" ) ;
    	ltitle.setFont( IAqtVar.title_font );
    	ltitle.setLayoutData(new GridData(SWT.FILL , SWT.TOP, true, false));

	    SashForm sashForm = new SashForm(compTitle, SWT.HORIZONTAL );	
	    sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	    
//	    Composite comp1 = new Composite(sashForm, SWT.NONE) ;
		tran1 = new TranInfo(sashForm, SWT.BORDER, tr1);
		
//	    Composite comp2 = new Composite(sashForm, SWT.NONE) ;
		
		tran2 = new TranInfo(sashForm, SWT.BORDER, tr2);
//		tran1.pack();
//		tran2.pack();
		
		sashForm.setWeights(new int[] { 5,5 });
		sashForm.setSashWidth(0);
		sashForm.pack();
		try {
//			compTrMsg( tran1.getTxtSendMsg() , tran2.getTxtSendMsg() ) ;
			compTrMsg( tran1.getTxtReceiveMsg() , tran2.getTxtReceiveMsg() ) ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}

	private void compTrMsg(StyledText stext1, StyledText stext2 ) throws UnsupportedEncodingException {
		String text1 = stext1.getText() ;
		String text2 = stext2.getText() ;
//		byte[] text1 = stext1.getText().getBytes("euc-kr") ;
//		byte[] text2 = stext2.getText().getBytes("euc-kr") ;

		List<StyleRange> ranges = new ArrayList<StyleRange>();
		int st = -1, n = text2.length() , n1 = text1.length() , hcnt = 0;
		boolean sw = false ;
		for (int i = 0 ; i < n; i++) {
			String x2 = text2.substring(i,i+1); //.charAt(i);
			String x1 = i < n1 ? text1.substring(i,i+1) :  "" ;
			if (x1.equals(x2) ) {
				if (sw) {
					ranges.add( new StyleRange(st+1, i - st - 1, null, SWTResourceManager.getColor(SWT.COLOR_YELLOW), SWT.BOLD));
					sw = false ;
					hcnt = 0 ;
//					System.out.format("%d : %d\n" ,st+1 , st - 1 );
				}
				st = i ;
			} else {
				sw = true ;
			}
//			if (Character.getType(x1.charAt(0)) == 5 ) hcnt++ ;
		}
		try {
			if (!ranges.isEmpty()) {
				stext1.setStyleRanges( (StyleRange[]) ranges.toArray(new StyleRange[0] ) );
				stext2.setStyleRanges( (StyleRange[]) ranges.toArray(new StyleRange[0] ) );
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (sw) {
			n1 = stext1.getText().length() ;
			if (st+1 < n1)
				stext1.setStyleRange(new StyleRange(st+1, n1 - st - 1, null, SWTResourceManager.getColor(SWT.COLOR_YELLOW), SWT.BOLD));
			n = stext2.getText().length() ;
			if (st+1 < n )
				stext2.setStyleRange(new StyleRange(st+1, n - st - 1, null, SWTResourceManager.getColor(SWT.COLOR_YELLOW), SWT.BOLD));
		}
		
	}
/*
	private void compTrMsg(StyledText stext1, StyledText stext2 ) throws UnsupportedEncodingException {
//		String text1 = stext1.getText() ;
//		String text2 = stext2.getText() ;
		byte[] text1 = stext1.getText().getBytes("euc-kr") ;
		byte[] text2 = stext2.getText().getBytes("euc-kr") ;

		List<StyleRange> ranges = new ArrayList<StyleRange>();
		int st = -1, n = text2.length , n1 = text1.length ;
		boolean sw = false ;
		for (int i = 0 ; i < n; i++) {
			char x2 = (char)text2[i]; //.charAt(i);
			char x1 = i < n1 ? (char)text1[i] :  0 ;
			if (x1 == x2) {
				if (sw) {
					ranges.add( new StyleRange(st+1, i - st - 1, null, SWTResourceManager.getColor(SWT.COLOR_YELLOW), SWT.BOLD));
					sw = false ;
				}
				st = i ;
			} else {
				sw = true ;
			}
		}
		try {
			if (!ranges.isEmpty()) {
				stext1.setStyleRanges( (StyleRange[]) ranges.toArray(new StyleRange[0] ) );
				stext2.setStyleRanges( (StyleRange[]) ranges.toArray(new StyleRange[0] ) );
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (sw) {
			n1 = stext1.getText().length() ;
			if (st+1 < n1)
				stext1.setStyleRange(new StyleRange(st+1, n1 - st - 1, null, SWTResourceManager.getColor(SWT.COLOR_YELLOW), SWT.BOLD));
			n = stext2.getText().length() ;
			if (st+1 < n )
				stext2.setStyleRange(new StyleRange(st+1, n - st - 1, null, SWTResourceManager.getColor(SWT.COLOR_YELLOW), SWT.BOLD));
		}
		
	}
*/	

	private class  TranInfo extends Composite {
		private Ttcppacket tr;  // testcode1 의 tr
		private Text txtSlen;
		private StyledText txtSendMsg;
		private Text txtRlen;
		private StyledText txtReceiveMsg;
		private Text txtPkey;
		private Text txtCmpid;
		private Text txtTestCode;
		private Text txtUri;
		private StyledText txtRhead;
		private Text txtStime;
		private Text txtRtime;
		private Text txtElapsed;
		private Text txtSvcTime;
		private Text txtRcode;
		private Text txtCdate;
		Composite compMessage, compDetail ;
		protected String sv_select = "UTF-8";
		
		public StyledText getTxtReceiveMsg() {
			return this.txtReceiveMsg ;
		}

		public StyledText getTxtSendMsg() {
			return this.txtSendMsg ;
		}

		private void setValue() {
			if (this.tr == null)
				return;
			SimpleDateFormat dformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S") ;
			txtSlen.setText(String.format("%,9d",tr.getSlen()));
			txtSendMsg.setText(tr.getSdata());
			txtRlen.setText(String.format("%,9d",tr.getRlen()));
			if (AqtMain.tconfig.getEncval() != null )
				sv_select = AqtMain.tconfig.getEncval() ;
			else if (! tr.getTmaster().getLvl().equals("0") && tr.getRhead().contains("EUC-KR") )  
				sv_select = "MS949";
			else
				sv_select = "UTF-8";

			txtReceiveMsg.setText(tr.getRdataENCODE(sv_select) );
			txtPkey.setText(tr.getPkey()+"");
			txtCmpid.setText(tr.getCmpid()+"");
			txtTestCode.setText(tr.getTcode());
			txtUri.setText(tr.getUri());
			txtStime.setText(tr.getStime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.n")).substring(0, 26));
			txtRtime.setText(tr.getRtime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.n")).substring(0, 26));
			txtElapsed.setText(String.format("%.3f",tr.getElapsed()));
			txtSvcTime.setText(String.format("%.3f",tr.getSvctime()));
			txtRcode.setText( tr.getSrcip()+" ->"+tr.getDstip()+":"+tr.getDstport()  );
			if (! tr.getProto().equals("0")) txtRhead.setText(tr.getRhead());
			txtCdate.setText(dformat.format(tr.getCdate()));
			compDetail.pack();
			compMessage.pack();

		}
		public TranInfo(Composite parent, int style) {
			super(parent, style) ;
		}
		public TranInfo(Composite parent, int style, Ttcppacket tr) {
			this(parent, style);
			this.tr = tr ;
			this.setLayout(new GridLayout(1,false));
			compDetail = new Composite(this, SWT.NONE);
			compDetail.setLayout(new GridLayout(4, false));
			compDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			Label lblcomm = new Label(compDetail, SWT.NONE);
			lblcomm.setText("테스트코드");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,true, false) );

			txtTestCode = new Text(compDetail, SWT.BORDER);
			txtTestCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			txtTestCode.setEditable(false);
			txtTestCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtTestCode.setFont( IAqtVar.font1);
			
			lblcomm = new Label(compDetail, SWT.NONE);
			lblcomm.setText("ID");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,true, false) );
			
			txtPkey = new Text(compDetail, SWT.BORDER);
			txtPkey.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			txtPkey.setEditable(false);
			txtPkey.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtPkey.setFont( IAqtVar.font1);
			
			lblcomm = new Label(compDetail, SWT.NONE);
			lblcomm.setText("패킷ID");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

			txtCmpid = new Text(compDetail, SWT.BORDER);
			txtCmpid.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			txtCmpid.setEditable(false);
			txtCmpid.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtCmpid.setFont( IAqtVar.font1);
			
			lblcomm = new Label(compDetail, SWT.NONE);
			lblcomm.setText("작업일시");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

			txtCdate = new Text(compDetail, SWT.BORDER);
			txtCdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			txtCdate.setEditable(false);
			txtCdate.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtCdate.setFont( IAqtVar.font1);

			lblcomm = new Label(compDetail, SWT.NONE);
			lblcomm.setText("송신시간");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

			txtStime = new Text(compDetail, SWT.BORDER);
			txtStime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			txtStime.setEditable(false);
			txtStime.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtStime.setFont( IAqtVar.font1);
			
			lblcomm = new Label(compDetail, SWT.NONE);
			lblcomm.setText("수신시간");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

			txtRtime = new Text(compDetail, SWT.BORDER);
			txtRtime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			txtRtime.setEditable(false);
			txtRtime.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtRtime.setFont( IAqtVar.font1);
			
			lblcomm = new Label(compDetail, SWT.NONE);
			lblcomm.setText("총소요시간");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

			txtElapsed = new Text(compDetail, SWT.BORDER);
			txtElapsed.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			txtElapsed.setEditable(false);
			txtElapsed.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtElapsed.setFont( IAqtVar.font1);
			
			lblcomm = new Label(compDetail, SWT.NONE);
			lblcomm.setText("서비스소요시간");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

			txtSvcTime = new Text(compDetail, SWT.BORDER);
			txtSvcTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			txtSvcTime.setEditable(false);
			txtSvcTime.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtSvcTime.setFont( IAqtVar.font1);
			
			lblcomm = new Label(compDetail, SWT.NONE);
			lblcomm.setText("송수신Host");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

			txtRcode = new Text(compDetail, SWT.BORDER);
			txtRcode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			txtRcode.setEditable(false);
			txtRcode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtRcode.setFont( IAqtVar.font1);

			lblcomm = new Label(compDetail, SWT.NONE);
			lblcomm.setText("서비스(URI)");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

			txtUri = new Text(compDetail, SWT.BORDER);
			txtUri.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			txtUri.setEditable(false);
			txtUri.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtUri.setFont( IAqtVar.font1);

			compMessage = new Composite(this, SWT.NONE);
			compMessage.setLayoutData(new GridData(SWT.FILL , SWT.FILL, true, true));

			GridLayout glm = new GridLayout(3, false) ;
			glm.marginTop = 10 ;
			
			compMessage.setLayout(glm);
			
			Label lblm = new Label(compMessage, SWT.NONE);
			lblm.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
			lblm.setText("송신Data");
			lblm.setFont( IAqtVar.font1) ;
//			lblm.pack();	
			
			lblm = new Label(compMessage, SWT.NONE);
			lblm.setText("송신Data길이");
			lblm.setFont( IAqtVar.font1);
			
			txtSlen = new Text(compMessage, SWT.BORDER | SWT.RIGHT);
			txtSlen.setEditable(false);
			txtSlen.setFont( IAqtVar.font1);
			txtSlen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtSlen.setSize(120, -1);
			
			txtSendMsg = new StyledText(compMessage, SWT.BORDER | SWT.MULTI | SWT.WRAP| SWT.V_SCROLL);
//			GridData gd_txtSendMsg = new GridData(GridData.FILL_BOTH );
//			gd_txtSendMsg.horizontalSpan = 3;
//			gd_txtSendMsg.verticalSpan = 40;
//			txtSendMsg.setLayoutData(gd_txtSendMsg);

			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).span(3, 1).hint(-1, 180).applyTo(txtSendMsg);

			txtSendMsg.setFont( IAqtVar.font1);
			txtSendMsg.setEditable(false);
			txtSendMsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtSendMsg.setText(" ");

			if (! tr.getProto().equals("0")) {
				Label lblReceiveMsg = new Label(compMessage, SWT.NONE);
				lblReceiveMsg.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
				lblReceiveMsg.setText("수신Header");
				lblReceiveMsg.setFont( IAqtVar.font1) ;
				txtRhead = new StyledText(compMessage, SWT.BORDER | SWT.MULTI | SWT.WRAP| SWT.V_SCROLL);
				GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).span(3, 1).hint(-1, 180).applyTo(txtRhead);
				txtRhead.setFont( IAqtVar.font1);
				txtRhead.setEditable(false);
				txtRhead.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				txtRhead.setText(" ");
			}
//			lblReceiveMsg = new Label(compMessage, SWT.NONE);
//			lblReceiveMsg.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
//			lblReceiveMsg.setText("수신Data");
//			lblReceiveMsg.setFont( IAqtVar.font1) ;
			
			Button button = new Button(compMessage, SWT.PUSH);
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
		        sv_select  = selected ;
		        txtReceiveMsg.setText(tr.getRdataENCODE(selected));
				try {
//					compTrMsg( tran1.getTxtSendMsg() , tran2.getTxtSendMsg() ) ;
					compTrMsg( tran1.getTxtReceiveMsg() , tran2.getTxtReceiveMsg() ) ;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

		      }
		    });
		    GridDataFactory.fillDefaults().grab(true, false).align(SWT.LEFT,SWT.CENTER).applyTo(button);

			
			Label lblRlen = new Label(compMessage, SWT.NONE);
			lblRlen.setText("수신Data길이");
			lblRlen.setFont( IAqtVar.font1);
			
			txtRlen = new Text(compMessage, SWT.BORDER | SWT.RIGHT);
			txtRlen.setEditable(false);
			txtRlen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtRlen.setFont( IAqtVar.font1);
			txtRlen.setSize(120, -1);

			txtReceiveMsg = new StyledText(compMessage, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
//			GridData gd_txtReceiveMsg = new GridData(GridData.FILL_BOTH );
//			gd_txtReceiveMsg.horizontalSpan = 3;
//			gd_txtReceiveMsg.verticalSpan = 40;
//			txtReceiveMsg.setLayoutData(gd_txtReceiveMsg);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).span(3, 1).hint(-1, 180).applyTo(txtReceiveMsg);

			txtReceiveMsg.setFont( IAqtVar.font1);
			txtReceiveMsg.setEditable(false);
			txtReceiveMsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			this.setValue();
		}
	}
}

