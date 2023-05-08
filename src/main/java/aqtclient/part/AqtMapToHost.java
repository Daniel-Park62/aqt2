package aqtclient.part;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Texecjob;
import aqtclient.model.Thostmap;
import aqtclient.model.Tmaster;

public class AqtMapToHost extends Dialog {

	private AqtTableView tv ;
	private String acode ;
	private List <Thostmap> thostList;
	private String[] cols = {"현행 IP","현행Port","변경 IP","변경Port"};
	private int[] colwd = {180,100,180,100} ;
	EntityManager em = AqtMain.emf.createEntityManager();
	
	protected AqtMapToHost(Shell parent, String scode) {
		super(parent);
		// TODO Auto-generated constructor stub
		setShellStyle(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.CLOSE | SWT.RESIZE | SWT.TITLE);
		acode = scode ;
	}
    @Override
    protected void configureShell(Shell newShell) {
    	newShell.setBackground(SWTResourceManager.getColor(215, 228, 242));
        super.configureShell(newShell);
        
        newShell.setText(acode + " : 테스트대상 호스트 정의 ");
    }

    @Override
    protected Point getInitialSize() {
        return new Point(700, 500);
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "저장", true).setFont(IAqtVar.font1b);
        createButton(parent, IDialogConstants.CLOSE_ID, "닫기", true).setFont(IAqtVar.font1b);
    }
    
    @Override
    protected void buttonPressed ( final int buttonId )
    {
    	switch (buttonId) {
		case IDialogConstants.CLOSE_ID:
			
			EntityTransaction trans = em.getTransaction() ;
			if (trans.isActive()) trans.rollback();
			em.close();
			close() ;
			break;
		case IDialogConstants.OK_ID:
			save();
			break;
		}
    }
    
	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		Composite container = (Composite) super.createDialogArea(parent);
		Composite compt1 = new Composite(container, SWT.NONE) ;
		GridLayoutFactory.fillDefaults().numColumns(4).equalWidth(false).spacing(1, 1).margins(0, 0).applyTo(compt1);
		
		CLabel lbl = new CLabel(compt1,SWT.SHADOW_OUT) ;
		lbl.setText(cols[0]) ;  // 현행ip
		lbl.setFont(IAqtVar.font1b);
//		lbl.setSize(100, 20);
		lbl.setAlignment(SWT.CENTER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).hint(colwd[0], 30).grab(true, true).applyTo(lbl);
		
		lbl = new CLabel(compt1,SWT.SHADOW_OUT) ;
		lbl.setText(cols[1]) ;
		lbl.setFont(IAqtVar.font1b);
//		lbl.setSize(100, 20);
		lbl.setAlignment(SWT.CENTER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).hint(colwd[1], 30).grab(true, true).applyTo(lbl);

		lbl = new CLabel(compt1,SWT.SHADOW_OUT) ;
		lbl.setText(cols[2]) ;
		lbl.setFont(IAqtVar.font1b);
		lbl.setToolTipText("Ctrl+G : 변경IP 채우기");
//		lbl.setSize(100, 20);
		lbl.setAlignment(SWT.CENTER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).hint(colwd[2], 30).grab(true, true).applyTo(lbl);

		lbl = new CLabel(compt1,SWT.SHADOW_OUT) ;
		lbl.setText(cols[3]) ;
		lbl.setFont(IAqtVar.font1b);
//		lbl.setSize(100, 20);
		lbl.setAlignment(SWT.CENTER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).hint(colwd[3], 30).grab(true, true).applyTo(lbl);
//		compt1.requestLayout();

		tv = new AqtTableView(container, SWT.BORDER | SWT.FULL_SELECTION ) ; 
		Table tbl = tv.getTable() ;
	    tbl.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int i = tbl.getSelectionIndex() ;
				if (i < 0)  return ;
				String lshost = tbl.getItem(i).getText(2) ;
				if (! lshost.isBlank() && e.stateMask == SWT.CTRL && e.keyCode == 'g'   ) {
					for(int ii = 0; ii < tbl.getItemCount(); ii++) {

						TableItem item = tbl.getItem(ii) ;
						Thostmap t = (Thostmap) item.getData() ;
						if (item.getText(2).isBlank()) {
							t.setThost2(lshost) ;
							item.setData(t) ;
						}
						tv.refresh();
					}
//				} else	if (i+1 == tbl.getItemCount() && e.keyCode == SWT.ARROW_DOWN ) {
//					Thostmap t = new Thostmap() ;
//					t.setTcode(acode);
//					em.persist(t);
//					thostList.add(t) ;
//					tv.refresh();
				}
			}
		});
	    
	    Menu popupMenu = tbl.getMenu() ;

