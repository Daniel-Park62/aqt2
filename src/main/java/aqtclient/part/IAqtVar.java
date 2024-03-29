package aqtclient.part;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wb.swt.SWTResourceManager;

public interface IAqtVar {
	final public static Font title_font = SWTResourceManager.getFont("맑은 고딕", 20, SWT.NORMAL ) ;
	final public static Font font1 = SWTResourceManager.getFont("맑은 고딕", 12, SWT.NORMAL); // 텍스트 기본
	final public static Font font1b = SWTResourceManager.getFont("맑은 고딕", 12, SWT.BOLD);
	final public static Font font13 = SWTResourceManager.getFont("맑은 고딕", 13, SWT.NORMAL); 
	final public static Font font13b = SWTResourceManager.getFont("맑은 고딕", 13, SWT.BOLD);
	final public static Font font15b = SWTResourceManager.getFont("맑은 고딕", 15, SWT.BOLD); // 소제목
	final public static Font font17b = SWTResourceManager.getFont("맑은 고딕", 17, SWT.BOLD); // 중제목
	final public static Font font22b = SWTResourceManager.getFont("맑은 고딕", 22, SWT.BOLD); // 대제목
    final public static Cursor handc = SWTResourceManager.getCursor( SWT.CURSOR_HAND);
    final public static Cursor busyc = SWTResourceManager.getCursor( SWT.CURSOR_WAIT);
    final public static Cursor cross = SWTResourceManager.getCursor( SWT.CURSOR_CROSS);
    final public static Cursor arrow = SWTResourceManager.getCursor( SWT.CURSOR_ARROW);
    final String titnm = "Application Quarity Test";
    final String[] typeArr = new String[] {"","배치","실시간"};
    final String[] lvlArr = new String[] {"Origin","단위","통합","정합성"};
    final String[] proArr = new String[] {"tcp","http","udp","tmax"};
    EntityManager em = AqtMain.emf.createEntityManager();
    
    final public static Map<String, String> lvlnm = Collections.unmodifiableMap(new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "Origin");
			put("1", "단위");
			put("2", "통합");
			put("3", "정합성");
		}
	});
    
    
    public static Object getLvl(Object value) { 
    	for(Object o: lvlnm.keySet()) { 
    		if(lvlnm.get(o).equals(value))  return o;  
    	} 
    	return null; 
    }
    
    public static void setAllFont(Composite parent, Font font) {
		
	    for (Control kid : parent.getChildren()) {
	    	try {
		        kid.setFont(font);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
	public static String[] getAppList() {
		return em.createQuery("select a.appid from Tapplication a ", String.class)
				.getResultList().stream().toArray(String[]::new);
	}

    
}	

enum AuthType { USER, TESTADM } 
enum RTN { FAIL, OK }
