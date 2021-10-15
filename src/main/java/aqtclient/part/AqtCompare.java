package aqtclient.part;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxisTick;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.TrxCompList;
import aqtclient.model.Ttcppacket;

public class AqtCompare {

	private Table tblResult;
	private Table tblDetailResult1;
	private Table tblDetailResult2;
	private Table topTable;

	private Text txtReceive1;
	private Text txtSend1;
	private Text txtReceive2;
	private Text txtSend2;
	private Text txtFind1 ;
	
	private AqtTcodeCombo cmbCode1;
	private AqtTcodeCombo cmbCode2;
	private Button btndiff ;

	private static List<Ttcppacket> tempTrxList1; // testcode1 의 ttransaction
	private static List<Ttcppacket> tempTrxList2; // testcode2 의 ttransaction
//	private static ArrayList<TrxResultList> tempTrxRsltList;  // 서비스별 트랜잭션 건수
	private static List<TrxCompList> tempTrxCompList; // 서비스별 트랜잭션 건수

	private TableViewer tableViewer;
	private AqtTranTable tableViewerDR1;
	private AqtTranTable tableViewerDR2;

	private Chart chart1;
	private Chart chart2;
	
	private EntityManager em = AqtMain.emf.createEntityManager();
//	int gcol1 = 0 , gcol2 = 0 ;

	public AqtCompare(Composite parent, int style) {
		create(parent, style);

		AqtMain.cback = new IAqtSetCode() {
			@Override
			public void setTcode(String s) {
				cmbCode1.findSelect(s + " :") ;
				cmbCode2.findSelect(cmbCode1.getCmpCode() + " :") ;
				tblResult.removeAll();
				if (tempTrxCompList != null) tempTrxCompList.clear();
				tblDetailResult1.removeAll();
			}
		};

	}

	private void create(final Composite parent, int style) {
		SashForm sashForm, sashForm2;

		parent.setLayout(new FillLayout());

		sashForm = new SashForm(parent, SWT.VERTICAL);
//		sashForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		Composite compHeader = new Composite(sashForm, SWT.NONE);
		GridLayout headerLayout = new GridLayout(1, false);
		headerLayout.verticalSpacing = 5;
		headerLayout.marginTop = 20;
		headerLayout.marginBottom = 0;
		headerLayout.marginWidth = 10;
		compHeader.setLayout(headerLayout);

//		compHeader.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		Label ltitle = new Label(compHeader, SWT.NONE);
//		ltitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		ltitle.setImage(AqtMain.getMyimage("tit_compare.png"));

		Composite compTitle = new Composite(compHeader, SWT.NONE);

//		compTitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

//		compTitle.setLayoutData(new GridData( SWT.FILL , SWT.TOP, true, false));
		GridData titleGridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		compTitle.setLayoutData(titleGridData);
		GridLayout glin = new GridLayout(6, false);
		glin.horizontalSpacing = 20;
		glin.marginBottom = 0;
		compTitle.setLayout(glin);

		Label lblTestCode1 = new Label(compTitle, SWT.NONE);
//		lblTestCode1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblTestCode1.setFont(IAqtVar.font1);
		lblTestCode1.setText("테스트ID-1");

		cmbCode1 = new AqtTcodeCombo(compTitle, SWT.READ_ONLY) ;
		cmbCode1.getControl().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				topTable.getColumn(2).setText(cmbCode1.getText() );
				cmbCode2.findSelect(cmbCode1.getCmpCode() + " :") ;
			}
		});
		Label lblTestCode2 = new Label(compTitle, SWT.NONE);
