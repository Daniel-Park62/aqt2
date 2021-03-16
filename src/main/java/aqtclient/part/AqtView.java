package aqtclient.part;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxisTick;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.ChartData;

public class AqtView {

	private Label textTstDt; // 테스트기준일자
	private Label textPrgrssRt; // 진척율
	private Label textTrxOccrCnt; // 트랜젝션발생건수
	private Label lblHost; // 대상호스트
	private Label textHost; // 대상호스트

	private AqtTcodeCombo cmbCode; // 코드리스트

	private Composite compChart;

	private Chart chart;
	private EntityManager em = AqtMain.emf.createEntityManager();
	private ILineSeries<?> lser0, lser1 ;

	public AqtView(Composite parent, int style) {
		create(parent, style);
		AqtMain.cback = new IAqtSetCode() {
			@Override
			public void setTcode(String s) {
				cmbCode.findSelect(s +" :") ;
			}
		};
	}

	private void create(Composite parent, int style) {

//		parent.setLayout(new GridLayout(1, false));
//		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);

		Composite mainform = new Composite(parent, SWT.NONE); 
		mainform.setLayout(new GridLayout(1, false));
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(mainform);
		
		Composite compHeader = new Composite(mainform, SWT.NONE);
		GridLayout headerLayout = new GridLayout(1, false);
		headerLayout.verticalSpacing = 20;
		headerLayout.marginTop = 20;
		headerLayout.marginBottom = 20;
		headerLayout.marginWidth = 15;
		compHeader.setLayout(headerLayout);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(compHeader);

		Label ltitle = new Label(compHeader, SWT.NONE);
//		ltitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		ltitle.setImage(AqtMain.getMyimage("tit_view.png"));
		ltitle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		Composite compIn = new Composite(compHeader, SWT.BORDER);
		GridData titleGridData = new GridData(SWT.FILL, SWT.TOP, true, false);

		compIn.setLayoutData(titleGridData);
		GridLayout glin = new GridLayout(7, false);
		glin.horizontalSpacing = 10;
		compIn.setLayout(glin);

		Label lbl = new Label(compIn, SWT.NONE);
		lbl.setText("테스트ID:");
		lbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		lbl.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER, false, false));
		lbl.setFont(IAqtVar.font1b);

		cmbCode = new AqtTcodeCombo(compIn,  SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		cmbCode.getControl().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshScreen();
			}
		});

		Label lblTstDt = new Label(compIn, SWT.NONE);
		lblTstDt.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		lblTstDt.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false));
		lblTstDt.setText("테스트기준일자:");
		lblTstDt.setFont(IAqtVar.font1b);

		textTstDt = new Label(compIn, SWT.NONE);
		textTstDt.setFont(IAqtVar.font1b);
		textTstDt.setText("YYYY-MM-DD  ");
		textTstDt.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER,false, false));
		textTstDt.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		

		lblHost = new Label(compIn, SWT.NONE);
		lblHost.setText("대상호스트:");
		lblHost.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		lblHost.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false, false));
		lblHost.setFont(IAqtVar.font1b);
		
		textHost = new Label(compIn, SWT.NONE);
		textHost.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		textHost.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER,true, false));
		textHost.setFont(IAqtVar.font1b);

		lbl = new Label(compIn, SWT.NONE);
		lbl.setImage(AqtMain.getMyimage("refresh.png"));
		lbl.setCursor(IAqtVar.handc);
		lbl.setLayoutData(new GridData(SWT.END,SWT.CENTER,true, false));
		lbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				refreshScreen();
			}
		});

		
		Label lblPrgrssRt = new Label(compIn, SWT.NONE);
		lblPrgrssRt.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));

		lblPrgrssRt.setText("전체누적진도율:");
		lblPrgrssRt.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,true, false));
		lblPrgrssRt.setFont(IAqtVar.font1b);

		textPrgrssRt = new Label(compIn, SWT.NONE);
		textPrgrssRt.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,1,1));
		textPrgrssRt.setFont(IAqtVar.font1);
		textPrgrssRt.setText("0.0% (0 / 0)");
		textPrgrssRt.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));

		Label lblTrxOccrCnt = new Label(compIn, SWT.NONE);
		lblTrxOccrCnt.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		lblTrxOccrCnt.setText("트랜잭션 건수:");
		lblTrxOccrCnt.setFont(IAqtVar.font1b);
		lblTrxOccrCnt.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,true, false));

		textTrxOccrCnt = new Label(compIn, SWT.NONE);
		textTrxOccrCnt.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,4,1));
		textTrxOccrCnt.setFont(IAqtVar.font1);
		textTrxOccrCnt.setText("% 0건 [정상: 0  실패: 0 성공율: 0%]" ) ;
		textTrxOccrCnt.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));

		compIn.pack();
		
		compChart = new Composite(mainform, SWT.NONE);
		compChart.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(compChart);
		FillLayout dtlLayout = new FillLayout();
		dtlLayout.marginHeight = 20;
		dtlLayout.marginWidth = 20;
		compChart.setLayout(dtlLayout);

