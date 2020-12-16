package aqtclient.part;

import java.text.SimpleDateFormat;

import javax.persistence.EntityManager;

import org.eclipse.swt.SWT;
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

public class AqtDetail extends Dialog {
	protected Shell shell;
	protected Object result;
	private Ttransaction ttransaction;  // testcode1 의 ttransaction
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
		shell.setLocation(20, 20);
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
		
		shell = new Shell(getParent(), getStyle() | SWT.RESIZE);
		shell.setSize(1600, 900);
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

//		compHeader.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Composite compTitle = new Composite(compHeader, SWT.LINE_DASH);
		
		GridData gd_compTitle = new GridData(SWT.FILL , SWT.TOP, true, false);
		gd_compTitle.horizontalSpan = 10;
		compTitle.setLayoutData(gd_compTitle);
		compTitle.setLayout(new GridLayout(3, false));
//		compTitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		

		Label ltitle = new Label(compTitle, SWT.NONE);
		
    	ltitle.setText("전문상세보기" ) ;
    	ltitle.setFont( IAqtVar.title_font );
//    	ltitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));		
		
		Composite compDetail = new Composite(compHeader, SWT.BORDER);
		compDetail.setLayoutData(new GridData(SWT.FILL , SWT.TOP, true, false));
		compDetail.setLayout(new GridLayout(8, false));
		compDetail.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Label lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("테스트코드");
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

		txtTestCode = new Text(compDetail, SWT.BORDER);
		txtTestCode.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtTestCode.setEditable(false);
		txtTestCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtTestCode.setFont( IAqtVar.font1);
		
		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("UUID");
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );
		
		txtUuid = new Text(compDetail, SWT.BORDER);
		txtUuid.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtUuid.setEditable(false);
		txtUuid.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtUuid.setFont( IAqtVar.font1);
		
		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("서비스ID");
//		lblcomm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

		txtSvcId = new Text(compDetail, SWT.BORDER);
		txtSvcId.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtSvcId.setEditable(false);
		txtSvcId.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtSvcId.setFont( IAqtVar.font1);
		
		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("화면ID");
//		lblcomm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

		txtScrno = new Text(compDetail, SWT.BORDER);
		txtScrno.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtScrno.setEditable(false);
		txtScrno.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtScrno.setFont( IAqtVar.font1);
		
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
		lblcomm.setText("수신코드");
//		lblcomm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

		txtMsgcd = new Text(compDetail, SWT.BORDER);
		txtMsgcd.setLayoutData(new GridData(GridData.FILL_BOTH) );
		txtMsgcd.setEditable(false);
		txtMsgcd.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtMsgcd.setFont( IAqtVar.font1);

		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("수신메세지");
//		lblcomm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

		txtRcvMsg = new Text(compDetail, SWT.BORDER);
		txtRcvMsg.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,true,false,3,1) );
		txtRcvMsg.setEditable(false);
		txtRcvMsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtRcvMsg.setFont( IAqtVar.font1);

		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("작업일시");
//		lblcomm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

		txtCdate = new Text(compDetail, SWT.BORDER);
		txtCdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtCdate.setEditable(false);
		txtCdate.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtCdate.setFont( IAqtVar.font1);

		Composite compMessage = new Composite(compHeader, SWT.NONE);
		compMessage.setLayoutData(new GridData(SWT.FILL , SWT.FILL, true, true));
		GridLayout glm = new GridLayout(3, false) ;
		compMessage.setLayout(glm);
//		compMessage.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Label lblm = new Label(compMessage, SWT.NONE);
		lblm.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, SWT.CENTER, true, false));
//		lblm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblm.setText("송신전문");
		lblm.setFont( IAqtVar.font1) ;
		
		lblm = new Label(compMessage, SWT.NONE);
		lblm.setText("송신Data길이");
//		lblm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblm.setFont( IAqtVar.font1);
		
		txtSlen = new Text(compMessage, SWT.BORDER | SWT.RIGHT);
		txtSlen.setEditable(false);
		txtSlen.setFont( IAqtVar.font1);
		txtSlen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		
		txtSendMsg = new StyledText(compMessage, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		GridData gd_txtSendMsg = new GridData(GridData.FILL_BOTH );
		gd_txtSendMsg.horizontalSpan = 3;
		gd_txtSendMsg.verticalSpan = 40;

		txtSendMsg.setLayoutData(gd_txtSendMsg);
		txtSendMsg.setFont( IAqtVar.font1);
		txtSendMsg.setEditable(false);
		txtSendMsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		Label lblReceiveMsg = new Label(compMessage, SWT.NONE);
		lblReceiveMsg.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, SWT.CENTER, true, false));
//		lblReceiveMsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblReceiveMsg.setText("수신전문");
		lblReceiveMsg.setFont( IAqtVar.font1) ;
		
		Label lblRlen = new Label(compMessage, SWT.NONE);
		lblRlen.setText("수신Data길이");
//		lblRlen.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblRlen.setFont( IAqtVar.font1);
		
		txtRlen = new Text(compMessage, SWT.BORDER | SWT.RIGHT);
		txtRlen.setEditable(false);
		txtRlen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtRlen.setFont( IAqtVar.font1);
		
		txtReceiveMsg = new StyledText(compMessage, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		GridData gd_txtReceiveMsg = new GridData(GridData.FILL_BOTH);
		gd_txtReceiveMsg.horizontalSpan = 3;
		gd_txtReceiveMsg.verticalSpan = 40;
		txtReceiveMsg.setLayoutData(gd_txtReceiveMsg);
		txtReceiveMsg.setFont( IAqtVar.font1);
		txtReceiveMsg.setEditable(false);
		txtReceiveMsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
//		sashForm.pack();
		compTitle.pack();
		new Label(compTitle, SWT.NONE);
		new Label(compTitle, SWT.NONE);
		compMessage.pack();
		compDetail.pack();
		compHeader.requestLayout();
	}

	private void fillScreen() {
		if (ttransaction == null)
			return;
		SimpleDateFormat dformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S") ;
		txtSlen.setText(Integer.toString(ttransaction.getSlen()));
		txtSendMsg.setText(ttransaction.getSdata());
		txtRlen.setText(Integer.toString(ttransaction.getRlen()));
		txtReceiveMsg.setText(ttransaction.getRdata());
		txtUuid.setText(ttransaction.getUuid());
		txtTestCode.setText(ttransaction.getTcode());
		txtSvcId.setText(ttransaction.getSvcid());
		txtScrno.setText(ttransaction.getScrno());
		txtStime.setText(dformat.format(ttransaction.getStime()));
		txtRtime.setText(dformat.format(ttransaction.getRtime()));
		txtElapsed.setText(String.format("%.3f",ttransaction.getElapsed()));
		txtSvcTime.setText(String.format("%.3f",ttransaction.getSvctime()));
		txtMsgcd.setText( ttransaction.getMsgcd() == null   ? "" : ttransaction.getMsgcd() );
		txtRcvMsg.setText(ttransaction.getRcvmsg());
		txtCdate.setText(dformat.format(ttransaction.getCdate()));
	}
	
	public void setTrxList(Ttransaction ttransaction) {
		this.ttransaction = ttransaction;
	}

	public void setTrx(int ipkey) {
		EntityManager em = AqtMain.emf.createEntityManager();
		this.ttransaction = em.find(Ttransaction.class, ipkey);
		em.close();
	}

}