//		lblTestCode2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblTestCode2.setText("테스트ID-2");
		lblTestCode2.setFont(IAqtVar.font1);

		cmbCode2 = new AqtTcodeCombo(compTitle, SWT.READ_ONLY);
		cmbCode2.getControl().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				topTable.getColumn(3).setText(cmbCode2.getText() );
			}
		});
		
		btndiff = new Button(compTitle, SWT.CHECK | SWT.NONE) ;
		btndiff.setText("값이다른전문만 보기");
		btndiff.setFont(IAqtVar.font1);

		Label btnSearch = new Label(compTitle, SWT.NONE);
		btnSearch.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				refreshScreen();
			}
		});
		btnSearch.setCursor(IAqtVar.handc);
		btnSearch.setImage(AqtMain.getMyimage("search.png"));
		

		Composite compScArea = new Composite(compHeader, SWT.NONE);
		GridLayout gl_compScArea = new GridLayout();
		gl_compScArea.marginHeight = 0;
		compScArea.setLayout(gl_compScArea);
		compScArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

//		compScArea.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

//		ScrolledComposite scrollArea = new ScrolledComposite(compScArea, SWT.NONE);
		Composite scrollArea = new Composite(compScArea, SWT.NONE);
//		scrollArea.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		scrollArea.setLayout(new GridLayout());

		scrollArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Composite compfind = new Composite(scrollArea, SWT.NONE);
		compfind.setLayout(new GridLayout(2, false));
		
		CLabel clb1 = new CLabel(compfind, SWT.NONE ) ;
		clb1.setText("Search:");
//		clb1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		txtFind1 = new Text(compfind, SWT.BORDER) ;

		txtFind1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtFind1.setLayoutData(new GridData(400,-1));
		txtFind1.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent arg0) {
				arg0.doit = true ;
				TableItem[] tia = tblResult.getItems() ;
				String sval = txtFind1.getText() ;
				if (sval.isEmpty() )  return ;

				loop1 : for(int i=0; i<tia.length ; i++) {
					for (int j=0; j < tblResult.getColumnCount(); j++)
						if ((tia[i].getText(j)).contains(sval)) {
							tblResult.setSelection(i);
							tbl2data(tempTrxCompList.get(i).getSvcid());
							break loop1;
						}
				}

			}

			public void keyPressed(KeyEvent arg0) {

			}
		});
		
		Composite content = new Composite(scrollArea, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		content.setLayout(gridLayout);
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
//		content.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		// First row table
		topTable = new Table(content, SWT.NO_SCROLL| SWT.HIDE_SELECTION|SWT.SINGLE|SWT.FULL_SELECTION|SWT.NONE);
		topTable.setFont(IAqtVar.font1b);
		topTable.setHeaderBackground(AqtMain.htcol);
		topTable.setHeaderForeground(AqtMain.forecol);
		topTable.setLinesVisible(false);
		topTable.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		topTable.setHeaderVisible(true);
		topTable.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

//	    topTable.setHeaderBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));

		String[] colTopNames1 = new String[] { "", "",  "Test Code 1", "Test Code 2" };

		for (int i = 0; i < colTopNames1.length; i++) {
			TableColumn tableColumn = new TableColumn(topTable, SWT.CENTER);
			tableColumn.setText(colTopNames1[i]);
			tableColumn.setWidth(500);
			tableColumn.setResizable(false);
		}

		tableViewer = new TableViewer(content, SWT.NONE | SWT.FULL_SELECTION);
		
		tblResult = tableViewer.getTable();

		tblResult.setHeaderVisible(true);
		tblResult.setLinesVisible(true);
		tblResult.setFont(IAqtVar.font1);
		tblResult.setHeaderBackground(AqtMain.htcol);
		tblResult.setHeaderForeground(AqtMain.forecol);
		tblResult.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
//		tblResult.setHeaderBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));

		tableViewer.setUseHashlookup(true);

		Point point = parent.getSize();
		int width = (point.x - 70) / 10;

		String[] columnNames1 = new String[] { "서비스", "서비스명",  "전문건수", "평균시간", "정상건수", "실패건수", "전문건수", "평균시간",
				"정상건수", "실패건수" };

		int[] columnWidths1 = new int[] { 300, width + 100, width - 20, width - 20, width - 20, width - 20, width - 20,
				width - 20, width - 20, width - 20 };

		int[] columnAlignments1 = new int[] { SWT.CENTER, SWT.LEFT,  SWT.CENTER, SWT.CENTER, SWT.CENTER,
				SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER };

		GridDataFactory.fillDefaults().grab(true, true).span(2, 1).applyTo(tblResult);

		for (int i = 0; i < columnNames1.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, columnAlignments1[i]);

			TableColumn tableColumn = tableViewerColumn.getColumn();
			tableColumn.setText(columnNames1[i]);
			tableColumn.setWidth(columnWidths1[i]);

			tableColumn.addControlListener(new ControlAdapter() {
				@Override
				public void controlResized(ControlEvent e) {
					alignTblHeader();
				}
			});

		}

		tblResult.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tbl2data(tempTrxCompList.get(tblResult.getSelectionIndex()).getSvcid());
				super.widgetSelected(e);
			}
		});
		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setLabelProvider(new TrxRsltLabelProvider());
