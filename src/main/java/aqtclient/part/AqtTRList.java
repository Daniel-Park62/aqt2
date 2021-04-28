package aqtclient.part;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Ttcppacket;

public class AqtTRList extends Dialog {

	private Table tblList;

	private Text txtReceive1;
	private Text txtSend1, txtcnt;
//	private CLabel lblRhead ;
	
	private int imax = 100 , itotal = -1, ipos = 0, istc = -1 ;
	private List<Ttcppacket> tempTrxList1 = new ArrayList<Ttcppacket>(); // testcode1 의 ttransaction
	private AqtTranTable tView;
	private String cond_str ;
	public AqtTRList(Shell parent, String cond_string) {
		super(parent) ; 
		setShellStyle(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.CLOSE | SWT.RESIZE | SWT.TITLE);
		this.cond_str = cond_string ;
	}

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        
        newShell.setText("Packet상세목록");
    }

    @Override
    protected Point getInitialSize() {
        return new Point(1600, 950);
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.ENTRY_FIELD_WIDTH, "<<첫페이지", false);
        createButton(parent, IDialogConstants.BACK_ID, "<이전페이지", false).setToolTipText(getMaxCnt() + " per page");
        createButton(parent, IDialogConstants.NEXT_ID, "다음페이지 >", false).setToolTipText(getMaxCnt() + " per page");
        createButton(parent, IDialogConstants.FINISH_ID, "끝페이지 >>", false);
        createButton(parent, IDialogConstants.SELECT_ALL_ID, "전체가져오기", false);
        createButton(parent, IDialogConstants.RETRY_ID, "새로고침", false);
        createButton(parent, IDialogConstants.CLOSE_ID, "Close", false);
    }
    
    private int getMaxCnt() {
		return imax;
	}
    
    public void setMaxCnt(int maxc) {
    	imax = maxc ;
    }
	@Override
    protected void buttonPressed ( final int buttonId )
    {
    	
        if ( buttonId == IDialogConstants.SELECT_ALL_ID ) {
        	ipos = 0 ;
        	setMaxCnt(0);
        	refreshScreen();
        } else if ( buttonId == IDialogConstants.ENTRY_FIELD_WIDTH ) {
            	if (ipos <= 0 ) return;
            	ipos = 0 ;
            	refreshScreen();
        } else if ( buttonId == IDialogConstants.BACK_ID ) {
        	if (ipos <= 0 ) return;
        	ipos -= getMaxCnt() ;
        	refreshScreen();
        } else if ( buttonId == IDialogConstants.NEXT_ID ) {
        	if (ipos + getMaxCnt() > itotal) return;
        	ipos += getMaxCnt() ;
        	refreshScreen();
        } else if ( buttonId == IDialogConstants.FINISH_ID ) {
        	if (ipos + getMaxCnt() > itotal) return;
        	ipos = getMaxCnt() == 0 ? 0 : (itotal / getMaxCnt()) * getMaxCnt()  ;
        	refreshScreen();
        } else if ( buttonId == IDialogConstants.CLOSE_ID )
            close ();
        else if ( buttonId == IDialogConstants.RETRY_ID ) {
        	itotal = 0;
        	tblList.notifyListeners(SWT.Resize, null);
        	refreshScreen();
        }
        	
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
		GridLayout gl_compCode1 = new GridLayout(4,false);
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
		Button btn_next = new Button(compCode1, SWT.PUSH );
		btn_next.setText("Next");
		btn_next.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String sval = txtFind.getText() ;
				if (sval.isEmpty() )  return ;
				TableItem[] tia = tblList.getItems() ;

				loop1 : for(int i=tblList.getSelectionIndex() ; i<tia.length ; i++) {
					for (int j=istc+1; j < tblList.getColumnCount(); j++)
						if ((tia[i].getText(j)).contains(sval)) {
							tblList.setSelection(i);
							istc = j ;
							break loop1;
						}
					istc = -1 ;
				}
				super.widgetSelected(e);
			}
		});
		GridDataFactory.fillDefaults().align(SWT.LEFT,SWT.CENTER) .grab(true, false).applyTo(btn_next);
		txtFind.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				arg0.doit = true ;
				istc = -1 ;
				btn_next.notifyListeners(SWT.Selection, null);
			}
		});
		
		
		txtcnt =  new Text(compCode1, SWT.BORDER) ;
		txtcnt.setText("0 건");
		txtcnt.setEditable(false);
		GridDataFactory.fillDefaults().align(SWT.RIGHT,SWT.TOP) .grab(true, false).applyTo(txtcnt);
		
		tView = new AqtTranTable(compCode1, SWT.NONE | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.MULTI);
		tblList = tView.getTable();
		tblList.addListener(SWT.Resize , (e) -> {
			AqtMain.aqtmain.setStatus(tblList.getItemHeight() + ":" + tblList.getSize().y);	
			if ( tblList.getSize().y > 0 ) {
				setMaxCnt( ( tblList.getSize().y - tblList.getHeaderHeight()) / tblList.getItemHeight()  );
				this.getButton(IDialogConstants.NEXT_ID).setToolTipText(getMaxCnt() + " per Page");
				this.getButton(IDialogConstants.BACK_ID).setToolTipText(getMaxCnt() + " per Page");
				if ( itotal == -1 )  refreshScreen();
			}
		});


		Menu pmenu = tblList.getMenu() ;
	    MenuItem delsvc = new MenuItem(pmenu, SWT.NONE);
	    delsvc.setText("삭제");
	    delsvc.setToolTipText("선택된 전문을 삭제합니다.\r\n복구는 불가능하며 원본으로부터 복제생성할 수 있습니다.");
	    delsvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				EntityManager em = AqtMain.emf.createEntityManager() ;
				StringBuffer del = new StringBuffer();
				em.getTransaction().begin();
				for ( TableItem item : tblList.getSelection() ) {
					Ttcppacket tr = (Ttcppacket)item.getData() ;
					tr = em.merge(tr) ;
					em.remove(tr);
					del.append(tr.getCmpid() + " ") ;
				}
				if ( del.length() == 0 ) { 
					em.close();
					return ;
				}
				del.append("\r\n 삭제하시겠습니까?") ;
				if ( MessageDialog.openQuestion(getParentShell(), "삭제", del.toString() ) ) {
					em.getTransaction().commit();
					refreshScreen(); 
				} else {
					em.getTransaction().rollback();
				}
				em.close();
			}
		});
	    delsvc.setEnabled(AqtMain.authtype == AuthType.TESTADM );
	    
	    MenuItem msetfirst = new MenuItem(pmenu, SWT.NONE);
	    msetfirst.setText("첫번째로 수행");
	    msetfirst.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		    	int i = tblList.getSelectionIndex() ;
		    	if (i<0) return ;
		    	if (! MessageDialog.openQuestion(getParentShell(),"확인","이 전문을 테스트 시 첫번째로 송신합니까?\r\n" + tblList.getItem(i).getText()) ) return ;
		    	
		    	Ttcppacket tr = (Ttcppacket)tblList.getItem(i).getData() ;
		    	EntityManager em = AqtMain.emf.createEntityManager() ;
		    	em.getTransaction().begin();
		    	em.createNativeQuery("update ttcppacket t, "
		    			+ "(SELECT DATE_ADD( MIN(o_stime), INTERVAL -1 SECOND) otime FROM ttcppacket WHERE tcode = ?) x"
		    			+ " set t.o_stime = x.otime where pkey = ?")
		    			.setParameter(1, tr.getTcode()).setParameter(2, tr.getPkey())
		    			.executeUpdate() ;
		    	em.getTransaction().commit();
		    	em.close();
		    	refreshScreen(); 
		    }
	    });
	    msetfirst.setEnabled(AqtMain.authtype == AuthType.TESTADM );
	    
	    tblList.setMenu(pmenu);
	    
		
		tblList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AqtMain.aqtmain.setStatus(tblList.getSelectionCount() + "건 선택됨");
				int i = tblList.getSelectionIndex() ;
				if (i<0) return ;
				Ttcppacket tr = ((Ttcppacket) tblList.getItem(i).getData()) ;
				txtSend1.setText(tr.getSdata());
