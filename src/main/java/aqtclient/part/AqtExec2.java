package aqtclient.part;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import org.eclipse.swt.events.MouseAdapter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Tmaster;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;



public class AqtExec2  {
	private Label textDesc;
	private Label textType;
	private Label textSdate;
	private Text textID;
	private Text textPassword;
	private Text textExec;
	private Table tblResult;
	private Text textResult;

	private Combo cmbHost;
	private static ArrayList<Tmaster> tempMstList;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AqtExec2(Composite parent, int style) {
		create (parent, style);
		
		refreshScreen(0);
	}
	
	// ID, 패스워드 및 실행 버튼에 대한 구체적인 구현은 되어 있지 않음
	private void create (Composite parent, int style) {
		SashForm sashForm;

		parent.setLayout(new FillLayout());
	    
	    sashForm = new SashForm(parent, SWT.VERTICAL);	
	    sashForm.setBackground(parent.getBackground());
		Composite compHeader = new Composite(sashForm, SWT.NONE);
		GridLayout headerLayout = new GridLayout(1,false);
		headerLayout.verticalSpacing = 10;
		headerLayout.marginTop = 20;
		headerLayout.marginWidth = 15;
		compHeader.setLayout(headerLayout);

		compHeader.setBackground(parent.getBackground());
		Label ltitle = new Label(compHeader, SWT.NONE);
		
    	ltitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));		
    	ltitle.setImage(AqtMain.getMyimage("tit_exec.png"));
		
		Composite compTitle = new Composite(compHeader, SWT.LINE_DASH);
		
		compTitle.setBackground(parent.getBackground());
		
//		compTitle.setLayoutData(new GridData( SWT.FILL , SWT.TOP, true, false));
		GridData titleGridData = new GridData(SWT.FILL , SWT.TOP, true, false);
		compTitle.setLayoutData(titleGridData);
		GridLayout glin = new GridLayout(11, false) ;
		glin.horizontalSpacing = 20 ;
		compTitle.setLayout(glin);
		compTitle.setBackground(parent.getBackground());
		
		Label lblHost = new Label(compTitle, SWT.NONE);
		lblHost.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblHost.setText("테스트코드");
		lblHost.setFont( IAqtVar.font1) ;
		
		cmbHost = new Combo(compTitle, SWT.READ_ONLY);
		cmbHost.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshScreen(1);
			}
		});
		cmbHost.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		cmbHost.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmbHost.setFont( IAqtVar.font1) ;
		
		textDesc = new Label(compTitle, SWT.NONE);
		textDesc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		textDesc.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		textDesc.setFont( IAqtVar.font1) ;
		
		Label lblType = new Label(compTitle, SWT.NONE);
		lblType.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblType.setText("  타입 :");
		lblType.setFont( IAqtVar.font1) ;
		
		textType = new Label(compTitle, SWT.NONE);
		textType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		textType.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		textType.setFont( IAqtVar.font1) ;
	
		Label lblSdate = new Label(compTitle, SWT.NONE);
		lblSdate.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblSdate.setText("테스트일자 :");
		lblSdate.setFont( IAqtVar.font1) ;
		
		textSdate = new Label(compTitle, SWT.NONE);
		textSdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		textSdate.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		textSdate.setFont( IAqtVar.font1) ;
		
		Label lblId = new Label(compTitle, SWT.NONE);
		lblId.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblId.setText("ID:");
		lblId.setFont( IAqtVar.font1) ;
		
		textID = new Text(compTitle, SWT.BORDER);
		textID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		textID.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textID.setFont( IAqtVar.font1) ;
		
		Label lblPasword = new Label(compTitle, SWT.NONE);
		lblPasword.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblPasword.setText("PASWORD:");
		lblPasword.setFont( IAqtVar.font1) ;
		
		textPassword = new Text(compTitle, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		textPassword.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textPassword.setFont( IAqtVar.font1) ;
		
		Label lblExec = new Label(compTitle, SWT.NONE);
		lblExec.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblExec.setText("초기실행  ");
		lblExec.setFont( IAqtVar.font1) ;
		
		textExec = new Text(compTitle, SWT.BORDER);
		GridData gd_textExec = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd_textExec.horizontalSpan = 10;
		textExec.setLayoutData(gd_textExec);

		textExec.setFont( IAqtVar.font1) ;
		textExec.addListener(SWT.DefaultSelection, new Listener() {
	        public void handleEvent(Event e) {
	          textResult.setText(execCommand());
	          
	          textExec.setText("");
	        }
	      });

		Composite compExecRslt = new Composite(compHeader, SWT.NONE);
		compExecRslt.setLayout(new FillLayout());
 		
		compExecRslt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		GridData rsltGridData = new GridData(GridData.FILL_BOTH);
		rsltGridData.horizontalSpan = 12;
		compExecRslt.setLayoutData(rsltGridData);
	
		textResult = new Text(compExecRslt, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
		textResult.setFont( IAqtVar.font1) ;
		textResult.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		Composite composite = new Composite(sashForm, SWT.NONE);

		GridLayout compLayout = new GridLayout(2, true);
		compLayout.verticalSpacing = 10;
		compLayout.marginTop = 20;
		compLayout.marginWidth = 20;
		compLayout.marginBottom = 20;
		
		composite.setLayout(compLayout);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		Label lblTrans = new Label(composite, SWT.NONE);

		lblTrans.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
		lblTrans.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblTrans.setText("전문송신");
		lblTrans.setFont( IAqtVar.font1) ;

		lblTrans = new Label(composite, SWT.NONE);
		lblTrans.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		lblTrans.setImage(AqtMain.getMyimage("execbtn.png"));
		lblTrans.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblTrans.setCursor(IAqtVar.handc);
		lblTrans.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
			
			}
		});
		
		tblResult = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_tblResult = new GridData(SWT.FILL, SWT.FILL, true, true );
		gd_tblResult.horizontalSpan = 2;
		tblResult.setLayoutData(gd_tblResult);
		tblResult.setHeaderVisible(true);
		tblResult.setLinesVisible(true);
		tblResult.setFont( IAqtVar.font1) ;

