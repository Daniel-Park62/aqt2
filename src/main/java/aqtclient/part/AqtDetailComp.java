package aqtclient.part;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Ttransaction;


/* 실제 데이터로 확인이 필요함 */

public class AqtDetailComp extends Dialog {
	protected Shell shell;
	protected Object result;
	private Ttransaction tr1, tr2;  // testcode1 의 tr
	private TranInfo tran1, tran2 ;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
//	public AqtDetail(Composite parent, int style) {
//		create(parent, style);
//	}
	public AqtDetailComp(Shell parent, int style, int pkey1, int pkey2) {
		super(parent, style);

		EntityManager em = AqtMain.emf.createEntityManager();
		this.tr1 = em.find(Ttransaction.class, pkey1);
		this.tr2 = em.find(Ttransaction.class, pkey2);
		em.close();

	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();

        shell.setBounds(10, 10, 1800,950);
		shell.setText(IAqtVar.titnm);

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
		
		sashForm.setWeights(new int[] { 5,5 });
		sashForm.setSashWidth(0);
		try {
			compTrMsg( tran1.getTxtSendMsg() , tran2.getTxtSendMsg() ) ;
			compTrMsg( tran1.getTxtReceiveMsg() , tran2.getTxtReceiveMsg() ) ;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void compTrMsg(StyledText stext1, StyledText stext2 ) throws UnsupportedEncodingException {
//		String text1 = stext1.getText() ;
//		String text2 = stext2.getText() ;
		byte[] text1 = stext1.getText().getBytes("EUC-KR") ;
		byte[] text2 = stext2.getText().getBytes("EUC-KR") ;

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
	
	private class  TranInfo extends Composite {
		private Ttransaction tr;  // testcode1 의 tr
		private Text txtSlen;
		private StyledText txtSendMsg;
		private Text txtRlen;
		private StyledText txtReceiveMsg;
		private Text txtUuid;
		private Text txtTestCode;
		private Text txtSvcId;
		private Text txtScrno;
		private Text txtStime;
		private Text txtRtime;
		private Text txtElapsed;
		private Text txtSvcTime;
		private Text txtRcvMsg;
		private Text txtMsgcd;
		private Text txtCdate;
		Composite compMessage, compDetail ;
		
		public StyledText getTxtReceiveMsg() {
			return this.txtReceiveMsg ;
		}

		public StyledText getTxtSendMsg() {
			return this.txtSendMsg ;
		}

		private void setValue() {
			if (tr == null)
				return;
			SimpleDateFormat dformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S") ;
			txtSlen.setText(String.format("%,9d",tr.getSlen()));
			txtSendMsg.setText(tr.getSdata());
			txtRlen.setText(String.format("%,9d",tr.getRlen()));

			txtReceiveMsg.setText(tr.getRdata());
			txtUuid.setText(tr.getUuid());
			txtTestCode.setText(tr.getTcode());
			txtSvcId.setText(tr.getSvcid());
			txtScrno.setText(tr.getScrno());
			txtStime.setText(dformat.format(tr.getStime()));
			txtRtime.setText(dformat.format(tr.getRtime()));
			txtElapsed.setText(String.format("%.3f",tr.getElapsed()));
			txtSvcTime.setText(String.format("%.3f",tr.getSvctime()));
			txtMsgcd.setText( tr.getMsgcd() == null   ? "" : tr.getMsgcd() );
			txtRcvMsg.setText(tr.getRcvmsg());
			txtCdate.setText(dformat.format(tr.getCdate()));
			compDetail.pack();
//			compMessage.pack();

		}
		public TranInfo(Composite parent, int style) {
			super(parent, style) ;
		}
		public TranInfo(Composite parent, int style, Ttransaction tr) {
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
			lblcomm.setText("UUID");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,true, false) );
			
			txtUuid = new Text(compDetail, SWT.BORDER);
			txtUuid.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			txtUuid.setEditable(false);
			txtUuid.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtUuid.setFont( IAqtVar.font1);
			
			lblcomm = new Label(compDetail, SWT.NONE);
			lblcomm.setText("서비스ID");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

			txtSvcId = new Text(compDetail, SWT.BORDER);
			txtSvcId.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			txtSvcId.setEditable(false);
			txtSvcId.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtSvcId.setFont( IAqtVar.font1);
			
			lblcomm = new Label(compDetail, SWT.NONE);
			lblcomm.setText("화면ID");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

			txtScrno = new Text(compDetail, SWT.BORDER);
			txtScrno.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			txtScrno.setEditable(false);
			txtScrno.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtScrno.setFont( IAqtVar.font1);
			
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
			lblcomm.setText("수신코드");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

			txtMsgcd = new Text(compDetail, SWT.BORDER);
			txtMsgcd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			txtMsgcd.setEditable(false);
			txtMsgcd.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtMsgcd.setFont( IAqtVar.font1);

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
			lblcomm.setText("수신메세지");
			lblcomm.setFont( IAqtVar.font1);
			lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

			txtRcvMsg = new Text(compDetail, SWT.BORDER);
			txtRcvMsg.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,true,false,3,1) );
			txtRcvMsg.setEditable(false);
			txtRcvMsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtRcvMsg.setFont( IAqtVar.font1);

			compMessage = new Composite(this, SWT.NONE);
			compMessage.setLayoutData(new GridData(SWT.FILL , SWT.FILL, true, true));

			GridLayout glm = new GridLayout(3, false) ;
			glm.marginTop = 10 ;
			
			compMessage.setLayout(glm);
			
			Label lblm = new Label(compMessage, SWT.NONE);
			lblm.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, SWT.CENTER, true, false));
			lblm.setText("송신전문");
			lblm.setFont( IAqtVar.font1) ;
			lblm.pack();
			
			lblm = new Label(compMessage, SWT.NONE);
			lblm.setText("송신Data길이");
			lblm.setFont( IAqtVar.font1);
			
			txtSlen = new Text(compMessage, SWT.BORDER | SWT.RIGHT);
			txtSlen.setEditable(false);
			txtSlen.setFont( IAqtVar.font1);
			txtSlen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			
			txtSendMsg = new StyledText(compMessage, SWT.BORDER | SWT.MULTI | SWT.WRAP| SWT.V_SCROLL);
			GridData gd_txtSendMsg = new GridData(GridData.FILL_BOTH );
			gd_txtSendMsg.horizontalSpan = 3;
			gd_txtSendMsg.verticalSpan = 40;

			txtSendMsg.setLayoutData(gd_txtSendMsg);
			txtSendMsg.setFont( IAqtVar.font1);
			txtSendMsg.setEditable(false);
			txtSendMsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtSendMsg.setText(" ");
			
			Label lblReceiveMsg = new Label(compMessage, SWT.NONE);
			lblReceiveMsg.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			lblReceiveMsg.setText("수신전문");
			lblReceiveMsg.setFont( IAqtVar.font1) ;
			
			Label lblRlen = new Label(compMessage, SWT.NONE);
			lblRlen.setText("수신Data길이");
			lblRlen.setFont( IAqtVar.font1);
			
			txtRlen = new Text(compMessage, SWT.BORDER | SWT.RIGHT);
			txtRlen.setEditable(false);
			txtRlen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			txtRlen.setFont( IAqtVar.font1);
			txtRlen.setSize(100, -1);

			txtReceiveMsg = new StyledText(compMessage, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			GridData gd_txtReceiveMsg = new GridData(GridData.FILL_BOTH );
			gd_txtReceiveMsg.horizontalSpan = 3;
			gd_txtReceiveMsg.verticalSpan = 40;
			txtReceiveMsg.setLayoutData(gd_txtReceiveMsg);
			txtReceiveMsg.setFont( IAqtVar.font1);
			txtReceiveMsg.setEditable(false);
			txtReceiveMsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			this.setValue();
		}
	}
}
