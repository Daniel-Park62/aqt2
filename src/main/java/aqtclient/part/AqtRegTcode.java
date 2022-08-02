/*
 * 서비스(프로그램) 목록등록
*/

package aqtclient.part;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Texecjob;
import aqtclient.model.Tmaster;

public class AqtRegTcode {
	private Table tblList;
	private CheckboxTableViewer tvList;
	
	private List <Tmaster> tcodeList;
	private Text txCode , txCodenm ;
	EntityManager em = AqtMain.emf.createEntityManager();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AqtRegTcode(Composite parent, int style) {
		createscr (parent, style);
		queryScr();
//		AqtMain.cback = new IAqtSetCode() {
//			@Override
//			public void setTcode(String s) {
//				initScreen();
//			}
//		};

	}
	
	private void createscr (Composite parent, int style) {
//		SashForm sashForm;
//
//		parent.setLayout(new FillLayout());
//	    
//	    sashForm = new SashForm(parent, SWT.VERTICAL);
	    Composite container = new Composite(parent, SWT.NONE) ;
	    GridLayoutFactory.fillDefaults().margins(15, 15).numColumns(1).equalWidth(false).applyTo(container);
	    GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(container);
	    
//		Label ltitle = new Label(compHeader, SWT.NONE);
//    	ltitle.setImage(AqtMain.getMyimage("tit_register.png"));
    	new AqtTitle(container, SWT.NONE, "테스트등록/전문생성", "tit_icon.png");

    	Label lbl = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		lbl.setBackground(container.getBackground());
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(lbl);

    	
		Composite compTit = new Composite(container, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(7).equalWidth(false).applyTo(compTit);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(compTit);
		
		Label lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" *테스트ID");
		lblt.setFont(IAqtVar.font1);

		txCode = new Text(compTit, SWT.BORDER);
		
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).hint(200, -1).applyTo(txCode);
		txCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txCode.setFont(IAqtVar.font1);

		txCode.setText("");
		txCode.addTraverseListener( (final TraverseEvent event) -> {
		      if (event.detail == SWT.TRAVERSE_RETURN) queryScr();
		});

		lblt = new Label(compTit, SWT.NONE);
		lblt.setText("테스트명");
		lblt.setFont(IAqtVar.font1);
		txCodenm = new Text(compTit, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).hint(300, -1).applyTo(txCodenm);
		txCodenm.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txCodenm.setFont(IAqtVar.font1);
		txCodenm.setText("");
		txCodenm.addTraverseListener( (final TraverseEvent event) -> {
		      if (event.detail == SWT.TRAVERSE_RETURN) queryScr();
		});
		
		AqtButton btnSearch = new AqtButton(compTit, SWT.PUSH,"조회");
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(true, false).minSize(100, -1).applyTo(btnSearch);
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				queryScr();
			}
		});
		
		AqtButton btnimp = new AqtButton(compTit, SWT.PUSH,"전문가져오기");
		btnimp.setToolTipText("패킷덤프한 pcap 파일에서 데이터를 가져옵니다.");
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(false, false).minSize(100, -1).applyTo(btnimp);
		btnimp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				int i = tblList.getSelectionIndex() ;
				if ( i < 0 ) {
					MessageDialog.openInformation(parent.getShell(), "테스트 선택", "대상 테스트ID를 선택(라인클릭)하세요.") ;
					return ;
				}
				Tmaster tm = (Tmaster) tblList.getItem(i).getData() ;
				if ( tm.getEndDate() != null ) {
					MessageDialog.openInformation(parent.getShell(), "작업불가", tm.getCode() + " 는 종료되었습니다.") ;
					return ;
				}
				String tcode = tm.getCode() ;
		    	String[] ext = {  "*.pcap", "*" }  ;
		    	final FileDialog dlg = new FileDialog ( Display.getDefault().getActiveShell() , SWT.APPLICATION_MODAL | SWT.OPEN );
		    	dlg.setFilterExtensions ( ext );
		    	dlg.setText ( "pcap 파일 import to " + tcode );

		    	String fileName = dlg.open ();
		    	if ( fileName == null ) return ;
		    	try {
					em.getTransaction().begin();
					Texecjob texecjob = new Texecjob() ;
					texecjob.setJobkind(1);
					texecjob.setTcode(tcode);
					texecjob.setInfile(fileName);
					texecjob.setTdesc("Import pcap파일");
					texecjob.setTnum(1);

					em.persist(texecjob);
					em.getTransaction().commit();
					MessageDialog.openInformation(parent.getShell(), "알림",
							"테스트ID " +  tcode + "에 " + fileName + " Import 요청되었습니다." ) ;
				} catch (Exception e1) {
					MessageDialog.openError(parent.getShell(), "알림", e1.getMessage() );
					e1.printStackTrace();
				}
			}
		});

		AqtButton btnCopy = new AqtButton(compTit, SWT.PUSH,"전문생성");
		btnCopy.setToolTipText("다른 테스트 정보로 부터 데이터를 복제하여 새로운 테스트 데이터를 생성합니다.");
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(false, false).minSize(100, -1).applyTo(btnCopy);
		btnCopy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String tcode = "";
				int i = tblList.getSelectionIndex() ;
				if ( i >= 0 ) {
					tcode = ((Tmaster) tblList.getItem(i).getData()).getCode() ;
				}
				AqtCopyTdata aqtcopy = new AqtCopyTdata(parent.getShell(), tcode );
				aqtcopy.open() ;
				tvList.refresh();
			}
		});

