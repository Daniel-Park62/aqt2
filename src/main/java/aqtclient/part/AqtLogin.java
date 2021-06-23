package aqtclient.part;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class AqtLogin extends Dialog {

	protected Shell shell;
	
	private Text txtPwd;
//  private AuthType authtype ;
	private String pass ;
	private Label lblmsg , lbluser, lbltester ;

	private Timer timer ;

	private	final Font font = SWTResourceManager.getFont("Calibri", 14, SWT.BOLD) ;

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
		
		Image img_login = AqtMain.getMyimage("login.png") ;
		
//		System.out.println(url.toString());
//		Image img_login = SWTResourceManager.getImage( "images/login.png");

		Composite parent2 = (Composite)super.createDialogArea(parent);
//		parent2.setSize(img_login.getImageData().width, img_login.getImageData().height);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		
//		parent2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Composite container  = new Composite(parent2, SWT.NONE) ;
//		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
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
		AqtMain.authtype = AuthType.TESTADM ;
		
		lbluser = new Label(container,SWT.NONE); 
		lbluser.setImage(AqtMain.getMyimage("user1.png"));
//		lbluser.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lbluser.setBounds(720, 383,64,36);

		lbluser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				AqtMain.authtype = AuthType.USER ;
				lbltester.setImage(AqtMain.getMyimage("tester1.png"));
				lbluser.setImage(AqtMain.getMyimage("user2.png"));
			}
			
		});

		
		lbltester = new Label(container,SWT.NONE); 
		lbltester.setImage(AqtMain.getMyimage("tester2.png"));
//		lbltester.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lbltester.setBounds(795, 383,64,36);
		lbltester.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				AqtMain.authtype = AuthType.TESTADM ;
				lbltester.setImage(AqtMain.getMyimage("tester2.png"));
				lbluser.setImage(AqtMain.getMyimage("user1.png"));
			}
		});

		
		txtPwd = new Text(container, SWT.NONE | SWT.PASSWORD);
		txtPwd.setBounds(724, 445, 200, 30);
		txtPwd.setFont(font);
//		txtPwd.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		txtPwd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.keyCode == SWT.CR) {
					okPressed();
				}
			}
		});

		
		lblmsg = new Label(container, SWT.NONE | SWT.CENTER );
		lblmsg.setBounds(631, 730, 400, 60);
		lblmsg.setText(" ");
		lblmsg.setFont(font);
		lblmsg.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		
		timer = new Timer() ;
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				Display.getDefault().syncExec(() -> {
						lblmsg.setVisible( !lblmsg.getVisible()) ;
					 }
				);
			}
		}, 0, 1000);
		
		Label lbl = new Label(container , SWT.NONE) ;
		lbl.setImage(AqtMain.getMyimage("loginbtn.png"));
		lbl.setBounds(721, 503, 200,64);
//		lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lbl.setCursor(IAqtVar.handc);
		lbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				okPressed();
			}
			
		});

		lbl = new Label(container , SWT.NONE) ;
		lbl.setImage(AqtMain.getMyimage("cancelbtn.png"));
		lbl.setBounds(936, 503, 200,64) ;
		lbl.setCursor(IAqtVar.handc);
//		lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
//		        timer.cancel();
				cancelPressed();
			}
			
		});
//		parent2.pack();
		txtPwd.setFocus();
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
    			lblmsg.setText("비밀번호를 확인하세요!!");
    			return ;
    		}
    	}
    	else
    		AqtMain.authtype = AuthType.USER ;
    	
//        timer.cancel();
        super.okPressed();
    }
    @Override
    public boolean close() {
    	timer.cancel();
    	return super.close();
    }
    @Override
    protected void configureShell(Shell shell) {

        super.configureShell(shell);
//        shell.setFullScreen(true);
//        shell.setText("AQT LOGIN");
    }
    
}
