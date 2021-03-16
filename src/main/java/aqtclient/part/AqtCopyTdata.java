package aqtclient.part;

import javax.persistence.EntityManager;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class AqtCopyTdata extends Dialog {

	private AqtTcodeCombo srcCode, dstCode;
	
	private Label lmsg ;
	private Text txtUri, txtRcode , txtEtc ;
	private String acode ;
	
	protected AqtCopyTdata(Shell parent, String scode) {
		super(parent);
		// TODO Auto-generated constructor stub
		setShellStyle(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.CLOSE | SWT.RESIZE | SWT.TITLE);
		acode = scode ;
	}
    @Override
    protected void configureShell(Shell newShell) {
    	newShell.setBackground(SWTResourceManager.getColor(215, 228, 242));
        super.configureShell(newShell);
        
        newShell.setText("테스트데이터 복제");
    }

    @Override
    protected Point getInitialSize() {
        return new Point(650, 500);
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "Copy", true).setFont(IAqtVar.font1b);
        createButton(parent, IDialogConstants.CLOSE_ID, "Close", true).setFont(IAqtVar.font1b);
    }
    
    @Override
    protected void buttonPressed ( final int buttonId )
    {
        if ( buttonId == IDialogConstants.CLOSE_ID )
            close ();
        else if ( buttonId == IDialogConstants.OK_ID ) {
        	if (dstCode.getTmaster().getEndDate() != null ) {
				MessageDialog.openInformation(Display.getCurrent().getActiveShell() , "작업불가", dstCode.getTcode() + " 는 종료되었습니다.") ;
				return ;
        	}
        	execCopy();
        }
    }
	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(true).margins(20, 20).applyTo(container);
		
		Label lbl = new Label(container,SWT.NONE) ;
		lbl.setText("테스트ID(From)  ->") ;
		lbl.setFont(IAqtVar.font1b);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).grab(true, false).applyTo(lbl);
		lbl = new Label(container,SWT.NONE) ;
		lbl.setText(" 테스트ID(To)") ;
		lbl.setFont(IAqtVar.font1b);
		srcCode = new AqtTcodeCombo(container, SWT.READ_ONLY) ;
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(srcCode.getControl());
		srcCode.findSelect(acode) ;

		dstCode = new AqtTcodeCombo(container, SWT.READ_ONLY) ;
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(dstCode.getControl());

		Group gr1 = new Group(container, SWT.SHADOW_ETCHED_IN) ;
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).margins(10, 10).applyTo(gr1);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).span(2, 1).grab(true, false).applyTo(gr1) ;
		gr1.setText("<< 데이터 선택 >>");
		gr1.setFont(IAqtVar.font1b);
		lbl = new Label(gr1,SWT.NONE ) ;
		lbl.setText("URI :") ;
		lbl.setFont(IAqtVar.font1b);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.TOP).grab(false, false).applyTo(lbl) ;
		
		txtUri = new Text(gr1,SWT.BORDER) ;
		txtUri.setFont(IAqtVar.font1);

		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(txtUri) ;
		
		lbl = new Label(gr1,SWT.NONE );
		lbl.setText("Return code :");
		lbl.setFont(IAqtVar.font1b);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.TOP).grab(false, false).applyTo(lbl) ;

		txtRcode  = new Text(gr1,SWT.BORDER) ;
		txtRcode.setFont(IAqtVar.font1);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(txtRcode) ;
		txtRcode.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent arg0) {
				arg0.doit = arg0.text.matches("[0-9]*") ;
				
			}
		});
		lbl = new Label(gr1,SWT.NONE );
		lbl.setText("기타쿼리 :");
		lbl.setFont(IAqtVar.font1b);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.TOP).grab(false, false).applyTo(lbl) ;

		txtEtc  = new Text(gr1,SWT.BORDER) ;
		txtEtc.setFont(IAqtVar.font1);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(txtEtc) ;
		
		lmsg = new Label(container, SWT.RIGHT);
		lmsg.setFont(IAqtVar.font1b);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BOTTOM).grab(true, false).span(2, 1).applyTo(lmsg);
//		container.pack();
		return container ;
	}
	
	private void execCopy() {
		String cond = (txtUri.getText().toString().isEmpty() ? "" 
				      : String.format("and uri like '%s' " , txtUri.getText().toString()) ) + 
				      (txtRcode.getText().toString().isEmpty() ? "" 
			          : String.format(" and rcode = %s " , txtRcode.getText().toString()) ) +
				      (txtEtc.getText().toString().isEmpty() ? "" 
					  : String.format(" and ( %s ) " , txtEtc.getText().toString()) ) ;
				      
//		System.out.println(cond);
		EntityManager em = AqtMain.emf.createEntityManager();
		try {
			em.getTransaction().begin();
			String rval  = em.createNativeQuery("call sp_copytestdata(?,?,?)")
					.setParameter(1, srcCode.getTcode())
					.setParameter(2, dstCode.getTcode())
					.setParameter(3, cond )
	                .getSingleResult().toString();
			em.createNativeQuery("call sp_summary(?)")
					.setParameter(1, dstCode.getTcode())
					.executeUpdate() ;
			em.getTransaction().commit();
			lmsg.setText(rval);
			lmsg.requestLayout();
		} catch (Exception e) {
			lmsg.setText(e.getMessage() );
		}
		
		em.close();
	}
}