//		scrollArea.setContent(content);
//		scrollArea.setExpandHorizontal(true);
//		scrollArea.setExpandVertical(true);
//		scrollArea.setAlwaysShowScrollBars(true);
//		scrollArea.setMinSize(tblResult.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		alignTblHeader();

		sashForm2 = new SashForm(sashForm, SWT.HORIZONTAL);
		sashForm2.setSashWidth(0);
		SashForm sashForm_1 = new SashForm(sashForm2, SWT.VERTICAL);
		SashForm sashForm_2 = new SashForm(sashForm_1, SWT.VERTICAL);

		Composite compCode1 = new Composite(sashForm_2, SWT.NONE);
		GridLayout gl_compCode1 = new GridLayout();
		gl_compCode1.verticalSpacing = 5;
		gl_compCode1.marginHeight = 10;
		gl_compCode1.marginLeft = 15;
		gl_compCode1.marginRight = 10;
		gl_compCode1.numColumns = 2;
		compCode1.setLayout(gl_compCode1);

		CLabel clb = new CLabel(compCode1, SWT.NONE ) ;
		clb.setText("Search:");
		clb.setLayoutData(new GridData(-1,28));
		final Text txtFind = new Text(compCode1, SWT.BORDER) ;
		GridData gdt = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gdt.widthHint = 400 ;
//		gdt.heightHint = 25;
		txtFind.setLayoutData(gdt);
		txtFind.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		txtFind.addKeyListener(new KeyAdapter() {
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
			
		});
		tableViewerDR1 = new AqtTranTable(compCode1, SWT.BORDER | SWT.FULL_SELECTION);
		tblDetailResult1 = tableViewerDR1.getTable();
		tblDetailResult1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tblDetailResult1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int ix = tblDetailResult1.getSelectionIndex() ;
				if (ix >= 0) {
					txtSend1.setText(tempTrxList1.get(ix).getSdata());
					txtReceive1.setText(tempTrxList1.get(ix).getRdatam());
					String suid = tblDetailResult1.getItem(ix).getText(0);
					if ( (ix = findRow(tblDetailResult2, suid)) >= 0) {
						txtSend2.setText(tempTrxList2.get(ix).getSdata());
						txtReceive2.setText(tempTrxList2.get(ix).getRdatam());
					}
				}
			}
		});

		SashForm sashForm_4 = new SashForm(sashForm2, SWT.VERTICAL);
		SashForm sashForm_5 = new SashForm(sashForm_4, SWT.VERTICAL);
		sashForm_4.setBackground(parent.getBackground());
		sashForm_5.setBackground(parent.getBackground());

		Composite compCode2 = new Composite(sashForm_5, SWT.NONE);
		GridLayout gl_compCode2 = new GridLayout();
		gl_compCode2.verticalSpacing = 5;
		gl_compCode2.marginHeight = 10;
		gl_compCode2.marginLeft = 10;
		gl_compCode2.marginRight = 20;
		gl_compCode2.numColumns = 2;
		compCode2.setLayout(gl_compCode2);

		Button btncmp = new Button(compCode2, SWT.PUSH );
		btncmp.setText("상세비교보기");
