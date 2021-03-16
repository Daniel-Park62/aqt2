package aqtclient.part;

/*
   상세수행현황( 업무별 )
*/
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Vtrxlist;

@SuppressWarnings("unchecked")
public class AqtListTask  {
	private Table tblLst;
	private Table tblDetail;
	private Text txtServiceCnt;
	private AqtTableView tblViewerList, tblViewerDetail;
	private long countResultT;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AqtListTask(Composite parent, int style) {
		create (parent, style);
		AqtMain.container.setCursor(IAqtVar.busyc);
		
		initScreen();
		AqtMain.container.setCursor(IAqtVar.arrow);
	}
	
	private void create (Composite parent, int style) {
		SashForm sashForm;

//		parent.setLayout(new FillLayout());
	    
	    sashForm = new SashForm(parent, SWT.VERTICAL);
	    
//	    sashForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
	    
		Composite compHeader = new Composite(sashForm, SWT.NONE);
    	GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(compHeader);
		
		GridLayoutFactory.fillDefaults().margins(15, 15).numColumns(2).equalWidth(false).applyTo(compHeader);
		
//		compHeader.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		Label ltitle = new Label(compHeader, SWT.NONE);
		
//    	ltitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    	ltitle.setImage(AqtMain.getMyimage("tit_listtask.png"));
    	ltitle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

    	txtServiceCnt = new Text(compHeader, SWT.READ_ONLY );
//    	txtServiceCnt.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    	txtServiceCnt.setEnabled(false);
    	txtServiceCnt.setText("대상서비스수:");
    	txtServiceCnt.setFont(IAqtVar.font13b);
    	txtServiceCnt.setLayoutData(new GridData( SWT.RIGHT, SWT.CENTER, true, true));

    	Composite compTestList = new Composite(compHeader, SWT.NONE);
//    	compTestList.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    	
    	GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).span(2, 1).applyTo(compTestList);
    	
    	GridLayoutFactory.fillDefaults().equalWidth(false).numColumns(3).applyTo(compTestList);
    	
    	tblViewerList = new AqtTableView(compTestList, SWT.NONE  | SWT.FULL_SELECTION);
    	
    	tblLst = tblViewerList.getTable();
    	tblLst.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
				int i = tblLst.getSelectionIndex() ;
				if (i >= 0) {
					String cond = tblLst.getItem(i).getText(8) ;
					fillDetail(cond);
				}
    		}
    	});

    	tblLst.setHeaderBackground(AqtMain.htcol);
    	tblLst.setHeaderForeground(AqtMain.forecol);
    	tblLst.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tblLst.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));

        int width = 1500 / 10;

        String[] columnNames1 = new String[] {
   	         " 업무명 ", "단계",  "서비스수", "패킷건수", "성공건수", "실패건수","실패서비스", "성공율(%)", ""};
        
        int[] columnWidths1 = new int[] { 180, 150, 130,130, 100, 100, 110, 110 ,0};

	    int[] columnAlignments1 = new int[] {
	    		SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER,SWT.CENTER,0};
	      
		for (int i = 0; i < columnNames1.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tblViewerList, columnAlignments1[i]);

			TableColumn tableColumn = tableViewerColumn.getColumn();
			tableColumn.setText(columnNames1[i]);
			tableColumn.setWidth(columnWidths1[i]);
			tableColumn.setResizable(i != 8);
		}
		
	    GridDataFactory.fillDefaults().span(2, 1).grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(tblLst);
	    
		tblLst.setHeaderVisible(true);
		tblLst.setLinesVisible(true);
		
		tblLst.setFont(IAqtVar.font1b);

	    tblViewerList.setUseHashlookup(true);

	    tblViewerList.setContentProvider(new ContentProvider());
	    tblViewerList.setLabelProvider(new VtrxLabelProvider());
	    