//	    MenuItem addsvc = new MenuItem(popupMenu, SWT.NONE);
//	    addsvc.setText("추가");
//	    addsvc.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent arg0) {
//				
//				Thostmap t = new Thostmap() ;
//				t.setTcode(acode);
//				
//				em.persist(t);
//				thostList.add(t) ;
//				tv.refresh();
//				tbl.select(tbl.getItemCount()-1);
//			}
//		});
	    
	    MenuItem delsvc = new MenuItem(popupMenu, SWT.NONE);
	    delsvc.setText("삭제");
	    delsvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int i = tbl.getSelectionIndex() ;
				if (i >= 0 ) {
					Thostmap t = (Thostmap) tbl.getItem(i).getData() ;
					em.remove(t);
					thostList.remove(t) ;
					tv.refresh();
				}
			}
		});

	    MenuItem masisGet = new MenuItem(popupMenu, SWT.NONE);
	    masisGet.setText("현행Host가져오기");
	    masisGet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				getAsisHost();
			}
		});

	    MenuItem memptySet = new MenuItem(popupMenu, SWT.NONE);
	    memptySet.setText("나머지채우기");
	    memptySet.setEnabled(false);
	    memptySet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int i = tbl.getSelectionIndex() ;
				if (i < 0)  return ;
				String lshost = tbl.getItem(i).getText(2) ;
				if (! lshost.isBlank() ) {
					for(int ii = 0; ii < tbl.getItemCount(); ii++) {

						TableItem item = tbl.getItem(ii) ;
						Thostmap t = (Thostmap) item.getData() ;
						if (item.getText(2).isBlank()) {
							t.setThost2(lshost) ;
							item.setData(t) ;
						}
						tv.refresh();
					}
				}
			}
		});

	    tbl.setMenu(popupMenu);
	    
		tbl.setLinesVisible(true);
    	tbl.setHeaderBackground(AqtMain.htcol);
    	tbl.setHeaderForeground(AqtMain.forecol);
    	tbl.setHeaderVisible(false);
//    	tbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
    	tbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    	tbl.setFont(IAqtVar.font1);
	    tv.setUseHashlookup(true);

	    TableViewerColumn tvcol ;
	    
	    for (int i = 0; i < cols.length; i++) {
	    	
	    	tvcol = new TableViewerColumn(tv, SWT.CENTER);

	    	TableColumn tableColumn = tvcol.getColumn();
	    	tableColumn.setText(cols[i]);
	    	tableColumn.setWidth(colwd[i]);
	    	tvcol.setLabelProvider(getColumnLabelProvider(cols[i]));
	    }
	    
	    tv.setContentProvider(new IStructuredContentProvider() {
			@Override
			public Object[] getElements(Object input) {
				if (input instanceof List<?> )
					return ((List<Thostmap>)input).toArray() ;
				return null;
			}
		});
//	    tv.setLabelProvider(myLabelProvider) ;
	    tv.setColumnProperties(cols);
	    CellEditor[] CELL_EDITORS = new CellEditor[] {
	    		null,
	    		null,
	    		new TextCellEditor(tbl, SWT.CENTER ),
	    		new TextCellEditor(tbl, SWT.CENTER )
	    		};
//	    CELL_EDITORS[1].setValidator( getValidateNum() );
	    CELL_EDITORS[3].setValidator( getValidateNum() );

