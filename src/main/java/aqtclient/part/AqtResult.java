package aqtclient.part;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxisTick;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Tservice;
import aqtclient.model.Ttranabbr;
import aqtclient.model.Vtrxdetail;

public class AqtResult {

	private Text textService;
	private Text txtMsgcd;
	private Table tblResult;
	private Table tblDetailResult1;
	private Button btnfail ;

	private Text txtReceive1;
	private Text txtSend1;

	private AqtTcodeCombo cmbCode;

	private List<Ttranabbr> tempTrxList1 = new ArrayList<Ttranabbr>(); // testcode1 의 ttransaction
	private List<Vtrxdetail> tempTrxCompList; // 서비스별 트랜잭션 건수

	private TableViewer tableViewer;
	private AqtTranTable tableViewerDR1;
	Composite parent ;
	private Chart chart1;
	
	int gcol = 0 ;

	public AqtResult(Composite parent, int style) {
		this.parent = parent ;
		create(parent, style);
		AqtMain.cback = new IAqtSetCode() {
			@Override
			public void setTcode(String s) {
				cmbCode.findSelect(s + " :") ;
				tblResult.removeAll();
				if (tempTrxCompList != null) tempTrxCompList.clear();
				tblDetailResult1.removeAll();
			}
		};
		
	}

	private void create(Composite parent, int style) {
		SashForm sashForm;

		parent.setLayout(new FillLayout());

		sashForm = new SashForm(parent, SWT.VERTICAL);
//		sashForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		Composite compHeader = new Composite(sashForm, SWT.NONE);
		GridLayout headerLayout = new GridLayout(1, false);
		headerLayout.verticalSpacing = 5;
		headerLayout.marginTop = 20;
		headerLayout.marginBottom = 5;
		headerLayout.marginWidth = 15;
		compHeader.setLayout(headerLayout);

//		compHeader.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		Label ltitle = new Label(compHeader, SWT.NONE);
//		ltitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		ltitle.setImage(AqtMain.getMyimage("tit_result.png"));

		Composite compTitle = new Composite(compHeader, SWT.NONE);

//		compTitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		compTitle.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		GridLayoutFactory.fillDefaults().numColumns(10).equalWidth(false).applyTo(compTitle);

		Label lblTestCode1 = new Label(compTitle, SWT.NONE);
//		lblTestCode1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblTestCode1.setFont(IAqtVar.font1);
		lblTestCode1.setText("테스트코드");

		cmbCode = new AqtTcodeCombo(compTitle, SWT.READ_ONLY);
		cmbCode.getControl().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tblResult.removeAll();
				if (tempTrxCompList != null) tempTrxCompList.clear();
				tblDetailResult1.removeAll();
				
			}
		});

		Label lblService = new Label(compTitle, SWT.NONE);
//		lblService.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblService.setText("서비스ID");
		lblService.setFont(IAqtVar.font1);

		textService = new Text(compTitle, SWT.BORDER);
		textService.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		textService.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textService.setFont(IAqtVar.font1);
		// txtService.setEditable(false);
		textService.setText("");
		textService.addTraverseListener(new TraverseListener() {
		    @Override
		    public void keyTraversed(final TraverseEvent event)
		    {
		      if (event.detail == SWT.TRAVERSE_RETURN)
		        { 
		    	  refreshScreen();
		        }
		    }
		  });

		Label lblScreenNo = new Label(compTitle, SWT.NONE);
//		lblScreenNo.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblScreenNo.setText("메세지코드");
		lblScreenNo.setFont(IAqtVar.font1);

		txtMsgcd = new Text(compTitle, SWT.BORDER);
		txtMsgcd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtMsgcd.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtMsgcd.setFont(IAqtVar.font1);
		txtMsgcd.setText("");
		txtMsgcd.addTraverseListener(new TraverseListener() {
		    @Override
		    public void keyTraversed(final TraverseEvent event)
		    {
		      if (event.detail == SWT.TRAVERSE_RETURN)
		        { 
		    	  refreshScreen();
		        }
		    }
		  });
		
		btnfail = new Button(compTitle, SWT.CHECK | SWT.NONE) ;
		btnfail.setText("실패건만 보기");
		
		Label btnSearch = new Label(compTitle, SWT.NONE);
		btnSearch.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false));
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				refreshScreen();
			}
		});
		btnSearch.setCursor(IAqtVar.handc);
		btnSearch.setImage(AqtMain.getMyimage("search.png"));
