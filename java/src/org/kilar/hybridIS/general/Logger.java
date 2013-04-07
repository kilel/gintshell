package org.kilar.hybridIS.general;

public class Logger {
	private static String log = "";
	public static void clear(){
		log = "";
	}
	public static void info(String str){
		log += "\n|INFO|: " + str;
	}
	public static void warn(String str){
		log += "\n|WARN|: " + str;
	}
	public static void error(String str){
		log += "\n|ERROR|: " + str;
	}
	public static String get(){
		return log;
	}
}
