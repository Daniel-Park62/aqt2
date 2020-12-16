package aqtclient.part;

/*
   상세수행현황
*/
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
		SashForm sashForm;

//		parent.setLayout(new FillLayout());
	    
	    sashForm = new SashForm(parent, SWT.VERTICAL);
//	    sashForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
	    
		Composite compHeader = new Composite(sashForm, SWT.NONE);
		GridLayout gl_compHeader = new GridLayout(2,false);
		gl_compHeader.verticalSpacing = 10;
		gl_compHeader.marginTop = 20;
		gl_compHeader.marginWidth = 15;
		compHeader.setLayout(gl_compHeader);
		
//		compHeader.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		Label ltitle = new Label(compHeader, SWT.NONE);
		
//    	ltitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    	ltitle.setImage(SWTResourceManager.getImage("images/tit_list.png"));
    	ltitle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));

		Composite compTitle = new Composite(compHeader, SWT.LINE_DASH);
		
		compTitle.setLayoutData(new GridData( SWT.FILL , SWT.TOP, true, false));
		compTitle.setLayout(new GridLayout(2, false));
//		compTitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
    	Label lblServiceCnt = new Label(compTitle, SWT.NONE );
//    	lblServiceCnt.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    	lblServiceCnt.setText("대상서비스수 :");
    	lblServiceCnt.setFont(IAqtVar.font15b);
    	lblServiceCnt.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
    	lblServiceCnt.setLayoutData(new GridData( SWT.RIGHT, SWT.CENTER, true, true));

    	txtServiceCnt = new Text(compTitle, SWT.READ_ONLY );
//    	txtServiceCnt.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    	txtServiceCnt.setEnabled(false);
    	txtServiceCnt.setText("");
    	txtServiceCnt.setFont(IAqtVar.font15b);
    	txtServiceCnt.setLayoutData(new GridData( SWT.RIGHT, SWT.CENTER, false, true));
		
    	Composite compTestList = new Composite(compHeader, SWT.NONE);
//    	compTestList.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    	
    	GridData gd_compTestList = new GridData(SWT.FILL, SWT.FILL, true, true,2,1);
    	compTestList.setLayoutData(gd_compTestList);
    	
    	GridLayout gl_compTestList = new GridLayout(3, false);
//    	gl_compTestList.verticalSpacing = 10;
    	
    	compTestList.setLayout(gl_compTestList);
    	
    	
    	tblViewerList = new AqtTableView(compTestList, SWT.NONE | SWT.FULL_SELECTION);
    	
    	tblTestList = tblViewerList.getTable();
    	tblTestList.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			fillDetail();
    		}
    	});

    	tblTestList.setHeaderBackground(AqtMain.htcol);
    	tblTestList.setHeaderForeground(AqtMain.forecol);
    	tblTestList.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

        int width = 1500 / 10;

        String[] columnNames1 = new String[] {
   	         "","테스트ID", "테스트명", "테스트일자", "단계", "대상호스트", "서비스수", "전문건수", "성공건수", "실패건수","실패서비스", "성공율(%)"};
        
        int[] columnWidths1 = new int[] {
//        		115, 350, 130, 130, 115, 115, 115, 110, 110};
        		0,180, 300, 150, 130,130, 100, 100, 100, 100, 100,100};

	    int[] columnAlignments1 = new int[] {
	    		SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER};
	      
	     for (int i = 0; i < columnNames1.length; i++) {
	         TableViewerColumn tableViewerColumn =
	            new TableViewerColumn(tblViewerList, columnAlignments1[i]);
	         
	         TableColumn tableColumn = tableViewerColumn.getColumn();
	         tableColumn.setText(columnNames1[i]);
	         tableColumn.setWidth(columnWidths1[i]);
	         tableColumn.setResizable(i != 0);

	     }
	    
	    GridData gridTblData = new GridData(GridData.FILL_BOTH);
	    gridTblData.horizontalSpan = 3;
	    tblTestList.setLayoutData(gridTblData);

		tblTestList.setHeaderVisible(true);
		tblTestList.setLinesVisible(true);
		tblTestList.setFont(IAqtVar.font1);
//    	tblTestList.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));

	    tblTestList.setHeaderVisible(true);
	    tblViewerList.setUseHashlookup(true);

	    tblViewerList.setContentProvider(new ContentProvider());
	    tblViewerList.setLabelProvider(new VtrxLabelProvider());
	    tblTestList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				int i = tblTestList.getSelectionIndex() ;
				if (  i >= 0 ) {
					Vtrxlist vlist = (Vtrxlist) tblTestList.getItem(i).getData() ;
					AqtMain.openTrList("t.tcode = '"+ vlist.getCode() + "' and t.sflag = '" 
					 + ( vlist.getFcnt() > 0  ?  "2'" : "1") ) ;
				}
			}

		});


	    Composite compDetail = new Composite(sashForm, SWT.NONE);
		GridLayout dtlLayout = new GridLayout(1,false );
		dtlLayout.marginHeight = 10;
		dtlLayout.marginWidth = 20;
		compDetail.setLayout(dtlLayout);

		Composite compfind = new Composite(compDetail, SWT.NONE);
		compfind.setLayout( new GridLayout(2,false) );

		CLabel clb1 = new CLabel(compfind, SWT.NONE ) ;
		clb1.setText("Search:");