//	    for (CellEditor cellEditor : CELL_EDITORS) {
//	    	cellEditor = new TextCellEditor(tbl, SWT.CENTER );
//		}
	    tv.setCellEditors(CELL_EDITORS);
	    tv.setCellModifier(mycellmodifier);
	    thostList = queryData();
	    tv.setInput(thostList);
	    
	    tbl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				memptySet.setEnabled(false) ;
				int i = tbl.getSelectionIndex() ;
				if (i < 0)  return ;
				String lshost = tbl.getItem(i).getText(2) ;
				if (! lshost.isBlank() ) memptySet.setEnabled(true);
			}
		});
	    GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(tbl);
	    
		return container ;
	}
	
	private List<Thostmap> queryData() {
		return em.createNamedQuery("Thostmap.findCode", Thostmap.class).setParameter("tcode", acode).getResultList() ;
		
	}
	private void getAsisHost() {
		
		List<Object[]> asisList = em.createNativeQuery("SELECT dstip, dstport, p.thost2, p.tport2 FROM ttcppacket t " +
								" LEFT JOIN thostmap p ON (p.tcode != t.tcode AND dstip  = thost AND dstport = tport)" +
								" WHERE t.tcode = ?" + 
								" AND NOT EXISTS (SELECT 1 FROM thostmap h WHERE h.tcode = t.tcode " +
								"      AND dstip = thost AND ( dstport = tport OR tport = 0)) group by dstip, dstport" )
				.setParameter(1, acode).getResultList() ;
		for( Object[] row : asisList) {
			Thostmap t = new Thostmap() ;
			t.setTcode(acode);
			t.setThost(row[0].toString());
			if (row[2] != null )  t.setThost2(row[2].toString());
			t.setTport(Integer.parseInt(row[1].toString()));
			if (row[3] != null ) 
				t.setTport2(Integer.parseInt(row[3].toString()));
			else
				t.setTport2(Integer.parseInt(row[1].toString()));
			em.persist(t);
			thostList.add(t) ;
		}
		tv.refresh();
	}
	
	private void save() {
		
		em.getTransaction().begin();
		em.getTransaction().commit();
		
		tv.refresh();
		MessageDialog.openInformation(this.getShell() , "Save Infomation", "수정 되었습니다.") ;
	}
	
	private ICellEditorValidator getValidateNum() {
		return (value) -> {
			AqtMain.aqtmain.setStatus("" );
			try {
				Integer.parseInt((String) value);
			} catch (NumberFormatException e) {
				AqtMain.aqtmain.setStatus("숫자만 가능합니다.!!" );
				return "숫자만 가능합니다.!!";
			}
			return null;
		} ;
	}
	
	private ColumnLabelProvider getColumnLabelProvider(final String title) {
		return new ColumnLabelProvider() {

			@Override
			public String getText(final Object element) {
				final Thostmap t = (Thostmap) element;
				if (title.equals(cols[0])) { return t.getThost(); }
				if (title.equals(cols[1])) { return String.valueOf(t.getTport()) ; }
				if (title.equals(cols[2])) { return t.getThost2(); }
				if (title.equals(cols[3])) { return String.valueOf(t.getTport2()) ; }
				return null ;
			}
			
			@Override
			public Color getBackground(Object element) {
				return (title.equals(cols[0]) || title.equals(cols[1])) ? 
						SWTResourceManager.getColor(250,250,255) :  super.getBackground(element);
			}
		};
	}
	
	private ICellModifier mycellmodifier = new ICellModifier() {
		
		@Override
		public void modify(Object element, String property, Object value) {
//			if (isBlank(value)) return ; 

			if (element instanceof Item)
				element = ((Item) element).getData();

			Thostmap m = (Thostmap) element;
			if (property.equals(cols[0]))
				m.setThost(value.toString());
			else if (property.equals(cols[1]))
				m.setTport(Integer.parseInt(value.toString()));
			else if (property.equals(cols[2]))
				m.setThost2(value.toString());
			else if (property.equals(cols[3]))
				m.setTport2(Integer.parseInt(value.toString()));
			else
				return ;

			// Force the viewer to refresh
			tv.refresh();
			
		}
		
		private boolean isBlank(Object value) {
			if (value == null || value.toString().isEmpty() ) return true ;
			return false;
		}

		@Override
		public Object getValue(Object element, String property) {
			// TODO Auto-generated method stub
			Thostmap t = (Thostmap)element ;
			if (property.equals(cols[0]))
				return t.getThost();
			else if (property.equals(cols[1]))
				return Integer.toString( t.getTport());
			else if (property.equals(cols[2]))
				return t.getThost2();
			else if (property.equals(cols[3]))
				return Integer.toString( t.getTport2());

			return null;
		}
		
		@Override
		public boolean canModify(Object element, String property) {
			// TODO Auto-generated method stub

			return true ;
			
		}
		
	};
}
