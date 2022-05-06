package aqtclient.part;

/*
   상세수행현황
*/
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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

import aqtclient.model.Vtrxdetail;
import aqtclient.model.Vtrxlist;

@SuppressWarnings("unchecked")
public class AqtList  {
	private Table tblTestList;
	private Table tblDetailList;
	private Text txtServiceCnt;
	private AqtTableView tblViewerList, tblViewerDetail;
	private long countResultT;
	
	private List <Vtrxlist> tempVtrxList;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AqtList(Composite parent, int style) {
		create (parent, style);
		AqtMain.container.setCursor(IAqtVar.busyc);
		
		initScreen();
		AqtMain.container.setCursor(IAqtVar.arrow);
	}
	
	private void create (Composite parent, int style) {
	    
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
//		Composite sashForm = new Composite(parent, SWT.NONE );
//		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(sashForm);
//		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).applyTo(sashForm);
	    
		Composite compHeader = new Composite(sashForm, SWT.BORDER);
		
		GridLayoutFactory.fillDefaults().margins(15, 10).numColumns(2).equalWidth(false).applyTo(compHeader);
    	GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, true).hint(-1, 300).applyTo(compHeader);
//		compHeader.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		Label ltitle = new Label(compHeader, SWT.NONE);
		
//    	ltitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    	ltitle.setImage(AqtMain.getMyimage("tit_list.png"));
    	ltitle.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

    	txtServiceCnt = new Text(compHeader, SWT.READ_ONLY );
//    	txtServiceCnt.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    	txtServiceCnt.setEnabled(false);
    	txtServiceCnt.setText("대상서비스수:");
    	txtServiceCnt.setFont(IAqtVar.font13b);
    	txtServiceCnt.setLayoutData(new GridData( SWT.RIGHT, SWT.TOP, true, false));

//    	Composite compTestList = new Composite(compHeader, SWT.BORDER);
//    	
//    	GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).span(2, 1).applyTo(compTestList);
//    	
//    	GridLayoutFactory.fillDefaults().equalWidth(false).numColumns(3).applyTo(compTestList);
    	
    	tblViewerList = new AqtTableView(compHeader, SWT.NONE | SWT.FULL_SELECTION);
    	
    	tblTestList = tblViewerList.getTable();
	    GridDataFactory.fillDefaults().span(2, 1).grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(tblTestList);

    	tblTestList.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			fillDetail();
    		}
    	});

    	tblTestList.setHeaderBackground(AqtMain.htcol);
    	tblTestList.setHeaderForeground(AqtMain.forecol);
    	tblTestList.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tblTestList.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));

        int width = 1500 / 10;

        String[] columnNames1 = new String[] {
   	         "","테스트ID", "테스트명", "테스트일자", "단계", "대상호스트", "서비스수", "패킷건수", "성공건수", "실패건수","실패서비스", "성공율(%)"};
        
        int[] columnWidths1 = new int[] {
//        		115, 350, 130, 130, 115, 115, 115, 110, 110};
        		0,180, 300, 150, 130,130, 100, 100, 100, 100, 100,100};

	    int[] columnAlignments1 = new int[] {
	    		SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER};
	      
		for (int i = 0; i < columnNames1.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tblViewerList, columnAlignments1[i]);

			TableColumn tableColumn = tableViewerColumn.getColumn();
			tableColumn.setText(columnNames1[i]);
			tableColumn.setWidth(columnWidths1[i]);
			tableColumn.setResizable(i != 0);

		}
	    
		tblTestList.setHeaderVisible(true);
		tblTestList.setLinesVisible(true);
		
		tblTestList.setFont(IAqtVar.font1b);

	    tblViewerList.setUseHashlookup(true);

	    tblViewerList.setContentProvider(new ContentProvider());
	    tblViewerList.setLabelProvider(new VtrxLabelProvider());
	    
	    tblTestList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				int i = tblTestList.getSelectionIndex() ;
				if (  i >= 0 ) {
					Vtrxlist vlist = (Vtrxlist) tblTestList.getItem(i).getData() ;
					AqtMain.openTrList("t.tcode = '"+ vlist.getCode() + "' and t.sflag = " 
					 + ( vlist.getFcnt() > 0  ?  "'2'" : "'1'") ) ;
				}
			}

		});

	    
	    Composite compDetail = new Composite(sashForm, SWT.NONE);
	    
		GridLayoutFactory.fillDefaults().margins(15, 5).numColumns(2).equalWidth(false).applyTo(compDetail);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(compDetail);
		
		CLabel clb1 = new CLabel(compDetail, SWT.NONE ) ;
		clb1.setText("Search:");
