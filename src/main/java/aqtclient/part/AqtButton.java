package aqtclient.part;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

public class AqtButton extends Button {

	public AqtButton(Composite parent, int style, String bname) {
		super(parent, style);
		this.setText(bname);
//		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).minSize(100, -1).applyTo(this);
		setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.BOLD));
		setForeground(SWTResourceManager.getColor(58,115,255));

	}
	
	@Override
	protected void checkSubclass() {
		// TODO Auto-generated method stub
//		super.checkSubclass();
	}

}