//		btncmp.setFont(IAqtVar.font1);
		GridData gdb = new GridData(SWT.RIGHT, SWT.CENTER, true, false,2,1);
		gdb.heightHint = 28 ;
		btncmp.setLayoutData(gdb);

		btncmp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				if (tblDetailResult1.getSelectionIndex() >= 0) {
					Ttcppacket tr1 = (Ttcppacket) tblDetailResult1.getItem(tblDetailResult1.getSelectionIndex()).getData() ;
					Ttcppacket tr2 = (Ttcppacket) tblDetailResult2.getItem(tblDetailResult2.getSelectionIndex()).getData() ;
					AqtDetailComp aqtDetail = new AqtDetailComp(parent.getShell(),
							SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.CLOSE |SWT.CENTER,
							tr1.getPkey() , tr2.getPkey()
					);
					aqtDetail.open();
				}
			}
		});

		tableViewerDR2 = new AqtTranTable(compCode2, SWT.BORDER | SWT.FULL_SELECTION);
		tblDetailResult2 = tableViewerDR2.getTable();
		tblDetailResult2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tblDetailResult2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int ix = tblDetailResult2.getSelectionIndex() ;
				if (ix >=0) {
					txtSend2.setText(tempTrxList2.get(ix).getSdata());
					txtReceive2.setText(tempTrxList2.get(ix).getRdatam());
					String suid = tblDetailResult2.getItem(ix).getText(0);
					if ( (ix = findRow(tblDetailResult1, suid)) >= 0) {
						txtSend1.setText(tempTrxList1.get(ix).getSdata());
						txtReceive1.setText(tempTrxList1.get(ix).getRdatam());
					}

				}
			}
		});


		Label lblSend2 = new Label(compCode2, SWT.NONE);
		lblSend2.setText("SEND");
//		lblSend2.setFont(IAqtVar.font1);
//		lblSend2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		txtSend2 = new Text(compCode2, SWT.BORDER);
		txtSend2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtSend2.setEditable(false);
//		txtSend2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
//		txtSend2.setFont(IAqtVar.font1);

		Label lblReceive2 = new Label(compCode2, SWT.NONE);
		lblReceive2.setText("RECEIVE");
//		lblReceive2.setFont(IAqtVar.font1);
//		lblReceive2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		txtReceive2 = new Text(compCode2, SWT.BORDER);
		txtReceive2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
//		txtReceive2.setFont(IAqtVar.font1);
		txtReceive2.setEditable(false);

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
		sashForm2.setWeights(new int[] { 5, 5 });

		SashForm sashForm_3 = new SashForm(sashForm_2, SWT.NONE);

		Composite compChart1 = new Composite(sashForm_3, SWT.NONE);
//		compChart1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FillLayout fl_compChart1 = new FillLayout();
		fl_compChart1.marginHeight = 10;
		fl_compChart1.marginWidth = 20;
		compChart1.setLayout(fl_compChart1);

		// compChart1.setLayout(new FillLayout());
		chart1 = createChart(compChart1);
		sashForm.setWeights(new int[] { 4, 6 });

		SashForm sashForm_6 = new SashForm(sashForm_5, SWT.NONE);

		Composite compChart2 = new Composite(sashForm_6, SWT.NONE);
//		compChart2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FillLayout fl_compChart2 = new FillLayout();
		fl_compChart2.marginHeight = 10;
		fl_compChart2.marginWidth = 20;
		compChart2.setLayout(fl_compChart2);

