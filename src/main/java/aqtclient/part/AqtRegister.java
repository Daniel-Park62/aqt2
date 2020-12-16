package aqtclient.part;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.EntityManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Tmaster;

public class AqtRegister extends Dialog {
	protected Shell shell;
	protected Object result;

	private Text txtCode;
	private Text txtCmpCode;
	private Text txtDesc;
	private Text txtDir;
	private Text txtUser;
	private Text txtHost;
	private Text txtPort;
	private Text txtEnv;
	
	private Button[] btnType = new Button[2];
	private Button[] btnLevel = new Button[4];
	
	private static Tmaster tmaster;
	private Text txtSdate, txtEdate;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	/*
	public AqtRegister(Composite parent, int style) {
		create (parent, style);
	}
	*/

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AqtRegister(Shell parent, int style) {
		super(parent, style);
	}

	public void setTdate() {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	    Calendar calendar = Calendar.getInstance();
	    String strToday = sdf.format(calendar.getTime());
	    tmaster.setTdate(strToday);
	}
	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.setText(IAqtVar.titnm);

		shell.open();
		shell.layout();
		shell.setBounds(50, 50, 700, 600);

		Display display = getParent().getDisplay();

		refreshScreen();

		if (tmaster.getCode() != null)
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
	
		shell = new Shell(getParent(), getStyle());
		shell.setLayout(new FillLayout());
		shell.setBackground(SWTResourceManager.getColor(225,230,246));
		shell.setBackgroundMode(SWT.INHERIT_FORCE);
	    
		Composite compHeader = new Composite(shell, SWT.BORDER);
		GridLayout gl_compHeader = new GridLayout();
		gl_compHeader.verticalSpacing = 15;
		gl_compHeader.marginHeight = 20;
		gl_compHeader.marginLeft = 20;
		gl_compHeader.marginRight = 20;
		gl_compHeader.marginBottom = 40;
		gl_compHeader.numColumns = 4;
		compHeader.setLayout(gl_compHeader);

		compHeader.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		Composite compTitle = new Composite(compHeader, SWT.LINE_DASH);
		
//		compTitle.setLayoutData(new GridData( SWT.FILL , SWT.TOP, true, false));
		GridData titleGridData = new GridData(SWT.FILL , SWT.TOP, true, false);
		titleGridData.horizontalSpan = 4;
		compTitle.setLayoutData(titleGridData);
		
		compTitle.setLayout(new GridLayout(3, false));
		
		Label ltitle = new Label(compTitle, SWT.NONE);
		
    	ltitle.setImage(SWTResourceManager.getImage("images/tit_register.png"));

		Label lblCode = new Label(compHeader, SWT.NONE);
		lblCode.setFont(IAqtVar.font1);
		lblCode.setText("코드");
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		
		txtCode = new Text(compHeader, SWT.BORDER);
		txtCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtCode.setFont(IAqtVar.font1);
		txtCode.setTextLimit(20);
		txtCode.setEditable(false);
		txtCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
//		Group grpType = new Group(compHeader, SWT.NONE);
//		GridData gd_grpType = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, GridData.HORIZONTAL_ALIGN_END, true, false);
//		gd_grpType.horizontalSpan = 2;
//		gd_grpType.
//		grpType.setLayoutData(gd_grpType);
//		RowLayout rl_grpType = new RowLayout(SWT.HORIZONTAL);
//		rl_grpType.spacing=2;
//		grpType.setLayout(rl_grpType);
		
		lblCode = new Label(compHeader, SWT.NONE| SWT.RIGHT);
		lblCode.setFont(IAqtVar.font1);
		lblCode.setText("타입");
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		
		RowLayout rlo = new RowLayout(SWT.HORIZONTAL) ;
		rlo.marginWidth = 10;
		rlo.spacing = 5 ;
		
		Composite compType = new Composite(compHeader, SWT.BORDER);
		compType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		compType.setLayout(rlo);
		
		btnType[0] = new Button(compType, SWT.RADIO);
		btnType[0].setText("배치");
		btnType[0].setFont(IAqtVar.font1);
		
