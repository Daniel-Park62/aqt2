package aqtclient.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import aqtclient.model.Ttransaction;

public class AqtTableView extends TableViewer {
	TableViewer tv ;
	public AqtTableView(Composite parent, int style) {
		super(parent, style);
		tv = this ;
		Table tbl = this.getTable() ;
		Menu popupMenu = new Menu(tbl);
	    MenuItem exportd = new MenuItem(popupMenu, SWT.NONE);
	    exportd.setText("CSV저장");
	    exportd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				int si = tbl.getColumn(0).getWidth() > 5 ? 0 : 1 ;
				AqtMain.exportTable(tv, si);
				
			}
		});
	    
	    tbl.setMenu(popupMenu);
		tbl.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int si = tbl.getColumn(0).getWidth() > 5 ? 0 : 1 ;
				if (e.stateMask == SWT.CTRL && (e.keyCode == 'e' || e.keyCode == 'E')) {
					AqtMain.exportTable(tv, si);
				}
//				if ((e.stateMask & SWT.ALT) != 0 && (e.keyCode == 'c' || e.keyCode == 'C')) {
//					System.out.println("okkk");
//					Clipboard clipboard = new Clipboard(Display.getDefault());
//					
//					List<String> list = new ArrayList<>() ;
//					for (TableItem item : tbl.getItems()) {
//						list.add(item.getText() ) ;
//						
//					}
//					
//					clipboard.setContents( new Object[] {list.toString()} , new Transfer[] { TextTransfer.getInstance() });
//					clipboard.dispose();
//					return ;
//				}

			}
		});
	}
}
