package aqtclient.part;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Tmaster;
import aqtclient.model.Trequest;
import aqtclient.model.Ttcppacket;

public class AqtTranTable extends AqtTableView {

	private int gcol = 0;
	protected MenuItem reSendItem ;
	private AqtTranTable aqtView = this ;
	
	public AqtTranTable(Composite parent, int style) {
		super(parent, style);
//		TableViewer tv = new TableViewer(parent, style) ;
		Table tbl = this.getTable();
		aqtView = this ;
		this.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				return compareElements( e1, e2);
			}
		});
		
		Menu popupMenu = tbl.getMenu();
	    MenuItem viewItem = new MenuItem(popupMenu, SWT.NONE);
	    viewItem.setText("상세보기");
	    viewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				if ( tbl.getSelectionIndex() >= 0) {
					AqtDetail aqtDetail = new AqtDetail(parent.getShell() , SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.CLOSE );
					aqtDetail.setTrx( ((Ttcppacket) tbl.getItem(tbl.getSelectionIndex() ).getData()).getPkey(), tbl ); 
					aqtDetail.open();
				}
				
			}
		});
	    
	    reSendItem = new MenuItem(popupMenu, SWT.NONE);
	    reSendItem.setText("재전송");
	    reSendItem.setEnabled(AuthType.TESTADM == AqtMain.authtype);

	    reSendItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if ( tbl.getSelectionIndex() >= 0) {
					EntityManager em = AqtMain.emf.createEntityManager();
					em.clear();
					em.getEntityManagerFactory().getCache().evictAll();
					
					Ttcppacket tr =  (Ttcppacket) tbl.getItem(tbl.getSelectionIndex() ).getData()  ;
					
					Tmaster tmst = tr.tmaster ;

					if ( tmst.getEndDate() != null ) {
						MessageDialog.openInformation(parent.getShell(), "작업불가", tmst.getCode() + " 는 종료되었습니다.") ;
						em.close();
						return ;
					}
					
					if (tmst != null && ("3".equals(tmst.getLvl() ) || "0".equals(tmst.getLvl() ) ) ) {
						MessageDialog.openInformation(parent.getShell(), "Info", "이 패킷은 재전송 불가합니다.(Origin or 실시간)");
						em.close();
						return ;
					}
					Trequest treq = em.find(Trequest.class, tr.getPkey()) ;
					if (treq != null) {
						MessageDialog.openInformation(parent.getShell(), "Info", "이미 재전송 요청되었습니다.");
						em.close();
						return ;
					}
					InetAddress local;
					String ip = "";
					try {
						local = InetAddress.getLocalHost();
						ip = local.getHostAddress();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					boolean result = MessageDialog.openConfirm(parent.getShell(), "재전송",
							tr.getCmpid() + " 재전송 요청등록하시겠습니까?" ) ;
					if (  result ) {
						em.getTransaction().begin();
						em.merge( new Trequest(tr.getPkey(), tr.getTcode(), tr.getCmpid(), ip ) );
						em.getTransaction().commit();
					}
					em.close();
				}
			}
		});

	    tbl.setMenu(popupMenu);
	    AqtDetail aqtDetail = new AqtDetail(parent.getShell() ,  SWT.DIALOG_TRIM | SWT.CLOSE );

		tbl.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				if ( tbl.getSelectionIndex() >= 0) {
//					AqtDetail aqtDetail = new AqtDetail(parent.getShell() , SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.CLOSE );
//					AqtDetail aqtDetail = new AqtDetail(parent.getShell() ,  SWT.DIALOG_TRIM | SWT.CLOSE );
//					aqtDetail.setTrxList( (Ttcppacket) tbl.getItem(tbl.getSelectionIndex() ).getData() ); 
					aqtDetail.setTrx( ((Ttcppacket) tbl.getItem(tbl.getSelectionIndex() ).getData()).getPkey() , tbl);
					aqtDetail.open();
				}

			}

			@Override
			public void mouseUp(MouseEvent e) {
				Point pt = new Point(e.x, e.y);
				TableItem item = tbl.getItem(pt);
				if (item != null) {
					for (int col = 0; col < tbl.getColumnCount(); col++) {
						Rectangle rect = item.getBounds(col);
						if (rect.contains(pt)) {
							gcol = col;
						}
					}
				}
				super.mouseUp(e);
			}
		});

		tbl.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.stateMask == SWT.CTRL && (e.keyCode == 'c' || e.keyCode == 'C')) {
					Clipboard clipboard = new Clipboard(Display.getDefault());
					String sdata = tbl.getItem(tbl.getSelectionIndex()).getText(gcol);
					clipboard.setContents(new Object[] { sdata }, new Transfer[] { TextTransfer.getInstance() });
					clipboard.dispose();
					return ;
				}
				if (e.keyCode == SWT.CR) {
					tbl.notifyListeners(SWT.MouseDoubleClick, null);
				}
			}
		});
		tbl.setHeaderVisible(true);
		tbl.setLinesVisible(true);
//		tbl.setFont(IAqtVar.font1);
		tbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tbl.setHeaderBackground(AqtMain.htcol);
		tbl.setHeaderForeground(AqtMain.forecol);

		this.setUseHashlookup(true);

		TableViewerColumn tvc;

		SimpleDateFormat smdfmt = new SimpleDateFormat("MM/dd HH:mm:ss");

		tvc = createTableViewerColumn("패킷id", 120, 0);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Ttcppacket tr = (Ttcppacket) element;
				return "" + tr.getCmpid();
			}
		});
		tvc = createTableViewerColumn("송신시간", 140, 1);
		tvc.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Ttcppacket tr = (Ttcppacket) element;
				return tr.getStime().format(DateTimeFormatter.ofPattern("MM/dd HH:mm:ss")) ;