		btnType[1] = new Button(compType, SWT.RADIO);
		btnType[1].setText("실시간");
		btnType[1].setFont(IAqtVar.font1);
//		btnType[1].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		/*		
		txtType = new Text(compHeader, SWT.BORDER);
		txtType.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		txtType.setTextLimit(1);
		txtType.setFont(IAqtVar.font1);
*/		
		lblCode = new Label(compHeader, SWT.NONE);
		lblCode.setText("테스트명");
		lblCode.setFont(IAqtVar.font1);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		
		txtDesc = new Text(compHeader, SWT.BORDER);
		GridData gd_txtDesc = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_txtDesc.horizontalSpan = 3;
		txtDesc.setLayoutData(gd_txtDesc);
		txtDesc.setTextLimit(50);
		txtDesc.setFont(IAqtVar.font1);
		txtDesc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		lblCode = new Label(compHeader, SWT.NONE);
		lblCode.setText("비교대상테스트");
		lblCode.setFont(IAqtVar.font1);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		
		txtCmpCode = new Text(compHeader, SWT.BORDER);
		txtCmpCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,3,1));
		txtCmpCode.setTextLimit(50);
		txtCmpCode.setFont(IAqtVar.font1);
		txtCmpCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		lblCode = new Label(compHeader, SWT.NONE);
		lblCode.setText("테스트시작일");
		lblCode.setFont(IAqtVar.font1);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		
		txtSdate = new Text(compHeader, SWT.BORDER);
		txtSdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtSdate.setFont(IAqtVar.font1);
		txtSdate.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		lblCode = new Label(compHeader, SWT.NONE);
		lblCode.setText("테스트종료일");
		lblCode.setFont(IAqtVar.font1);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		
		txtEdate = new Text(compHeader, SWT.BORDER);
		txtEdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtEdate.setFont(IAqtVar.font1);
	    txtEdate.setEditable(false);
		
		lblCode = new Label(compHeader, SWT.NONE| SWT.RIGHT);
		lblCode.setFont(IAqtVar.font1);
		lblCode.setText("단계");
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		
		Composite compLevel = new Composite(compHeader, SWT.BORDER);
		compLevel.setLayout(rlo);
		compLevel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,3,1));
		
		btnLevel[0] = new Button(compLevel, SWT.RADIO );
		btnLevel[0].setFont(IAqtVar.font1);
		btnLevel[0].setText("대상아님");
		
		btnLevel[1] = new Button(compLevel, SWT.RADIO);
		btnLevel[1].setText("단위");
		btnLevel[1].setFont(IAqtVar.font1);

		btnLevel[2] = new Button(compLevel, SWT.RADIO);
		btnLevel[2].setText("통합");
		btnLevel[2].setFont(IAqtVar.font1);

		btnLevel[3] = new Button(compLevel, SWT.RADIO);
		btnLevel[3].setText("실시간");
		btnLevel[3].setFont(IAqtVar.font1);
		
		/*
		txtStep = new Text(compHeader, SWT.BORDER);
		txtStep.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		txtStep.setFont(IAqtVar.font1);
		txtDesc.setTextLimit(1);
		*/
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

	    Calendar calendar = Calendar.getInstance();
	    String strToday = sdf.format(calendar.getTime());
		
	    txtSdate.setText(strToday);
	    txtSdate.setEditable(false);
	    txtSdate.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseDoubleClick(MouseEvent e) {
	    		Point pt = AqtMain.aqtmain.getShell().getDisplay().getCursorLocation() ; 
	        	CalDialog cd = new CalDialog(Display.getCurrent().getActiveShell() , pt.x, pt.y + 20 );
	    		
                String s = (String)cd.open();
                if (s != null) {
                	txtSdate.setText(s.replace('-', '/') ) ;
                }
	    		super.mouseDoubleClick(e);
	    	}
		});

		lblCode = new Label(compHeader, SWT.NONE);
		lblCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblCode.setText("환경파일");
		lblCode.setFont(IAqtVar.font1);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		
		txtEnv = new Text(compHeader, SWT.BORDER);
		GridData gd_txtEnv = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_txtEnv.horizontalSpan = 3;
		txtEnv.setLayoutData(gd_txtEnv);
		txtEnv.setTextLimit(80);
		txtEnv.setFont(IAqtVar.font1);

		lblCode = new Label(compHeader, SWT.NONE);
		lblCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblCode.setText("전송Label");
		lblCode.setFont(IAqtVar.font1);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		
		txtHost = new Text(compHeader, SWT.BORDER);
		txtHost.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false,2,1));
		txtHost.setTextLimit(50);
		txtHost.setFont(IAqtVar.font1);

		lblCode = new Label(compHeader, SWT.NONE);
		lblCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblCode.setText("*환경파일 내  Label ");
		lblCode.setFont(IAqtVar.font1);
		lblCode.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));

