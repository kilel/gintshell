package org.kilar.hybridIS.general;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private static String log = "";
	public static void clear(){
		log = "";
	}
	private static String getFormattedDate(){
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
	public static void info(String str){
		log += "\n"+ getFormattedDate() +"|INFO|: " + str;
	}
	public static void warn(String str){
		log += "\n"+ getFormattedDate() +"|WARN|: " + str;
	}
	public static void error(String str){
		log += "\n"+ getFormattedDate() +"|ERROR|: " + str;
	}
	public static String get(){
		return log;
	}
}
