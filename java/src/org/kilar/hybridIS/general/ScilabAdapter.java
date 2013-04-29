package org.kilar.hybridIS.general;

import org.scilab.modules.javasci.JavasciException;
import org.scilab.modules.javasci.JavasciException.InitializationException;
import org.scilab.modules.javasci.Scilab;
import org.scilab.modules.types.ScilabType;

public class ScilabAdapter {
	
	private static Scilab scilab = null;
	private static boolean isCreated = false;

	static public void initialize(){
		if(isCreated){
			return;
		}
		try {
			scilab = new Scilab();
			isCreated = true;
		} catch (InitializationException e) {
			Logger.error("Ошибка инициализации scilab: " + e.getMessage() );
			return;
		}
	}
	
	public static Scilab getScilab() {
		return scilab;
	}
	
	public static boolean set(String varName, ScilabType value){
		try {
			return scilab.put(varName, value);
		} catch (JavasciException e) {
			Logger.error("Ошибка установки переменной: " + varName );
			e.printStackTrace();
			throw new RuntimeException("Ошибка установки переменной: " + varName);
		}
	}
	
	public static void open(){
		initialize();
		try {
			scilab.open();
		} catch (JavasciException e) {
			Logger.error("Ошибка открытия scilab: " + e.getMessage() );
			e.printStackTrace();
			//throw new RuntimeException("Ошибка открытия scilab: " + e.getMessage());
		}
	}
	
	public static void close(){
		scilab.close();
	}
	
	public static void exec(String query){
		scilab.exec(query);
	}
	
	public static ScilabType get(String name){
		try {
			return scilab.get(name);
		} catch (JavasciException e) {
			Logger.error("Ошибка считывания переменной из Scilab");
			e.printStackTrace();
			throw new RuntimeException("Ошибка считывания переменной из Scilab");
		}
	}
	
	public static boolean isExistScilab(){
		return isCreated;
	}
}
