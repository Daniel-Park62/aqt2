package aqtclient.part;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class DateText extends Text {

	@Override
	protected void checkSubclass() {
	    //  allow subclass

	}
	
	public DateText(Composite parent, int style) {
		super(parent, style) ;
		// TODO Auto-generated constructor stub
		setTextLimit(10);
		setText("YYYY-MM-DD");
		final Calendar calendar = Calendar.getInstance();
		addListener(SWT.FocusIn, new Listener() {
		      @Override
			public void handleEvent(Event e) {
		          setSelection(0,10);
		        }
		});
		
		addMouseListener( new MouseAdapter() {
	    	@Override
	    	public void mouseDoubleClick(MouseEvent e) {
	    		
	    		Point pt = parent.getDisplay().getCursorLocation() ; 
	        	CalDialog cd = new CalDialog(Display.getCurrent().getActiveShell() , pt.x, pt.y + 10 );
	        	
	            String s = (String)cd.open();
	            
	            if (s != null) {
	            	((Text)e.getSource()).setText(s ) ;
	            }
//	    		super.mouseDoubleClick(e);
	    	}
		});
		
		addListener(SWT.Verify , new Listener() {
			boolean ignore;

			@Override
			public void handleEvent(Event e) {
		        if (ignore)
		            return;
		          e.doit = false;
		          StringBuffer buffer = new StringBuffer(e.text);
		          char[] chars = new char[buffer.length()];
		          buffer.getChars(0, chars.length, chars, 0);
		          if (e.character == '\b') {
		            for (int i = e.start; i < e.end; i++) {
		              switch (i) {
		              case 0: /* [Y]YYY */
		              case 1: /* Y[Y]YY */
		              case 2: /* YY[Y]Y */
		              case 3: /* YYY[Y] */{
		                buffer.append('Y');
		                break;
		              }
		              case 5: /* [M]M */
		              case 6: /* M[M] */{
		                buffer.append('M');
		                break;
		              }
		              case 8: /* [D]D */
		              case 9: /* D[D] */{
		                buffer.append('D');
		                break;
		              }
		              case 4: /* YYYY[/]MM */
		              case 7: /* MM[/]DD */{
		                buffer.append('-');
		                break;
		              }
		              default:
		                return;
		              }
		            }
		            
		            setSelection(e.start, e.start + buffer.length());
		            ignore = true;
		            insert(buffer.toString());
		            ignore = false;
		            setSelection(e.start, e.start);
		            return;
		          }

		          int start = e.start;
		          if (start > 9)
		            return;
		          int index = 0;
		          for (int i = 0; i < chars.length; i++) {
		            if (start + index == 4 || start + index == 7) {
		              if (  chars[i] == '-' || chars[i] == '/' ) {
		                index++;
		                continue;
		              }
		              buffer.insert(index++, '-');
		            }
		            if (chars[i] < '0' || '9' < chars[i])
		              return;
		            if (start + index == 5 && '1' < chars[i])
		              return; /* [M]M */
		            if (start + index == 8 && '3' < chars[i])
		              return; /* [D]D */
		            index++;
		          }
		          
		          String newText = buffer.toString();
		          int length = newText.length();

		          StringBuffer sdate = new StringBuffer(getText());
		          
		          sdate.replace(e.start, e.start + length, newText);
//		          if ( date.length() < 10 ) return ;

		          calendar.set(Calendar.YEAR, 1901);
		          calendar.set(Calendar.MONTH, Calendar.JANUARY);
		          calendar.set(Calendar.DATE, 1);
		          String yyyy = sdate.substring(0, 4);
		          if (yyyy.indexOf('Y') == -1) {
		            int year = Integer.parseInt(yyyy);
		            calendar.set(Calendar.YEAR, year);
		          }
		          String mm = sdate.substring(5, 7);
		          if (mm.indexOf('M') == -1) {
		            int month = Integer.parseInt(mm) - 1 ;
		            int maxMonth = calendar.getActualMaximum(Calendar.MONTH);
		            	
		            if (start == 6 && ( 0 > month || month > maxMonth))
		              return;
		            calendar.set(Calendar.MONTH, month);
		          }
		          String dd = sdate.substring(8, 10);
		          if (dd.indexOf('D') == -1) {
		            int day = Integer.parseInt(dd);
		            int maxDay = calendar.getActualMaximum(Calendar.DATE);
		            if (start == 9 && (1 > day || day > maxDay))
		              return;
		            calendar.set(Calendar.DATE, day);
		          } else {
		            if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY) {
		              char firstChar = sdate.charAt(8);
		              if (firstChar != 'D' && '2' < firstChar)
		                return;
		            }
		          }
		          
		          setSelection(e.start, e.start + length);
		          ignore = true;
		          insert(newText);
		          ignore = false;
		   				
			}
		});
		

	}

}
