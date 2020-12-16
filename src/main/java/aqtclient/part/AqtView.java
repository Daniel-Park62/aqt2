package aqtclient.part;

/*
 * 전문처리현황
*/
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxisTick;
import org.eclipse.swtchart.ILegend;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.ChartData;
import aqtclient.model.Tmaster;
import aqtclient.model.TrxCount;
import aqtclient.model.Ttransaction;

public class AqtView {

	private Label textTstDt; // 테스트기준일자
	private Label textPrgrssRt; // 진척율
	private Label textTrxOccrCnt; // 트랜젝션발생건수
	private Label lblHost; // 대상호스트
	private Label textHost; // 대상호스트

	private AqtTcodeCombo cmbCode; // 코드리스트

	private Composite compChart;

	private Chart chart;

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
		SashForm sashForm;

		parent.setLayout(new FillLayout());

		sashForm = new SashForm(parent, SWT.VERTICAL);

		Composite compHeader = new Composite(sashForm, SWT.NONE);
		GridLayout headerLayout = new GridLayout(1, false);
		headerLayout.verticalSpacing = 20;
		headerLayout.marginTop = 20;
		headerLayout.marginBottom = 20;
		headerLayout.marginWidth = 15;
		compHeader.setLayout(headerLayout);

		Label ltitle = new Label(compHeader, SWT.NONE);
//		ltitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		ltitle.setImage(SWTResourceManager.getImage("images/tit_view.png"));
		ltitle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		Composite compIn = new Composite(compHeader, SWT.BORDER);
		GridData titleGridData = new GridData(SWT.FILL, SWT.TOP, true, false);

		compIn.setLayoutData(titleGridData);
		GridLayout glin = new GridLayout(7, false);
		glin.horizontalSpacing = 10;
		compIn.setLayout(glin);

		Label lbl = new Label(compIn, SWT.NONE);
		lbl.setText("테스트코드:");
		lbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		lbl.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,true, false));
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
		lblTstDt.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,true, false));
		lblTstDt.setText("테스트기준일자:");
		lblTstDt.setFont(IAqtVar.font1b);

		textTstDt = new Label(compIn, SWT.NONE);
		textTstDt.setFont(IAqtVar.font1b);
		textTstDt.setText("YYYY/MM/DD");
		textTstDt.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER,true, false));
		textTstDt.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		

		lblHost = new Label(compIn, SWT.NONE);
		lblHost.setText("대상호스트:");
		lblHost.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		lblHost.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,true, false));
		lblHost.setFont(IAqtVar.font1b);
		
		textHost = new Label(compIn, SWT.NONE);
		textHost.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		textHost.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER,true, false));
		textHost.setFont(IAqtVar.font1b);

		lbl = new Label(compIn, SWT.NONE);
		lbl.setImage(SWTResourceManager.getImage("images/refresh.png"));
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
		textPrgrssRt.setText("  ");
		textPrgrssRt.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));

		Label lblTrxOccrCnt = new Label(compIn, SWT.NONE);
		lblTrxOccrCnt.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		lblTrxOccrCnt.setText("트랜잭션 건수:");
		lblTrxOccrCnt.setFont(IAqtVar.font1b);
		lblTrxOccrCnt.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,true, false));

		textTrxOccrCnt = new Label(compIn, SWT.NONE);
		textTrxOccrCnt.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,4,1));
		textTrxOccrCnt.setFont(IAqtVar.font1);
		textTrxOccrCnt.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));

		compChart = new Composite(sashForm, SWT.NONE);
		compChart.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FillLayout dtlLayout = new FillLayout();
		dtlLayout.marginHeight = 20;
		dtlLayout.marginWidth = 20;
		compChart.setLayout(dtlLayout);

//		Composite compChart = new Composite(composite, SWT.NONE);

