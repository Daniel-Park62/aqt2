/*
 * 서비스(프로그램) 목록등록
*/

package aqtclient.part;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.PopupList;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Tservice;
import aqtclient.model.Ttcppacket;

public class AqtRegSvc {
	private Table tblList;
	private CheckboxTableViewer tblViewerList;
	private CCombo combo_app ;
	private List <Tservice> tsvcList;
	private Text textsvc , textsvcnm, textasknm ;
	EntityManager em = AqtMain.emf.createEntityManager();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AqtRegSvc(Composite parent, int style) {
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
	    GridLayoutFactory.fillDefaults().margins(15, 15).numColumns(1).equalWidth(true).applyTo(container);
	    GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(container);
	    
//		Composite compHeader = new Composite(container, SWT.NONE);
//		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).equalWidth(true).applyTo(compHeader);
//		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(compHeader);
		
//		CLabel ltitle = new CLabel(compHeader, SWT.NONE);
//    	ltitle.setImage(AqtMain.getMyimage("icon_1.png"));
//    	ltitle.setText(" 서비스 등록");
//    	ltitle.setFont(IAqtVar.font15b);
//    	ltitle.setForeground(SWTResourceManager.getColor(58,115,255));
		new AqtTitle(container, SWT.NONE, "서비스 등록", "tit_icon.png");
		
		Label lbl = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		lbl.setBackground(container.getBackground());
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(lbl);

		Composite compTit = new Composite(container, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(10).equalWidth(false).applyTo(compTit);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(compTit);
		
		Label lblt = new Label(compTit, SWT.NONE);
		lblt.setText("*APPID");
		lblt.setFont(IAqtVar.font1);

		combo_app = new CCombo(compTit, SWT.READ_ONLY | SWT.FLAT | SWT.BORDER);
		
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).applyTo(combo_app);
		combo_app.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		combo_app.setFont(IAqtVar.font1);
		combo_app.setItems( Stream.concat(Arrays.stream(new String[] {"ALL"}), Arrays.stream(getAppList()) ).toArray(String[]::new) );
		combo_app.setText("ALL");
		combo_app.requestLayout();
		combo_app.addSelectionListener( new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				queryScr();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" *서비스");
		lblt.setFont(IAqtVar.font1);

		textsvc = new Text(compTit, SWT.BORDER);
		
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).hint(200, -1).applyTo(textsvc);
		textsvc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textsvc.setFont(IAqtVar.font1);
		textsvc.setText("");

		lblt = new Label(compTit, SWT.NONE);
		lblt.setText("서비스명");
		lblt.setFont(IAqtVar.font1);
		textsvcnm = new Text(compTit, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).hint(300, -1).applyTo(textsvcnm);
		textsvcnm.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textsvcnm.setFont(IAqtVar.font1);
		textsvcnm.setText("");
		textsvcnm.addTraverseListener((final TraverseEvent event) -> {
		      if (event.detail == SWT.TRAVERSE_RETURN)	  queryScr();
		  });

		lblt = new Label(compTit, SWT.NONE);
		lblt.setText("업무명");
		lblt.setFont(IAqtVar.font1);
		textasknm = new Text(compTit, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).hint(300, -1).applyTo(textasknm);
		textasknm.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textasknm.setFont(IAqtVar.font1);
		textasknm.setText("");
		textasknm.addTraverseListener((final TraverseEvent event) -> {
		      if (event.detail == SWT.TRAVERSE_RETURN)	  queryScr();
		  });

		AqtButton btnSearch = new AqtButton(compTit, SWT.PUSH,"조회");
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(true, false).minSize(100, -1).applyTo(btnSearch);
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				queryScr();
			}
		});
		
		AqtButton btnimp = new AqtButton(compTit, SWT.PUSH,"파일에서가져오기");
		btnimp.setToolTipText("콤마로 분리된 CSV 형식 파일로 부터 읽어 등록합니다.\n 데이터의 항목순서는 표와같습니다.");
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(false, false).minSize(100, -1).applyTo(btnimp);
		btnimp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				tblList.getMenu().getItem(1).notifyListeners(SWT.Selection, null);
			}
		});
		
		btnimp.setEnabled( AqtMain.authtype == AuthType.TESTADM );

