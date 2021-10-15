/*
 * 전문검색
*/

package aqtclient.part;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Tmaster;
import aqtclient.model.Trequest;
import aqtclient.model.Ttcppacket;

public class AqtSearch {
	private Table tblList;
	private Text textsvc , textRcode, textCmpid , textSdata, textEtc;
	
	private AqtTcodeCombo cmbCode;
	private Text txtReceive1;
	private Text txtSend1, txtcnt;

	private AqtTranTable tView;
	private int imax = 100 , itotal = -1, ipos = 0  ;
	private List<Ttcppacket> trList = new ArrayList<Ttcppacket>();
	
	EntityManager em = AqtMain.emf.createEntityManager();

    private int getMaxCnt() {
		return imax;
	}
    
    public void setMaxCnt(int maxc) {
    	imax = maxc ;
    }

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AqtSearch(Composite parent, int style) {

	    Composite container = new Composite(parent, SWT.NONE) ;
	    GridLayoutFactory.fillDefaults().margins(15, 15).numColumns(1).equalWidth(true).applyTo(container);
	    GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(container);
	    
		new AqtTitle(container, SWT.NONE, "전문검색", "search_i.png");
		
		Label lbl = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		lbl.setBackground(container.getBackground());
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(lbl);

		Composite compTit = new Composite(container, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(10).equalWidth(false).applyTo(compTit);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(compTit);
		
		Label lblt = new Label(compTit, SWT.NONE);
		lblt.setText("*테스트ID");
		lblt.setFont(IAqtVar.font1);

		cmbCode = new AqtTcodeCombo(compTit, SWT.READ_ONLY | SWT.FLAT);
		cmbCode.getControl().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tblList.removeAll();
			}
		});
		
		cmbCode.getControl().add(" % : ALL");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).applyTo(cmbCode.getControl());
		
		lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" *서비스");
		lblt.setFont(IAqtVar.font1);

		textsvc = new Text(compTit, SWT.BORDER);
		
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).hint(300, -1).applyTo(textsvc);
		textsvc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textsvc.setFont(IAqtVar.font1);
		textsvc.setText("");
		textsvc.addTraverseListener((final TraverseEvent event) -> {
		      if (event.detail == SWT.TRAVERSE_RETURN)	{ itotal = -1;  queryScr(); }
		  });

		lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" 응답코드");
		lblt.setFont(IAqtVar.font1);
		textRcode = new Text(compTit, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).hint(100, -1).applyTo(textRcode);
		textRcode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textRcode.setFont(IAqtVar.font1);
		textRcode.setText("");
		textRcode.addTraverseListener((final TraverseEvent event) -> {
		      if (event.detail == SWT.TRAVERSE_RETURN)	{ itotal = -1;  queryScr(); }
		  });

		lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" 패킷ID");
		lblt.setFont(IAqtVar.font1);
		textCmpid = new Text(compTit, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).hint(200, -1).applyTo(textCmpid);
		textCmpid.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textCmpid.setFont(IAqtVar.font1);
		textCmpid.setText("");
		textCmpid.addTraverseListener((final TraverseEvent event) -> {
		      if (event.detail == SWT.TRAVERSE_RETURN)	{ itotal = -1;  queryScr(); }
		  });

		lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" 송신데이터");
		lblt.setFont(IAqtVar.font1);
		textSdata = new Text(compTit, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).hint(200, -1).applyTo(textSdata);
		textSdata.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textSdata.setFont(IAqtVar.font1);
		textSdata.setText("");
		textSdata.addTraverseListener((final TraverseEvent event) -> {
		      if (event.detail == SWT.TRAVERSE_RETURN)	{ itotal = -1;  queryScr(); }
		  });
		
		lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" 기타조건");
		lblt.setFont(IAqtVar.font1);
		textEtc = new Text(compTit, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(7, 1).applyTo(textEtc);
		textEtc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textEtc.setFont(IAqtVar.font1);
		textEtc.setText("");
		textEtc.addTraverseListener((final TraverseEvent event) -> {
		      if (event.detail == SWT.TRAVERSE_RETURN)	{ itotal = -1;  queryScr(); }
		  });
		
		AqtButton btnSearch = new AqtButton(compTit, SWT.PUSH,"조회");
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(true, false).span(2, 1).minSize(100, -1).applyTo(btnSearch);
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				itotal = -1;
				queryScr();
			}
		});
		