//		compChart2.setLayout(new FillLayout());
		chart2 = createChart(compChart2);
//		sashForm3.setWeights(new int[] {1, 1});
//		sashForm.setWeights(new int[] {143, 248, 214, 286});

	}
	
	private int findRow(Table t, String sval) {
		TableItem[] tia = t.getItems() ;
		for(int i=0; i<tia.length ; i++) {
			if (sval.equals(tia[i].getText(0))) {
				t.setSelection(i);
				return i;
			}
		}
		return -1;
	}

	public void refreshScreen() {

		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();

		topTable.getColumn(2).setText(cmbCode1.getText());
		topTable.getColumn(3).setText(cmbCode2.getText());
		
		String sdiff = (btndiff.getSelection() ? "AND (a.rcode <> b.rcode or a.rcode > 399 or b.rcode > 399) ": "");
		List<Object[]> resultList = em.createNativeQuery(
				"WITH tmpt AS (SELECT a.uri, a.cmpid FROM Ttcppacket a JOIN Ttcppacket b"
				+ " ON ( a.tcode = ? and b.tcode = ? AND  a.cmpid = b.cmpid )  " + 
				"WHERE 1=1 " + sdiff + "  ) " + 
				"select  a.uri, ifnull(svckor,'no register') ,  sum(a.tcnt1) , sum(a.avgt1) ,sum(a.scnt1) ,sum(a.fcnt1)  " + 
				", sum(a.tcnt2), sum(a.avgt2) ,sum(a.scnt2)  ,sum(a.fcnt2)  " + 
				"from   ( " + 
				"select t.tcode, t.uri,dstip,dstport,  count(1) tcnt1, avg(t.svctime) avgt1 " + 
				"    , sum(case when t.sflag = '1' then 1 else 0 end) scnt1 " + 
				"    , sum(case when t.sflag = '2' then 1 else 0 end) fcnt1,0 tcnt2,0 avgt2,0 scnt2,0 fcnt2 " + 
				"from   Ttcppacket t, tmpt x where t.tcode = ? AND t.uri = x.uri AND t.cmpid = x.cmpid " + 
				"group by t.tcode, t.uri " + 
				"UNION ALL  " + 
				"select t.tcode, t.uri,dstip,dstport, 0,0,0,0,count(1) tcnt2, avg(t.svctime) avgt2 " + 
				"    , sum(case when t.sflag = '1' then 1 else 0 end) scnt2 " + 
				"    , sum(case when t.sflag = '2' then 1 else 0 end) fcnt2 " + 
				"from   Ttcppacket t, tmpt x where t.tcode = ? AND t.uri = x.uri AND t.cmpid = x.cmpid " + 
				"group by t.tcode, t.uri " + 
				") as a " + 
				"left outer join Tservice s on (a.uri = s.svcid and uf_getapp(dstip,dstport) = s.appid )" + 
				"GROUP BY a.uri "  
				)
				.setParameter(1, cmbCode1.getTcode()).setParameter(2, cmbCode2.getTcode())
				.setParameter(3, cmbCode1.getTcode()).setParameter(4, cmbCode2.getTcode())
				.getResultList();
		
		tempTrxCompList = new ArrayList<TrxCompList>();

		tempTrxCompList = resultList.stream()
				.map(r -> new TrxCompList(r[0].toString(), r[1].toString(), 
						((BigDecimal) r[2]).longValue(), ((Double) r[3]), ((BigDecimal) r[4]).longValue(),
						((BigDecimal) r[5]).longValue(), ((BigDecimal) r[6]).longValue(), ((Double) r[7]),
						((BigDecimal) r[8]).longValue(), ((BigDecimal) r[9]).longValue() ) )
				.collect(Collectors.toCollection(ArrayList::new));

		tempTrxCompList.add(0, new TrxCompList("%", "총계", 
				tempTrxCompList.stream().mapToLong(TrxCompList::getTcnt1).sum() ,
				tempTrxCompList.stream().mapToDouble(TrxCompList::getTcnt1).average().orElse(0),
				tempTrxCompList.stream().mapToLong(TrxCompList::getScnt1).sum() ,
				tempTrxCompList.stream().mapToLong(TrxCompList::getFcnt1).sum() ,
				tempTrxCompList.stream().mapToLong(TrxCompList::getTcnt2).sum() ,
				tempTrxCompList.stream().mapToDouble(TrxCompList::getTcnt2).average().orElse(0),
				tempTrxCompList.stream().mapToLong(TrxCompList::getScnt2).sum() ,
				tempTrxCompList.stream().mapToLong(TrxCompList::getFcnt2).sum() )
		);
		tableViewer.setInput(tempTrxCompList);
		if (tableViewer.getTable().getItemCount() > 1) tableViewer.getTable().setSelection(1);
		if ( tempTrxCompList.size() > 1 ) {
			AqtMain.aqtmain.setGtcode(cmbCode1.getTcode());
			tableViewer.getTable().setSelection(1);
			tbl2data(tempTrxCompList.get(1).getSvcid());
		}
	}

	private void tbl2data(String svcid) {

		em.clear();
		
		tempTrxList1 = new ArrayList<Ttcppacket>();
		tempTrxList2 = new ArrayList<Ttcppacket>();
		String tcode = cmbCode1.getTcode();
		String sdiff = (btndiff.getSelection() ? "AND (a.rcode <> b.rcode or a.sflag = '2' or b.sflag = '2') ": "");
		Query qTrx = em.createNativeQuery(
				"WITH tmpt AS (SELECT a.uri, a.cmpid FROM Ttcppacket a JOIN Ttcppacket b ON ( a.uri = b.uri AND a.cmpid = b.cmpid)  " + 
				"WHERE a.tcode = '" + cmbCode1.getTcode() + "' AND b.tcode = '" + cmbCode2.getTcode() + "' " +" and a.uri like '"+svcid+"'  " + sdiff + "  ) " + 
				 "SELECT t.* FROM Ttcppacket t, tmpt x where t.tcode = '" + tcode + "' and  t.cmpid = x.cmpid order by t.cmpid " ,
						Ttcppacket.class) ;

		tempTrxList1 = qTrx.getResultList();

		tcode = cmbCode2.getTcode();
		qTrx = em.createNativeQuery(
				"WITH tmpt AS (SELECT a.uri, a.cmpid FROM Ttcppacket a JOIN Ttcppacket b ON ( a.uri = b.uri AND a.cmpid = b.cmpid)  " + 
				"WHERE a.tcode = '" + cmbCode1.getTcode() + "' AND b.tcode = '" + cmbCode2.getTcode() + "' " +" and a.uri like '"+svcid+"'  " + sdiff + "  ) " + 
				 "SELECT t.* FROM Ttcppacket t, tmpt x where t.tcode = '" + tcode + "' and  t.cmpid = x.cmpid order by t.cmpid " ,
						Ttcppacket.class) ;

		tempTrxList2 = qTrx.getResultList();
//		query  = em.createNativeQuery(
//				"WITH tmpt AS (SELECT a.svcid, a.uuid FROM Ttcppacket a JOIN Ttcppacket b ON ( a.svcid = b.svcid AND a.uuid = b.uuid)  " + 
//				"WHERE a.tcode = '" + cmbCode1.getTcode() + "' AND b.tcode = '" + cmbCode2.getTcode() + "' " +" and a.svcid = '"+svcid+"'  " + sdiff + "  ) " + 
//				 "SELECT pkey , t.uuid, ifnull(t.msgcd,''),  ifnull(cast(t.rcvmsg as char(100)),''), ifnull(cast(t.errinfo as char(100)),''), " +
//					" cast(rdata as char(150)) rdata,  t.rlen , t.rtime,  t.scrno, " +
//					" cast(sdata as char(150)) sdata, t.sflag, t.slen ,t.stime," +
//					" t.svrnm, t.svcid, t.userid,  t.svctime, t.tcode " +
//					 "FROM 	ttransaction t, tmpt x where t.tcode = '" + tcode + "' and  t.svcid = x.svcid and t.uuid = x.uuid " ) ;
//		resultList = query.getResultList();
//		tempTrxList2 = resultList.stream().map( (r) -> 
//	    new Ttcppacket((int)(long)r[0], r[1].toString(), r[2].toString(), r[3].toString(),
//	    		r[4].toString(), r[5].toString(), (int)(long)r[6], Timestamp.valueOf(r[7].toString()), 
//	    		r[8].toString(), r[9].toString(), r[10].toString(), (int)(long)r[11], 
//	    		Timestamp.valueOf(r[12].toString()), r[13].toString(), r[14].toString(), 
//	    		r[15].toString(), (double)r[16], r[17].toString()) 
//			)
//			.collect(Collectors.toCollection(ArrayList::new));
		
		txtSend1.setText("");
		txtReceive1.setText("");
		txtSend2.setText("");
		txtReceive2.setText("");

		if (!tempTrxList1.isEmpty()) {

			txtSend1.setText(tempTrxList1.get(0).getSdata());
			txtReceive1.setText(tempTrxList1.get(0).getRdatam());
		}

		if (!tempTrxList2.isEmpty()) {
			txtSend2.setText(tempTrxList2.get(0).getSdata());
			txtReceive2.setText(tempTrxList2.get(0).getRdatam());
		}
		tableViewerDR1.setInput(tempTrxList1);
		tableViewerDR2.setInput(tempTrxList2);
		tableViewerDR1.getTable().setSelection(0);
		tableViewerDR2.getTable().setSelection(0);
		
		tableViewerDR1.setResendEnabled (cmbCode1.getTmaster() != null && !cmbCode1.getTmaster().getThost().isEmpty() );
		tableViewerDR2.setResendEnabled (cmbCode2.getTmaster() != null && !cmbCode2.getTmaster().getThost().isEmpty() );

		tblDetailResult1.notifyListeners(SWT.Selection, null);
		redrawChart(tempTrxList1, chart1);
		redrawChart(tempTrxList2, chart2);

	}

	SimpleDateFormat hmsf = new SimpleDateFormat("HH:mm.ss");
	SimpleDateFormat dtfmt = new SimpleDateFormat("yy/MM/dd HH:mm.ss");

	private Chart createChart(Composite parent) {

//		Date[] xSeries = { new Date("11/27/2019 00:00"), // new year
//				new Date("11/27/2019 00:00") };
//
		double[] ySeries = {  };

		// create a chart
		Chart chart = new Chart(parent, SWT.NONE);
		chart.getPlotArea().addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent arg0) {

				Date x = new Date((long) chart.getAxisSet().getXAxis(0).getDataCoordinate(arg0.x));
				double y = chart.getAxisSet().getYAxis(0).getDataCoordinate(arg0.y);
				try {
					chart.getPlotArea().setToolTipText(dtfmt.format(x) + "\n 소요시간:" + String.format("%.2f", y));
				} catch (Exception arg1) {
					// TODO: handle exception
				}
			}
		});

		chart.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		// set titles
		chart.getTitle().setText("시간대별 TR 현황");
		chart.getAxisSet().getXAxis(0).getTitle().setText("수행시간");
		chart.getAxisSet().getYAxis(0).getTitle().setText("소요시간");

		// create line series
		ILineSeries<?> scatterSeries = (ILineSeries<?>) chart.getSeriesSet().createSeries(SeriesType.LINE, "CMPCHART");

		IAxisTick xTick = chart.getAxisSet().getXAxis(0).getTick();
		xTick.setFormat(hmsf);
		scatterSeries.setSymbolColor(SWTResourceManager.getColor(SWT.COLOR_RED));
		scatterSeries.setSymbolType(PlotSymbolType.CIRCLE);
		scatterSeries.setSymbolSize(2);

		scatterSeries.setLineStyle(LineStyle.NONE);
