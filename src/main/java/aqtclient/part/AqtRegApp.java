/*
 * App 등록
*/

package aqtclient.part;

import java.util.List;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Tapphost;
import aqtclient.model.Tapplication;

public class AqtRegApp {
	private Table tblList, tbl2;
	private CheckboxTableViewer tblView1, tblView2 ;
	
	private List <Tapplication> tappList;
	private Tapplication curr_app ;
	
	EntityManager em = AqtMain.emf.createEntityManager();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AqtRegApp(Composite parent, int style) {
		createscr (parent, style);
		queryScr();

	}
	
	private void createscr (Composite parent, int style) {
	    Composite container = new Composite(parent, SWT.NONE) ;
	    GridLayoutFactory.fillDefaults().margins(15, 15).numColumns(2).equalWidth(true).applyTo(container);
	    GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(container);
	    
		new AqtTitle(container, SWT.NONE, "APP 등록", "tit_icon.png");
		AqtButton btnSearch = new AqtButton(container, SWT.PUSH,"조회");
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(true, false).minSize(100, -1).applyTo(btnSearch);
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				queryScr();
			}
		});
		
    	Label lbl = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		lbl.setBackground(container.getBackground());
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).align(SWT.FILL, SWT.FILL).applyTo(lbl);
		
		SashForm sashform = new SashForm(container, SWT.HORIZONTAL) ;
		GridDataFactory.fillDefaults().grab(true, true).span(2, 1).align(SWT.FILL, SWT.FILL).applyTo(sashform);
		
		Composite comp1 = new Composite(sashform, SWT.NONE) ;
		Composite comp2 = new Composite(sashform, SWT.NONE) ;
		GridLayoutFactory.fillDefaults().margins(0, 0).equalWidth(false).applyTo(comp1);
		GridLayoutFactory.fillDefaults().margins(0, 0).applyTo(comp2);

		lbl = new Label(comp1, SWT.NONE) ;
		lbl.setText("* 업무ID 등록");
		lbl = new Label(comp2, SWT.NONE) ;
		lbl.setText("* 업무ID별 IP 및 서비스포트 지정");
		
    	tblView1 = CheckboxTableViewer.newCheckList(comp1, SWT.BORDER | SWT.FULL_SELECTION );
    	tblView2 = CheckboxTableViewer.newCheckList(comp2, SWT.BORDER | SWT.FULL_SELECTION );
    	
    	createTv1( parent ) ;
    	createTv2( parent ) ; 

    	sashform.setSashWidth(5);
    	sashform.setWeights(new int[] {7,4});
    	sashform.requestLayout();


	}

	private void createTv1(Composite parent) {
    	tblList = tblView1.getTable();
	    createPop(parent, new Menu(tblList) );
    	
	    GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(tblList);
		tblList.setLinesVisible(true);
    	tblList.setHeaderBackground(AqtMain.htcol);
    	tblList.setHeaderForeground(AqtMain.forecol);
    	tblList.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
    	tblList.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
	    tblList.setFont(IAqtVar.font1b);

	    tblView1.setUseHashlookup(true);
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
        		{  " APP Id", "  APP 설명", "담당자" };

        int[] columnWidths1 = new int[] {  200, 300,  200};

	    int[] colas1 = new int[] 
	    		{SWT.CENTER, SWT.LEFT, SWT.LEFT };
	    TableViewerColumn tableViewerColumn ;
	    for (int i = 0; i < cols1.length; i++) {
	    	tableViewerColumn =
	    			new TableViewerColumn(tblView1, colas1[i]);

	    	TableColumn tableColumn = tableViewerColumn.getColumn();
	    	tableColumn.setText(cols1[i]);
	    	tableColumn.setWidth(columnWidths1[i]);
//	    	tableColumn.setResizable(i != 0);

	    }
	    tblList.addListener(SWT.MeasureItem ,  new Listener() {
	    	public void handleEvent(Event arg0) {
	    		arg0.height = (int)(arg0.gc.getFontMetrics().getHeight() * 1.6);

	    	}
	    });
	    tblList.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
				int i = tblList.getSelectionIndex() ;
				if (i >= 0) {
					curr_app = (Tapplication) tblList.getItem(i).getData() ;
					tblView2.setInput(curr_app.getTapphosts());
					tblView2.refresh();
				}
	    		super.widgetSelected(e);
	    	}
	    });
	    
	    tblList.setHeaderVisible(true);
	    
	    tblView1.setColumnProperties(cols1);
		CellEditor[] CELL_EDITORS = new CellEditor[cols1.length];
		for (int i = 0; i < CELL_EDITORS.length; i++) {
			CELL_EDITORS[i] = new TextCellEditor(tblList);
		}