//	    tblLst.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseDoubleClick(MouseEvent arg0) {
//				int i = tblLst.getSelectionIndex() ;
//				if (i >= 0) {
//					String cond = tblLst.getItem(i).getText(8) ;
//					fillDetail(cond);
//				}
//			}
//
//		});

	    
	    Composite compDetail = new Composite(sashForm, SWT.NONE);
	    
		GridLayoutFactory.fillDefaults().margins(15, 5).numColumns(2).equalWidth(false).applyTo(compDetail);
		
		CLabel clb1 = new CLabel(compDetail, SWT.NONE ) ;
		clb1.setText("Search:");

		Text txtFind1 = new Text(compDetail, SWT.BORDER) ;

		txtFind1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtFind1.setLayoutData(new GridData(400,-1));
		txtFind1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				arg0.doit = true ;
				TableItem[] tia = tblDetail.getItems() ;
				if (tia == null) return ;
				String sval = txtFind1.getText() ;
				if (sval.isEmpty() )  return ;
				
				loop1 : for(int i=0; i<tia.length ; i++) {
					for (int j=0; j < tblDetail.getColumnCount(); j++)
						if ((tia[i].getText(j)).contains(sval)) {
							tblDetail.setSelection(i);
							break loop1;
						}
				}
			}
			
		});
	    
	    
		tblViewerDetail = new AqtTableView(compDetail, SWT.NONE | SWT.FULL_SELECTION);
		
		tblDetail = tblViewerDetail.getTable();
		tblDetail.setHeaderBackground(AqtMain.htcol);
		tblDetail.setHeaderForeground(AqtMain.forecol);
		tblDetail.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		tblDetail.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).span(2, 1).applyTo(tblDetail);

		sashForm.setWeights(new int[] {4,6});
		sashForm.setSashWidth(5);
		
		width = 1500 / 8;
		
        String[] columnNames2 = new String[] {
        		" 서비스", " 서비스명",  "패킷건수", "평균시간", "정상건수", "실패건수", "성공율(%)","미수행","" };
        
        int[] columnWidths2 = new int[] {
//        		150, 480, 150, 130, 130, 130, 130};
        		220, 240,  width-40, width-40, width-40, width-40, width-40, width-40, 0};
        		
	    int[] columnAlignments2 = new int[] {
	    		SWT.CENTER,  SWT.LEFT, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER,SWT.NONE};
	      
	     for (int i = 0; i < columnNames2.length; i++) {
	         TableViewerColumn tableViewerColumn =
	            new TableViewerColumn(tblViewerDetail, columnAlignments2[i]);
	         
	         TableColumn tableColumn = tableViewerColumn.getColumn();
	         tableColumn.setText(columnNames2[i]);
	         tableColumn.setWidth(columnWidths2[i]);
	         tableColumn.setResizable(i != columnNames2.length-1 );
	     }

	    tblDetail.setHeaderVisible(true);
	    tblDetail.setLinesVisible(true);
	    tblDetail.setFont(IAqtVar.font1);
		
	    tblViewerDetail.setUseHashlookup(true);
	    
		tblViewerDetail.setContentProvider(new ContentProvider());
		tblViewerDetail.setLabelProvider(new TrxDtlLabelProvider());
		tblDetail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				int i = tblDetail.getSelectionIndex() ;
				if (  i >= 0 ) {
					String val =  String.format("t.uri ='%s' and t.tmaster.lvl = '%s' " ,
							tblDetail.getItem(i).getText(0), tblDetail.getItem(i).getText(8) ) ;
					AqtMain.openTrList(val) ;
				}
			}

		});