/*		
		lblCode = new Label(compHeader, SWT.NONE);
		lblCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblCode.setText("포트");
		lblCode.setFont(IAqtVar.font1);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		
		txtPort = new Text(compHeader, SWT.BORDER);
		txtPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtPort.setTextLimit(5);
		txtPort.setFont(IAqtVar.font1);
		
		
	    lblCode = new Label(compHeader, SWT.NONE);
	    lblCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
	    lblCode.setText("위치");
	    lblCode.setFont(IAqtVar.font1);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		
		txtDir = new Text(compHeader, SWT.BORDER);
		GridData gd_txtDir = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd_txtDir.horizontalSpan = 3;
		txtDir.setLayoutData(gd_txtDir);

		txtDir.setTextLimit(80);
		txtDir.setFont(IAqtVar.font1);
		
		lblCode = new Label(compHeader, SWT.NONE);
		lblCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblCode.setText("사용자");
		lblCode.setFont(IAqtVar.font1);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		
		txtUser = new Text(compHeader, SWT.BORDER);
		GridData gd_txtUser = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd_txtUser.horizontalSpan = 3;
		txtUser.setLayoutData(gd_txtUser);

		txtUser.setTextLimit(20);
		txtUser.setFont(IAqtVar.font1);
		
*/
		Composite compButton = new Composite(compHeader, SWT.NONE);
		
		GridData gd_compButton = new GridData(SWT.FILL , SWT.CENTER, true, false);
		gd_compButton.horizontalSpan = 4;
		compButton.setLayoutData(gd_compButton);
		
		compButton.setLayout(new GridLayout(4, false));
		compButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