// 	    Composite compDetail = new Composite(container, SWT.NONE);
// 	    GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(compDetail);
//		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(true).applyTo(compDetail);

		tView = new AqtTranTable(container, SWT.NONE | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.MULTI);
		tblList = tView.getTable();
		tblList.addListener(SWT.Resize , (e) -> {
			AqtMain.aqtmain.setStatus(tblList.getItemHeight() + ":" + tblList.getSize().y);	
			if ( tblList.getSize().y > 0 ) {
				setMaxCnt( ( tblList.getSize().y - tblList.getHeaderHeight()) / tblList.getItemHeight()  );
//				if ( itotal == -1 )  queryScr();
			}
		});


		Menu pmenu = tblList.getMenu() ;
	    MenuItem delsvc = new MenuItem(pmenu, SWT.NONE);
	    delsvc.setText("삭제");
	    delsvc.setToolTipText("선택된 전문을 삭제합니다.\r\n복구는 불가능하며 원본으로부터 복제생성할 수 있습니다.");
	    delsvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				StringBuffer del = new StringBuffer();
				StringBuffer dtcode = new StringBuffer();
				int dcnt = 0 ;
				
				em.getTransaction().begin();
				for ( TableItem item : tblList.getSelection() ) {
					Ttcppacket tr = (Ttcppacket)item.getData() ;
					tr = em.merge(tr) ;
					em.remove(tr);
					if (dtcode.length() < 1) dtcode.append(tr.getTcode()) ;
					if ( ++dcnt < 11 ) 	del.append(tr.getCmpid() + " ") ;
				}
				if ( del.length() == 0 ) { 
					return ;
				}
				if ( dcnt > 10) del.append(String.format("외 %d건 ", dcnt - 10 )) ;
				del.append("\r\n 삭제하시겠습니까?") ;
				if ( MessageDialog.openQuestion(AqtMain.aqtmain.getShell(), "삭제", del.toString() ) ) {
					em.createNativeQuery("call sp_summary(?)")
					.setParameter(1, dtcode.toString() )
					.executeUpdate() ;
					em.getTransaction().commit();
					queryScr(); 
				} else {
					em.getTransaction().rollback();
				}
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
		    	if (! MessageDialog.openQuestion(AqtMain.aqtmain.getShell(),"확인","이 전문을 테스트 시 첫번째로 송신합니까?\r\n" + tblList.getItem(i).getText()) ) return ;
		    	
		    	Ttcppacket tr = (Ttcppacket)tblList.getItem(i).getData() ;
		    	em.getTransaction().begin();
		    	em.createNativeQuery("update ttcppacket t, "
		    			+ "(SELECT DATE_ADD( MIN(o_stime), INTERVAL -1 SECOND) otime FROM ttcppacket WHERE tcode = ?) x"
		    			+ " set t.o_stime = x.otime where pkey = ?")
		    			.setParameter(1, tr.getTcode()).setParameter(2, tr.getPkey())
		    			.executeUpdate() ;
		    	em.getTransaction().commit();
		    	queryScr(); 
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
		
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(tblList) ;
		
		Composite compCode1 = new Composite(container, SWT.NONE);
		
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(compCode1) ;
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(compCode1) ;
		
		Label lblSend1 = new Label(compCode1, SWT.NONE);
		lblSend1.setText("SEND");
		lblSend1.setFont(IAqtVar.font1);