//		btnSearch.setCursor(IAqtVar.handc);
//		btnSearch.setImage(AqtMain.getMyimage("search.png"));

		
    	tvList = CheckboxTableViewer.newCheckList(container, SWT.NONE | SWT.FULL_SELECTION );
    	
    	tblList = tvList.getTable();
    	
	    GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(tblList);
		tblList.setLinesVisible(true);
    	tblList.setHeaderBackground(AqtMain.htcol);
    	tblList.setHeaderForeground(AqtMain.forecol);
    	tblList.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
    	tblList.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    	
    	tblList.setFont(IAqtVar.font1b);
	    tvList.setUseHashlookup(true);
	    
	    tblList.addTraverseListener((e) -> {
	    	if (e.detail == SWT.TRAVERSE_TAB_NEXT )
	            e.doit = false;
	    });

	    tblList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
		    	if (e.keyCode == SWT.TAB) {
		    		e.doit = false;
		    	}
	            
				int i = tblList.getSelectionIndex() ;
				if (i >= 0 && e.stateMask == SWT.CTRL && (e.keyCode == 'c' || e.keyCode == 'C')) {
					Clipboard clipboard = new Clipboard(Display.getDefault());
					String sdata =  "" ; // tblList.getItem(i).getText(gcol);
					
					for (int j = 0;j<tblList.getColumnCount(); j++) {
						sdata += tblList.getItem(i).getText(j) + "\t" ;
					}
					
					clipboard.setContents(new Object[] { sdata }, new Transfer[] { TextTransfer.getInstance() });
					clipboard.dispose();
				}
			}
		});
	    
	    Menu popupMenu = new Menu(tblList);
	    
	    MenuItem addsvc = new MenuItem(popupMenu, SWT.NONE);
	    addsvc.setText("새로등록");
	    addsvc.setToolTipText("테스트ID를 신규 입력합니다.");
	    
	    addsvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