//		clb1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		Text txtFind1 = new Text(compfind, SWT.BORDER) ;

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
	    
	     
		tblViewerDetail = new AqtTableView(compDetail, SWT.NONE | SWT.FULL_SELECTION);
		
		tblDetailList = tblViewerDetail.getTable();
		tblDetailList.setHeaderBackground(AqtMain.htcol);
		tblDetailList.setHeaderForeground(AqtMain.forecol);
		tblDetailList.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tblDetailList.setLayoutData( new GridData(GridData.FILL_BOTH));

		sashForm.setWeights(new int[] {4,6});

		width = 1500 / 8;
		
        String[] columnNames2 = new String[] {
        		"","서비스", "서비스명", "화면번호", "누적건수", "처리건수", "평균시간", "정상건수", "실패건수"};
        
        int[] columnWidths2 = new int[] {
//        		150, 480, 150, 130, 130, 130, 130};
        		0, 220, width + 200, width-40, width-40, width-40, width-40, width-40, width-40};
        		
	    int[] columnAlignments2 = new int[] {
	    		SWT.CENTER, SWT.CENTER, SWT.LEFT, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER};
	      
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
		
	    tblDetailList.setHeaderVisible(true);

	    tblViewerDetail.setUseHashlookup(true);
	    
		tblViewerDetail.setContentProvider(new ContentProvider());
		tblViewerDetail.setLabelProvider(new TrxDtlLabelProvider());
		tblDetailList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				int i = tblDetailList.getSelectionIndex() ;
				if (  i >= 0 ) {
					Vtrxdetail vlist = (Vtrxdetail) tblDetailList.getItem(i).getData() ;
					AqtMain.openTrList("t.tcode = '"+ vlist.getTcode() + "' and t.svcid = '" + vlist.getSvcid() + "'") ;
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
	    tempVtrxList = new ArrayList<Vtrxlist>();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		
		/* Tservice 테이블의 전체 건수 */

//        Query query = em.createQuery("select count(t.svcid) from Tservice t ");
        Query query = em.createNamedQuery("Tservice.TotalCnt", Long.class);

        countResultT = (long)query.getSingleResult();
        
        txtServiceCnt.setText( String.format("%,d ", countResultT));

//        TypedQuery<Vtrxlist> qVlist = em.createQuery("select t from Vtrxlist t order by t.tdate desc ", Vtrxlist.class);
        TypedQuery<Vtrxlist> qVlist = em.createNamedQuery("Vtrxlist.findAll", Vtrxlist.class);
    	
//        qVlist.getResultList().stream().forEach( t -> tempVtrxList.add(t));
        tempVtrxList = qVlist.getResultList() ;

	    tblViewerList.setInput(tempVtrxList);
	    
	    tblTestList.setSelection(0);
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

		List<Vtrxdetail> listtrx = em.createNativeQuery("select uuid_short()  pkey, a.tcode, a.svcid, a.scrno, s.svckor svckor, a.tcnt, a.avgt ,a.scnt ,a.fcnt, ifnull(s.cumcnt,0) cumcnt " + 
				"FROM   ((" + 
				"select t.tcode, t.svcid, t.scrno, count(1) tcnt, avg(t.svctime) avgt, sum(case when t.sflag = '1' then 1 else 0 end) scnt\r\n" + 
				", sum(case when t.sflag = '2' then 1 else 0 end) fcnt\r\n" + 
				"from   Ttransaction t, tmaster m \r\n" + 
				"WHERE m.code = '" + tempVtrxList.get(tblTestList.getSelectionIndex()).getCode() + 
				"' and m.code = t.tcode and m.lvl > '0' " + 
				"group by t.tcode, t.svcid, t.scrno) a " + 
				"left outer join tservice s on a.svcid = s.svcid )" , Vtrxdetail.class)
			.getResultList() ;

		tblViewerDetail.setInput(listtrx);
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
					  return trx.getScrno();
				  case 4:
					  return String.format("%,d", trx.getCumcnt() ) ;
				  case 5:
					  return String.format("%,d", trx.getTcnt() ) ;
				  case 6:
					  return String.format("%.3f", trx.getAvgt());
				  case 7:
					  return String.format("%,d", trx.getScnt() );
				  case 8:
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