//		lblSend1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		txtSend1 = new Text(compCode1, SWT.BORDER);
		txtSend1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtSend1.setEditable(false);
		txtSend1.setFont(IAqtVar.font1);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(txtSend1) ;
		
		lblSend1 = new Label(compCode1, SWT.NONE);
		lblSend1.setText("RECEIVE");
		lblSend1.setFont(IAqtVar.font1);

		txtReceive1 = new Text(compCode1, SWT.BORDER);
		txtReceive1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtReceive1.setEditable(false);
		txtReceive1.setFont(IAqtVar.font1);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(txtReceive1) ;
		
		Composite compfooter = new Composite(container, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).applyTo(compfooter);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(compfooter);


		AqtButton btn = new AqtButton(compfooter, SWT.PUSH,"첫페이지") ;
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
            	if (ipos <= 0 ) return;
            	ipos = 0 ;
	        	queryScr();
			}
		}) ;
		GridDataFactory.fillDefaults().align(SWT.RIGHT,SWT.TOP) .grab(true, false).applyTo(btn);

		btn = new AqtButton(compfooter, SWT.PUSH,"이전페이지") ;
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
	        	if (ipos <= 0 ) return;
	        	ipos -= getMaxCnt() ;
	        	queryScr();
			}
		}) ;

		btn = new AqtButton(compfooter, SWT.PUSH,"다음페이지") ;
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
	        	if (ipos + getMaxCnt() > itotal) return;
	        	ipos += getMaxCnt() ;
	        	queryScr();
			}
		}) ;
		btn = new AqtButton(compfooter, SWT.PUSH,"끝페이지") ;
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
	        	if (ipos + getMaxCnt() > itotal) return;
	        	ipos = getMaxCnt() == 0 ? 0 : (itotal / getMaxCnt()) * getMaxCnt()  ;
	        	queryScr();
			}
		}) ;
		txtcnt =  new Text(compfooter, SWT.NONE) ;
		txtcnt.setText("조회건수");
		txtcnt.setEditable(false);
		txtcnt.setFont(IAqtVar.font1) ;
		txtcnt.setForeground(btn.getForeground()) ;
		GridDataFactory.fillDefaults().align(SWT.END,SWT.CENTER) .grab(true, false).applyTo(txtcnt);
		
		tView.reSendItem.removeListener(SWT.Selection, tView.reSendItem.getListeners(SWT.Selection)[0]) ;
	    tView.reSendItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if ( tblList.getSelectionIndex() >= 0) {
					boolean result = MessageDialog.openConfirm(parent.getShell(), "재전송",
							" 재전송 요청등록하시겠습니까?" ) ;
					if (  ! result ) return ;

					EntityManager em = AqtMain.emf.createEntityManager();
					em.clear();
					em.getEntityManagerFactory().getCache().evictAll();
					InetAddress local;
					String ip = "";
					try {
						local = InetAddress.getLocalHost();
						ip = local.getHostAddress();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					em.getTransaction().begin();
					int cnt = 0;
					for ( TableItem item : tblList.getSelection() ) {
						Ttcppacket tr = (Ttcppacket)item.getData() ;
						Tmaster tmst = tr.tmaster ;
	
						if ( tmst.getEndDate() != null ) {
							continue ;
						}
						if (tmst != null && ("3".equals(tmst.getLvl() ) || "0".equals(tmst.getLvl() ) ) ) {
							continue ;
						}
						Trequest treq = em.find(Trequest.class, tr.getPkey()) ;
						if (treq != null) {
							continue ;
						}
						cnt++ ;
						em.merge( new Trequest(tr.getPkey(), tr.getTcode(), tr.getCmpid(), ip ) );
					}
					em.getTransaction().commit();
					em.close();
					MessageDialog.openInformation(parent.getShell(), "Info", cnt + "건 재전송 요청되었습니다.");
				}
			}
		});

		
	}
	
	private void queryScr () {

		em.clear();
//		em.getEntityManagerFactory().getCache().evictAll();
		
		trList = new ArrayList<Ttcppacket>();
		AqtMain.container.setCursor(IAqtVar.busyc);
		StringBuilder qstr = new StringBuilder("where t.tcode like ? ") ; 
		if (! textsvc.getText().isEmpty()  ) 
			qstr.append(" and t.uri rlike '" + textsvc.getText().trim() + "'");
		if (! textRcode.getText().isEmpty()  ) 
			qstr.append(" and t.rcode = " + textRcode.getText().trim() );
		if (! textCmpid.getText().isEmpty()  ) 
			qstr.append(" and t.cmpid = " + textCmpid.getText().trim() );
		if (! textSdata.getText().isEmpty()  ) 
			qstr.append(" and instr(t.sdata, '" + textSdata.getText().trim() + "') > 0 " );
		if (! textEtc.getText().isEmpty()  ) 
			qstr.append(" and (" + textEtc.getText().trim() + ")  " );

		if (itotal <= 0) {
			itotal = Math.toIntExact(  (long) em.createNativeQuery("select count(1) from Ttcppacket t " + qstr.toString()  )
					.setParameter(1, cmbCode.getTcode())
					.getSingleResult() );
			ipos = 0 ;
		} 
		Query qTrx = em
				.createNativeQuery("select t.* from Ttcppacket t " + qstr.toString() + " order by t.o_Stime",
						Ttcppacket.class).setParameter(1, cmbCode.getTcode()) ;
		
//		System.out.println(ipos + ":" + getMaxCnt() );
		if ( getMaxCnt() > 0 ) {
			qTrx.setFirstResult(ipos);
			qTrx.setMaxResults(getMaxCnt());
		}

		trList = qTrx.getResultList();

		txtSend1.setText("");
		txtReceive1.setText("");
		if ( getMaxCnt() > 0)
			txtcnt.setText(String.format("총 %,d 건  ( %d / %d Page )", itotal, ipos / getMaxCnt() + 1, itotal / getMaxCnt() + 1));
		else 
			txtcnt.setText(String.format("총 %,d 건", itotal) );
		
		txtcnt.requestLayout();
		tView.setInput(trList);
		
		if (!trList.isEmpty()) {
			txtSend1.setText(trList.get(0).getSdata()) ;
			txtReceive1.setText(trList.get(0).getRdatam());
			txtSend1.requestLayout();
			txtReceive1.requestLayout();
			tblList.setSelection(0);
		}

		AqtMain.container.setCursor(IAqtVar.arrow);

	    tblList.setSelection(0);
	    AqtMain.aqtmain.setStatus(String.format(">> 조회건수 %,d 건" ,trList.size() ) );
	    AqtMain.container.setCursor(IAqtVar.arrow);
	    

	}
	
}