//		scatterSeries.setXDateSeries(xSeries);
		scatterSeries.setYSeries(ySeries);
		chart.getLegend().setVisible(false);

		return chart;
	}

	private void redrawChart(List<Ttcppacket> tempTrxList, Chart chart) {
		
		Date[] xSeries = { };
		double[] ySeries = { };

		if (tempTrxList.size() > 0) {
			xSeries = tempTrxList.stream().map(a -> Date.from(a.getStime().atZone( ZoneId.systemDefault()).toInstant() )).toArray(Date[]::new);
			ySeries = tempTrxList.stream().mapToDouble(a -> a.getSvctime()).toArray();
//			chart.getTitle().setText("시간대별 TR 현황 (" + tempTrxList.get(0).getSvcid() + ")" );
		}
		chart.setRedraw(false);
		ILineSeries scatterSeries = (ILineSeries) chart.getSeriesSet().getSeries()[0];
		scatterSeries.setVisible(true);

		scatterSeries.setXDateSeries(xSeries);
		scatterSeries.setYSeries(ySeries);

//		chart.getAxisSet().getYAxis(0).adjustRange();
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
			List<Ttcppacket> arrayList = (List<Ttcppacket>) input;
			return arrayList.toArray();
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	private class TrxLabelProvider implements ITableLabelProvider {
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
		SimpleDateFormat smdfmt = new SimpleDateFormat("MM/dd HH.mm.ss");
		public String getColumnText(Object element, int columnIndex) {
			Ttcppacket trx = (Ttcppacket) element;
			if (trx != null)
				switch (columnIndex) {
				case 0:
					return trx.getCmpid()+"";
				case 1:
					return smdfmt.format(trx.getStime());
				case 2:
					return smdfmt.format(trx.getRtime());
				case 3:
					return String.format("%.3f", trx.getSvctime());
				case 4:
					return trx.getRcode()+"";
				case 5:
					return trx.getRhead();
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

	private class TrxRsltLabelProvider implements ITableLabelProvider {
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
			TrxCompList trx = (TrxCompList) element;
			if (trx != null)
				switch (columnIndex) {
				case 0:
					return trx.getSvcid();
				case 1:
					return trx.getSvckor();
				case 2:
					return String.format("%,d", trx.getTcnt1());
				case 3:
					return String.format("%.3f", trx.getAvgt1());
				case 4:
					return String.format("%,d", trx.getScnt1());
				case 5:
					return String.format("%,d", trx.getFcnt1());
				case 6:
					return String.format("%,d", trx.getTcnt2());
				case 7:
					return String.format("%.3f", trx.getAvgt2());
				case 8:
					return String.format("%,d", trx.getScnt2());
				case 9:
					return String.format("%,d", trx.getFcnt2());
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

	private void alignTblHeader() {
		for (int i = 0; i < topTable.getColumnCount(); i++) {

			if (i < 2)
				topTable.getColumn(i).setWidth(tblResult.getColumn(i).getWidth() );
			else if (i == 2) {
				int len = tblResult.getColumn(i).getWidth() + tblResult.getColumn(i + 1).getWidth()
						+ tblResult.getColumn(i + 2).getWidth() + tblResult.getColumn(i + 3).getWidth() ;
				topTable.getColumn(i).setWidth(len);
			} else {
				int len = tblResult.getColumn(i + 3).getWidth() + tblResult.getColumn(i + 4).getWidth()
						+ tblResult.getColumn(i + 5).getWidth() + tblResult.getColumn(i + 6).getWidth() ;
				topTable.getColumn(i).setWidth(len);
			}
		}
	}

}
