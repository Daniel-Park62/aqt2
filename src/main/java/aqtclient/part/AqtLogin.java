package aqtclient.part;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Tmaster;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public class AqtLogin extends Dialog {

	protected Shell shell;
	private Text txtPwd;
//  private AuthType authtype ;
	private String pass ;
	Label lblmsg , lbluser, lbltester ;
	
	final Font font = SWTResourceManager.getFont("Calibri", 14, SWT.NORMAL);
	final Font fonth = SWTResourceManager.getFont("맑은 고딕", 13, SWT.NORMAL);

	/**
	 * @wbp.parser.constructor
	 */
	protected AqtLogin(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.SYSTEM_MODAL );
		// TODO Auto-generated constructor stub
	}

	protected AqtLogin(Shell parentShell, String pass) {
		this(parentShell);
		this.pass = pass ;
		// TODO Auto-generated constructor stub
	}

    @Override
    protected Point getInitialSize() {
        return new Point(1560, 1000);
    }
	
	@Override
	protected Control createDialogArea(Composite parent) {

		Image img_login = SWTResourceManager.getImage( "images/login.png");

		Composite parent2 = (Composite)super.createDialogArea(parent);
//		parent2.setSize(img_login.getImageData().width, img_login.getImageData().height);

		parent2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Composite container  = new Composite(parent2, SWT.NONE) ;
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		container.setBackgroundImage(img_login);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		container.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.keyCode == SWT.CR) {
					okPressed();
				}
			}
		});
		
		lbluser = new Label(container,SWT.NONE); 
		lbluser.setImage(SWTResourceManager.getImage("images/user2.png"));
		lbluser.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lbluser.setBounds(720, 383,64,36);

		lbluser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				AqtMain.authtype = AuthType.USER ;
				lbltester.setImage(SWTResourceManager.getImage("images/tester1.png"));
				lbluser.setImage(SWTResourceManager.getImage("images/user2.png"));
			}
			
		});

		
		lbltester = new Label(container,SWT.NONE); 
		lbltester.setImage(SWTResourceManager.getImage("images/tester1.png"));
		lbltester.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lbltester.setBounds(795, 383,64,36);
		lbltester.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				AqtMain.authtype = AuthType.TESTADM ;
				lbltester.setImage(SWTResourceManager.getImage("images/tester2.png"));
				lbluser.setImage(SWTResourceManager.getImage("images/user1.png"));
			}
		});

		
		txtPwd = new Text(container, SWT.NONE | SWT.PASSWORD);
		txtPwd.setBounds(724, 450, 200, 30);
		txtPwd.setFont(font);
		txtPwd.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		txtPwd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.keyCode == SWT.CR) {
					okPressed();
				}
			}
		});

		
		lblmsg = new Label(container, SWT.NONE );
		lblmsg.setBounds(631, 742, 400, 60);
		lblmsg.setText(" ");
		lblmsg.setFont(font);
		lblmsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Label lbl = new Label(container , SWT.NONE) ;
		lbl.setImage(SWTResourceManager.getImage("images/loginbtn.png"));
		lbl.setBounds(721, 503, 200,64);
		lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lbl.setCursor(IAqtVar.handc);
		lbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				okPressed();
			}
			
		});

		lbl = new Label(container , SWT.NONE) ;
		lbl.setImage(SWTResourceManager.getImage("images/cancelbtn.png"));
		lbl.setBounds(936, 503, 200,64) ;
		lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				cancelPressed();
			}
			
		});
//		parent2.pack();
		return parent2 ;

	}

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
    	
    	GridLayout layout = (GridLayout)parent.getLayout();
    	layout.marginHeight = 0;
    }


    @Override
    protected void okPressed() {
    	if (AqtMain.authtype == AuthType.TESTADM ) {
    		if (! pass.equals( txtPwd.getText() ) ){
    			lblmsg.setText("비밀번호가 맞지않습니다.");
    			return ;
    		}
    	}
    	else
    		AqtMain.authtype = AuthType.USER ;
    	
        super.okPressed();
    }
    @Override
    protected void configureShell(Shell shell) {

        super.configureShell(shell);
//        shell.setFullScreen(true);
//        shell.setText("AQT LOGIN");
    }


}
