package aqtclient.part;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

public class AqtTitle extends CLabel {

	public AqtTitle(Composite parent, int style, String titleName, String img) {
		this( parent, style, titleName) ;
		setImage(AqtMain.getMyimage(img));
	}
	
	public AqtTitle(Composite parent, int style, String titleName) {
		super(parent, style);
		setText(titleName);
		setFont(SWTResourceManager.getFont("맑은 고딕", 17, SWT.BOLD));
		setForeground(SWTResourceManager.getColor(58,115,255));
		
	}

}