//		CELL_EDITORS[0] = new CheckboxCellEditor(tblList) ;
		tblView1.setCellEditors(CELL_EDITORS);
		tblView1.setCellModifier(new ICellModifier() {
			
			@Override
			public void modify(Object element, String property, Object value) {
				// TODO Auto-generated method stub
				if (value == null)
					return;
				if (value.toString().isEmpty())
					return;

				if (element instanceof Item)
					element = ((Item) element).getData();

				Tapplication m = (Tapplication) element;
				if (property.equals(cols1[0]))
					m.setAppid(value.toString().trim());
				else if (property.equals(cols1[1]))
					m.setAppnm(value.toString());
				else if (property.equals(cols1[2]))
					m.setManager(value.toString());
				else
					return ;

				// Force the viewer to refresh
				tblView1.refresh();
				
			}
			
			@Override
			public Object getValue(Object element, String property) {
				// TODO Auto-generated method stub
				Tapplication t = (Tapplication)element ;
				if (property.equals(cols1[0]))
					return t.getAppid();
				else if (property.equals(cols1[1]))
					return t.getAppnm();
				else if (property.equals(cols1[2]))
					return t.getManager();
			
				return null;
			}
			
			@Override
			public boolean canModify(Object element, String property) {
				// TODO Auto-generated method stub
				return ( tblView1.getChecked(element) ) ;
			}
		});
	    

	    //	    	tblList.pack();		    
	    tblView1.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public Object[] getElements(Object input) {
				// TODO Auto-generated method stub
				if (input instanceof List<?> )
					return ((List<Tapplication>)input).toArray() ;

				return null;
			}
		});
	    
	    tblView1.setLabelProvider(new ITableLabelProvider() {
			
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
				  Tapplication s = (Tapplication) element;
				  
				  if ( s != null )
					  switch (columnIndex) {
					  case 0:
						  return s.getAppid() ;
					  case 1:
						  return s.getAppnm();
					  case 2:
						  return s.getManager();
					  }
				  return "";
			}
			
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	    
	}

	private void createTv2(Composite parent) {
    	tbl2 = tblView2.getTable();
	    createPop2(parent, new Menu(tbl2) );

	    GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(tbl2);
		tbl2.setLinesVisible(true);
    	tbl2.setHeaderBackground(AqtMain.htcol);
    	tbl2.setHeaderForeground(AqtMain.forecol);
    	tbl2.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
    	tbl2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
	    tbl2.setFont(IAqtVar.font1b);

	    tblView2.setUseHashlookup(true);
	    tbl2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int i = tbl2.getSelectionIndex() ;
				if (i >= 0 && e.stateMask == SWT.CTRL && (e.keyCode == 'c' || e.keyCode == 'C')) {
					Clipboard clipboard = new Clipboard(Display.getDefault());
					String sdata =  "" ; // tbl2.getItem(i).getText(gcol);
					
					for (int j = 0;j<tbl2.getColumnCount(); j++) {
						sdata += tbl2.getItem(i).getText(j) + "\t" ;
					}
					
					clipboard.setContents(new Object[] { sdata }, new Transfer[] { TextTransfer.getInstance() });
					clipboard.dispose();
				}
			}
		});
	    
        String[] cols1 = new String[] 
        		{  " App id", " Host", "Port" };

        int[] columnWidths1 = new int[] {  200, 200,  100};

	    int[] colas1 = new int[] 
	    		{SWT.CENTER, SWT.LEFT, SWT.LEFT };
	    TableViewerColumn tableViewerColumn ;
	    for (int i = 0; i < cols1.length; i++) {
	    	tableViewerColumn =
	    			new TableViewerColumn(tblView2, colas1[i]);

	    	TableColumn tableColumn = tableViewerColumn.getColumn();
	    	tableColumn.setText(cols1[i]);
	    	tableColumn.setWidth(columnWidths1[i]);
//	    	tableColumn.setResizable(i != 0);

	    }
	    tbl2.addListener(SWT.MeasureItem ,  new Listener() {
	    	public void handleEvent(Event arg0) {
	    		arg0.height = (int)(arg0.gc.getFontMetrics().getHeight() * 1.6);

	    	}
	    });
	    tbl2.setHeaderVisible(true);
	    
	    tblView2.setColumnProperties(cols1);
		CellEditor[] CELL_EDITORS = new CellEditor[cols1.length];
		for (int i = 0; i < CELL_EDITORS.length; i++) {
			CELL_EDITORS[i] = new TextCellEditor(tbl2);
		}
		tblView2.setCellEditors(CELL_EDITORS);
		tblView2.setCellModifier(new ICellModifier() {
			
			@Override
			public void modify(Object element, String property, Object value) {
				// TODO Auto-generated method stub
				if (value == null)
					return;
				if (value.toString().isEmpty())
					return;

				if (element instanceof Item)
					element = ((Item) element).getData();

				Tapphost m = (Tapphost) element;
				if (property.equals(cols1[0]) )
					if ( tblView2.getChecked(element) )
						m.setTapplication(curr_app);
					else
						m.setTapplication(null);
				else if (property.equals(cols1[1]))
					m.setThost(value.toString());
				else if (property.equals(cols1[2]))
					m.setTport(Integer.parseInt(value.toString()));
				else
					return ;

				// Force the viewer to refresh
				tblView2.refresh();
				
			}
			
			@Override
			public Object getValue(Object element, String property) {
				// TODO Auto-generated method stub
				Tapphost t = (Tapphost)element ;
				if (property.equals(cols1[0]))
					return t.getTapplication().getAppid();
				else if (property.equals(cols1[1]))
					return t.getThost();
				else if (property.equals(cols1[2]))
					return t.getTport()+"";
			
				return null;
			}
			
			@Override
			public boolean canModify(Object element, String property) {
				// TODO Auto-generated method stub
				return (!property.equals(cols1[0]) && tblView2.getChecked(element)) ;
			}
		});
	    

	    //	    	tbl2.pack();		    
	    tblView2.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public Object[] getElements(Object input) {
				// TODO Auto-generated method stub
				if (input instanceof List<?> )
					return ((List<Tapphost>)input).toArray() ;

				return null;
			}
		});
	    
	    tblView2.setLabelProvider(new ITableLabelProvider() {
			@Override
			public String getColumnText(Object element, int columnIndex) {
				// TODO Auto-generated method stub
				  Tapphost s = (Tapphost) element;
				  
				  if ( s != null )
					  switch (columnIndex) {
					  case 0:
						  return s.getTapplication().getAppid() ;
					  case 1:
						  return s.getThost();
					  case 2:
						  return s.getTport() + "";
					  }
				  return "";
			}
			
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				// TODO Auto-generated method stub
				return null;
			}
			
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
			
		});
	    		
	}

	private void createPop(Composite parent, Menu popupMenu) {

	    MenuItem addsvc = new MenuItem(popupMenu, SWT.NONE);
	    addsvc.setText("새로등록");
	    addsvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int i = tblList.getSelectionIndex() ;
				if ( i < 0) i = 0 ;
				Tapplication t = new Tapplication() ;
				t.setAppid("id");
				t.setAppnm("new");
				t.setManager("담당자");
				em.persist(t);
				tappList.add(i,t) ;
				tblView1.refresh();
				tblView1.setChecked(t, true);
			}
		});

	    MenuItem delsvc = new MenuItem(popupMenu, SWT.NONE);
	    delsvc.setText("삭제");
	    delsvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (tblView1.getCheckedElements().length > 0 ) {
					em.getTransaction().begin();
					for ( Object s : tblView1.getCheckedElements() ) {
						em.remove((Tapplication)s);
						tappList.remove((Tapplication)s) ;
					}
					try {
						em.getTransaction().commit();
						tblView1.refresh();
						MessageDialog.openInformation(parent.getShell(), "Delete Infomation", "삭제 되었습니다.") ;
					} catch (Exception e) {
						MessageDialog.openInformation(parent.getShell(), "Delete Error", e.getMessage()) ;
					}
				}
			}
		});

	    MenuItem savesvc = new MenuItem(popupMenu, SWT.NONE);
	    savesvc.setText("저장");
	    savesvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (tblView1.getCheckedElements().length > 0 ) {
					try {
						em.getTransaction().begin();
						em.getTransaction().commit();
						MessageDialog.openInformation(parent.getShell(), "Save Infomation", "수정 되었습니다.") ;
						tblView1.refresh();
						tblView1.setAllChecked(false);
					} catch (Exception e) {
						MessageDialog.openInformation(parent.getShell(), "Save Error", e.getMessage()) ;
					}
				}
			}
		});
	    addsvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    savesvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    delsvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    
	    tblList.setMenu(popupMenu);
	}
	private void createPop2(Composite parent, Menu popupMenu) {

	    MenuItem addsvc = new MenuItem(popupMenu, SWT.NONE);
	    addsvc.setText("새로등록");
	    addsvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int i = tbl2.getSelectionIndex() ;
				if ( i < 0) i = 0 ;
				Tapphost t = new Tapphost() ;
				t.setTapplication(curr_app);
//				t.setAppid(curr_app.getAppid());
				t.setThost("0.0.0.0");
//				System.out.println(t.getTapplication());
				em.persist(t);
				curr_app.getTapphosts().add(i,t) ;
				tblView2.refresh();
				tblView2.setChecked(t, true);
			}
		});

	    MenuItem delsvc = new MenuItem(popupMenu, SWT.NONE);
	    delsvc.setText("삭제");
	    delsvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (tblView2.getCheckedElements().length > 0 ) {
					em.getTransaction().begin();
					for ( Object s : tblView2.getCheckedElements() ) {
						em.remove((Tapphost)s);
						curr_app.getTapphosts().remove((Tapphost)s) ;
					}
						
					em.getTransaction().commit();
					tblView2.refresh();
					MessageDialog.openInformation(parent.getShell(), "Delete Infomation", "삭제 되었습니다.") ;
				}
			}
		});

	    MenuItem savesvc = new MenuItem(popupMenu, SWT.NONE);
	    savesvc.setText("저장");
	    savesvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (tblView2.getCheckedElements().length > 0 ) {
					em.getTransaction().begin();
					em.getTransaction().commit();
					MessageDialog.openInformation(parent.getShell(), "Save Infomation", "수정 되었습니다.") ;
					tblView2.refresh();
				}
			}
		});
	    addsvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    savesvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    delsvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    
	    tbl2.setMenu(popupMenu);
	}
	
	
	private void queryScr () {
		
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		AqtMain.container.setCursor(IAqtVar.busyc);
		
        tappList = em.createNamedQuery("Tapplication.findAll", Tapplication.class).getResultList();
        		 
	    tblView1.setInput(tappList);
	    
	    tblList.setSelection(0);
	    tblList.notifyListeners( SWT.Selection, null);
	    AqtMain.container.setCursor(IAqtVar.arrow);
	    

	}
	

}