// 	    Composite compDetail = new Composite(container, SWT.NONE);
// 	    GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(compDetail);
//		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(true).applyTo(compDetail);

    	tblViewerList = CheckboxTableViewer.newCheckList(container, SWT.NONE | SWT.FULL_SELECTION );
    	tblViewerList.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				return compareElements( e1, e2);
			}
		});
    	tblList = tblViewerList.getTable();
	    createPop(parent);
    	
	    GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(tblList);
		tblList.setLinesVisible(true);
    	tblList.setHeaderBackground(AqtMain.htcol);
    	tblList.setHeaderForeground(AqtMain.forecol);
    	tblList.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
    	tblList.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
	    tblList.setFont(IAqtVar.font1b);
	    tblList.setHeaderVisible(true);
	    
	    tblViewerList.setUseHashlookup(true);
	    tblList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
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
	    
        String[] cols1 = new String[] 
        		{  " APP ID", "서비스(URI)", "  내용설명(한글)", "  설명(영문)", "업무명", "담당자", "서비스종류"};

        int[] columnWidths1 = new int[] {  130,200, 300, 300, 200,100, 150};

	    int[] colas1 = new int[] 
	    		{SWT.CENTER, SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.CENTER };
	    TableViewerColumn tableViewerColumn ;
	    for (int i = 0; i < cols1.length; i++) {
	    	tableViewerColumn =
	    			new TableViewerColumn(tblViewerList, colas1[i]);

	    	TableColumn tcol = tableViewerColumn.getColumn();
	    	tcol.setText(cols1[i]);
	    	tcol.setWidth(columnWidths1[i]);
	    	tcol.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					Table table = tcol.getParent() ;
					if (tcol.equals(table.getSortColumn())) {
						int dire = table.getSortDirection() ;
						table.setSortDirection(dire == SWT.UP ? SWT.DOWN : dire == SWT.NONE ? SWT.UP : SWT.NONE );
					} else {
						table.setSortColumn(tcol);
						table.setSortDirection(SWT.UP);
					}
					tblViewerList.refresh();
				}
			});
//	    	tableColumn.setResizable(i != 0);

	    }
	    tblList.addListener(SWT.MeasureItem ,  (Event arg0) -> 
	    		arg0.height = (int)(arg0.gc.getFontMetrics().getHeight() * 1.6) 
	    );

	    tblList.addListener(SWT.MouseDoubleClick, (e) -> {
			int i = tblList.getSelectionIndex() ;
			if (i < 0) return ;
			if ( ! tblViewerList.getChecked(tblList.getItem(i).getData() ) ) return ;	
			if ( ! tblList.getItem(i).getTextBounds(0).contains(e.x, e.y) ) return ;
			Tservice tsvc = (Tservice) tblList.getItem(i).getData() ;
			
			PopupList lst = new PopupList(parent.getShell() , 5);
			lst.setFont(IAqtVar.font1);
			lst.setItems(getAppList()) ;
	        lst.select( tsvc.getAppid() );
	        Point pt = parent.getDisplay().getCursorLocation() ;

	        String selected = lst.open(new Rectangle(pt.x, pt.y - 40, 80, 30));
	        if (selected == null) return ;
	        tsvc.setAppid(selected);
	        tblViewerList.refresh();
	    	
	    });

	    
	    tblViewerList.setColumnProperties(cols1);
		CellEditor[] CELL_EDITORS = new CellEditor[cols1.length];
		for (int i = 0; i < CELL_EDITORS.length; i++) {
			CELL_EDITORS[i] = new TextCellEditor(tblList);
		}
