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
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.persistence.internal.sessions.DirectCollectionChangeRecord.NULL;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.wb.swt.SWTResourceManager;

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
	    GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).equalWidth(false).applyTo(container);
	    GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(container);
	    
//		Label ltitle = new Label(compHeader, SWT.NONE);
//    	ltitle.setImage(AqtMain.getMyimage("tit_register.png"));
    	new AqtTitle(container, SWT.NONE, "테스트등록/전문생성", "tit_icon.png");

    	Label lbl = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		lbl.setBackground(container.getBackground());
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(lbl);

    	
		Composite compTit = new Composite(container, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(6).equalWidth(false).applyTo(compTit);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(compTit);
		
		Label lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" *테스트ID");
		lblt.setFont(IAqtVar.font1);

		txCode = new Text(compTit, SWT.BORDER);
		
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).hint(200, -1).applyTo(txCode);
		txCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txCode.setFont(IAqtVar.font1);

		txCode.setText("");

		lblt = new Label(compTit, SWT.NONE);
		lblt.setText("테스트명");
		lblt.setFont(IAqtVar.font1);
		txCodenm = new Text(compTit, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).hint(300, -1).applyTo(txCodenm);
		txCodenm.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txCodenm.setFont(IAqtVar.font1);
		txCodenm.setText("");
		txCodenm.addTraverseListener(new TraverseListener() {
		    @Override
		    public void keyTraversed(final TraverseEvent event)
		    {
		      if (event.detail == SWT.TRAVERSE_RETURN)
		        { 
		    	  queryScr();
		        }
		    }
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
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(false, false).minSize(100, -1).applyTo(btnimp);
		btnimp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String tcode = "";
				int i = tblList.getSelectionIndex() ;
				if ( i >= 0 ) {
					tcode = ((Tmaster) tblList.getItem(i).getData()).getCode() ;
				}
				AqtCopyTdata aqtcopy = new AqtCopyTdata(parent.getShell(), tcode );
				aqtcopy.open() ;
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
    	
//	    tblList.setFont(SWTResourceManager.getFont("맑은 고딕", 15, SWT.NORMAL));
	    
	    tvList.setUseHashlookup(true);
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
        		{  " 테스트ID", "  테스트명", "타입", "단계", "대상코드", "테스트시작일","테스트종료일","대상서버정보", "전문건수"};

        int[] columnWidths1 = new int[] {  150, 300, 80, 80, 150,200,200, 200,140};

	    int[] colas1 = new int[] 
	    		{SWT.CENTER, SWT.LEFT, SWT.CENTER, SWT.CENTER, SWT.CENTER , SWT.CENTER, SWT.CENTER, SWT.CENTER ,SWT.CENTER };
	    TableViewerColumn tableViewerColumn ;
	    for (int i = 0; i < cols1.length; i++) {
	    	tableViewerColumn =
	    			new TableViewerColumn(tvList, colas1[i]);

	    	TableColumn tableColumn = tableViewerColumn.getColumn();
	    	tableColumn.setText(cols1[i]);
	    	tableColumn.setWidth(columnWidths1[i]);
	    	
	    	if ( i == 5) {
	    		tableColumn.addListener(SWT.MouseDoubleClick, new Listener() {
					
					@Override
					public void handleEvent(Event e) {
			    		Point pt = parent.getDisplay().getCursorLocation() ; 
			        	CalDialog cd = new CalDialog(Display.getCurrent().getActiveShell() , pt.x, pt.y + 10 );
			        	
			            String s = (String)cd.open();
			            try {
							Date dt =  sdf.parse(s) ;
							TableItem item = (TableItem) tblList.getSelection() [0];
							Tmaster tmaster = (Tmaster) item.getData() ;
							tmaster.setTdate(dt);
						} catch (Exception e2) {
							System.out.println(e2);
							// TODO: handle exception
						}
						
					}
				});
	    	}
//	    	tableColumn.setResizable(i != 0);

	    }
	    tblList.addListener(SWT.MeasureItem ,  new Listener() {
	    	public void handleEvent(Event arg0) {
	    		arg0.height = (int)(arg0.gc.getFontMetrics().getHeight() * 1.8);

	    	}
	    });
	    tblList.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				TableItem item = (TableItem) ((Table) e.getSource()).getSelection() [0];
				Tmaster tmaster = (Tmaster) item.getData() ;
				AqtRegister aqtregister = new AqtRegister(container.getShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM) ;
				aqtregister.setTmaster(tmaster);
				aqtregister.open();
				
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
				CELL_EDITORS[i] = new TextCellEditor(tblList);
			}
			
		}
		CELL_EDITORS[0].setValidator( new ICellEditorValidator() {
			
			@Override
			public String isValid(Object input) {
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
		});
		
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
					return t.getDataCnt() ;

				return null;
			}
			
			@Override
			public boolean canModify(Object element, String property) {
				// TODO Auto-generated method stub
				if (tvList.getChecked(element)) {
					Tmaster t = (Tmaster)element ;
					if ( ! (property.equals(cols1[0]) || property.equals(cols1[6]) || property.equals(cols1[8]))  ) 
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
	    
	    Menu popupMenu = new Menu(tblList);

	    MenuItem addsvc = new MenuItem(popupMenu, SWT.NONE);
	    addsvc.setText("테스트코드등록");
	    addsvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int i = tblList.getSelectionIndex() ;
				Tmaster to = (Tmaster) tblList.getItem(i).getData() ;
				Tmaster t = new Tmaster() ;
				t.setDesc1("new");
				t.setCmpCode(to.getCmpCode());
				t.setThost(to.getThost());
				t.setLvl(to.getLvl());
				t.setTdate(to.getTdate());
				t.setNew(true);
				em.persist(t);
				tcodeList.add(i,t) ;
				tvList.refresh();
				tvList.setChecked(t, true);
			}
		});


	    MenuItem delsvc = new MenuItem(popupMenu, SWT.NONE);
	    delsvc.setText("삭제");
	    delsvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (tvList.getCheckedElements().length > 0 ) {
					em.getTransaction().begin();
					for ( Object s : tvList.getCheckedElements() ) {
//						if ( ! ((Tmaster)s).isNew() ) em.remove((Tmaster)s);
						em.remove((Tmaster)s);
						tcodeList.remove((Tmaster)s) ;
					}
						
					em.getTransaction().commit();
					tvList.refresh();
					MessageDialog.openInformation(parent.getShell(), "Delete Infomation", "삭제 되었습니다.") ;
				}
			}
		});

	    MenuItem savesvc = new MenuItem(popupMenu, SWT.NONE);
	    savesvc.setText("저장");
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
	    impdat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String tcode = "";
				int i = tblList.getSelectionIndex() ;
				if ( i >= 0 ) {
					tcode = ((Tmaster) tblList.getItem(i).getData()).getCode() ;
				}
				AqtCopyTdata aqtcopy = new AqtCopyTdata(parent.getShell(), tcode );
				aqtcopy.open() ;

			}
		});

	    MenuItem copysvc = new MenuItem(popupMenu, SWT.NONE);
	    copysvc.setText("전문생성");
	    copysvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String tcode = "";
				int i = tblList.getSelectionIndex() ;
				if ( i >= 0 ) {
					tcode = ((Tmaster) tblList.getItem(i).getData()).getCode() ;
				}
				AqtCopyTdata aqtcopy = new AqtCopyTdata(parent.getShell(), tcode );
				aqtcopy.open() ;

			}
		});

	    addsvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    savesvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    delsvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    copysvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    
	    tblList.setMenu(popupMenu);

	}
	
	private void queryScr () {
		
//		EntityManager em = AqtMain.emf.createEntityManager();
//	    tempVtrxList = new ArrayList<Vtrxlist>();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		AqtMain.container.setCursor(IAqtVar.busyc);
		StringBuilder qstr = new StringBuilder("SELECT t FROM Tmaster t") ; 
		if (! txCodenm.getText().isEmpty()  ) {
			qstr.append(" where t.descl like '" + txCodenm.getText().trim() + "%'");
		}
        tcodeList = em.createQuery(qstr.toString(), Tmaster.class).getResultList();
        		 
	    tvList.setInput(tcodeList);
	    
	    tblList.setSelection(0);

	    AqtMain.container.setCursor(IAqtVar.arrow);
	    
	    tblList.setFont(IAqtVar.font1b);

	}
	
}