//		Label label = new Label(compButton, SWT.NONE);
//		label.setText("          ");
//		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
//    	label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button btnUpsert = new Button(compButton, SWT.NONE);
		btnUpsert.setLayoutData(new GridData(SWT.END, SWT.TOP, true, true));
		btnUpsert.setFont(IAqtVar.font1);
		btnUpsert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				upsertData();
				shell.close();
			}
		});
		btnUpsert.setText("등록(수정)");
		btnUpsert.setEnabled(AqtMain.authtype == AuthType.TESTADM  && tmaster.getTdate() .compareTo( tmaster.getEndDate() ) > 0);

		Button btnEnd = new Button(compButton, SWT.NONE);
		btnEnd.setLayoutData(new GridData(SWT.END, SWT.TOP, true, true));
		btnEnd.setFont(IAqtVar.font1);
		btnEnd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				testEnding();
				shell.close();
			}
		});
		btnEnd.setText("테스트종료");
		btnEnd.setEnabled(AqtMain.authtype == AuthType.TESTADM && tmaster.getTdate() .compareTo( tmaster.getEndDate() ) > 0);

		Button btnDelete = new Button(compButton, SWT.NONE);
		btnDelete.setLayoutData(new GridData(SWT.END, SWT.TOP, true, false));
		btnDelete.setFont(IAqtVar.font1);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteData();
				shell.close();
			}
		});
		btnDelete.setText("    삭제    ");
		btnDelete.setEnabled(AqtMain.authtype == AuthType.TESTADM );
		
		Button btnClose = new Button(compButton, SWT.NONE);
		btnClose.setLayoutData(new GridData(SWT.END, SWT.TOP, true, false));
		btnClose.setFont(IAqtVar.font1);
		
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnClose.setText("    닫기    ");
		
		compHeader.pack();
		compTitle.pack();
	}
	
	private void refreshScreen() {
		txtCode.setText("");
		btnType[0].setSelection(true);
		btnType[1].setSelection(false);

		txtDesc.setText("");
		txtCmpCode.setText("");
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	    Calendar calendar = Calendar.getInstance();
	    String strToday = sdf.format(calendar.getTime());
	    txtSdate.setText(strToday);
		btnLevel[1].setSelection(true);
//		btnLevel[0].setSelection(false);
//		txtDir.setText("");
//		txtUser.setText("");
		txtHost.setText("");
//		txtPort.setText("");
		txtEnv.setText("");
	}
	 
	private void fillScreen() {
		txtCode.setText(tmaster.getCode());
		txtCode.setEditable( tmaster.getCode().isEmpty()  );

		btnType[0].setSelection("1".equals(tmaster.getType()) ? true : false);
		btnType[1].setSelection(!btnType[0].getSelection());
		
		txtDesc.setText(tmaster.getDesc1());
		if (tmaster.getCmpCode() != null)
			txtCmpCode.setText(tmaster.getCmpCode());
		else
			txtCmpCode.setText("");
		
		txtSdate.setText(tmaster.getTdate());
		txtEdate.setText(tmaster.getEndDate());
		
		btnLevel[0].setSelection("0".equals(tmaster.getLvl()) ? true : false);
		btnLevel[1].setSelection("1".equals(tmaster.getLvl()) ? true : false);
		btnLevel[2].setSelection("2".equals(tmaster.getLvl()) ? true : false);
		btnLevel[3].setSelection("3".equals(tmaster.getLvl()) ? true : false);

		txtHost.setText(tmaster.getThost());
		txtEnv.setText(tmaster.getTenv());
	}
	
	public void setTmaster (Tmaster tmaster) {
		this.tmaster = tmaster;
	}
	
	private void upsertData () {
		if (txtCode.getText().equals("")) {
//			MessageBox messageBox = new MessageBox(parent.getShell(), SWT.ICON_ERROR);
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
			messageBox.setMessage("코드는 필수 입력 항목 입니다.");
			messageBox.open();
			
			return;
		}
/*
		MessageBox msgUpsert = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		msgUpsert.setText("저장확인");
		msgUpsert.setMessage("저장 하시겠습니까?");
		int flag = msgUpsert.open();
		
		if (flag == SWT.NO)
			return;
*/		
	    EntityManager em = AqtMain.emf.createEntityManager();
//	    tmaster = new Tmaster();
	    
	    tmaster.setCode(txtCode.getText());
	    String strType = "1";
	    
	    strType = btnType[0].getSelection() ? "1" : "2";
	    
	    tmaster.setType(strType);
	    tmaster.setDesc1(txtDesc.getText());
	    tmaster.setCmpCode(txtCmpCode.getText());
	    tmaster.setTdate(txtSdate.getText());
	    
	    String strLevel = "1";
	    
	    strLevel = btnLevel[1].getSelection() ? "1" : 
	    	btnLevel[2].getSelection() ? "2" : btnLevel[3].getSelection() ? "3" : "0";
	    
	    tmaster.setLvl(strLevel);
//	    tmaster.setTdir(txtDir.getText());
//	    tmaster.setTuser(txtUser.getText());
	    tmaster.setThost(txtHost.getText());
//	    tmaster.setTport(txtPort.getText());
	    tmaster.setTenv(txtEnv.getText());

		em.getTransaction().begin();
//		Tmaster temp = em.find(Tmaster.class, txtCode.getText());
		em.merge(tmaster);
/*		
		if (temp == null) {
			em.persist(tmaster);
		} else {

			MessageBox msgUpsert = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			msgUpsert.setText("수정확인");
			msgUpsert.setMessage("데이터가 존재 합니다. 수정 하시겠습니까?");
			int flag = msgUpsert.open();
			
			if (flag == SWT.NO)
				return;
			
			em.merge(tmaster);
		}
*/		
		em.getTransaction().commit();
		em.close();
	}

	private void testEnding () {
		if (txtCode.getText().equals("")) {
			return;
		}
		MessageBox msg = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		msg.setText("테스트완료 확인");
		msg.setMessage("테스트 완료 처리 하시겠습니까?\n 이 테스트 코드로는 더이상 테스트 수행할 수 없게됩니다.");
		int flag = msg.open();
		
		if (flag == SWT.NO)
			return;

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	    Calendar calendar = Calendar.getInstance();
	    String strToday = sdf.format(calendar.getTime());
	    txtEdate.setText(strToday);
		
	    EntityManager em = AqtMain.emf.createEntityManager();
	    
	    tmaster.setEndDate(strToday);

		em.getTransaction().begin();
		em.merge(tmaster);
		em.getTransaction().commit();
		em.close();
	}

	private void deleteData () {
		if (txtCode.getText().equals("")) {
//			MessageBox messageBox = new MessageBox(parent.getShell(), SWT.ICON_ERROR);
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
			messageBox.setMessage("삭제할 데이터가 없습니다.");
			messageBox.open();
			
			return;
		}

		MessageBox msgDelete = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		msgDelete.setText("삭제확인");
		msgDelete.setMessage("삭제 하시겠습니까?");
		int flag = msgDelete.open();
		
		if (flag == SWT.NO)
			return;
		
	    EntityManager em = AqtMain.emf.createEntityManager();
	    tmaster = new Tmaster();
	    tmaster.setCode(txtCode.getText());
	    
		em.getTransaction().begin();
		if (!em.contains(tmaster))
			tmaster = em.merge(tmaster);
		em.remove(tmaster);
		
		em.getTransaction().commit();
		em.close();
	}
}