//		sashForm.setWeights(new int[] {50, 50});
		
	}

	// 외부 명령어 실행 (디렉토리 변경은 불가능 함)
	// 리눅스 상에서 실행 확인이 필요함
	private String execCommand() {
        StringBuffer readBuffer; 
		String OS = System.getProperty("os.name").toLowerCase();
		String cmd = "";


        if(OS.indexOf("win") >= 0) {
        	// Dos 명령어 cmd 가 들어가면 실행이 안됨
        	if (textExec.getText().toUpperCase().equals("CMD"))
        		textExec.setText("");
        	cmd = "cmd /c " + textExec.getText();
        }
        else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0)
        	cmd = textExec.getText();
        else if(OS.indexOf("sunos") >= 0)
        	cmd = textExec.getText();
        
        /* change directory 불가능 */
        
		try {
			Process p = Runtime.getRuntime().exec(cmd);
	        InputStream stderr = p.getErrorStream();
	        String line = "";
	        
	        if (stderr != null) {
	        	BufferedReader  br = new BufferedReader(new InputStreamReader(stderr, "MS949"));
               
        		readBuffer = new StringBuffer ();

        		while ((line = br.readLine()) != null) {
                    readBuffer.append(line);
                    readBuffer.append("\n");
                }
        		
        		if (readBuffer.length() > 0) 
        			return readBuffer.toString();
            }
	        
			BufferedReader reader=new BufferedReader(new InputStreamReader(
                    p.getInputStream(), "MS949"));
            		
            		readBuffer = new StringBuffer ();
            		
            		while((line = reader.readLine()) != null) { 
                       readBuffer.append(line);
                       readBuffer.append("\n");
            		} 
			p.destroy();
			
			p.waitFor();
			return readBuffer.toString();
			
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 데이터 추출하여 화면에 보여준다.
	private void refreshScreen (int idx) {
	    EntityManager em = AqtMain.emf.createEntityManager();
	    tempMstList = new ArrayList<Tmaster>();
	    
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
//        TypedQuery<Tmaster> qMst = em.createQuery("select t from Tmaster t order by t.code ", Tmaster.class);
        TypedQuery<Tmaster> qMst = em.createNamedQuery("Tmaster.findAll", Tmaster.class);
    	
        qMst.getResultList().stream().forEach( t -> tempMstList.add(t));
		
		switch (idx) {
		case 0:
			cmbHost.removeAll();
	
			for (Tmaster m : tempMstList)
				cmbHost.add(m.getCode() + " : " + m.getDesc1());
	
			cmbHost.select(0);
			break;
			default:
		}
		
		if (tempMstList != null) {
			textDesc.setText(tempMstList.get(cmbHost.getSelectionIndex()).getDesc1());
			textSdate.setText(tempMstList.get(cmbHost.getSelectionIndex()).getTdate());
			String sType = tempMstList.get(cmbHost.getSelectionIndex()).getTypeNm();
			
			textType.setText(sType);
		}
        
		em.close();		
	}
}
