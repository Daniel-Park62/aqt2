package aqtclient.part;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
/*
 * 총괄현황
*/
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Ttcppacket;
import aqtclient.model.Vtrxlist;

public class AqtStatus {
	private Table tblTestList;
	private AqtTableView tblViewerList;
	
	private CLabel[] lblstatus = new CLabel[12] ;
	private Composite compS;
	private List <Vtrxlist> tempVtrxList;
	
	private Timer jobScheduler ;
	ScheduledJob job ;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AqtStatus(Composite parent, int style) {
		create (parent, style);
		initScreen();
//		AqtMain.cback = new IAqtSetCode() {
//			@Override
//			public void setTcode(String s) {
//				initScreen();
//			}
//		};
	  job  = new ScheduledJob();
      jobScheduler = new Timer();
      jobScheduler.scheduleAtFixedRate(job, 180000, 180000);
	  AqtMain.jobScheduler = jobScheduler ;		

	}
	
	class ScheduledJob extends TimerTask {


	    // run is a abstract method that defines task performed at scheduled time.
	    public void run() {
			Display.getDefault().syncExec(new Runnable() {
			    public void run() {
			    	initScreen();
			    }
			});
	    }
	}


	private Composite create_pan(Composite parent) {
     	final Image img_pan = AqtMain.getMyimage("status_panel.png");
		// 전체 누적진척률
     	compS = new Composite(parent, SWT.NONE);
//     	compS.setBackground(parent.getBackground());
     	GridLayout glo = new GridLayout(2,true);
     	glo.marginWidth = 25;
     	glo.marginHeight = 20;
     	compS.setLayout(glo);
     	compS.setBackgroundImage(img_pan);
     	compS.setLayoutData(new GridData(265,185));

     	return compS ;
	}

	
	private CLabel status_data(Composite compS,  String nm,  String val ) {

		Label lbl = new Label(compS, SWT.NONE);
//     	lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
//     	lbl.setFont(IAqtVar.font1);
     	lbl.setFont(SWTResourceManager.getFont("맑은 고딕", 10, SWT.NORMAL));
     	lbl.setText(nm);
     	lbl.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true ));
     	
     	CLabel clbl = new CLabel(compS, SWT.NONE);
     	clbl.setFont(IAqtVar.font1b);
     	clbl.setText(val);
     	clbl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true ));
     	
     	return clbl ;

		
	}
	private void create (Composite parent, int style) {
//		SashForm sashForm;
//
//		parent.setLayout(new FillLayout());
//	    
//	    sashForm = new SashForm(parent, SWT.VERTICAL);
	    Composite container = new Composite(parent, SWT.NONE) ;
	    container.setLayout(new GridLayout(1, false));
	    GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(container);
//	    container.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
	    
		Composite compHeader = new Composite(container, SWT.NONE);
		GridLayout glo = new GridLayout(2,true);
		glo.marginHeight = 20;
		glo.marginWidth = 20;
		compHeader.setLayout(glo);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(compHeader);
		
//		compHeader.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Label ltitle = new Label(compHeader, SWT.NONE);
		
//    	ltitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    	ltitle.setImage(AqtMain.getMyimage("tit_status.png"));
    	ltitle = new Label(compHeader, SWT.NONE);
    	ltitle.setImage(AqtMain.getMyimage("refresh.png"));
    	ltitle.setCursor(IAqtVar.handc);
	    GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(true, true).applyTo(ltitle);

    	ltitle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				initScreen();
			}
		});
    	
    	Label lbl = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		lbl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		lbl.setBackground(container.getBackground());

     	Composite compStatus = new Composite(container, SWT.NONE);
//    	compStatus.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
    	
    	GridData gd_compStatus = new GridData(SWT.FILL, SWT.FILL, true, false);
    	compStatus.setLayoutData(gd_compStatus);

    	GridLayout gl_compStatus = new GridLayout(2, true);
    	gl_compStatus.marginHeight = 20;
    	gl_compStatus.marginWidth = 20;
    	gl_compStatus.horizontalSpacing = 35;
		compStatus.setLayout(gl_compStatus);
		
		lbl = new Label(compStatus, SWT.NONE);
		lbl.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		lbl.setText("단위테스트");
		lbl.setFont(IAqtVar.font15b);
		lbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		lbl.setBackground(compStatus.getBackground());
		
		lbl = new Label(compStatus, SWT.NONE);
		lbl.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		lbl.setText("통합테스트");
		lbl.setFont(IAqtVar.font15b);
		lbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		lbl.setBackground(compStatus.getBackground());
		
     	Composite compStatus1 = new Composite(compStatus, SWT.NONE);