//		parent.setRedraw(false);
//		sashForm.pack();
//		compHeader.pack();
//		compTitle.pack();
//		ltitle.pack();
//		compDetail.pack();
//		parent.setRedraw(true);
	}
	
	private void initScreen () {
	    EntityManager em = AqtMain.emf.createEntityManager();
	    List<Object[]> tList = new ArrayList<Object[]>();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		
		/* Tservice 테이블의 전체 건수 */

//        Query query = em.createQuery("select count(t.svcid) from Tservice t ");
        Query query = em.createNamedQuery("Tservice.TotalCnt", Long.class);

        countResultT = (long)query.getSingleResult();
        
        txtServiceCnt.setText( String.format("대상서비스수: %,d ", countResultT));

        tList = em.createNativeQuery(
        		"select task,lvl,svc_cnt,data_cnt, scnt, fcnt, fsvc_cnt, ifnull(scnt * 100 / (scnt+fcnt) ,0.0)  spct from ttasksum ")
        		.getResultList() ;

	    tblViewerList.setInput(tList);
	    tblLst.setSelection(0);
	    em.close();
        for( TableItem item : tblLst.getItems() ) 
        {
            if (item.getText(1).equals("Origin"))
            {
                item.setBackground( SWTResourceManager.getColor(SWT.COLOR_YELLOW));
            }
        }
	    
	    if ( tblLst.getSelectionIndex() >= 0 )	    fillDetail(tblLst.getItem(0).getText(8));
		
	}
	
	private void fillDetail (String acond ) {

	    EntityManager em = AqtMain.emf.createEntityManager();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		AqtMain.container.setCursor(IAqtVar.busyc);

//		TypedQuery <Vtrxdetail> qTrxList = em.createNamedQuery("Vtrxdetail.findByCode", Vtrxdetail.class);
//		qTrxList.setParameter("tcode", tempVtrxList.get(tblLst.getSelectionIndex()).getCode());
//		List<Vtrxdetail> listtrx = qTrxList.getResultList() ;

//		List<Vtrxdetail> tList = em.createNativeQuery(
//				"select uuid_short()  pkey, a.tcode, a.svcid, s.svckor svckor, a.tcnt, a.avgt ,a.scnt ,a.fcnt, " +
//				" sum(tcnt) OVER (PARTITION BY a.svcid) cumcnt\r\n" + 
//				"FROM   ((" + 
//				"select t.tcode, t.uri svcid,  count(1) tcnt, avg(t.svctime) avgt, sum(case when t.sflag = '1' then 1 else 0 end) scnt\r\n" + 
//				", sum(case when t.sflag = '2' then 1 else 0 end) fcnt\r\n" + 
//				"from   Ttcppacket t join tmaster m on (t.tcode = m.code) join tservice s on (t.uri = s.svcid) " + 
//				"WHERE  " + acond +  
//				" group by t.tcode, t.uri) a " + 
//				"left outer join tservice s on a.svcid = s.svcid )" , Vtrxdetail.class)
//			.getResultList() ;
		List<Object[]> tList = new ArrayList<Object[]>();
        tList = em.createNativeQuery(
				"select a.svcid, s.svckor svckor, a.tcnt, a.avgt ,cast(a.scnt as int) ,cast(a.fcnt as int), "
				+ "ifnull(scnt * 100 / (scnt+fcnt) ,0.0)  spct,cast( a.tcnt - (scnt+fcnt) as int), lvl " +
				"FROM   ((" + 
				"select  t.uri svcid, lvl,  count(1) tcnt, avg(t.svctime) avgt, sum(case when t.sflag = '1' then 1 else 0 end) scnt\r\n" + 
				", sum(case when t.sflag = '2' then 1 else 0 end) fcnt\r\n" + 
				"from   Ttcppacket t join tmaster m on (t.tcode = m.code) join tservice s on (t.uri = s.svcid) " + 
				"WHERE  " + acond +  
				" group by t.uri, lvl) a " + 
				"left outer join tservice s on a.svcid = s.svcid )"  )
        		.getResultList() ;
		
		
		em.close();
		
		tblViewerDetail.setInput(tList);
		AqtMain.container.setCursor(IAqtVar.arrow);
	}
	
	private static class ContentProvider implements IStructuredContentProvider {
		/**
		 * 
		 */
		@Override
		public Object[] getElements(Object input) {
			//return new Object[0];
			List<Vtrxlist> arrayList = (List<Vtrxlist>)input;
			return arrayList.toArray();
		}
		@Override
		public void dispose() {
		}
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	
	private static class VtrxLabelProvider implements ITableLabelProvider {
		
		
		  public Image getColumnImage(Object element, int columnIndex) {
		    return null;
		  }

		  /**
		   * Returns the column text
		   * 
		   * @param element
		   *            the element
		   * @param columnIndex
		   *            the column index
		   * @return String
		   */
		  public String getColumnText(Object element, int columnIndex) {
			  Object[] trx = (Object[]) element;
			  if ( trx != null )
				  
				  switch (columnIndex) {
				  case 0:
					  return trx[0].toString() ;
				  case 1:
					  return IAqtVar.lvlnm.get(trx[1]).toString() ;
				  case 2:
				  case 3:
				  case 4:
				  case 5:
				  case 6:
					  return String.format("%,d", trx[columnIndex] );
				  case 7:
					  return String.format("%.2f",  trx[7]) ;
				  case 8:
					  return String.format("task = '%s' and lvl = '%s'", trx[0].toString(), trx[1].toString()) ;
				  }
			  return null;
		  }

		  /**
		   * Adds a listener
		   * 
		   * @param listener
		   *            the listener
		   */
		  public void addListener(ILabelProviderListener listener) {
		    // Ignore it
		  }

		  /**
		   * Disposes any created resources
		   */
		  public void dispose() {
		    // Nothing to dispose
		  }

		  /**
		   * Returns whether altering this property on this element will affect the
		   * label
		   * 
		   * @param element
		   *            the element
		   * @param property
		   *            the property
		   * @return boolean
		   */
		  public boolean isLabelProperty(Object element, String property) {
		    return false;
		  }

		  /**
		   * Removes a listener
		   * 
		   * @param listener
		   *            the listener
		   */
		  public void removeListener(ILabelProviderListener listener) {
		    // Ignore
		  }
		}



	private static class TrxDtlLabelProvider implements ITableLabelProvider {
		  /**
		   * Returns the image
		   * 
		   * @param element
		   *            the element
		   * @param columnIndex
		   *            the column index
		   * @return Image
		   */
		
		  public Image getColumnImage(Object element, int columnIndex) {
		    return null;
		  }

		  /**
		   * Returns the column text
		   * 
		   * @param element
		   *            the element
		   * @param columnIndex
		   *            the column index
		   * @return String
		   */
		  public String getColumnText(Object element, int ci) {
			  Object[] trx = (Object[]) element;
			  if ( trx != null )
				  switch (ci) {
				  case 0:
				  case 1:
				  case 8:
					  return trx[ci].toString();
				  case 2:
					  return String.format("%,d", trx[ci] ) ;
				  case 3:
					  return String.format("%.3f", trx[ci] ) ;
				  case 4:
					  return String.format("%,d", trx[ci] ) ;
				  case 5:
					  return String.format("%,d", trx[ci] ) ;
				  case 6:
					  return String.format("%.2f", trx[ci] ) ;
				  case 7:
					  return String.format("%,d", trx[ci] ) ;
				  }
			  return null;
		  }

		  /**
		   * Adds a listener
		   * 
		   * @param listener
		   *            the listener
		   */
		  public void addListener(ILabelProviderListener listener) {
		    // Ignore it
		  }

		  /**
		   * Disposes any created resources
		   */
		  public void dispose() {
		    // Nothing to dispose
		  }

		  /**
		   * Returns whether altering this property on this element will affect the
		   * label
		   * 
		   * @param element
		   *            the element
		   * @param property
		   *            the property
		   * @return boolean
		   */
		  public boolean isLabelProperty(Object element, String property) {
		    return false;
		  }

		  /**
		   * Removes a listener
		   * 
		   * @param listener
		   *            the listener
		   */
		  public void removeListener(ILabelProviderListener listener) {
		    // Ignore
		  }
		}	
	
}
