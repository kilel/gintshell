package org.kilar.hybridIS.general;

import org.scilab.modules.javasci.JavasciException;
import org.scilab.modules.javasci.Scilab;

public class ScilabAdapter {
	
	private static Scilab scilab = null;
	private static boolean isCreated = false;

	public ScilabAdapter(){
		try {
			scilab = new Scilab();
			scilab.open();
			isCreated = true;
		} catch (JavasciException e) {
			e.printStackTrace();
		}
	}
	
	public static Scilab getScilab() {
		return scilab;
	}
	
	public static boolean isExistScilab(){
		return isCreated;
	}
}