//     	compStatus1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
     	glo = new GridLayout(2,false);
     	glo.horizontalSpacing = 20 ;
     	compStatus1.setLayout(glo);
     	compStatus1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
//     	compStatus1.setBackgroundMode(SWT.INHERIT_FORCE);
     	
		// 전체 누적진척률
     	Composite compS = create_pan(compStatus1);
     	
     	lbl = new Label(compS, SWT.NONE);
//     	lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
     	lbl.setFont(IAqtVar.font1b);
     	lbl.setText("전체누적 진척율");
     	lbl.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false,2,1));
     	
     	lblstatus[0] = new CLabel(compS, SWT.NONE);
     	lblstatus[0].setFont(IAqtVar.font22b);
     	lblstatus[0].setForeground(SWTResourceManager.getColor(106,64,255));
    	lblstatus[0].setText("90%");
    	lblstatus[0].setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true,2,1));

    	lblstatus[1] = status_data(compS,  "대상서비스", "10,000");
    	lblstatus[2] = status_data(compS,  "진행",       "9,000");
    	

    	// 최신테스트현황
     	compS = create_pan(compStatus1);

     	lbl = new Label(compS, SWT.NONE);
//     	lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
     	lbl.setFont(IAqtVar.font1b);
     	lbl.setText("최신 테스트현황");
     	lbl.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true,2,2));

     	lblstatus[3] = status_data(compS,  "수행건수",  "99,999");
     	lblstatus[4] = status_data(compS,  "성공건수",  "99,990");
     	lblstatus[5] = status_data(compS,  "성공률"  ,  "99%");

     	
     	compStatus1 = new Composite(compStatus, SWT.NONE);
//     	compStatus1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
     	glo = new GridLayout(2,true);
     	glo.horizontalSpacing = 30 ;
     	compStatus1.setLayout(glo);
     	compStatus1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
     	compStatus1.setBackgroundMode(SWT.INHERIT_FORCE);

		// 전체 누적진척률
     	compS = create_pan(compStatus1);
     	
     	lbl = new Label(compS, SWT.NONE);
//     	lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
     	lbl.setFont(IAqtVar.font1b);
     	lbl.setText("전체누적 진척율");
     	lbl.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false,2,1));
     	
     	lblstatus[6] = new CLabel(compS, SWT.NONE);
     	lblstatus[6].setFont(IAqtVar.font22b);
     	lblstatus[6].setForeground(SWTResourceManager.getColor(34,170,225));
    	lblstatus[6].setText("90%");
    	lblstatus[6].setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true,2,1));

    	lblstatus[7] = status_data(compS,  "대상서비스", "10,000");
    	lblstatus[8] = status_data(compS,  "진행",       "9,000");


    	// 최신테스트현황
     	compS = create_pan(compStatus1);

     	lbl = new Label(compS, SWT.NONE);
//     	lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
     	lbl.setFont(IAqtVar.font1b);
     	lbl.setText("최신 테스트현황");
     	lbl.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true,2,2));

     	lblstatus[9] = status_data(compS,  "수행건수",  "99,999");
     	lblstatus[10] = status_data(compS,  "성공건수",  "99,990");
     	lblstatus[11] = status_data(compS,  "성공률"  ,  "99%");

//    	for (int i = 0 ; i < lblstatus.length ; i++) {
//    		lblstatus[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
//    	}

     	
 	    Composite compDetail = new Composite(container, SWT.NONE);
//		compDetail.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
    	GridData gd_compDetail = new GridData(SWT.FILL, SWT.FILL, true, true);
    	//gd_compDetail.horizontalSpan = 50;
    	compDetail.setLayoutData(gd_compDetail);
    	GridLayout gl_compDetail = new GridLayout();
    	gl_compDetail.marginWidth = 20;
    	gl_compDetail.marginTop = 20;
    	gl_compDetail.numColumns = 1;