//		CELL_EDITORS[0] = new CheckboxCellEditor(tblList) ;
		tblViewerList.setCellEditors(CELL_EDITORS);
		tblViewerList.setCellModifier(new ICellModifier() {
			
			@Override
			public void modify(Object element, String property, Object value) {
				// TODO Auto-generated method stub
				if (value == null)
					return;
				if (value.toString().isEmpty())
					return;

				if (element instanceof Item)
					element = ((Item) element).getData();

				Tservice m = (Tservice) element;
				if (property.equals(cols1[0]))
					m.setAppid(value.toString());
				else if (property.equals(cols1[1]))
					m.setSvcid(value.toString());
				else if (property.equals(cols1[2]))
					m.setSvckor(value.toString());
				else if (property.equals(cols1[3]))
					m.setSvceng(value.toString());
				else if (property.equals(cols1[4]))
					m.setTask(value.toString());
				else if (property.equals(cols1[5]))
					m.setManager(value.toString());
				else if (property.equals(cols1[6]))
					m.setSvckind(value.toString());
				else
					return ;

				// Force the viewer to refresh
				tblViewerList.refresh();
				
			}
			
			@Override
			public Object getValue(Object element, String property) {
				// TODO Auto-generated method stub
				Tservice t = (Tservice)element ;
				if (property.equals(cols1[0]))
					return t.getAppid();
				else if (property.equals(cols1[1]))
					return t.getSvcid();
				else if (property.equals(cols1[2]))
					return t.getSvckor();
				else if (property.equals(cols1[3]))
					return t.getSvceng();
				else if (property.equals(cols1[4]))
					return t.getTask() ;
				else if (property.equals(cols1[5]))
					return t.getManager() ;
				else if (property.equals(cols1[6]))
					return t.getSvckind() ;
			
				return null;
			}
			
			@Override
			public boolean canModify(Object element, String property) {
				// TODO Auto-generated method stub
				if (tblViewerList.getChecked(element) && !  property.equals(cols1[0]) )  return true ;

				return false ;
				
			}
		});
	    

	    //	    	tblList.pack();		    
	    tblViewerList.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public Object[] getElements(Object input) {
				// TODO Auto-generated method stub
				if (input instanceof List<?> )
					return ((List<Tservice>)input).toArray() ;

				return null;
			}
		});
	    
	    tblViewerList.setLabelProvider(new ITableLabelProvider() {
			
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
				  Tservice s = (Tservice) element;
				  
				  if ( s != null )
					  switch (columnIndex) {
					  case 0:
						  return s.getAppid() ;
					  case 1:
						  return s.getSvcid() ;
					  case 2:
						  return s.getSvckor();
					  case 3:
						  return s.getSvceng();
					  case 4:
						  return s.getTask();
					  case 5:
						  return s.getManager();
					  case 6:
						  return s.getSvckind();
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

	private String[] getAppList() {
		return em.createQuery("select a.appid from Tapplication a ", String.class)
				.getResultList().stream().toArray(size -> new String[size]);
	}

	private void createPop(Composite parent) {
		Menu popupMenu = new Menu(tblList);

	    MenuItem addsvc = new MenuItem(popupMenu, SWT.NONE);
	    addsvc.setText("서비스등록");
	    addsvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int i = tblList.getSelectionIndex() ;
				Tservice t = new Tservice() ;
				t.setSvceng("new");
				t.setNew(true);
				em.persist(t);
				tsvcList.add(i,t) ;
				tblViewerList.refresh();
				tblViewerList.setChecked(t, true);
			}
		});

	    MenuItem impcsv = new MenuItem(popupMenu, SWT.NONE);
	    impcsv.setText("파일에서 가져오기");
	    impcsv.setToolTipText("콤마로 분리된 CSV 형식 파일로 부터 읽어 등록합니다.\n 데이터의 항목순서는 표와같습니다.");
	    impcsv.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					importCsv();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tblViewerList.refresh();
				tblViewerList.setAllChecked(true);
			}
		});

	    MenuItem delsvc = new MenuItem(popupMenu, SWT.NONE);
	    delsvc.setText("삭제");
	    delsvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (tblViewerList.getCheckedElements().length > 0 ) {
					em.getTransaction().begin();
					for ( Object s : tblViewerList.getCheckedElements() ) {
//						if ( ! ((Tservice)s).isNew() ) em.remove((Tservice)s);
						em.remove((Tservice)s);
						tsvcList.remove((Tservice)s) ;
					}
						
					em.getTransaction().commit();
					tblViewerList.refresh();
					MessageDialog.openInformation(parent.getShell(), "Delete Infomation", "삭제 되었습니다.") ;
				}
			}
		});

	    MenuItem savesvc = new MenuItem(popupMenu, SWT.NONE);
	    savesvc.setText("저장");
	    savesvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (tblViewerList.getCheckedElements().length > 0 ) {
					em.getTransaction().begin();
//					for ( Object s : tblViewerList.getCheckedElements() ) { 
//						em.merge((Tservice)s);
//						((Tservice)s).setNew(false);
//					}
					em.getTransaction().commit();
					MessageDialog.openInformation(parent.getShell(), "Save Infomation", "수정 되었습니다.") ;
					tblViewerList.refresh();
					tblViewerList.setAllChecked(false);
				}
			}
		});
	    addsvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    savesvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    impcsv.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    delsvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    
	    tblList.setMenu(popupMenu);
	}
	
	private void queryScr () {
		
//		EntityManager em = AqtMain.emf.createEntityManager();
//	    tempVtrxList = new ArrayList<Vtrxlist>();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		AqtMain.container.setCursor(IAqtVar.busyc);
		StringBuilder qstr = new StringBuilder("SELECT t FROM Tservice t where 1=1 ") ; 
		if (! combo_app.getText().equals("ALL")) 
			qstr.append(" and t.appid = '" + combo_app.getText() + "'");
		if (! textsvc.getText().isEmpty()  ) 
			qstr.append(" and t.svcid like '%" + textsvc.getText().trim() + "%'");
		if (! textsvcnm.getText().isEmpty()  ) 
			qstr.append("  and t.svckor like '%" + textsvcnm.getText().trim() + "%'");
		if (! textasknm.getText().isEmpty()  ) 
			qstr.append("  and t.task like '%" + textasknm.getText().trim() + "%'");
		
        tsvcList = em.createQuery(qstr.toString(), Tservice.class).getResultList();
        		 
	    tblViewerList.setInput(tsvcList);
	    
	    tblList.setSelection(0);

	    AqtMain.container.setCursor(IAqtVar.arrow);
	    

	}
	
	private void importCsv() throws FileNotFoundException, UnsupportedEncodingException {
    	String[] ext = { "*.csv" }  ;
    	final FileDialog dlg = new FileDialog ( Display.getDefault().getActiveShell() , SWT.APPLICATION_MODAL | SWT.OPEN );
    	dlg.setFilterExtensions ( ext );
    	dlg.setOverwrite ( true );
    	dlg.setText ( "파일명 선택" );

    	String fileName = dlg.open ();
    	if ( fileName == null )
    	{
    		return ;
    	}
    	FileInputStream fis = new FileInputStream(fileName);
    	InputStreamReader isr = new InputStreamReader(fis, "MS949");
    	try (BufferedReader br = new BufferedReader(isr)) {
    	    String line;
    	    tsvcList.clear();
    	    while ((line = br.readLine()) != null) {
    	        String[] v = line.split(",");
    	        if (v.length < 2) continue ;
    	        Tservice s = new Tservice() ;
    	        s.setNew(true);
    	        for (int i=0; v.length > i ; i++) {
    	        	switch (i) {
    	        	case 0:
    	        		s.setAppid(v[i]);
    	        		continue ;
    	        	case 1:
    	        		s.setSvcid(v[i]);
    	        		continue ;
    	        	case 2:
    	        		s.setSvckor(v[i]);
    	        		continue ;
    	        	case 3:
    	        		s.setSvceng(v[i]);
    	        		continue ;
    	        	case 4:
    	        		s.setTask(v[i]);
    	        		continue ;
    	        	case 5:
    	        		s.setManager(v[i]);
    	        		continue ;
    	        	case 6:
    	        		s.setSvcid(v[i]);
    	        		continue ;
    	        	}
    	        }
    	        
    	        s = em.merge(s);
    	        tsvcList.add(s) ;
    	    }
    	    tblViewerList.setInput(tsvcList);
    	    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MessageDialog.openError(AqtMain.aqtmain.getShell()  , "파일입력오류", e.toString() );
		}
		
	}
    protected int compareElements(Object e1, Object e2) {
		Table table = tblViewerList.getTable();
		int index = Arrays.asList(table.getColumns()).indexOf(table.getSortColumn());
		int result = 0;
		LocalDateTime d1, d2 ;
		Long l1, l2 ;
		Double db1,db2;
		String s1,s2 ;
		if (index != -1) {
			switch (index) {
			case 0:
				s1 = ((Tservice)e1).getAppid() ;
				s2 = ((Tservice)e2).getAppid() ;
				result = s1.compareTo(s2);
				break;
			case 1:
				s1 = ((Tservice)e1).getSvcid() ;
				s2 = ((Tservice)e2).getSvcid() ;
				result = s1.compareTo(s2);
				break;
			case 2:
				s1 = ((Tservice)e1).getSvckor() ;
				s2 = ((Tservice)e2).getSvckor() ;
				result = s1.compareTo(s2);
				break;
			case 3:
				s1 = ((Tservice)e1).getSvceng() ;
				s2 = ((Tservice)e2).getSvceng() ;
				result = s1.compareTo(s2);
				break;
			case 4:
				s1 = ((Tservice)e1).getTask() ;
				s2 = ((Tservice)e2).getTask() ;
				result = s1.compareTo(s2);
				break;
			case 5:
				s1 = ((Tservice)e1).getManager() ;
				s2 = ((Tservice)e2).getManager() ;
				result = s1.compareTo(s2);
				break;
			}
		}
		return table.getSortDirection() == SWT.UP ? result : -result;
	}

}