//		clb1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		Text txtFind1 = new Text(compDetail, SWT.BORDER) ;

		txtFind1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtFind1.setLayoutData(new GridData(400,-1));
		txtFind1.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				arg0.doit = true ;
				TableItem[] tia = tblDetailList.getItems() ;
				if (tia == null) return ;
				String sval = txtFind1.getText() ;
				if (sval.isEmpty() )  return ;
				
				loop1 : for(int i=0; i<tia.length ; i++) {
					for (int j=0; j < tblDetailList.getColumnCount(); j++)
						if ((tia[i].getText(j)).contains(sval)) {
							tblDetailList.setSelection(i);
							break loop1;
						}
				}
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				
			}
		});
	    
		tblViewerDetail = new AqtTableView(compDetail, SWT.BORDER | SWT.FULL_SELECTION);
		
		tblDetailList = tblViewerDetail.getTable();
		tblDetailList.setHeaderBackground(AqtMain.htcol);
		tblDetailList.setHeaderForeground(AqtMain.forecol);
		tblDetailList.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblDetailList.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).span(2, 1).applyTo(tblDetailList);

		sashForm.setWeights(new int[] {3,6});
		sashForm.setSashWidth(5);
		sashForm.requestLayout();
	
		width = 1500 / 8;
		
        String[] columnNames2 = new String[] {
        		"","서비스", "서비스명",  "누적건수", "패킷건수", "평균시간", "성공건수", "실패건수"};
        
        int[] columnWidths2 = new int[] {
//        		150, 480, 150, 130, 130, 130, 130};
        		0, 220, width + 200,  width-40, width-40, width-40, width-40, width-40};
        		
	    int[] columnAlignments2 = new int[] {
	    		SWT.CENTER,  SWT.LEFT, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER};
	      
	     for (int i = 0; i < columnNames2.length; i++) {
	         TableViewerColumn tableViewerColumn =
	            new TableViewerColumn(tblViewerDetail, columnAlignments2[i]);
	         
	         TableColumn tableColumn = tableViewerColumn.getColumn();
	         tableColumn.setText(columnNames2[i]);
	         tableColumn.setWidth(columnWidths2[i]);
	         tableColumn.setResizable(i != 0);
	     }

	    tblDetailList.setHeaderVisible(true);
	    tblDetailList.setLinesVisible(true);
	    tblDetailList.setFont(IAqtVar.font1);
		
	    tblViewerDetail.setUseHashlookup(true);
	    
		tblViewerDetail.setContentProvider(new ContentProvider());
		tblViewerDetail.setLabelProvider(new TrxDtlLabelProvider());
		tblDetailList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				int i = tblDetailList.getSelectionIndex() ;
				if (  i >= 0 ) {
					Vtrxdetail vlist = (Vtrxdetail) tblDetailList.getItem(i).getData() ;
					AqtMain.openTrList("t.tcode = '"+ vlist.getTcode() 
					   + (vlist.getSvcid().equals("총계") ? "'" : "' and t.uri = '" + vlist.getSvcid() + "'") ) ;
				}
			}

		});

		
