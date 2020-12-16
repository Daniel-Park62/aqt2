package aqtclient.part;

import java.util.List;

import javax.persistence.EntityManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import aqtclient.model.Tmaster;

public class AqtTcodeCombo  {
	private List<Tmaster> tlist ;
	private Combo combo ;
	
	public AqtTcodeCombo(Composite parent, int style) {
		combo = new Combo(parent, style);
		
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		combo.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		combo.setFont(IAqtVar.font1);
		
		EntityManager em = AqtMain.emf.createEntityManager();

		tlist = em.createNamedQuery("Tmaster.findAll", Tmaster.class).getResultList();
		combo.setItems(	tlist.stream().map((m) -> (" " + m.getCode() + " : " + m.getDesc1())).toArray(String[]::new));
		combo.select(0);
	}
	public Combo getControl() {
		return combo ;
	}
	public String getTcode() {
		if (tlist == null)  return "";
		return tlist.get(combo.getSelectionIndex()).getCode() ;
	}

	public String getCmpCode() {
		if (tlist == null)  return "";
		return tlist.get(combo.getSelectionIndex()).getCmpCode();
	}

	public Tmaster getTmaster() {
		if (tlist == null || combo.getSelectionIndex() < 0 )  return null;
		
		return tlist.get(combo.getSelectionIndex()) ;
	}

	public int getItemCount() {
		return combo.getItemCount() ;
	}
	
	public int getSelectionIndex() {
		return combo.getSelectionIndex() ;
	}
	
	public String getText() {
		return combo.getText();
	}
	
	public int findSelect(String s) {
		if (s == null) return -1;
		String st[] = combo.getItems() ;
		for (int i=0; i<st.length; i++) {
			if (st[i].contains(s)) {
				combo.select(i);
				return i;
			}
		}
		return -1;
	}
}