//		Composite compChart = new Composite(composite, SWT.NONE);

//		compChart.setLayout(new FillLayout());
//		sashForm.setWeights(new int[] { 20, 80 });
		chart = createChart(compChart);

	}

	/* 데이터를 추출하여 화면에 보여준다. */
	public void refreshScreen() {

		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		SimpleDateFormat sfmt = new SimpleDateFormat("yyyy-MM-dd  ");
		String tcode = "" ;
		if (cmbCode.getItemCount() > 0) {
			tcode = cmbCode.getTcode() ;
			textTstDt.setText( sfmt.format( cmbCode.getTmaster().getTdate() ) );
			textHost.setText(cmbCode.getTmaster().getThost());
			textHost.requestLayout();
			AqtMain.aqtmain.setGtcode(tcode) ;
		}
		
		/* Tservice 테이블의 전체 건수 */
		long countResultT = (long) em.createNamedQuery("Tservice.TotalCnt").getSingleResult();

//		query = em.createQuery("select count(distinct t.svcid) from Ttcppacket t") ;
		Query query = em.createNamedQuery("Ttcppacket.SvcCnt", Integer.class).setParameter("tcode", tcode);

		long countResultS = (long) query.getSingleResult();

		String strRslt = String.format("%1.1f%% (%,d / %,d)", 
				countResultT == 0 ? 0 : countResultS * 100.0 / countResultT,  countResultS, countResultT ) ;

		textPrgrssRt.setText(strRslt);
		textPrgrssRt.requestLayout();

		/*
		 * query = em.createQuery("select count(t.uuid) trxCnt " +
		 * ", count(case when t.sflag = '1' then 1 else null end) validCnt " +
		 * ", count(case when t.sflag = '2' then 1 else null end) invalidCnt " +
		 * " from Ttcppacket t where t.tcode = :tcode") ;
		 */
		query = em.createNamedQuery("Ttcppacket.FlagCnt");

		query.setParameter("tcode", tcode);

		Object[] rst = (Object[]) query.getSingleResult();

//		TrxCount trxCnt = new TrxCount((long) rst[0], (long) rst[1], (long) rst[2]);
		long tcnt = (long) rst[0] , scnt =  (long) rst[1] , fcnt = (long) rst[2] ;

//		strRslt = NumberFormat.getInstance().format(trxCnt.getTrxCnt()) + " 건 ( 정상 : "
//				+ NumberFormat.getInstance().format(trxCnt.getValidCnt()) + " 건   성공률 "
//				+ String.format("%.2f",
//						trxCnt.getTrxCnt() == 0 ? 0.0 : trxCnt.getValidCnt() * 100.0 / trxCnt.getTrxCnt())
//				+ "%   실패 : " + NumberFormat.getInstance().format(trxCnt.getInvalidCnt()) + " 건 ) ";
		strRslt = String.format("%,d건 [정상: %,d  실패: %,d 성공율: %.2f%%]", tcnt, scnt, fcnt , scnt != 0 ? scnt*100.0 / scnt+fcnt : 0 );
		textTrxOccrCnt.setText(strRslt);
		textTrxOccrCnt.requestLayout();

		redrawChart();
		chart.setFocus();
	}

	// 최초 Chart 그리기
	public Chart createChart(Composite parent) {
		// create a chart
		Chart chart = new Chart(parent, SWT.NONE);
		SimpleDateFormat dformat = new SimpleDateFormat("MM/dd HH:mm.ss");
		SimpleDateFormat dfmt = new SimpleDateFormat("YYYY-MM-dd HH:mm");
		IPlotArea plotArea = (IPlotArea)chart.getPlotArea();
		Composite plot = (Composite)chart.getPlotArea() ;
		IAxis yAxis = chart.getAxisSet().getYAxis(0) ;
		
		plot.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent arg0) {

				Date dt = new Date((long)chart.getAxisSet().getXAxis(0).getDataCoordinate(arg0.x));
				double y = (double) yAxis.getDataCoordinate(arg0.y);

				try {
					plotArea.setToolTipText(dformat.format(dt)
							+ String.format("\n전문건수: %2.0f", y));
				} catch (Exception arg1) {
					plotArea.setToolTipText(null);
				}
			}
		});
		plot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				Date dt = new Date((long)chart.getAxisSet().getXAxis(0).getDataCoordinate(e.x));
				String sdate = dfmt.format(dt) ;
				AqtMain.openTrList("t.tcode ='" + cmbCode.getTcode() + "' AND t.stime like '" + sdate + "%'") ;				
				super.mouseDoubleClick(e);
			}
		});
		
