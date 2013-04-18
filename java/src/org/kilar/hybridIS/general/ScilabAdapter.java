package org.kilar.hybridIS.general;

import org.scilab.modules.javasci.JavasciException;
import org.scilab.modules.javasci.JavasciException.InitializationException;
import org.scilab.modules.javasci.Scilab;

public class ScilabAdapter {
	
	private static Scilab scilab = null;
	private static boolean isCreated = false;

	static public void initialize(){
		if(isCreated){
			return;
		}
		try {
			scilab = new Scilab();
			scilab.open();
			isCreated = true;
		} catch (InitializationException e) {
			Logger.error("Ошибка инициализации scilab: " + e.getMessage() );
			return;
		} catch (JavasciException e) {
			Logger.error("Общая ошибка scilab: " + e.getMessage());
			return;
		}		
	}
	
	public static Scilab getScilab() {
		return scilab;
	}
	
	public static boolean isExistScilab(){
		return isCreated;
	}
}