//		compHeader.pack();
//		compTitle.pack();
//		ltitle.pack();
//		compDetail.pack();
		parent.requestLayout();
	}
	
	private void initScreen () {
	    EntityManager em = AqtMain.emf.createEntityManager();
	    tempVtrxList = new ArrayList<Vtrxlist>();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		
		/* Tservice 테이블의 전체 건수 */

//        Query query = em.createQuery("select count(t.svcid) from Tservice t ");
        Query query = em.createNamedQuery("Tservice.TotalCnt", Long.class);

        countResultT = (long)query.getSingleResult();
        
        txtServiceCnt.setText( String.format("대상서비스수: %,d ", countResultT));

//        TypedQuery<Vtrxlist> qVlist = em.createQuery("select t from Vtrxlist t order by t.tdate desc ", Vtrxlist.class);
        TypedQuery<Vtrxlist> qVlist = em.createNamedQuery("Vtrxlist.findAll", Vtrxlist.class);
    	
//        qVlist.getResultList().stream().forEach( t -> tempVtrxList.add(t));
        tempVtrxList = qVlist.getResultList() ;

	    tblViewerList.setInput(tempVtrxList);
	    
	    tblTestList.setSelection(0);
	    em.close();

        for( TableItem item : tblTestList.getItems() ) 
        {
            if (item.getText(4).equals("Origin"))
            {
                item.setBackground( SWTResourceManager.getColor(SWT.COLOR_YELLOW));
            }
        }

	    if ( tblTestList.getSelectionIndex() >= 0 )	    fillDetail();
		
	}
	
	private void fillDetail () {

	    EntityManager em = AqtMain.emf.createEntityManager();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		AqtMain.container.setCursor(IAqtVar.busyc);

//		TypedQuery <Vtrxdetail> qTrxList = em.createNamedQuery("Vtrxdetail.findByCode", Vtrxdetail.class);
//		qTrxList.setParameter("tcode", tempVtrxList.get(tblTestList.getSelectionIndex()).getCode());
//		List<Vtrxdetail> listtrx = qTrxList.getResultList() ;

		List<Vtrxdetail> listtrx = em.createNativeQuery(
				"select uuid_short()  pkey, a.tcode, a.svcid, ifnull(s.svckor,'') svckor, a.tcnt, a.avgt ,a.scnt ,a.fcnt, " +
				" s.cumcnt \n" + 
				"FROM   (" + 
				"select t.tcode, t.uri svcid,  dstip, dstport, count(1) tcnt, avg(t.svctime) avgt, sum(case when t.sflag = '1' then 1 else 0 end) scnt\r\n" + 
				", sum(case when t.sflag = '2' then 1 else 0 end) fcnt\r\n" + 
				"from   Ttcppacket t " + 
				"WHERE t.tcode = ? " + 
				"group by t.tcode, t.uri ) a " + 
				"left outer join tservice s on (a.svcid = s.svcid and uf_getapp(dstip,dstport) = s.appid) order by a.svcid " , Vtrxdetail.class)
				.setParameter(1, tempVtrxList.get(tblTestList.getSelectionIndex()).getCode())
			.getResultList() ;

		em.close();
		
		tblViewerDetail.setInput(listtrx);
		AqtMain.container.setCursor(IAqtVar.arrow);
//		AqtMain.aqtmain.setStatus(String.format(">> 조회건수 %,d 건" ,listtrx.size() ) );
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
			  Vtrxlist trx = (Vtrxlist) element;
			  if ( trx != null )
				  switch (columnIndex) {
				  case 1:
					  return trx.getCode();
				  case 2:
					  return trx.getDesc1();
				  case 3:
					  return trx.getTdate();
				  case 4:
					  return trx.getLvlNm();
				  case 5:
					  return trx.getThost();
				  case 6:
					  return String.format("%,d", trx.getSvcCnt() ) ;
				  case 7:
					  return String.format("%,d", trx.getDataCnt());
				  case 8:
					  return String.format("%,d", trx.getScnt());
				  case 9:
					  return String.format("%,d", trx.getFcnt());
				  case 10:
					  return String.format("%,d", trx.getFsvcCnt()) ;
				  case 11:
					  return String.format("%.2f",  trx.getSpct()) ;
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
		  public String getColumnText(Object element, int columnIndex) {
			  Vtrxdetail trx = (Vtrxdetail) element;
			  if ( trx != null )
				  switch (columnIndex) {
				  case 1:
					  return trx.getSvcid();
				  case 2:
					  return trx.getSvckor();
				  case 3:
					  return String.format("%,d", trx.getCumcnt() ) ;
				  case 4:
					  return String.format("%,d", trx.getTcnt() ) ;
				  case 5:
					  return String.format("%.3f", trx.getAvgt());
				  case 6:
					  return String.format("%,d", trx.getScnt() );
				  case 7:
					  return String.format("%,d", trx.getFcnt() );
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