//				em.getTransaction().begin();
				int i = tblList.getSelectionIndex() ;
				Tmaster t = new Tmaster() ;
				t.setDesc1("New");
				t.setTdate(new Date());
				t.setNew(true);
				if (i >= 0 ) {
					Tmaster to = (Tmaster) tblList.getItem(i).getData() ;
					t.setDesc1(to.getDesc1()+"");
					t.setCode(to.getCode().replaceAll("[0-9]$", "?"));
					t.setCmpCode(to.getCmpCode());
					t.setThost(to.getThost());
					t.setLvl(to.getLvl());
				} else 
					i=0;
				
				em.persist(t);
				tcodeList.add(i,t) ;

				tvList.refresh();
				tvList.setChecked(t, true);
			}
		});

	    MenuItem delsvc = new MenuItem(popupMenu, SWT.NONE);
	    delsvc.setText("삭제");
	    delsvc.setToolTipText("좌측 체크가 되어 있는 테스트ID를 삭제합니다.\n또한 관련 전문 모두 삭제됩니다.");
	    delsvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (tvList.getCheckedElements().length > 0 ) {
					StringBuffer undel = new StringBuffer();
					StringBuffer del = new StringBuffer();
					em.getTransaction().begin();
					for ( Object s : tvList.getCheckedElements() ) {
						Tmaster tm = (Tmaster)s ;
//						if (  tm.getEndDate() != null || tm.getDataCnt() > 0 ) {
						if (  tm.getEndDate() != null ) {
							undel.append(tm.getCode() + " ") ;
							continue ;
						}
						int cnt = tm.getDataCnt() ;
						del.append(tm.getCode() + " ") ;
						em.createQuery("DELETE FROM Ttcppacket t WHERE t.tcode = :tcode")
							.setParameter("tcode", tm.getCode()).executeUpdate() ;
					
						if (cnt>0) del.append(String.format("(전문 %,d건 삭제)", cnt)) ;
						em.remove(tm);
						tcodeList.remove(s) ;
						
					}
					if ( del.length() > 0 && ! MessageDialog.openQuestion(parent.getShell(), "Delete", del + " 삭제 진행하시겠습니까?")) {
						em.getTransaction().rollback() ;
						return ;
					}
						
					em.getTransaction().commit();
					tvList.refresh();
					MessageDialog.openInformation(parent.getShell(), "Delete Infomation", 
							(del.length() > 0 ? del + "삭제 되었습니다.\r\n\r\n" : "")  + 
							(undel.length() > 0 ? undel + "삭제 할 수 없습니다.":"")) ;
				}
			}
		});

	    MenuItem pmclear = new MenuItem(popupMenu, SWT.NONE);
	    pmclear.setText("전문삭제");
	    pmclear.setToolTipText("체크된 테스트ID의 모든전문을 삭제합니다.");
	    pmclear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (tvList.getCheckedElements().length > 0 ) {
					StringBuffer undel = new StringBuffer();
					StringBuffer del = new StringBuffer();
					em.getTransaction().begin();
					for ( Object s : tvList.getCheckedElements() ) {
						Tmaster tm = (Tmaster)s ;
//						if (  tm.getEndDate() != null || tm.getDataCnt() > 0 ) {
						if (  tm.getEndDate() != null ) {
							undel.append(tm.getCode() + " ") ;
							continue ;
						}
						int cnt = tm.getDataCnt() ;
						del.append(tm.getCode() + " ") ;
						cnt = em.createQuery("DELETE FROM Ttcppacket t WHERE t.tcode = :tcode")
							.setParameter("tcode", tm.getCode()).executeUpdate() ;
						if (cnt>0) del.append(String.format("(전문 %,d건 삭제)", cnt)) ;
						em.createNativeQuery("call sp_summary(?)")
								.setParameter(1, tm.getCode()).executeUpdate() ;
						
					}
					if ( del.length() > 0 && ! MessageDialog.openQuestion(parent.getShell(), "전문삭제", del + " 삭제 진행하시겠습니까?")) {
						em.getTransaction().rollback() ;
						return ;
					}
						
					em.getTransaction().commit();
					queryScr();
					MessageDialog.openInformation(parent.getShell(), "전문삭제", 
							(del.length() > 0 ? del + "삭제 되었습니다.\r\n\r\n" : "")  + 
							(undel.length() > 0 ? undel + "삭제 할 수 없습니다.":"")) ;
				}
			}
		});

	    MenuItem savesvc = new MenuItem(popupMenu, SWT.NONE);
	    savesvc.setText("저장");
	    savesvc.setToolTipText("신규입력, 수정된 정보를 저장합니다. \n저장하지 않으면 수정한 정보를 잃게됩니다.");
	    savesvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (tvList.getCheckedElements().length > 0 ) {
					em.getTransaction().begin();
//					for ( Object s : tvList.getCheckedElements() ) { 
//						em.merge((Tmaster)s);
//						((Tmaster)s).setNew(false);
//					}
					em.getTransaction().commit();
					MessageDialog.openInformation(parent.getShell(), "Save Infomation", "수정 되었습니다.") ;
					tvList.refresh();
					tvList.setAllChecked(false);
				}
			}
		});

	    MenuItem impdat = new MenuItem(popupMenu, SWT.NONE);
	    impdat.setText("전문가져오기");
	    impdat.setToolTipText(btnimp.getToolTipText());
	    impdat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnimp.notifyListeners(SWT.MouseDown, null);
			}
		});

	    MenuItem copysvc = new MenuItem(popupMenu, SWT.NONE);
	    copysvc.setText("전문생성");
	    copysvc.setToolTipText(btnCopy.getToolTipText());
	    copysvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnCopy.notifyListeners(SWT.MouseDown, null);

			}
		});

	    MenuItem maptohost = new MenuItem(popupMenu, SWT.NONE);
	    maptohost.setText("IP 매핑");
	    maptohost.setToolTipText("현행 ip addr 과 port에 대응하는 신규시스템 ip 및 port를 지정합니다.");
	    maptohost.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String tcode = "";
				int i = tblList.getSelectionIndex() ;
				if ( i >= 0 ) {
					tcode = ((Tmaster) tblList.getItem(i).getData()).getCode() ;
				}
				AqtMapToHost aqtmaptohost = new AqtMapToHost(parent.getShell(), tcode );
				aqtmaptohost.open() ;

			}
		});

	    MenuItem pmEndTest = new MenuItem(popupMenu, SWT.NONE);
	    pmEndTest.setText("테스트종료처리");
	    pmEndTest.setToolTipText("테스트가 완료되어 더이상 사용하지 않게됩니다.");
	    pmEndTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int ix = tblList.getSelectionIndex() ;
				if (ix < 0)  return ;
				TableItem item = tblList.getItem(ix) ;
				Tmaster tmaster = ((Tmaster)item.getData())  ;
				boolean result = MessageDialog.openConfirm(parent.getShell(), "테스트종료",
						"테스트ID ["+ tmaster.getCode() + "] 종료하시겠습니까?\r\n종료후에 이 테스트ID로 어떤작업도 할 수 없습니다." ) ;
				if (  result ) {
				
					em.getTransaction().begin();
					tmaster.setEndDate(new Date() ); 
					em.getTransaction().commit();
					tvList.refresh();
				}

			}
		});

	    popupMenu.setEnabled( AqtMain.authtype == AuthType.TESTADM );
