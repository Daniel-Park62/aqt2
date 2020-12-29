package aqtclient.part;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class Calsel extends CLabel {
    final Image calsel_img = AqtMain.getMyimage("calsel.png");
//    Text t;
//    public void setTarget(Text t) {
//    	this.t = t ;
//    }
	public Calsel(Composite parent, int style, Text t ) {
		super(parent, style); 
		setCursor(IAqtVar.handc);
		setImage(calsel_img);
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(org.eclipse.swt.events.MouseEvent arg0) {
	    		Point pt = AqtMain.aqtmain.getShell().getDisplay().getCursorLocation() ; 
//	    		Point ppt = ((Text)e.getSource()).getParent().getLocation() ;
	        	CalDialog cd = new CalDialog(Display.getCurrent().getActiveShell() , pt.x, pt.y + 20 );
	        	
	            String s = (String)cd.open();
	            
	            if (s != null) {
	            	t.setText(s ) ;
	            }
			}
			
			@Override
			public void mouseDown(org.eclipse.swt.events.MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