//    	gl_compDetail.verticalSpacing = 20;

    	compDetail.setLayout(gl_compDetail);
    	   	
    	tblViewerList = new AqtTableView(compDetail, SWT.NONE | SWT.FULL_SELECTION );
    	
    	tblTestList = tblViewerList.getTable();
	    tblTestList.setLayoutData(new GridData(GridData.FILL_BOTH));
	    
		tblTestList.setLinesVisible(true);
    	tblTestList.setHeaderBackground(AqtMain.htcol);
    	tblTestList.setHeaderForeground(AqtMain.forecol);
    	tblTestList.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
    	tblTestList.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    	
	    tblViewerList.setUseHashlookup(true);
	    tblTestList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int i = tblTestList.getSelectionIndex() ;
				if (i >= 0 && e.stateMask == SWT.CTRL && (e.keyCode == 'c' || e.keyCode == 'C')) {
					Clipboard clipboard = new Clipboard(Display.getDefault());
					String sdata =  "" ; // tblTestList.getItem(i).getText(gcol);
					
					for (int j = 0;j<tblTestList.getColumnCount(); j++) {
						sdata += tblTestList.getItem(i).getText(j) + "\t" ;
					}
					
					clipboard.setContents(new Object[] { sdata }, new Transfer[] { TextTransfer.getInstance() });
					clipboard.dispose();
				}
			}
		});
	    
        String[] columnNames1 = new String[] {
   	         "", "테스트ID", "  테스트명", "테스트일자", "단계", "대상호스트", "URI수", "패킷건수", "성공건수", "실패건수", "실패URI","성공율(%)","잔여건수"};

        int[] columnWidths1 = new int[] {
        		0, 100, 200, 150, 130, 130, 100, 100, 100, 100, 120,110,100};

	    int[] columnAlignments1 = new int[] {
	    		SWT.CENTER, SWT.LEFT, SWT.LEFT, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER};
	    TableViewerColumn tableViewerColumn ;
	     for (int i = 0; i < columnNames1.length; i++) {
	         tableViewerColumn =
	            new TableViewerColumn(tblViewerList, columnAlignments1[i]);
	         
	         TableColumn tableColumn = tableViewerColumn.getColumn();
	         tableColumn.setText(columnNames1[i]);
	         tableColumn.setWidth(columnWidths1[i]);
	         tableColumn.setResizable(i != 0);
	         
	     }
//	     tblTestList.getColumn(0).pack();

	    	tblTestList.addListener(SWT.MeasureItem ,  new Listener() {
				@Override
				public void handleEvent(Event arg0) {
					arg0.height = (int)(arg0.gc.getFontMetrics().getHeight() * 1.5);
					
				}
			});
	    	tblTestList.setHeaderVisible(true);
	    	
