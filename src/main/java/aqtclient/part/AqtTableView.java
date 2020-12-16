package aqtclient.part;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;

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
			}
		});
	}
}
