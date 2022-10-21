package aqtclient.part;

import java.util.List;

import javax.persistence.EntityManager;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Tmaster;

public class AqtCopyTdata extends Dialog {

	private AqtTcodeCombo  dstCode;
	private CCombo srcCode ;
	private Label lmsg ;
	private Text txtUri, txtRcode , txtEtc ;
	private Spinner spnum ;
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
        
        newShell.setText("테스트데이터 복제(전문생성)");
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
		
		// 2022.10 변경, tloaddata 에서 복제하는것으로
		srcCode = new CCombo(container, SWT.READ_ONLY | SWT.BORDER) ;
		srcCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		srcCode.setFont(IAqtVar.font1);

		EntityManager em = AqtMain.emf.createEntityManager();
		
		List<Object> tlist = em.createNativeQuery("select tcode from tloaddata group by tcode").getResultList();
		srcCode.setItems(	tlist.stream().toArray(String[]::new));

		em.close();


		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(srcCode);

		dstCode = new AqtTcodeCombo(container, SWT.READ_ONLY) ;
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(dstCode.getControl());
		dstCode.findSelect(acode) ;
		srcCode.select(0); 

		Group gr1 = new Group(container, SWT.SHADOW_ETCHED_IN) ;
		GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).margins(10, 10).applyTo(gr1);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).span(2, 1).grab(true, false).applyTo(gr1) ;
		gr1.setText("<< 데이터 선택 >>");
		gr1.setFont(IAqtVar.font1b);
		lbl = new Label(gr1,SWT.NONE ) ;
		lbl.setText("URI :") ;
		lbl.setFont(IAqtVar.font1b);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.TOP).grab(false, false).applyTo(lbl) ;
		
		txtUri = new Text(gr1,SWT.BORDER) ;
		txtUri.setFont(IAqtVar.font1);

		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2,1).applyTo(txtUri) ;
		txtUri.setFocus() ;

		lbl = new Label(gr1,SWT.NONE ) ;
		lbl.setText("URI 별 건수 :") ;
		lbl.setFont(IAqtVar.font1b);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.TOP).grab(false, false).applyTo(lbl) ;
		
		spnum = new Spinner(gr1,SWT.BORDER ) ;
		spnum.setFont(IAqtVar.font1);
		spnum.setSelection(0);
		spnum.setMaximum(999999);

		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.TOP).minSize(150, -1).grab(true, false).applyTo(spnum) ;
		
		lbl = new Label(gr1,SWT.NONE ) ;
		lbl.setText("지정한 건수이내로 URI별 복제됩니다.\n 0을 선택하면 원본에 있는대로 복제함") ;
//		lbl.setFont(IAqtVar.font1b);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.TOP).grab(false, false).applyTo(lbl) ;

		lbl = new Label(gr1,SWT.NONE );
		lbl.setText("Return code :");
		lbl.setFont(IAqtVar.font1b);
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.TOP).grab(false, false).applyTo(lbl) ;

		txtRcode  = new Text(gr1,SWT.BORDER) ;
		txtRcode.setFont(IAqtVar.font1);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2,1).applyTo(txtRcode) ;
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
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2,1).applyTo(txtEtc) ;
//		txtEtc.setToolTipText("* method: POST, GET 등 \n* rcode: 응답코드(200 등)\n* sflag: 0.미수행 1.성공 2.실패 \n* srcip,srcport: 소스ip,port\n* dstip,dstport: 목적지ip,port\n* svctime: 응답소요시간");
//		txtEtc.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				if ( e.keyCode == SWT.F1) {
//					lmsg.setText(txtEtc.getToolTipText());
//					lmsg.requestLayout();
//				}
//				super.keyReleased(e);
//			}
//		});
		
		lbl = new Label(gr1,SWT.NONE );
		lbl = new Label(gr1,SWT.NONE );
		lbl.setText("* method: POST, GET 등 \n* rcode: 응답코드(200 등)\n* sflag: 0.미수행 1.성공 2.실패 ") ;
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.TOP).span(1, 1).grab(false, false).applyTo(lbl) ;
		lbl = new Label(gr1,SWT.NONE );
		lbl.setText("* srcip,srcport: 소스ip,port\n* dstip,dstport: 목적지ip,port\n* svctime: 응답소요시간");
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.TOP).span(1, 1).grab(false, false).applyTo(lbl) ;
		
		lmsg = new Label(container, SWT.LEFT);
		lmsg.setFont(IAqtVar.font1);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BOTTOM).grab(true, false).span(2, 1).applyTo(lmsg);
//		container.pack();
		return container ;
	}
	
	private void execCopy() {
		String cond = (txtUri.getText().toString().isEmpty() ? "" 
				      : String.format("and uri rlike '%s' " , txtUri.getText().toString()) ) + 
				      (txtRcode.getText().toString().isEmpty() ? "" 
			          : String.format(" and rcode = %s " , txtRcode.getText().toString()) ) +
				      (txtEtc.getText().toString().trim().isEmpty() ? "" 
					  : String.format(" and ( %s ) " , txtEtc.getText().toString()) ) ;
				      
//		System.out.println(cond);
		EntityManager em = AqtMain.emf.createEntityManager();
		try {
			lmsg.setText("...작업중...");
			em.getTransaction().begin();
			String rval  = em.createNativeQuery("call sp_loaddata2(?,?,?,?)")
					.setParameter(1, srcCode.getText())
					.setParameter(2, dstCode.getTcode())
					.setParameter(3, cond )
					.setParameter(4, spnum.getSelection())
	                .getSingleResult().toString();
			em.createNativeQuery("call sp_summary(?)")
					.setParameter(1, dstCode.getTcode())
					.executeUpdate() ;
			em.getTransaction().commit();
			lmsg.setText(rval);
		} catch (Exception e) {
			lmsg.setText(e.getMessage() );
		}
		lmsg.requestLayout();
		em.close();
	}
}