//	    	tblTestList.pack();		    
	     tblViewerList.setContentProvider(new ContentProvider());
	    tblViewerList.setLabelProvider(new VtrxLabelProvider());
	    tblTestList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				int i = tblTestList.getSelectionIndex() ;
				if (  i >= 0 ) {
					Vtrxlist vlist = (Vtrxlist) tblTestList.getItem(i).getData() ;
					AqtMain.openTrList("t.tcode = '"+ vlist.getCode() + "'" 
					+ " order by t.uri " ) ;
				}
			}

		});
		Menu popupMenu = tblTestList.getMenu();

		MenuItem flitem = new MenuItem(popupMenu, SWT.NONE);
		flitem.setText("실패건만 보기");
		flitem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				int i = tblTestList.getSelectionIndex() ;
				if (  i >= 0 ) {
					Vtrxlist vlist = (Vtrxlist) tblTestList.getItem(i).getData() ;
					AqtMain.openTrList("t.tcode = '"+ vlist.getCode() + "' and t.sflag = '2' " 
					+ " order by t.uri " ) ;
				}
				
			}
		});
		
		MenuItem scitem = new MenuItem(popupMenu, SWT.NONE);
		scitem.setText("성공건만 보기");
		scitem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				int i = tblTestList.getSelectionIndex() ;
				if (  i >= 0 ) {
					Vtrxlist vlist = (Vtrxlist) tblTestList.getItem(i).getData() ;
					AqtMain.openTrList("t.tcode = '"+ vlist.getCode() + "' and t.sflag = '1' " 
					+ " order by t.uri " ) ;
				}
				
			}
		});
	    
		tblTestList.setMenu(popupMenu);
	    /*
	    Button btn = new Button(compDetail, SWT.PUSH);
	    btn.setText("Off");
	    btn.setToolTipText("자동조회끄기");
	    GridDataFactory.fillDefaults().align(SWT.END, SWT.BOTTOM).grab(false, false).applyTo(btn);
	    btn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if ( btn.getText().equals("Off")) {
					jobScheduler.cancel();
					btn.setText("On");
					btn.setToolTipText("자동조회켜기");
				} else {
					job  = new ScheduledJob();
					jobScheduler = new Timer();
					jobScheduler.scheduleAtFixedRate(job, 120000, 180000);
					btn.setText("Off");
					btn.setToolTipText("자동조회끄기");
					
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    */
		
	}
	
	private void initScreen () {
		
		EntityManager em = AqtMain.emf.createEntityManager();
//	    tempVtrxList = new ArrayList<Vtrxlist>();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		AqtMain.container.setCursor(IAqtVar.busyc);
        int tcnt = em.createQuery("SELECT COUNT(a.svcid) FROM Tservice a" , Long.class).getSingleResult().intValue() ;
        lblstatus[1].setText(String.format("%,d", tcnt)) ;
        lblstatus[7].setText(String.format("%,d", tcnt)) ;

//        TypedQuery<Vtrxlist> qVlist = em.createQuery("select t from Vtrxlist t order by t.tdate desc ", Vtrxlist.class);
        tempVtrxList = em.createNamedQuery("Vtrxlist.findAll", Vtrxlist.class).getResultList();
//        tempVtrxList = em.createNativeQuery("SELECT  t.code, t.type, t.lvl, desc1, cmpCode, tdate, endDate, tdir, tuser, thost, tport, tenv," + 
//        		"		 l.svc_cnt, fsvc_cnt, data_cnt, scnt, fcnt, scnt * 100 / (scnt+fcnt) spct, tot_svccnt " + 
//        		"FROM tmaster t JOIN ttrxlist L ON t.code = L.tcode " +
//        		" left join " +
//        		" (select lvl, count(distinct svcid) tot_svccnt from  ttransaction join tmaster m " +
//        		"   on tcode = m.code AND lvl > '0' group by lvl) tot " +
//        		" on t.lvl = tot.lvl order by tdate desc"
//        		, Vtrxlist.class)
//        		.getResultList() ;
//        qVlist.getResultList().stream().forEach( t -> tempVtrxList.add(t));
        		 
	    tblViewerList.setInput(tempVtrxList);
	    
	    tblTestList.setSelection(0);

	    AqtMain.container.setCursor(IAqtVar.arrow);
	    
	    tblTestList.setFont(IAqtVar.font1b);
	    long icnt ;
	    if ( tempVtrxList.stream().filter( a -> "1".equals(a.getLvl() ) ).findAny().isPresent())
	    	icnt = tempVtrxList.stream().filter( a -> "1".equals(a.getLvl() ) ).findAny().get().getTotSvcCnt() ;
	    else
	    	icnt = 0;
	    
        lblstatus[2].setText(String.format("%,d", icnt)) ;
        
        lblstatus[0].setText(String.format("%.1f", icnt * 100.0 / tcnt) + "%") ;
        
	    long dcnt = tempVtrxList.stream().filter(a -> a.getLvl().equals("1") ).mapToLong(  a -> a.getScnt() + a.getFcnt() ).sum() ;
        lblstatus[3].setText(String.format("%,d", dcnt)) ;

	    int scnt = tempVtrxList.stream().filter(a -> a.getLvl().equals("1") ).flatMapToInt( a -> IntStream.of(a.getScnt().intValue()) ).sum() ;
	    int fcnt = tempVtrxList.stream().filter(a -> a.getLvl().equals("1") ).flatMapToInt( a -> IntStream.of(a.getFcnt().intValue()) ).sum() ;
        lblstatus[4].setText(String.format("%,d", scnt)) ;
        double spct = 0;
        try {
        	spct =  scnt * 100 / (scnt+fcnt) ;
		} catch (Exception e) {
			// TODO: handle exception
		}

        lblstatus[5].setText(String.format("%.2f", spct) + "%") ;
// 통합
        if ( tempVtrxList.stream().filter( a -> "2".equals(a.getLvl() ) ).findAny().isPresent() )
        	icnt = tempVtrxList.stream().filter( a -> "2".equals(a.getLvl() ) ).findAny().get().getTotSvcCnt().intValue() ;
        else
        	icnt = 0 ;
        
        lblstatus[8].setText(String.format("%,d", icnt)) ;

        lblstatus[6].setText(String.format("%.1f", icnt * 100.0 / tcnt) + "%") ;
        
	    dcnt = tempVtrxList.stream().filter(a -> a.getLvl().equals("2") ).mapToLong( a -> a.getDataCnt()  ).sum() ;
        lblstatus[9].setText(String.format("%,d", dcnt)) ;

	    scnt = tempVtrxList.stream().filter(a -> a.getLvl().equals("2") ).flatMapToInt( a -> IntStream.of(a.getScnt().intValue()) ).sum() ;
	    fcnt = tempVtrxList.stream().filter(a -> a.getLvl().equals("2") ).flatMapToInt( a -> IntStream.of(a.getFcnt().intValue()) ).sum() ;
        lblstatus[10].setText(String.format("%,d", scnt)) ;
        try {
        	spct =  scnt * 100 / (scnt+fcnt) ;
		} catch (Exception e) {
			// TODO: handle exception
		}
        lblstatus[11].setText(String.format("%.2f", spct) + "%") ;
//        lblstatus[11].setText( (dcnt > 0 ? String.format("%.2f", scnt * 100.0 / dcnt)  : 0.0) + "%" ) ;
        for (int i = 0 ; i < lblstatus.length ; i++) {
    		lblstatus[i].requestLayout();
    	}
        
        for( TableItem item : tblTestList.getItems() ) 
        {
            if (item.getText(4).equals("Origin"))
            {
                item.setBackground( SWTResourceManager.getColor(SWT.COLOR_YELLOW));
            }
        }

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
					  return String.format("%,d",trx.getSvcCnt());
				  case 7:
					  return String.format("%,d",trx.getDataCnt());
				  case 8:
					  return String.format("%,d",trx.getScnt());
				  case 9:
					  return String.format("%,d",trx.getFcnt());
				  case 10:
					  return String.format("%,d",trx.getFsvcCnt()) ;
				  case 11:
					  return String.format("%.2f",trx.getSpct()) ;
				  case 12:
					  return String.format("%,d",trx.getDataCnt() - trx.getScnt() - trx.getFcnt());
				  }
			  return "";
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