//		Date[] xSeries = { new Date("11/27/2021 10:00"), new Date("11/27/2021 10:00") };
		double[] ySeries = {} ;
		SimpleDateFormat hmf = new SimpleDateFormat("HH:mm");


		chart.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		// set titles
		chart.getTitle().setText("시간대별 전문 현황");
		chart.getAxisSet().getXAxis(0).getTitle().setText("수행시간");
		chart.getAxisSet().getYAxis(0).getTitle().setText("전문 건수");
		chart.getLegend().setVisible(true);
		chart.getLegend().setFont(IAqtVar.font13) ;
		chart.getLegend().setPosition(SWT.RIGHT);
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		// create line series
		lser0 = (ILineSeries<?>) chart.getSeriesSet().createSeries(SeriesType.LINE, "총건수");
		lser1 = (ILineSeries<?>) chart.getSeriesSet().createSeries(SeriesType.LINE, "실패건수");

		lser0.setAntialias(SWT.ON);
		lser0.setSymbolColor(SWTResourceManager.getColor(SWT.COLOR_RED));
		lser0.setLineColor(SWTResourceManager.getColor(SWT.COLOR_RED));
		lser0.setSymbolType(PlotSymbolType.CIRCLE);
		lser0.setSymbolSize(3);
		lser0.setLineStyle(LineStyle.DOT);
		lser0.setLineWidth(2);
//	lineSeries.setXDateSeries(xSeries);
		lser0.setVisibleInLegend(true);
		lser0.setYSeries(ySeries);
