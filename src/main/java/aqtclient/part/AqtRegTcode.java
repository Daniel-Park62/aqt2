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
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Tmaster;

public class AqtRegTcode {
	private Table tblList;
	private CheckboxTableViewer tvList;
	
	private List <Tmaster> tcodeList;
	private Text txCode , txCodenm ;
	EntityManager em = AqtMain.emf.createEntityManager();

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
	    GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).applyTo(container);
	    
		Composite compHeader = new Composite(container, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).equalWidth(true).applyTo(compHeader);
		
		Label ltitle = new Label(compHeader, SWT.NONE);
		
    	ltitle.setImage(SWTResourceManager.getImage("images/tit_register.png"));

		Composite compTit = new Composite(container, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).applyTo(compTit);
		
		Label lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" *테스트코드");
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
		
		Label btnSearch = new Label(compTit, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(btnSearch);
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				queryScr();
			}
		});
		btnSearch.setCursor(IAqtVar.handc);
		btnSearch.setImage(SWTResourceManager.getImage("images/search.png"));

		
    	Label lbl = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		lbl.setBackground(container.getBackground());
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(lbl);

 	    Composite compDetail = new Composite(container, SWT.NONE);
 	    GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(compDetail);
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).equalWidth(true).applyTo(compDetail);

    	tvList = CheckboxTableViewer.newCheckList(compDetail, SWT.NONE | SWT.FULL_SELECTION );
    	
    	tblList = tvList.getTable();
    	
	    GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(tblList);
		tblList.setLinesVisible(true);
    	tblList.setHeaderBackground(AqtMain.htcol);
    	tblList.setHeaderForeground(AqtMain.forecol);
    	tblList.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
    	tblList.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    	
	    tblList.setFont(SWTResourceManager.getFont("맑은 고딕", 15, SWT.NORMAL));
	    
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
        		{  " 테스트코드", "  테스트명", "타입", "단계", "비교대상코드", "테스트시작일","테스트종료일","대상서버정보"};

        int[] columnWidths1 = new int[] {  150, 300, 80, 80, 150,150,150, 150};

	    int[] colas1 = new int[] 
	    		{SWT.CENTER, SWT.LEFT, SWT.CENTER, SWT.CENTER, SWT.CENTER , SWT.CENTER, SWT.CENTER, SWT.CENTER };
	    TableViewerColumn tableViewerColumn ;
	    for (int i = 0; i < cols1.length; i++) {
	    	tableViewerColumn =
	    			new TableViewerColumn(tvList, colas1[i]);

	    	TableColumn tableColumn = tableViewerColumn.getColumn();
	    	tableColumn.setText(cols1[i]);
	    	tableColumn.setWidth(columnWidths1[i]);
//	    	tableColumn.setResizable(i != 0);

	    }
	    tblList.addListener(SWT.MeasureItem ,  new Listener() {
	    	public void handleEvent(Event arg0) {
	    		arg0.height = (int)(arg0.gc.getFontMetrics().getHeight() * 1.8);

	    	}
	    });
	    tblList.setHeaderVisible(true);
	    
	    tvList.setColumnProperties(cols1);
		CellEditor[] CELL_EDITORS = new CellEditor[cols1.length];
		for (int i = 0; i < CELL_EDITORS.length; i++) {
			CELL_EDITORS[i] = new TextCellEditor(tblList);
		}
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
				if (property.equals(cols1[1]))
					m.setDesc1(value.toString());
				else if (property.equals(cols1[2]))
					m.setType(value.toString());
				else if (property.equals(cols1[3]))
					m.setLvl(value.toString());
				else if (property.equals(cols1[4]))
					m.setCmpCode(value.toString());
				else if (property.equals(cols1[5]))
					m.setTdate(value.toString());
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
					return t.getTcode();
				else if (property.equals(cols1[1]))
					return t.getDesc1();
				else if (property.equals(cols1[2]))
					return t.getType();
				else if (property.equals(cols1[3]))
					return t.getLvl() ;
				else if (property.equals(cols1[4]))
					return t.getCmpCode() ;
				else if (property.equals(cols1[5]))
					return t.getTdate() ;
				else if (property.equals(cols1[6]))
					return t.getEndDate() ;
				else if (property.equals(cols1[7]))
					return t.getThost() ;

				return null;
			}
			
			@Override
			public boolean canModify(Object element, String property) {
				// TODO Auto-generated method stub
				if (tvList.getChecked(element)) {
					Tmaster t = (Tmaster)element ;
					if ( ! property.equals(cols1[0]) ) return true ;

					return ( t.getTcode() == 0 ) ;
				}
				else
					return false ;
				
//				if (property.equals(cols1[1]) || property.equals(cols1[2]) || property.equals(cols1[3]) ) {
//					return true ;
//				}
//				Tmaster t = (Tmaster)element ;
//				if ( property.equals(cols1[0]) && t.getSvcid().isEmpty() ) return true;
//				return false;
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
				  
				  if ( s != null )
					  switch (columnIndex) {
					  case 0:
						  return s.getTcode() + "" ;
					  case 1:
						  return s.getDesc1() ;
					  case 2:
						  return s.getType();
					  case 3:
						  return s.getLvl();
					  case 4:
						  return s.getCmpCode();
					  case 5:
						  return s.getTdate();
					  case 6:
						  return s.getEndDate();
					  case 7:
						  return s.getThost();
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
				Tmaster t = new Tmaster() ;
				t.setDesc1("new");
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
	    addsvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    savesvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    delsvc.setEnabled( AqtMain.authtype == AuthType.TESTADM );
	    
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