//		compChart.setLayout(new FillLayout());
		sashForm.setWeights(new int[] { 20, 80 });
		chart = createChart(compChart);

	}

	/* 데이터를 추출하여 화면에 보여준다. */
	public void refreshScreen() {
		EntityManager em = AqtMain.emf.createEntityManager();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();

		String tcode = "" ;
		if (cmbCode.getItemCount() > 0) {
			tcode = cmbCode.getTcode() ;
			textTstDt.setText(cmbCode.getTmaster().getTdate());
			textHost.setText(cmbCode.getTmaster().getThost());
			textHost.requestLayout();
		}
		
		/* Tservice 테이블의 전체 건수 */
//        Query query = em.createQuery("select count(t.svcid) from Tservice t ");
		Query query = em.createNamedQuery("Tservice.TotalCnt");

		long countResultT = (long) query.getSingleResult();

//		query = em.createQuery("select count(distinct t.svcid) from Ttransaction t") ;
		query = em.createNamedQuery("Ttransaction.SvcCnt", Integer.class).setParameter("tcode", tcode);

		long countResultS = (long) query.getSingleResult();

		String strRslt = String.format("%1.1f%%", countResultT == 0 ? 0 : countResultS * 100.0 / countResultT)
				+  String.format(" ( %,d / %,d )", countResultS, countResultT ) ;

		textPrgrssRt.setText(strRslt);
		textPrgrssRt.requestLayout();

		/*
		 * query = em.createQuery("select count(t.uuid) trxCnt " +
		 * ", count(case when t.sflag = '1' then 1 else null end) validCnt " +
		 * ", count(case when t.sflag = '2' then 1 else null end) invalidCnt " +
		 * " from Ttransaction t where t.tcode = :tcode") ;
		 */
		query = em.createNamedQuery("Ttransaction.FlagCnt");

		query.setParameter("tcode", tcode);

		Object[] rst = (Object[]) query.getSingleResult();

		TrxCount trxCnt = new TrxCount((long) rst[0], (long) rst[1], (long) rst[2]);

		strRslt = NumberFormat.getInstance().format(trxCnt.getTrxCnt()) + " 건 ( 정상 : "
				+ NumberFormat.getInstance().format(trxCnt.getValidCnt()) + " 건   성공률 "
				+ String.format("%.2f",
						trxCnt.getTrxCnt() == 0 ? 0.0 : trxCnt.getValidCnt() * 100.0 / trxCnt.getTrxCnt())
				+ "%   실패 : " + NumberFormat.getInstance().format(trxCnt.getInvalidCnt()) + " 건 ) ";

		textTrxOccrCnt.setText(strRslt);
		textTrxOccrCnt.requestLayout();
		em.close();

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
							+ "\n-TR 건수:" + String.format("%2.0f", y));
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
		
		Date[] xSeries = { new Date("11/27/2019 10:00"), new Date("11/27/2019 10:00") };
		double[] ySeries = {1.0, 2.0} ;
		SimpleDateFormat hmf = new SimpleDateFormat("HH:mm");


		chart.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		// set titles
		chart.getTitle().setText("시간대별 TR 현황");
		chart.getAxisSet().getXAxis(0).getTitle().setText("수행시간");
		chart.getAxisSet().getYAxis(0).getTitle().setText("TR 건수");

		// create line series
		ILineSeries lineSeries = (ILineSeries) chart.getSeriesSet().createSeries(SeriesType.LINE, "aqtview");

		lineSeries.setAntialias(SWT.ON);
		lineSeries.setLineColor(SWTResourceManager.getColor(SWT.COLOR_RED));

		lineSeries.setSymbolType(PlotSymbolType.NONE);
//	lineSeries.setXDateSeries(xSeries);
		lineSeries.setYSeries(ySeries);
//		lineSeries.setYAxisId(0);

		final IAxisTick xTick = chart.getAxisSet().getXAxis(0).getTick();
		xTick.setFormat(hmf);
		xTick.setTickMarkStepHint(30);
		
		chart.getLegend().setVisible(false);


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

		EntityManager em = AqtMain.emf.createEntityManager();
		ArrayList<ChartData> chartData = new ArrayList<ChartData>();

		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
//        Query query = em.createNativeQuery("select date_format(t.stime, '%Y-%m-%d %H:%i:%s') stime, count(t.uuid) cnt from Ttransaction t where t.tcode = ? group by date_format(t.stime, '%Y-%m-%d %H:%i:%s')");
		Query query = em.createNamedQuery("Ttransaction.chartData", Ttransaction.class);

		query.setParameter(1, cmbCode.getTcode());

		List<Object[]> resultList = query.getResultList();

		chartData = resultList.stream().map(r -> new ChartData(Timestamp.valueOf(r[0].toString()), (Long) r[1]))
				.collect(Collectors.toCollection(ArrayList::new));

		em.close();
		
		double[] ySeries = chartData.stream().mapToDouble(d -> d.getTrxCnt()).toArray();
		Date[] xSeries = chartData.stream().map(a -> a.getDtime()).toArray(Date[]::new) ;
		
		chart.setRedraw(false);

		// create line series
		ILineSeries lineSeries = (ILineSeries) chart.getSeriesSet().getSeries("aqtview");

		lineSeries.setXDateSeries(xSeries);
		lineSeries.setYSeries(ySeries);
		lineSeries.setYAxisId(0);

		// adjust the axis range
		chart.getAxisSet().adjustRange();
		final IAxis yAxis = chart.getAxisSet().getYAxis(0);
		yAxis.adjustRange();
		chart.getAxisSet().getXAxis(0).adjustRange();
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