//		lineSeries.setYAxisId(0);
		lser1.setAntialias(SWT.ON);
		lser1.setSymbolColor(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		lser1.setLineColor(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		lser1.setSymbolType(PlotSymbolType.DIAMOND);
		lser1.setSymbolSize(3);
		lser1.setLineStyle(LineStyle.DOT);
		lser1.setLineWidth(1);
		lser1.setYSeries(ySeries);
		lser1.setVisibleInLegend(true);

		final IAxisTick xTick = chart.getAxisSet().getXAxis(0).getTick();
		xTick.setFormat(hmf);
		xTick.setTickMarkStepHint(30);
		
//		chart.getAxisSet().getXAxis(0).enableCategory(true);
		
		/*
		 * IAxisTick xTick = chart.getAxisSet().getXAxis(0).getTick(); DateFormat format
		 * = new SimpleDateFormat("HH:mm.ss"); xTick.setFormat(format);
		 */
		// adjust the axis range
//		chart.getAxisSet().adjustRange();
//		chart.setRedraw(true);
		return chart;
	}

	// 선택된 값에 따라 Chart에 값을 설정 한다.
	public void redrawChart() {

		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		
		List<ChartData> chartData = em.createNativeQuery(
        		"SELECT DATE_ADD( MIN(t.stime),interval -1 MINute) dtime  , 0 trxCnt, 0 fCnt from Ttcppacket t  where t.tcode = ?1 " + 
        		" union " + 
        		"select cast( date_format(t.stime, '%Y-%m-%d %H:%i:00') as datetime) dtime,"
        		+ " count(t.pkey) trxCnt , sum(case when sflag = '2' then 1 else 0 end) fCnt from Ttcppacket t "
        		+ " where t.tcode = ?2 group by date_format(t.stime, '%Y-%m-%d %H:%i:00') "
        		+ " UNION " + 
        		"SELECT DATE_ADD( Max(t.stime),interval 1 MINute)  , 0 ,0 from Ttcppacket t  where t.tcode = ?3 "  
        	, ChartData.class)
        		.setParameter(1, cmbCode.getTcode())
        		.setParameter(2, cmbCode.getTcode())
        		.setParameter(3, cmbCode.getTcode())
        		.getResultList() ;
//		Query query = em.createNamedQuery("Ttcppacket.chartData", Ttcppacket.class);

//		query.setParameter(1, cmbCode.getTcode());
//
//		List<Object[]> resultList = query.getResultList();
//
//		chartData = resultList.stream().map(r -> new ChartData(Timestamp.valueOf(r[0].toString()), (Long) r[1]))
//				.collect(Collectors.toCollection(ArrayList::new));

		double[] ySeries = chartData.stream().mapToDouble(d -> d.getTrxCnt()).toArray();
		double[] yS1 = chartData.stream().mapToDouble(d -> d.getfCnt()).toArray();
		Date[] xSeries = chartData.stream().map(a -> a.getDtime()).toArray(Date[]::new) ;
		
		chart.setRedraw(false);

		// create line series
//		ILineSeries lineSeries = (ILineSeries) chart.getSeriesSet().getSeries("aqtview");

		lser0.setXDateSeries(xSeries);
		lser0.setYSeries(ySeries);
		lser0.setYAxisId(0);

		lser1.setXDateSeries(xSeries);
		lser1.setYSeries(yS1);
		lser1.setYAxisId(0);

		// adjust the axis range
		chart.getAxisSet().adjustRange();
		chart.getAxisSet().getYAxis(0).adjustRange();
//		chart.getAxisSet().getXAxis(0).adjustRange();
//		chart.getAxisSet().getXAxis(1).adjustRange();
//		yAxis.zoomOut();
		chart.setRedraw(true);

/*
		chart.addMouseMoveListener(e -> {
			for(IAxis axis : chart.getAxisSet().getAxes()) {

				Rectangle r = axis.getTick().getBounds();

				// check if mouse cursor is on axis tick

				if(r.x < e.x && e.x < r.x + r.width && r.y < e.y && e.y < r.y + r.height) {

					// get pixel coordinate on axis tick

					int pixelCoord;

					if(axis.getDirection() == Direction.X) {

						pixelCoord = e.x - r.x;

					} else {

						pixelCoord = e.y - r.y;

					}

					// get data coordinate

					double dataCoord = axis.getDataCoordinate(pixelCoord);

					// show tool-tip

					chart.setToolTipText(String.valueOf(dataCoord));

					return;

				}

			}

			chart.setToolTipText(null);

		});
		*/
	}
	
}
