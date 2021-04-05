package aqtclient.part;

import java.text.SimpleDateFormat;

import javax.persistence.EntityManager;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PopupList;
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
	private Text txtStime;
	private Text txtRtime;
	private Text txtElapsed;
	private Text txtSvcTime;
	private StyledText txtRhead;
	private Text txtRcode;
	private Text txtCdate;
	protected String sv_select = "UTF-8";

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
		shell.setSize(1800, 1000);
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
		lblcomm.setText("수신코드");
//		lblcomm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.TOP,false, false) );

		txtRcode = new Text(compDetail, SWT.BORDER);
		txtRcode.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false, false)  );
		txtRcode.setEditable(false);
		txtRcode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtRcode.setFont( IAqtVar.font1);

		lblcomm = new Label(compDetail, SWT.NONE);
		lblcomm.setText("URI");
//		lblcomm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false) );

		txtUri = new Text(compDetail, SWT.BORDER);
		txtUri.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false,5,1));
		txtUri.setEditable(false);
		txtUri.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtUri.setFont( IAqtVar.font1);
		

		Composite compMessage = new Composite(compHeader, SWT.NONE);
		compMessage.setLayoutData(new GridData(SWT.FILL , SWT.FILL, true, true));
		compMessage.setLayout(new GridLayout(3, false));
//		compMessage.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Label lblm = new Label(compMessage, SWT.NONE);
		lblm.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, SWT.CENTER, true, false));
//		lblm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblm.setText("송신Data");
		lblm.setFont( IAqtVar.font1) ;
		
		lblm = new Label(compMessage, SWT.NONE);
		lblm.setText("송신Data길이");
//		lblm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblm.setFont( IAqtVar.font1);
		
		txtSlen = new Text(compMessage, SWT.BORDER | SWT.RIGHT);
		txtSlen.setEditable(false);
		txtSlen.setFont( IAqtVar.font1);
		txtSlen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		
		txtSendMsg = new StyledText(compMessage, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).hint(-1, 200).span(3, 1).applyTo(txtSendMsg);
		txtSendMsg.setFont( IAqtVar.font1);
		txtSendMsg.setEditable(false);
		txtSendMsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		lblcomm = new Label(compMessage, SWT.NONE);
		lblcomm.setText("수신Header");
		lblcomm.setFont( IAqtVar.font1);
		lblcomm.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,false, false,3,1) );

		txtRhead = new StyledText(compMessage, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).span(3, 1).hint(-1, 180).applyTo(txtRhead);
//		txtRhead.setLayoutData(new GridData(SWT.FILL, SWT.TOP,true,false,3,1) );
		txtRhead.setEditable(false);
		txtRhead.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtRhead.setFont( IAqtVar.font1);

		
//		Label lblReceiveMsg = new Label(compMessage, SWT.NONE);
//		lblReceiveMsg.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, SWT.CENTER, true, false));
//		lblReceiveMsg.setText("수신Data");
//		lblReceiveMsg.setFont( IAqtVar.font1) ;

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
	        sv_select = selected ;
	        txtReceiveMsg.setText(tpacket.getRdataENCODE(selected));
	      }
	    });
	    GridDataFactory.fillDefaults().grab(true, false).align(SWT.LEFT,SWT.CENTER).applyTo(button);
	    
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
		if (tpacket == null)
			return;
		SimpleDateFormat dformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S") ;
		txtPkey.setText(tpacket.getPkey()+"");
		txtSlen.setText(String.format("%,d",tpacket.getSlen()));
		txtSendMsg.setText(tpacket.getSdata());
		txtRlen.setText(String.format("%,d", tpacket.getRlen()));
		txtReceiveMsg.setText(tpacket.getRdataUTF());
		txtCmpid.setText(tpacket.getCmpid()+"");
		txtTestCode.setText(tpacket.getTcode());
		txtUri.setText(tpacket.getUri());
//		txtScrno.setText(tpacket.getScrno());
		txtStime.setText(dformat.format(tpacket.getStime()));
		txtRtime.setText(dformat.format(tpacket.getRtime()));
		txtElapsed.setText(String.format("%.3f",tpacket.getElapsed()));
		txtSvcTime.setText(String.format("%.3f",tpacket.getSvctime()));
		txtRcode.setText( tpacket.getRcode()+"" );
		txtRhead.setText(tpacket.getRhead());
		txtCdate.setText(dformat.format(tpacket.getCdate()));
		txtRlen.requestLayout();
		txtSlen.requestLayout();
	}
	
	public void setTrxList(Ttcppacket tpacket) {
		this.tpacket = tpacket;
	}

	public void setTrx(long ipkey) {
		EntityManager em = AqtMain.emf.createEntityManager();
		this.tpacket = em.find(Ttcppacket.class, ipkey);
		em.close();
	}

}