//	    popupMenu.setVisible( popupMenu.getEnabled() );
//	    addsvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
//	    savesvc.setEnabled( addsvc.getEnabled() );
//	    delsvc.setEnabled( addsvc.getEnabled() );
//	    copysvc.setEnabled( addsvc.getEnabled() );
//	    impdat.setEnabled( addsvc.getEnabled() );
//	    pmEndTest.setEnabled( addsvc.getEnabled() );
	    
	    tblList.setMenu(popupMenu);
	    
	    btnimp.setEnabled(popupMenu.getEnabled() );
	    btnCopy.setEnabled(popupMenu.getEnabled() );
	    
	    
        String[] cols1 = new String[] 
        		{  " 테스트ID", "  테스트명", "타입", "단계", "대상코드", "테스트시작일","테스트종료일","서버IP", "서버 Port","전문건수"};

        int[] columnWidths1 = new int[] {  130, 250, 80, 80, 150,160,160, 200,100,140};

	    int[] colas1 = new int[] 
	    		{SWT.CENTER, SWT.LEFT, SWT.CENTER, SWT.CENTER, SWT.CENTER , SWT.CENTER, SWT.CENTER, SWT.CENTER ,SWT.CENTER ,SWT.CENTER };
	    TableViewerColumn tableViewerColumn ;
	    for (int i = 0; i < cols1.length; i++) {
	    	tableViewerColumn =
	    			new TableViewerColumn(tvList, colas1[i]);

	    	TableColumn tableColumn = tableViewerColumn.getColumn();
	    	tableColumn.setText(cols1[i]);
	    	tableColumn.setWidth(columnWidths1[i]);

	    }
	    
	    tblList.addListener(SWT.MeasureItem ,  (arg0) -> {
	    		arg0.height = (int)(arg0.gc.getFontMetrics().getHeight() * 1.6);
	    });
	    
	    tblList.addListener(SWT.MouseDoubleClick, (e) -> {
			int i = tblList.getSelectionIndex() ;
			if (i < 0) return ;
			if (  tblList.getItem(i).getTextBounds(9).contains(e.x, e.y) ) {
				Tmaster tmaster = (Tmaster) tblList.getItem(i).getData() ;
				AqtMain.openTrList("t.tcode = '"+ tmaster.getCode() + "'" ) ; 
				queryScr();
				tvList.refresh() ;

			}
			if ( ! tvList.getChecked(tblList.getItem(i).getData() ) ) return ;	
			if ( ! tblList.getItem(i).getTextBounds(5).contains(e.x, e.y) ) return ;
			Tmaster tmaster = (Tmaster) tblList.getItem(i).getData() ;
			
    		Point pt = parent.getDisplay().getCursorLocation() ; 
        	CalDialog cd = new CalDialog(tblList.getShell() , pt.x, pt.y + 10, tmaster.getTdate() );
        	
            String s = (String)cd.open();
            if (s == null) return ;
            try {
				Date dt =  sdf.parse(s.replace('-', '/')) ;
				tmaster.setTdate(dt);
				tvList.refresh();
			} catch (Exception e2) {
				System.out.println(e2);
				// TODO: handle exception
			}
	    	
	    });
	    
	    tblList.setHeaderVisible(true);
	    
	    tvList.setColumnProperties(cols1);
		CellEditor[] CELL_EDITORS = new CellEditor[cols1.length];
		
		for (int i = 0; i < CELL_EDITORS.length; i++) {
			if ( i == 2 ) {
				CELL_EDITORS[i] = new ComboBoxCellEditor(tblList, IAqtVar.typeArr,  SWT.READ_ONLY ) ;
			} else if ( i == 3 ) {
					CELL_EDITORS[i] = new ComboBoxCellEditor(tblList, IAqtVar.lvlArr , SWT.READ_ONLY ) ;
			} else {
				CELL_EDITORS[i] = new TextCellEditor(tblList, tblList.getColumn(i).getStyle() );
			}
			
		}
		
		CELL_EDITORS[0].setValidator( (input) -> {
				if(input == null || input.toString().isEmpty()){
					AqtMain.aqtmain.setStatus("코드값을 입력하세요." );
					return "코드값을 입력하세요." ;
				}
				TableItem[] items = tblList.getItems() ;
	            for(int i = 0 ; i < items.length ; i++ ){
	                if( ((Tmaster)items[i].getData()).getCode() .equals(input)){
	                	AqtMain.aqtmain.setStatus("이미 사용된 코드값입니다." );
	                    return "이미 사용된 코드값입니다." ;
	                }
	            }
	            AqtMain.aqtmain.setStatus("" );
				return null;
			}
		);
		
		CELL_EDITORS[8].setValidator( (value) -> {
				AqtMain.aqtmain.setStatus("" );
				try {
					Integer.parseInt((String) value);
				} catch (NumberFormatException e) {
					AqtMain.aqtmain.setStatus("숫자만 가능합니다.!!" );
					return "숫자만 가능합니다.!!";
				}
				return null;
			}			
		);

