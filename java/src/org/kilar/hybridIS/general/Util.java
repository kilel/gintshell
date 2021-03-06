package org.kilar.hybridIS.general;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.kilar.hybridIS.neuralIS.ModuleConfigNeural;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

public class Util {
	
	public static List<Double> getZeroList(int length){
		List<Double> out = new ArrayList<>();
		for(int i = 0; i < length; ++i)
			out.add(0d);
		return out;
	}
	public static void saveObjectToFile(Object obj, File dest) throws IOException{
		dest.createNewFile();
		Gson g = new Gson();
		JsonWriter wr = new JsonWriter(new FileWriter(dest));
		wr.setIndent("\t");
		g.toJson(obj, obj.getClass(), wr);
		wr.close();
	}
	public static void saveToFile(File file, String s){
		try {
			file.createNewFile();
			FileWriter wr = new FileWriter(file);
			wr.write(s);
			wr.close();
		} catch (IOException e) {
			Logger.error("Не могу записать изменения в файл");
			throw new RuntimeException();
		}
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
	
	static public String getFileTextFull(File file){
		String ret = "";
		Scanner sc;
		try {
			sc = new Scanner(file);
			while(sc.hasNextLine()){
				ret += sc.nextLine() + "\n";
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return ret;
	}
	
	public static String ListToString(Object li){
		if(List.class.isInstance(li)){
			String out = "["; 
			List<Object> a = (List<Object>) li;
			for(int i = 0; i < a.size(); ++i){
				if(i != 0)
					out += ",";
				out += ListToString(a.get(i));
			}
			return out + "]";
		}
		else return li.toString();
	}
	
	public static String ListToScilabArray(List<List<Double>> li){
		String s = "[";
		DecimalFormat formatter = new DecimalFormat("#.####");
		formatter.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
		for(List<Double> el : li){
			for(Double o : el){
				s += formatter.format(o) + " ";
				
			}
			s += ";";
		}
		return s + "]";
	}
	
	public static String ListToScilabVector(List<Double> li){
		String s = "[";
		DecimalFormat formatter = new DecimalFormat("#.####");
		formatter.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
		for(Object o : li){
			s += formatter.format(o) + " ";
		}
		return s + "]";
	}
}