//				lblRhead.setText(tempTrxList1.get(i).getRhead());
				txtReceive1.setText(tr.getRdatam());
			}
		});
		
		tblList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
		Label lblSend1 = new Label(compCode1, SWT.NONE);
		lblSend1.setText("SEND");
		lblSend1.setFont(IAqtVar.font1);
//		lblSend1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		txtSend1 = new Text(compCode1, SWT.BORDER);
		txtSend1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false,3,1));
		txtSend1.setEditable(false);
		txtSend1.setFont(IAqtVar.font1);

//		lblSend1 = new Label(compCode1, SWT.NONE);
//		lblSend1.setText("R-HEADER");
//		lblSend1.setFont(IAqtVar.font1);
//
//		lblRhead = new CLabel(compCode1, SWT.BORDER);
//		lblRhead.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false,2,1));
//		lblRhead.setFont(IAqtVar.font1);

		lblSend1 = new Label(compCode1, SWT.NONE);
		lblSend1.setText("RECEIVE");
		lblSend1.setFont(IAqtVar.font1);

		txtReceive1 = new Text(compCode1, SWT.BORDER);
		txtReceive1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false,3,1));
		txtReceive1.setEditable(false);
		txtReceive1.setFont(IAqtVar.font1);
		
//		refreshScreen() ;

		return container ;
	}

	private synchronized void refreshScreen() {
		EntityManager em = AqtMain.emf.createEntityManager();
//		em.clear();
//		em.getEntityManagerFactory().getCache().evictAll();
		
		tempTrxList1 = new ArrayList<Ttcppacket>();
		AqtMain.container.setCursor(IAqtVar.busyc);
		if (itotal <= 0) {
			itotal = Math.toIntExact(  em.createQuery("select count(1) from Ttcppacket t where " + cond_str, Long.class ).getSingleResult() );
			ipos = 0 ;
		} 
		TypedQuery<Ttcppacket> qTrx = em
				.createQuery("select t from Ttcppacket t where " + cond_str + "order by t.oStime",
						Ttcppacket.class) ;
		
//		System.out.println(ipos + ":" + getMaxCnt() );
		if ( getMaxCnt() > 0 ) {
			qTrx.setFirstResult(ipos);
			qTrx.setMaxResults(getMaxCnt());
		}
//		Query query  = em.createNativeQuery(
//				 "SELECT pkey , t.uuid, ifnull(t.msgcd,''),  ifnull(cast(t.rcvmsg as char(100)),''), ifnull(cast(t.errinfo as char(100)),''), " +
//					" cast(ifnull(rdata,'') as char(150)) rdata,  t.rlen , t.rtime,  t.scrno, " +
//					" cast(sdata as char(150)) sdata, t.sflag, t.slen ,t.stime," +
//					" t.svrnm, t.svcid, t.userid,  t.svctime, t.tcode " +
//				 "FROM 	ttransaction t where " + cond_str 
//										) ;
//		List<Object[]> resultList = query.getResultList();
//		tempTrxList1 = resultList.stream().map( (r) -> 
//		    new Ttcppacket((int)(long)r[0], r[1].toString(), r[2].toString(), r[3].toString(),
//		    		r[4].toString(), r[5].toString(), (int)(long)r[6], Timestamp.valueOf(r[7].toString()), 
//		    		r[8].toString(), r[9].toString(), r[10].toString(), (int)(long)r[11], 
//		    		Timestamp.valueOf(r[12].toString()), r[13].toString(), r[14].toString(), 
//		    		r[15].toString(), (double)r[16], r[17].toString()) 
//				)
//				.collect(Collectors.toCollection(ArrayList::new));
		tempTrxList1 = qTrx.getResultList();

		txtSend1.setText("");
		txtReceive1.setText("");
		if ( getMaxCnt() > 0)
			txtcnt.setText(String.format("총 %,d 건  ( %d / %d Page )", itotal, ipos / getMaxCnt() + 1, itotal / getMaxCnt() + 1));
		else 
			txtcnt.setText(String.format("총 %,d 건", itotal) );
		
		txtcnt.requestLayout();
		em.close();
		tView.setInput(tempTrxList1);
		
		if (!tempTrxList1.isEmpty()) {
			txtSend1.setText(tempTrxList1.get(0).getSdata());
			txtReceive1.setText(tempTrxList1.get(0).getRdatam());
			tblList.setSelection(0);
		}

		AqtMain.container.setCursor(IAqtVar.arrow);
		
	}


}