//		btnSearch.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		new Label(compTitle, SWT.NONE);

//		Composite compResult = new Composite(sashForm, SWT.NONE);
//		compResult.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
//		compResult.setBounds(10, 80, 1055, 156);

		Composite compScArea = new Composite(compHeader, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(0, 5).applyTo(compScArea);
		compScArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

//		compScArea.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

//		Composite content = new Composite(compScArea, SWT.NONE);
//		
//		GridLayout gridLayout = new GridLayout(2, false);
//
//		//gridLayout.marginLeft = 10;
//	    gridLayout.verticalSpacing = 0;
//	    //gridLayout.marginLeft = 10;
//	    content.setLayout(gridLayout);
//	    content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//	    content.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		// First row table

		tableViewer = new TableViewer(compScArea, SWT.NONE | SWT.FULL_SELECTION);
		tblResult = tableViewer.getTable();

		tblResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tblResult.setHeaderVisible(true);
		tblResult.setLinesVisible(true);
		tblResult.setFont(IAqtVar.font1);
		tblResult.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tblResult.setHeaderBackground(AqtMain.htcol);
		tblResult.setHeaderForeground(AqtMain.forecol);
		tblResult.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int idx = tableViewer.getTable().getSelectionIndex();
				if (idx >= 0)
					tbl2data(cmbCode.getTcode(),
							tempTrxCompList.get(idx).getSvcid());
			}
		});
		

		tableViewer.setUseHashlookup(true);

		Point point = parent.getSize();
		int width = (point.x - 70) / 8;

		String[] columnNames1 = new String[] { "", "서비스", "서비스명", "화면번호", "누적건수", "처리건수", "평균시간", "정상건수", "실패건수" };

		int[] columnWidths1 = new int[] {
//   	         90, 310, 120/*, 110*/, 95, 95, 95, 95, 95, 95, 95, 95 };
				0, 180, 250, 120, 120 , 120, 120, 120, 120, 120,	120, 120, 120};

		int[] columnAlignments1 = new int[] { SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER,
				SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER };

		TableColumn tc;
		TableViewerColumn tvc;
		for (int i = 0; i < columnNames1.length; i++) {
			tvc = new TableViewerColumn(tableViewer, columnAlignments1[i]);

			tc = tvc.getColumn();
			tc.setText(columnNames1[i]);
			tc.setWidth(columnWidths1[i]);
		
		}
		
//       tblResult.addListener(SWT.Selection, new Listener() {
//    	      public void handleEvent(Event event) {
//    	        System.out.println(event.item + " " + ((Vtrxdetail)(event.item.getData())).getSvcid() );
//    	      }
//    	    });

		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setLabelProvider(new TrxRsltLabelProvider());
		
		Composite compCode1 = new Composite(sashForm, SWT.NONE);
		GridLayout gl_compCode1 = new GridLayout(2,false);
		gl_compCode1.verticalSpacing = 5;
		gl_compCode1.marginHeight = 10;
		gl_compCode1.marginWidth = 15;
		compCode1.setLayout(gl_compCode1);

		CLabel clb = new CLabel(compCode1, SWT.NONE ) ;
		clb.setText("Search:");
		Text txtFind = new Text(compCode1, SWT.BORDER) ;
		txtFind.setLayoutData(new GridData(400, -1));
		txtFind.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
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

		/*
		 * Label lblView1 = new Label(compCode1, SWT.NONE);
		 * 
		 * lblView1.setText("송수신전문"); lblView1.setFont(IAqtVar.font1);
		 * lblView1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		 * 
		 * lblView1 = new Label(compCode1, SWT.NONE); lblView1.setLayoutData(new
		 * GridData(SWT.RIGHT, SWT.TOP, true, false));
		 * lblView1.setCursor(IAqtVar.handc);
		 * lblView1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		 * lblView1.addMouseListener(new MouseAdapter() {
		 * 
		 * @Override public void mouseDown(MouseEvent e) { if
		 * (tblDetailResult1.getSelectionIndex() < 0) { MessageBox messageBox = new
		 * MessageBox(parent.getShell(), SWT.ICON_INFORMATION);
		 * messageBox.setMessage("상세정보가 없습니다."); messageBox.open(); } else { AqtDetail
		 * aqtDetail = new AqtDetail(parent.getShell(), SWT.APPLICATION_MODAL |
		 * SWT.DIALOG_TRIM); if (!tempTrxList1.isEmpty()) {
		 * aqtDetail.setTrxList(tempTrxList1.get(tblDetailResult1.getSelectionIndex()));
		 * aqtDetail.open(); } } } });
		 * lblView1.setImage(AqtMain.getMyimage("detailview.png"));
		 */
		Label lblSend1 = new Label(compCode1, SWT.NONE);
		lblSend1.setText("SEND");