/*
create or replace view vtrxlist
as
select m.code, m.desc1, m.tdate, m.thost, count(distinct t.svcid) svc_cnt, count(t.uuid) data_cnt
, count(case when t.sflag = '1' then t.uuid else null end) scnt
, count(case when t.sflag = '2' then t.uuid else null end) fcnt
, case when count(t.uuid) = 0 then 0
else count(case when t.sflag = '1' then t.uuid else null end) / count(t.uuid) end * 100 spct
from tmaster m
left outer join ttransaction t on m.code = t.tcode
group by m.code, m.desc1, m.tdate, m.thost


create or replace view vtrxdetail as
select a.tcode, a.svcid, a.scrno, max(s.svckor) svckor, sum(a.tcnt) tcnt, sum(a.avgt) avgt, sum(a.scnt) scnt, sum(a.fcnt) fcnt 
from   (
select t.tcode, t.svcid, t.scrno, count(1) tcnt, avg(t.svctime) avgt, count(case when t.sflag = '1' then 1 else null end) scnt
, count(case when t.sflag = '2' then 1 else null end) fcnt
from   Ttransaction t
group by t.tcode, t.svcid, t.scrno
) as a
left outer join Tservice s on a.svcid = s.svcid
group  by a.tcode, a.svcid, a.scrno

*/