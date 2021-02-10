package aqtclient.part;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class AqtCopyTdata extends Dialog {

	private AqtTcodeCombo srcCode, dstCode;
	
	private Label lmsg ;
	private Text txtUri, txtRcode ;
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
        return new Point(600, 500);
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "Copy", true);
        createButton(parent, IDialogConstants.CLOSE_ID, "Close", true);
    }
    
    @Override
    protected void buttonPressed ( final int buttonId )
    {
        if ( buttonId == IDialogConstants.CLOSE_ID )
            close ();
        else if ( buttonId == IDialogConstants.OK_ID )
        	execCopy();
    }
	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(true).margins(20, 20).applyTo(container);
		
		Label lbl = new Label(container,SWT.NONE) ;
		lbl.setText("테스트ID(From)   ->") ;
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).grab(true, false).applyTo(lbl);
		lbl = new Label(container,SWT.NONE) ;
		lbl.setText("테스트ID(To)") ;
		srcCode = new AqtTcodeCombo(container, SWT.READ_ONLY) ;
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(srcCode.getControl());
		srcCode.findSelect(acode) ;

		dstCode = new AqtTcodeCombo(container, SWT.READ_ONLY) ;
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(dstCode.getControl());

		Group gr1 = new Group(container, SWT.SHADOW_ETCHED_IN) ;
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).margins(10, 10).applyTo(gr1);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).span(2, 1).grab(true, false).applyTo(gr1) ;
		gr1.setText("<< 데이터 선택 >>");
		lbl = new Label(gr1,SWT.NONE ) ;
		lbl.setText("URI :") ;
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.TOP).grab(false, false).applyTo(lbl) ;
		
		txtUri = new Text(gr1,SWT.BORDER) ;
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(txtUri) ;
		
		lbl = new Label(gr1,SWT.NONE );
		lbl.setText("Return code :");
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.TOP).grab(false, false).applyTo(lbl) ;

		txtRcode  = new Text(gr1,SWT.BORDER) ;
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(txtRcode) ;
		txtRcode.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent arg0) {
				arg0.doit = arg0.text.matches("[0-9]*") ;
				
			}
		});
		
		lmsg = new Label(container, SWT.RIGHT);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BOTTOM).grab(true, false).span(2, 1).applyTo(lmsg);
		
		return container ;
	}
	
	private void execCopy() {
		EntityManager em = AqtMain.emf.createEntityManager();
		em.getTransaction().begin();
		int num = ((Number)em.createNativeQuery("call sp_copytestdata(?,?,?)")
				.setParameter(1, srcCode.getTcode())
				.setParameter(2, dstCode.getTcode())
				.setParameter(2, "")
                .getSingleResult()).intValue();
		em.createNativeQuery("call sp_summary(?)")
				.setParameter(1, dstCode.getTcode())
				.executeUpdate() ;
		em.getTransaction().commit();
		
		lmsg.setText(num + " 건 복제되었음.");
		em.close();
	}
}
