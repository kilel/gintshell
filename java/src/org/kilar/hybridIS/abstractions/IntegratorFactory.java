package org.kilar.hybridIS.abstractions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.kilar.hybridIS.general.IntegratorBasic;
import org.kilar.hybridIS.general.Logger;


import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class IntegratorFactory {
	public static Integrator produce(String path){
		Logger.info("Начинаю инициализацию интератора " + path);
		Gson g = new Gson();
		IntegratorConfig config;
		try {
			config = g.fromJson(new FileReader(new File(path)), IntegratorConfig.class);
		} catch (JsonSyntaxException e) {
			Logger.error("Ошибка парсинга файла интегратора");
			return null;
		} catch (JsonIOException e) {
			Logger.error("Ошибка чтения файла интегратора");
			return null;
		} catch (FileNotFoundException e) {
			Logger.error("Не найден файл интегратора");
			return null; 
		}
		Integrator integrator;
		if(config.getType().equalsIgnoreCase(IntegratorType.Basic)){
			integrator = new IntegratorBasic(config);
		} else {
			Logger.error("Неизвестный тип  интегратора");
			return null;
		}
		Logger.info("Готово");
		Logger.info("Интегратор " + integrator.getName() + " загружен");
		return integrator;
	}
}
