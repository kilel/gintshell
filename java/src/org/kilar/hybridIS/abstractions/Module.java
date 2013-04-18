package org.kilar.hybridIS.abstractions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.kilar.hybridIS.general.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * @author hkyten
 * 
 */

public abstract class Module implements CertaintyCalculator {
	protected ModuleConfig config;

	public Module(String path){
		Logger.info("Пытаюсь прочитать структуру модуля " + path);
		File f = new File(path);
		if(!f.exists()){
			Logger.error("Не найден файл модуля!");
			return;
		}
		Gson g = new Gson();
		
		try {
			config = g.fromJson(new FileReader(f), ModuleConfig.class);
		} catch (JsonSyntaxException e) {
			Logger.error("Ошибка парсинга файла модуля");
			return;
		} catch (JsonIOException e) {
			Logger.error("Ошибка чтения файла модуля");
			return;
		} catch (FileNotFoundException e) {
			// checked 
		}
		
		Logger.info("Базовая конфигурация модуля загружена");
	}
	
	public Module(ModuleConfig config){
		this.config = config;
	}

	public String getName() {
		return config.getName();
	}

	public void setName(String name) {
		config.setName(name);
	}
	
	public ModuleConfig getConfig(){
		return config;
	}
};
