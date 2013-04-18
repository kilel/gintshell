package org.kilar.hybridIS.general;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Util {
	
	public static List<Double> getZeroList(int length){
		List<Double> out = new ArrayList<>();
		for(int i = 0; i < length; ++i)
			out.add(0d);
		return out;
	}
	
	public static boolean isFileNameValid(String name){
		if(name.length() == 0){
			return false;
		}
		String path = System.getenv("TMP");
		if(path == null){ //linux
			path = System.getenv("HOME");
			if(path == null){
				path = "/temp";
				File p = new File(path);
				if(!p.exists()){
					Logger.warn("Не могу проверить имя файла на валидность, переменную среды TMP, и укажите в ней путь к каталогу временных файлов");
					return false;
				}
			}
		}
		File pathFile = new File(path);
		if(!pathFile.exists()){
			Logger.error("Каталог временных файлов не существует! (переменные окружения TMP, HOME");
			return false;
		}
		File validator = new File(path, name);
		try {
			if(validator.createNewFile())
				validator.delete(); //delete only if created by me
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
