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

import aqtclient.model.Tservice;

public class AqtRegSvc {
	private Table tblList;
	private CheckboxTableViewer tblViewerList;
	
	private List <Tservice> tsvcList;
	private Text textsvc , textsvcnm ;
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
	    GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).applyTo(container);
	    
		Composite compHeader = new Composite(container, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).equalWidth(true).applyTo(compHeader);
		
		Label ltitle = new Label(compHeader, SWT.NONE);
		
    	ltitle.setImage(AqtMain.getMyimage("tit_regsvc.png"));

		Composite compTit = new Composite(container, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).applyTo(compTit);
		
		Label lblt = new Label(compTit, SWT.NONE);
		lblt.setText(" *서비스ID");
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
		textsvcnm.addTraverseListener(new TraverseListener() {
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
		btnSearch.setImage(AqtMain.getMyimage("search.png"));

		
    	Label lbl = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		lbl.setBackground(container.getBackground());
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(lbl);

 	    Composite compDetail = new Composite(container, SWT.NONE);
 	    GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(compDetail);
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).equalWidth(true).applyTo(compDetail);

    	tblViewerList = CheckboxTableViewer.newCheckList(compDetail, SWT.NONE | SWT.FULL_SELECTION );
    	
    	tblList = tblViewerList.getTable();
    	
	    GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(tblList);
		tblList.setLinesVisible(true);
    	tblList.setHeaderBackground(AqtMain.htcol);
    	tblList.setHeaderForeground(AqtMain.forecol);
    	tblList.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
    	tblList.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    	
	    tblList.setFont(SWTResourceManager.getFont("맑은 고딕", 15, SWT.NORMAL));
	    
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
        		{  " 서비스ID", "  서비스한글명", "  서비스영문명", "서비스종류"};

        int[] columnWidths1 = new int[] {  200, 300, 300, 200};

	    int[] colas1 = new int[] 
	    		{SWT.CENTER, SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.CENTER };
	    TableViewerColumn tableViewerColumn ;
	    for (int i = 0; i < cols1.length; i++) {
	    	tableViewerColumn =
	    			new TableViewerColumn(tblViewerList, colas1[i]);

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
					m.setSvcid(value.toString());
				else if (property.equals(cols1[1]))
					m.setSvckor(value.toString());
				else if (property.equals(cols1[2]))
					m.setSvceng(value.toString());
				else if (property.equals(cols1[3]))
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
					return t.getSvcid();
				else if (property.equals(cols1[1]))
					return t.getSvckor();
				else if (property.equals(cols1[2]))
					return t.getSvceng();
				else if (property.equals(cols1[3]))
					return t.getSvckind() ;
			
				return null;
			}
			
			@Override
			public boolean canModify(Object element, String property) {
				// TODO Auto-generated method stub
				if (tblViewerList.getChecked(element)) {
					Tservice t = (Tservice)element ;
					if ( ! property.equals(cols1[0]) ) return true ;

					return t.isNew() ;
				}
				else
					return false ;
				
//				if (property.equals(cols1[1]) || property.equals(cols1[2]) || property.equals(cols1[3]) ) {
//					return true ;
//				}
//				Tservice t = (Tservice)element ;
//				if ( property.equals(cols1[0]) && t.getSvcid().isEmpty() ) return true;
//				return false;
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
						  return s.getSvcid() ;
					  case 1:
						  return s.getSvckor();
					  case 2:
						  return s.getSvceng();
					  case 3:
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
		StringBuilder qstr = new StringBuilder("SELECT t FROM Tservice t") ; 
		if (! textsvc.getText().isEmpty()  ) {
			qstr.append(" where t.svcid like '" + textsvc.getText().trim() + "%'");
			if (! textsvcnm.getText().isEmpty() ) qstr.append(" and t.svckor like '" + textsvcnm.getText().trim() + "%'");
		} else if (! textsvcnm.getText().isEmpty()  ) {
			qstr.append(" where t.svckor like '" + textsvcnm.getText().trim() + "%'");
		}
        tsvcList = em.createQuery(qstr.toString(), Tservice.class).getResultList();
        		 
	    tblViewerList.setInput(tsvcList);
	    
	    tblList.setSelection(0);

	    AqtMain.container.setCursor(IAqtVar.arrow);
	    
	    tblList.setFont(IAqtVar.font1b);

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
    	        if (v.length < 4) continue ;
    	        Tservice s = new Tservice() ;
    	        s.setSvcid(v[0]);
    	        s.setSvckor(v[1]);
    	        s.setSvceng(v[2]);
    	        s.setSvckind(v[3]);
    	        s.setNew(true);
    	        s = em.merge(s);
    	        tsvcList.add(s) ;
    	    }
    	    tblViewerList.setInput(tsvcList);
    	    
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}