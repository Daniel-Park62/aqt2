/*
 * 전문검색
*/

package aqtclient.part;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Ttcppacket;

public class AqtSearch2 {
	private Table tblList;
	private Text textsvc , textRcode, textCmpid , textSdata, textEtc;
	AqtButton btn ;
	private AqtTcodeCombo cmbCode;
	private Text txtReceive1;
	private Text txtSend1, txtcnt;

	private AqtTranTable tView;
	private Button benc0, benc1 ;
	private int imax = 100 ;
	private StringBuilder sbOStime  = new StringBuilder("");
	private StringBuilder sbtime2  = new StringBuilder("");
	private Spinner spn_min ;
	List<Ttcppacket> trListG = new ArrayList<Ttcppacket>() ;
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
	public AqtSearch2(Composite parent, int style) {

	    Composite container = new Composite(parent, SWT.NONE) ;
	    GridLayoutFactory.fillDefaults().margins(15, 15).numColumns(1).equalWidth(true).applyTo(container);
	    GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(container);
	    
		new AqtTitle(container, SWT.NONE, "전문검색(대용량)", "search_i.png");
		
		Label lbl = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		lbl.setBackground(container.getBackground());
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(lbl);

		Composite compTit = new Composite(container, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(11).equalWidth(false).applyTo(compTit);
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
		
//		cmbCode.getControl().add(" % : ALL");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).hint(160, -1).applyTo(cmbCode.getControl());
		
		lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" *서비스");
		lblt.setFont(IAqtVar.font1);

		textsvc = new Text(compTit, SWT.BORDER);
		
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).hint(100, -1).applyTo(textsvc);
		textsvc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textsvc.setFont(IAqtVar.font1);
		textsvc.setText("");

		lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" 응답코드");
		lblt.setFont(IAqtVar.font1);
		textRcode = new Text(compTit, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).hint(80, -1).applyTo(textRcode);
		textRcode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textRcode.setFont(IAqtVar.font1);
		textRcode.setText("");

		lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" 패킷ID");
		lblt.setFont(IAqtVar.font1);
		textCmpid = new Text(compTit, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).hint(120, -1).applyTo(textCmpid);
		textCmpid.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textCmpid.setFont(IAqtVar.font1);
		textCmpid.setText("");

		lblt = new Label(compTit, SWT.NONE|SWT.RIGHT);
		lblt.setText(" 송신데이터");
		lblt.setFont(IAqtVar.font1);
		textSdata = new Text(compTit, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).span(2, 1).grab(true, false).hint(200, -1).applyTo(textSdata);
		textSdata.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textSdata.setFont(IAqtVar.font1);
		textSdata.setText("");
		
		lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" 기타조건");
		lblt.setFont(IAqtVar.font1);
		lblt.setAlignment(SWT.RIGHT);
		lblt.setToolTipText("[수신ip] DSTIP\r[수신port] DSTPORT\r[원송신시간] O_STIME\r[수신데이터] RDATA\r[소요시간] SVCTIME\r[응답코드] RCODE") ;
		textEtc = new Text(compTit, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(5, 1).applyTo(textEtc);
		textEtc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textEtc.setFont(IAqtVar.font1);
		textEtc.setText(AqtMain.aqtmain.getCond("aqtsearch1") );

		lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" Encoding");
		lblt.setFont(IAqtVar.font1);
		lblt.setAlignment(SWT.RIGHT);

		Composite compo_enc = new Composite(compTit, SWT.BORDER ) ;
		RowLayoutFactory.fillDefaults().margins(20, 5).type(SWT.HORIZONTAL).spacing(10).applyTo(compo_enc);
		
		benc0 = new Button(compo_enc, SWT.RADIO);
		benc1 = new Button(compo_enc, SWT.RADIO);
		benc0.setText("UTF-8");
		benc1.setText("MS949");
		benc0.setSelection(AqtMain.tconfig.getEncval() == "UTF-8") ;
		benc1.setSelection(!benc0.getSelection());

		lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" 조회간격(분)");
		lblt.setFont(IAqtVar.font1);
		lblt.setAlignment(SWT.RIGHT);

		spn_min = new Spinner(compTit, SWT.BORDER | SWT.CENTER) ;
		spn_min.setMinimum(1) ;
		spn_min.setFont(IAqtVar.font1) ;
		spn_min.setSelection(2);

		AqtButton btnSearch = new AqtButton(compTit, SWT.PUSH,"조회");
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(true, false).minSize(100, -1).applyTo(btnSearch);
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				trListG = new ArrayList<Ttcppacket>() ;
				sbOStime.setLength(0);
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
//				txtReceive1.setText(tr.getRdataENCODE(AqtMain.tconfig.getEncval() ,250));
				txtReceive1.setText( tblList.getItem(i).getText(9) );
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
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(compfooter);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(compfooter);


		btn = new AqtButton(compfooter, SWT.PUSH,"다음페이지") ;
		btn.setEnabled(trListG.size() > 0) ;
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
	        	queryScr();
			}
		}) ;
		GridDataFactory.fillDefaults().align(SWT.RIGHT,SWT.TOP) .grab(true, false).applyTo(btn);

		txtcnt =  new Text(compfooter, SWT.NONE) ;
		txtcnt.setText("조회건수");
		txtcnt.setEditable(false);
		txtcnt.setFont(IAqtVar.font1) ;
		txtcnt.setForeground(btn.getForeground()) ;
		GridDataFactory.fillDefaults().align(SWT.END,SWT.CENTER) .grab(true, false).applyTo(txtcnt);

		
	}
	
	private void queryScr () {

		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		if (benc0.getSelection() ) tView.setSvEnc(benc0.getText());
		if (benc1.getSelection() ) tView.setSvEnc(benc1.getText());
		List<Ttcppacket> trList ; // = new ArrayList<Ttcppacket>();
		int imin = 2;
		try {
			imin = Integer.parseInt(spn_min.getText());
		} catch (Exception e) {
			imin = 2;
		}
		AqtMain.container.setCursor(IAqtVar.busyc);
		
		if ( sbOStime.length() == 0) {
			trList = em.createNativeQuery("select t.* from Ttcppacket t use index(tcode) where tcode = ? order by t.o_stime limit 1 ",
						Ttcppacket.class).setParameter(1, cmbCode.getTcode()).getResultList() ;
			if (trList.size() > 0)
			  sbOStime.append(trList.get(0).getOStime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.n")));
		}
		StringBuilder qstr = new StringBuilder("where t.tcode = ? ") ; 
		if ( sbOStime.length() > 0 )
			qstr.append(" and t.o_stime between '" + sbOStime + "' and cast('" + sbOStime + "' + interval " + imin + " minute as datetime)") ;
		if (! textsvc.getText().isEmpty()  ) 
			qstr.append(" and t.uri rlike '" + textsvc.getText().trim() + "'");
		if (! textRcode.getText().isEmpty()  ) 
			qstr.append(" and t.rcode = " + textRcode.getText().trim() );
		if (! textCmpid.getText().isEmpty()  ) 
			qstr.append(" and t.cmpid = " + textCmpid.getText().trim() );
		if (! textSdata.getText().isBlank() ) 
			qstr.append(" and t.sdata rlike '" + textSdata.getText().trim() + "'" );
		if (! textEtc.getText().isEmpty()  ) {
			qstr.append(" and (" + textEtc.getText().trim() + ")  " );
			AqtMain.aqtmain.setCond("aqtsearch1",textEtc.getText());
		}

 		Query qTrx = em
				.createNativeQuery("select t.* from Ttcppacket t use index(tcode)  " 
									+ qstr.toString() + " order by t.o_stime ",
						Ttcppacket.class).setParameter(1, cmbCode.getTcode()) ;
		
//		System.out.println(ipos + ":" + getMaxCnt() );
//		if ( getMaxCnt() > 0 ) {
//			qTrx.setFirstResult(ipos);
//			qTrx.setMaxResults(getMaxCnt());
//		}

		trList = qTrx.getResultList();

		trListG.addAll(trList) ;
		txtSend1.setText("");
		txtReceive1.setText("");
		
		txtcnt.setText(String.format("총 %,d 건", trListG.size() ) );
		txtcnt.requestLayout();
		btn.setEnabled(trListG.size() > 0) ;
		int ico = trListG.size() ;
		tView.setInput(trListG);
		
		if (!trListG.isEmpty()) {
			txtSend1.setText(trListG.get(ico-1).getSdata()) ;
			txtReceive1.setText(tblList.getItem(ico-1).getText(9));
			txtSend1.requestLayout();
			txtReceive1.requestLayout();
			tblList.setSelection(ico-1);
		}
		int ic = tblList.getItemCount() ;
		sbOStime.setLength(0) ;

		if (ic > 0) {
			sbOStime.append(trListG.get(ic -1).getOStime().plusNanos(imin*1000).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.n")));
		} 
		AqtMain.container.setCursor(IAqtVar.arrow);

//	    tblList.setSelection(0);
	    AqtMain.aqtmain.setStatus(String.format(">> 조회건수 %,d 건" ,trList.size() ) );
	    AqtMain.container.setCursor(IAqtVar.arrow);
	    

	}
	
}