//		lblSend1.setFont(IAqtVar.font1);
//		lblSend1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		txtSend1 = new Text(compCode1, SWT.BORDER);
		txtSend1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtSend1.setEditable(false);
//		txtSend1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
//		txtSend1.setFont(IAqtVar.font1);

		Label lblReceive1 = new Label(compCode1, SWT.NONE);
		lblReceive1.setText("RECEIVE");
//		lblReceive1.setFont(IAqtVar.font1);
//		lblReceive1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		txtReceive1 = new Text(compCode1, SWT.BORDER);
		txtReceive1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtReceive1.setEditable(false);
//		txtReceive1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
//		txtReceive1.setFont(IAqtVar.font1);

		Composite compChart1 = new Composite(sashForm, SWT.BORDER);
//		compChart1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FillLayout fl_compChart1 = new FillLayout();
		fl_compChart1.marginHeight = 5;
		fl_compChart1.marginWidth = 20;
		compChart1.setLayout(fl_compChart1);

		chart1 = createChart(compChart1);
		sashForm.setWeights(new int[] { 30, 30, 30 });
		sashForm.setSashWidth(3);

	}

	public void refreshScreen() {
		EntityManager em = AqtMain.emf.createEntityManager();
		em.clear();
		String tcode = cmbCode.getTcode(); 
		String sfail = btnfail.getSelection() ? "and sflag='2' " : "";
		String ssvc  = textService.getText().trim().isEmpty() ? "": "and svcid like '" + textService.getText().trim() + "' ";
		String smsg  = txtMsgcd.getText().trim().isEmpty() ? "": "and msgcd like '" + txtMsgcd.getText().trim() + "' ";
//		String qstr = "SELECT v FROM Vtrxdetail v WHERE v.tcode = :tcode and v.svcid like :svcid and v.scrno like :scrno" ;
		String qstr = 
				"select uuid_short() pkey, a.tcode, a.svcid, a.scrno, ifnull(s.svckor,'') svckor, a.tcnt, a.avgt ,a.scnt ,a.fcnt, " + 
				" sum(tcnt) OVER (PARTITION BY a.svcid) cumcnt\r\n" + 
				"from   (\r\n" + 
				"select t.tcode, t.svcid, t.scrno, count(1) tcnt, avg(t.svctime) avgt, sum(case when t.sflag = '1' then 1 else 0 end) scnt\r\n" + 
				", sum(case when t.sflag = '2' then 1 else 0 end) fcnt\r\n" + 
 				"from   Ttransaction t, tmaster m where m.code = t.tcode and m.lvl > '0' and t.tcode = '" + tcode +"' " + sfail + ssvc + smsg +
				"group by t.tcode, t.svcid, t.scrno\r\n" + 
				") as a\r\n" + 
				"left outer join Tservice s on a.svcid = s.svcid " ;
				
		Query qTrxList = em.createNativeQuery(qstr, Vtrxdetail.class);
//		qTrxList.setParameter("tcode", tcode);
		
//		if (textService.getText().trim().isEmpty() )
//			qTrxList.setParameter("svcid", "%");
//		else
//			qTrxList.setParameter("svcid", textService.getText().trim());
//
//		if (txtMsgcd.getText().trim().isEmpty() )
//			qTrxList.setParameter("scrno", "%");
//		else
//			qTrxList.setParameter("scrno", txtMsgcd.getText().trim());
		
		tempTrxCompList = new ArrayList<Vtrxdetail>();

		tempTrxCompList = qTrxList.getResultList();

		em.close();
		tableViewerDR1.setResendEnabled (cmbCode.getTmaster() != null
					   && ! "3".equals(cmbCode.getTmaster().getLvl() )
				       && !cmbCode.getTmaster().getThost().isEmpty() );
		tableViewer.setInput(tempTrxCompList);
		tableViewer.getTable().select(0) ;
		
		if (tempTrxCompList.size() > 0)
			tbl2data(tcode ,tempTrxCompList.get(0).getSvcid());
		
	}

	private void tbl2data(String tcode, String svcid) {
		EntityManager em = AqtMain.emf.createEntityManager();
		em.clear();
		tempTrxList1 = new ArrayList<Ttranabbr>();

		String sfail = btnfail.getSelection() ? "and sflag='2' " : "";
//		TypedQuery<Ttranabbr> qTrx = em
//				.createQuery("select t from Ttranabbr t where t.tcode = :tcode and t.svcid = :svcid",
//						Ttranabbr.class)
//				.setParameter("tcode", tcode).setParameter("svcid", svcid);
//
//		tempTrxList1 = qTrx.getResultList();
		Query query  = em.createNativeQuery(
				 "SELECT pkey , t.uuid, ifnull(t.msgcd,''),  ifnull(cast(t.rcvmsg as char(100)),''), ifnull(cast(t.errinfo as char(100)),''), " +
					" ifnull(convert(rdata using euckr),'') rdata,  t.rlen , t.rtime,  t.scrno, " +
					" cast(sdata as char(150)) sdata, t.sflag, t.slen ,t.stime," +
					" t.svrnm, t.svcid, t.userid,  t.svctime, t.tcode " +
				 "FROM 	ttransaction t where t.tcode = '" + tcode + "' and svcid = '" + svcid + "' " +
					sfail) ;

		List<Object[]> resultList = query.getResultList();
		tempTrxList1 = resultList.stream().map( (r) -> 
		    new Ttranabbr((int)(long)r[0], r[1].toString(), r[2].toString(), r[3].toString(),
		    		r[4].toString(), r[5].toString(), (int)(long)r[6], Timestamp.valueOf(r[7].toString()), 
		    		r[8].toString(), r[9].toString(), r[10].toString(), (int)(long)r[11], 
		    		Timestamp.valueOf(r[12].toString()), r[13].toString(), r[14].toString(), 
		    		r[15].toString(), (double)r[16], r[17].toString()) 
				)
				.collect(Collectors.toCollection(ArrayList::new));
		
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
		redrawChart(tempTrxList1, chart1);

	}

	SimpleDateFormat dformat = new SimpleDateFormat("HH:mm.ss");
	SimpleDateFormat dtfmt = new SimpleDateFormat("yy/MM/dd HH:mm.ss");

	public Chart createChart(Composite parent) {

		// create a chart
		Chart chart = new Chart(parent, SWT.NONE);
		chart.setCursor(IAqtVar.cross);
		chart.getPlotArea().addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent arg0) {
				
				Date x = new Date((long) chart.getAxisSet().getXAxis(0).getDataCoordinate(arg0.x));
				double y = chart.getAxisSet().getYAxis(0).getDataCoordinate(arg0.y);
				try {
					chart.getPlotArea().setToolTipText(dtfmt.format(x) + "\n 응답시간:" + String.format("%.3f", y));
				} catch (Exception arg1) {
					// TODO: handle exception
				}
			}
		});
		Date[] xSeries = { new Date("11/27/2019 10:00"), new Date("11/27/2019 10:00") };
		double[] ySeries = {1.0, 2.0} ;

		chart.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		// set titles
		chart.getTitle().setText("시간대별 TR 현황");
		chart.getAxisSet().getXAxis(0).getTitle().setText("수행시간");
		chart.getAxisSet().getYAxis(0).getTitle().setText("응답시간");

		// create line series
		ILineSeries scatterSeries = (ILineSeries) chart.getSeriesSet().createSeries(SeriesType.LINE, "mychart");
		scatterSeries.setAntialias(SWT.ON);
		// set label visible
		scatterSeries.setSymbolColor(SWTResourceManager.getColor(SWT.COLOR_RED));
		scatterSeries.setSymbolType(PlotSymbolType.DIAMOND);
		scatterSeries.setSymbolSize(2);
		scatterSeries.setXDateSeries(xSeries);
		scatterSeries.setYSeries(ySeries);

		IAxisTick xTick = chart.getAxisSet().getXAxis(0).getTick();
		xTick.setFormat(dformat);
		xTick.setTickMarkStepHint(10);

		scatterSeries.setLineStyle(LineStyle.NONE);

		chart.getLegend().setVisible(false);

		return chart;
	}

	public void redrawChart(List<Ttranabbr> tempTrxList, Chart chart) {

		Date[] xSeries = { new Date("11/27/2019 10:00"), new Date("11/27/2019 10:00") };
		double[] ySeries = {1.0, 2.0} ;
		chart.getTitle().setText("시간대별 TR 현황");
		if ( tempTrxList.size() > 0 ) {
			xSeries = tempTrxList.stream().map(a -> a.getStime()).toArray(Date[]::new);
			ySeries = tempTrxList.stream().mapToDouble(a -> a.getSvctime()).toArray();
			chart.getTitle().setText("시간대별 TR 현황 (" + tempTrxList.get(0).getSvcid() + ")" );
		} 

		chart.setRedraw(false);
		
		ILineSeries scatterSeries = (ILineSeries) chart.getSeriesSet().getSeries("mychart");
		scatterSeries.setXDateSeries(xSeries);
		scatterSeries.setYSeries(ySeries);

		chart.getAxisSet().adjustRange();
		chart.setRedraw(true);
	}

	private class ContentProvider implements IStructuredContentProvider {
		/**
		 * 
		 */
		@Override
		public Object[] getElements(Object input) {
			// return new Object[0];
			List<Ttranabbr> arrayList = (List<Ttranabbr>) input;
			return arrayList.toArray();
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	private  class TrxLabelProvider implements ITableLabelProvider {
		/**
		 * Returns the image
		 * 
		 * @param element     the element
		 * @param columnIndex the column index
		 * @return Image
		 */

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		/**
		 * Returns the column text
		 * 
		 * @param element     the element
		 * @param columnIndex the column index
		 * @return String
		 */
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

		public String getColumnText(Object element, int columnIndex) {
			Ttranabbr trx = (Ttranabbr) element;
			if (trx != null)
				switch (columnIndex) {
				case 0:
					return trx.getUuid();
				case 1:
					return fmt.format(trx.getStime());
				case 2:
					return fmt.format(trx.getRtime());
				case 3:
					return trx.getSflagNm() ; 
				case 4:
					return String.format("%.3f",trx.getSvctime());
				case 5:
					return (trx.getMsgcd() == null) ? "": trx.getMsgcd() ;
				case 6:
					return (trx.getRcvmsg() == null) ? "": trx.getRcvmsg() ;
				}
			
			return null;
		}

		/**
		 * Adds a listener
		 * 
		 * @param listener the listener
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
		 * Returns whether altering this property on this element will affect the label
		 * 
		 * @param element  the element
		 * @param property the property
		 * @return boolean
		 */
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/**
		 * Removes a listener
		 * 
		 * @param listener the listener
		 */
		public void removeListener(ILabelProviderListener listener) {
			// Ignore
		}
	}

	private static class TrxRsltLabelProvider implements ITableLabelProvider {
		/**
		 * Returns the image
		 * 
		 * @param element     the element
		 * @param columnIndex the column index
		 * @return Image
		 */

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		/**
		 * Returns the column text
		 * 
		 * @param element     the element
		 * @param columnIndex the column index
		 * @return String
		 */
		public String getColumnText(Object element, int columnIndex) {
//			  TrxResultList trx = (TrxResultList) element;
			Vtrxdetail trx = (Vtrxdetail) element;
			if (trx != null)
				switch (columnIndex) {
				case 1:
					return trx.getSvcid();
				case 2:
					return trx.getSvckor();
				case 3:
					return trx.getScrno();
				case 4:
					return String.format("%,d", trx.getCumcnt());
				case 5:
					return String.format("%,d", trx.getTcnt());
				case 6:
					return String.format("%.3f", trx.getAvgt());
				case 7:
					return String.format("%,d", trx.getScnt());
				case 8:
					return String.format("%,d", trx.getFcnt());
				}
			return "";
		}

		/**
		 * Adds a listener
		 * 
		 * @param listener the listener
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
		 * Returns whether altering this property on this element will affect the label
		 * 
		 * @param element  the element
		 * @param property the property
		 * @return boolean
		 */
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/**
		 * Removes a listener
		 * 
		 * @param listener the listener
		 */
		public void removeListener(ILabelProviderListener listener) {
			// Ignore
		}
	}

}