//				return smdfmt.format(tr.getStime());
			}
		});
		tvc = createTableViewerColumn("수신시간", 140, 2);
		tvc.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Ttcppacket tr = (Ttcppacket) element;
				return tr.getRtime().format(DateTimeFormatter.ofPattern("MM/dd HH:mm:ss")) ;
			}
		});
		tvc = createTableViewerColumn("소요시간", 70, 3);
		tvc.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Ttcppacket tr = (Ttcppacket) element;
				return String.format("%.3f", tr.getSvctime());
			}
		});
		if (AqtMain.tconfig.getProto() != '0') {
			tvc = createTableViewerColumn("Method", 70, 4);
			tvc.setLabelProvider(new ColumnLabelProvider() {
				public String getText(Object element) {
					if (element == null)
						return super.getText(element);
					Ttcppacket tr = (Ttcppacket) element;
					return tr.getMethod();
				}
			});
		}
		tvc = createTableViewerColumn("서비스(URI)", 200, 5);
		tvc.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Ttcppacket tr = (Ttcppacket) element;
				return tr.getUri();
			}
		});
		tvc = createTableViewerColumn("응답코드", 70, 6);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Ttcppacket tr = (Ttcppacket) element;
				return tr.getRcode() + ""  ;
			}
		});
		tvc = createTableViewerColumn("수신크기", 70, 7);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Ttcppacket tr = (Ttcppacket) element;
				return String.format("%,d", tr.getRlen() );
			}
		});

		tvc = createTableViewerColumn(" 수신데이터", 500, 8);
		tvc.getColumn().setAlignment(SWT.LEFT);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Ttcppacket tr = (Ttcppacket) element;
				if (tr.getErrinfo().length() > 0 ) 
					return tr.getErrinfo() ;
				else
					return AqtMain.tconfig.getProto() != '0' ? tr.getRhead() : tr.getRdataENCODE(AqtMain.tconfig.getEncval(),200);
			}
		});

		tvc = createTableViewerColumn("테스트id", 80,9);
		tvc.setLabelProvider(new myColumnProvider() {
			public String getText(Object element) {
				if (element == null)
					return super.getText(element);
				Ttcppacket tr = (Ttcppacket) element;
				return "" + tr.getTcode();
			}
		});


		
		this.setContentProvider(new ContentProvider());
//		this.setLabelProvider(new TrxLabelProvider());

	}
	
	public void setResendEnabled(boolean b) {
		this.reSendItem.setEnabled(b);
	}

    private TableViewerColumn createTableViewerColumn(String header, int width, int idx) 
    {
        TableViewerColumn column = new TableViewerColumn(this, SWT.CENTER );
        TableColumn tcol = column.getColumn() ;
        tcol.setText(header);
        tcol.setWidth(width);
        tcol.setResizable(true);
        tcol.setMoveable(true);
        if (idx < 8)
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
					aqtView.refresh();
				}
			});
        
        return column;
    }
    
    protected int compareElements(Object e1, Object e2) {
		Table table = aqtView.getTable();
		int index = Arrays.asList(table.getColumns()).indexOf(table.getSortColumn());
		int result = 0;
		LocalDateTime d1, d2 ;
		Long l1, l2 ;
		Double db1,db2;
		String s1,s2 ;
		if (index != -1) {
			switch (index) {
			case 0:
				l1 = ((Ttcppacket)e1).getCmpid() ;
				l2 = ((Ttcppacket)e2).getCmpid() ;
				result = l1.compareTo(l2);
				break;
			case 1:
				d1 = ((Ttcppacket)e1).getStime() ;
				d2 = ((Ttcppacket)e2).getStime() ;
				result = d1.compareTo(d2);
				break;
			case 2:
				d1 = ((Ttcppacket)e1).getRtime() ;
				d2 = ((Ttcppacket)e2).getRtime() ;
				result = d1.compareTo(d2);
				break;
			case 3:
				db1 = ((Ttcppacket)e1).getSvctime() ;
				db2 = ((Ttcppacket)e2).getSvctime() ;
				result = db1.compareTo(db2);
				break;
			case 4:
				s1 = ((Ttcppacket)e1).getMethod() ;
				s2 = ((Ttcppacket)e2).getMethod() ;
				result = s1.compareTo(s2);
				break;
			case 5:
				s1 = ((Ttcppacket)e1).getUri() ;
				s2 = ((Ttcppacket)e2).getUri() ;
				result = s1.compareTo(s2);
				break;
			case 6:
				l1 = (long) ((Ttcppacket)e1).getRcode() ;
				l2 = (long) ((Ttcppacket)e2).getRcode() ;
				result = l1.compareTo(l2);
				break;
			case 7:
				l1 = (long) ((Ttcppacket)e1).getRlen() ;
				l2 = (long) ((Ttcppacket)e2).getRlen() ;
				result = l1.compareTo(l2);
				break;
			}
		}
		return table.getSortDirection() == SWT.UP ? result : -result;
	}

	
	private class myColumnProvider extends ColumnLabelProvider {
		@Override
		public Color getForeground(Object element) {
			if (element == null)
				return super.getForeground(element);
			return (((Ttcppacket) element).getSflag()) == '2' ? SWTResourceManager.getColor(SWT.COLOR_RED)
					: SWTResourceManager.getColor(SWT.COLOR_BLACK);
		}
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
					return trx.getCmpid() + "";
				case 1:
					return smdfmt.format(trx.getStime());
				case 2:
					return smdfmt.format(trx.getRtime());
				case 3:
					return String.format("%.3f", trx.getSvctime());
				case 4:
					return trx.getRcode() + "";
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

}