//		CELL_EDITORS[0] = new CheckboxCellEditor(tblList) ;
		tvList.setCellEditors(CELL_EDITORS);
		tvList.setCellModifier(new ICellModifier() {
			
			@Override
			public void modify(Object element, String property, Object value) {
				// TODO Auto-generated method stub
				if (value == null)
					return;
				if (value.toString().isEmpty())
					return;

				if (element instanceof Item)
					element = ((Item) element).getData();

				Tmaster m = (Tmaster) element;
				if (property.equals(cols1[0]))
					m.setCode(value.toString());
				else if (property.equals(cols1[1]))
					m.setDesc1(value.toString());
				else if (property.equals(cols1[2]))
					m.setType(value.toString());
				else if (property.equals(cols1[3]))
					m.setLvl(value.toString());
				else if (property.equals(cols1[4]))
					m.setCmpCode(value.toString());
				else if (property.equals(cols1[5])) 
					try {
						m.setTdate(sdf.parse(value.toString()) );
					} catch (ParseException e) {
						// TODO Auto-generated catch block
					}
					
				else if (property.equals(cols1[7]))
					m.setThost(value.toString());
				else if (property.equals(cols1[8]))
					m.setTport(Integer.parseInt(value.toString()));
				else
					return ;

				// Force the viewer to refresh
				tvList.refresh();
				
			}
			
			@Override
			public Object getValue(Object element, String property) {
				// TODO Auto-generated method stub
				Tmaster t = (Tmaster)element ;
				if (property.equals(cols1[0]))
					return t.getCode();
				else if (property.equals(cols1[1]))
					return t.getDesc1();
				else if (property.equals(cols1[2]))
					return Integer.valueOf(t.getType());
				else if (property.equals(cols1[3]))
					return Integer.valueOf(t.getLvl()) ;
				else if (property.equals(cols1[4]))
					return t.getCmpCode() ;
				else if (property.equals(cols1[5]))
					return t.getTdate() != null ? sdf.format(t.getTdate()):"" ;
				else if (property.equals(cols1[6]))
					return t.getEndDate() != null ? sdf.format(t.getEndDate()):"" ;
				else if (property.equals(cols1[7]))
					return t.getThost() ;
				else if (property.equals(cols1[8]))
					return t.getTport() +"" ;
				else if (property.equals(cols1[9]))
					return t.getDataCnt() ;

				return null;
			}
			
			@Override
			public boolean canModify(Object element, String property) {
				// TODO Auto-generated method stub
				if (tvList.getChecked(element)) {
					Tmaster t = (Tmaster)element ;
					if ( ! (property.equals(cols1[0]) || property.equals(cols1[5]) 
							|| property.equals(cols1[6]) || property.equals(cols1[9]))  ) 
						return true ;

					return ( t.isNew() ) ;
				}

				return false ;
				
			}
			
		});
	    

	    //	    	tblList.pack();		    
	    tvList.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public Object[] getElements(Object input) {
				// TODO Auto-generated method stub
				if (input instanceof List<?> )
					return ((List<Tmaster>)input).toArray() ;

				return null;
			}
		});
	    
	    tvList.setLabelProvider(new ITableLabelProvider() {
			
			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public String getColumnText(Object element, int columnIndex) {
				// TODO Auto-generated method stub
				  Tmaster s = (Tmaster) element;
				  int i ;
				  if ( s != null )
					  switch (columnIndex) {
					  case 0:
						  return s.getCode() ;
					  case 1:
						  return s.getDesc1() ;
					  case 2:
						  i = Integer.valueOf(s.getType()) ;
						  return i < IAqtVar.typeArr.length ? IAqtVar.typeArr[i] : "";
					  case 3:
						  i = Integer.valueOf(s.getLvl()) ;
						  return i < IAqtVar.lvlArr.length ? IAqtVar.lvlArr[i] : "";
					  case 4:
						  return s.getCmpCode();
					  case 5:
						  return s.getTdate() != null ? sdf.format(s.getTdate()) : "";
					  case 6:
						  return s.getEndDate() != null ? sdf.format(s.getEndDate()) : "";
					  case 7:
						  return s.getThost();
					  case 8:
						  return s.getTport() +"";
					  case 9:
						  return String.format("%,d", s.getDataCnt());
					  }
				  return "";
//				return null;
			}
			
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
	
	private void queryScr () {
		
//		EntityManager em = AqtMain.emf.createEntityManager();
//	    tempVtrxList = new ArrayList<Vtrxlist>();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		AqtMain.container.setCursor(IAqtVar.busyc);
//		StringBuilder qstr = new StringBuilder("SELECT t FROM Tmaster t") ; 
//		if (! txCodenm.getText().isEmpty()  ) {
//			qstr.append(" where t.desc1 like '" + txCodenm.getText().trim() + "%'");
//		}
		
        tcodeList = em.createQuery("SELECT t FROM Tmaster t WHERE t.code like :cd and t.desc1 like :nm", Tmaster.class)
        		.setParameter("cd", txCode.getText().trim() + "%")
        		.setParameter("nm", txCodenm.getText().trim() + "%")
        		.getResultList();
        		 
	    tvList.setInput(tcodeList);
	    
	    tblList.setSelection(0);

	    AqtMain.container.setCursor(IAqtVar.arrow);
	    AqtMain.aqtmain.setStatus(String.format(">> 조회건수 %,d 건" ,tcodeList.size() ) );	    
//	    tblList.setFont(IAqtVar.font1b);

	}
	
}