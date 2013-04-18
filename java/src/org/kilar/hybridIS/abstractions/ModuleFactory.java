package org.kilar.hybridIS.abstractions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.kilar.hybridIS.general.Logger;
import org.kilar.hybridIS.neuralIS.ModuleConfigNeural;
import org.kilar.hybridIS.neuralIS.NeuralISScilab;
import org.kilar.hybridIS.productionIS.ModuleConfigProduction;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * @author hkyten
 *
 */
public class ModuleFactory {
	/**
	 * creates module from given path
	 * @param path
	 * @return
	 */
	/**
	 * @param path
	 * @return
	 */
	static public Module produce(String path){
		Logger.info("Пытаюсь прочитать общую структуру модуля..." + path);
		Gson g = new Gson();
		ModuleConfig basicConfig;
		try {
			basicConfig = g.fromJson(new FileReader(new File(path)), ModuleConfig.class);
		} catch (JsonSyntaxException e) {
			Logger.error("Ошибка парсинга файла модуля");
			return null;
		} catch (JsonIOException e) {
			Logger.error("Ошибка чтения файла модуля");
			return null;
		} catch (FileNotFoundException e) {
			Logger.error("Не найден файл модуля");
			return null; 
		} 
		Logger.info("Готово");
		Logger.info("Имя " + basicConfig.getName() + ", тип " + basicConfig.getType());
		Module module = null;
		if(basicConfig.type.equalsIgnoreCase(ModuleType.Neural)){
			ModuleConfigNeural config;
			try {
				config = g.fromJson(new FileReader(new File(path)), ModuleConfigNeural.class);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				Logger.error("Ошибка чтения файла модуля");
				return null;
			}
			module = new NeuralISScilab(config);
		} else if(basicConfig.type.equalsIgnoreCase(ModuleType.Production)){
			ModuleConfigProduction config;
			try {
				config = g.fromJson(new FileReader(new File(path)), ModuleConfigProduction.class);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				Logger.error("Ошибка чтения файла модуля");
				return null;
			}
			module = new NeuralISScilab(config);
		} else if(basicConfig.type.equalsIgnoreCase(ModuleType.Fuzzy)){
			
		} else {
			Logger.error("Неизвестный тип модуля");
			return null;
		}
		
		
		return module;
	}
}
