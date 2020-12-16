package aqtclient.part;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Tservice;
import aqtclient.model.Ttranabbr;

public class AqtTRList extends Dialog {

	private Table tblDetailResult1;

	private Text txtReceive1;
	private Text txtSend1;
	
	private List<Ttranabbr> tempTrxList1 = new ArrayList<Ttranabbr>(); // testcode1 의 ttransaction
	private AqtTranTable tableViewerDR1;
	private String cond_str ;
	public AqtTRList(Shell parent, String cond_string) {
		super(parent) ; 
		setShellStyle(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.CLOSE | SWT.RESIZE | SWT.TITLE);
		this.cond_str = cond_string ;
	}

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        
        newShell.setText("전문상세목록");
    }

    @Override
    protected Point getInitialSize() {
        return new Point(1600, 950);
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.ABORT_ID, "Refresh", true);
        
        createButton(parent, IDialogConstants.CLOSE_ID, "Close", true);
    }
    
    @Override
    protected void buttonPressed ( final int buttonId )
    {
        if ( buttonId == IDialogConstants.CLOSE_ID )
            close ();
        else if ( buttonId == IDialogConstants.ABORT_ID )
        	refreshScreen();
    }
	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());
		Composite compHeader = new Composite(container, SWT.NONE);
		GridLayout headerLayout = new GridLayout(1, true);
		headerLayout.verticalSpacing = 5;
		headerLayout.marginTop = 20;
		headerLayout.marginBottom = 5;
		headerLayout.marginWidth = 15;
		compHeader.setLayout(headerLayout);

//		compHeader.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		Label ltitle = new Label(compHeader, SWT.NONE);
		
    	ltitle.setText("전문상세목록" ) ;
    	ltitle.setFont( IAqtVar.title_font );

		Composite compCode1 = new Composite(compHeader, SWT.NONE);
		GridLayout gl_compCode1 = new GridLayout(2,false);
		gl_compCode1.verticalSpacing = 5;
		gl_compCode1.marginHeight = 10;
		gl_compCode1.marginWidth = 15;
		compCode1.setLayout(gl_compCode1);
		compCode1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		CLabel clb = new CLabel(compCode1, SWT.NONE ) ;
		clb.setText("Search:");
//		clb.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		Text txtFind = new Text(compCode1, SWT.BORDER) ;
		txtFind.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtFind.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
//		txtFind.setSize(600, -1);
		txtFind.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				arg0.doit = true ;
				TableItem[] tia = tblDetailResult1.getItems() ;
				String sval = txtFind.getText() ;
				if (sval.isEmpty() )  return ;
				
				loop1 : for(int i=0; i<tia.length ; i++) {
					for (int j=0; j < tblDetailResult1.getColumnCount(); j++)
						if ((tia[i].getText(j)).contains(sval)) {
							tblDetailResult1.setSelection(i);
							break loop1;
						}
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				
			}
		});
		tableViewerDR1 = new AqtTranTable(compCode1, SWT.NONE | SWT.FULL_SELECTION | SWT.VIRTUAL );
		tblDetailResult1 = tableViewerDR1.getTable();
//		final TableCursor cursor = new TableCursor(tblDetailResult1, SWT.NONE);
//		cursor.setFont(IAqtVar.font1 );
//		cursor.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
//		cursor.addSelectionListener(new SelectionAdapter() {
//		      public void widgetSelected(SelectionEvent e) {
//		            Clipboard clipboard = new Clipboard(Display.getDefault());
//		            String sdata = cursor.getRow().getText(cursor.getColumn()) ;
//		            clipboard.setContents(new Object[] { sdata }, new Transfer[] { TextTransfer.getInstance() });
//		            clipboard.dispose();
//		        }
//		});
		
		tblDetailResult1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtSend1.setText(tempTrxList1.get(tblDetailResult1.getSelectionIndex()).getSdata());
				txtReceive1.setText(tempTrxList1.get(tblDetailResult1.getSelectionIndex()).getRdata());
			}
		});
		
		tblDetailResult1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		Label lblSend1 = new Label(compCode1, SWT.NONE);
		lblSend1.setText("SEND");
		lblSend1.setFont(IAqtVar.font1);
//		lblSend1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		txtSend1 = new Text(compCode1, SWT.BORDER);
		txtSend1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtSend1.setEditable(false);
		txtSend1.setFont(IAqtVar.font1);

		Label lblReceive1 = new Label(compCode1, SWT.NONE);
		lblReceive1.setText("RECEIVE");
		lblReceive1.setFont(IAqtVar.font1);
//		lblReceive1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		txtReceive1 = new Text(compCode1, SWT.BORDER);
		txtReceive1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtReceive1.setEditable(false);
		txtReceive1.setFont(IAqtVar.font1);
		refreshScreen() ;
		tableViewerDR1.setResendEnabled(true);
		return container ;
	}

	private void refreshScreen() {
		EntityManager em = AqtMain.emf.createEntityManager();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		
		tempTrxList1 = new ArrayList<Ttranabbr>();
		AqtMain.container.setCursor(IAqtVar.busyc);
		
//		TypedQuery<Ttransaction> qTrx = em
//				.createQuery("select t from Ttransaction t where " + cond_str,
//						Ttransaction.class) ;
		Query query  = em.createNativeQuery(
				 "SELECT pkey , t.uuid, ifnull(t.msgcd,''),  ifnull(cast(t.rcvmsg as char(100)),''), ifnull(cast(t.errinfo as char(100)),''), " +
					" cast(ifnull(rdata,'') as char(150)) rdata,  t.rlen , t.rtime,  t.scrno, " +
					" cast(sdata as char(150)) sdata, t.sflag, t.slen ,t.stime," +
					" t.svrnm, t.svcid, t.userid,  t.svctime, t.tcode " +
				 "FROM 	ttransaction t where " + cond_str 
										) ;
		List<Object[]> resultList = query.getResultList();
		tempTrxList1 = resultList.stream().map( (r) -> 
		    new Ttranabbr((int)(long)r[0], r[1].toString(), r[2].toString(), r[3].toString(),
		    		r[4].toString(), r[5].toString(), (int)(long)r[6], Timestamp.valueOf(r[7].toString()), 
		    		r[8].toString(), r[9].toString(), r[10].toString(), (int)(long)r[11], 
		    		Timestamp.valueOf(r[12].toString()), r[13].toString(), r[14].toString(), 
		    		r[15].toString(), (double)r[16], r[17].toString()) 
				)
				.collect(Collectors.toCollection(ArrayList::new));
//		tempTrxList1 = qTrx.getResultList();

		txtSend1.setText("");
		txtReceive1.setText("");

		/* 상단 서비스정보 및 화면 정보의 정확한 정의가 필요함 -> 추후 수정 */
		if (!tempTrxList1.isEmpty()) {

			TypedQuery<Tservice> qSvc = em.createNamedQuery("Tservice.findById", Tservice.class);
			qSvc.setParameter("svcid", tempTrxList1.get(0).getSvcid());

			qSvc.getResultList();
			txtSend1.setText(tempTrxList1.get(0).getSdata());
			txtReceive1.setText(tempTrxList1.get(0).getRdata());
			tblDetailResult1.setSelection(0);

		}

		em.close();
		tableViewerDR1.setInput(tempTrxList1);
		AqtMain.container.setCursor(IAqtVar.arrow);

	}


}
