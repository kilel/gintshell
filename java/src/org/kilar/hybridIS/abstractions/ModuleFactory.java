package org.kilar.hybridIS.abstractions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.kilar.hybridIS.fuzzyIS.FuzzyISScilab;
import org.kilar.hybridIS.fuzzyIS.ModuleConfigFuzzy;
import org.kilar.hybridIS.general.Logger;
import org.kilar.hybridIS.general.Project;
import org.kilar.hybridIS.general.Util;
import org.kilar.hybridIS.neuralIS.ModuleConfigNeural;
import org.kilar.hybridIS.neuralIS.NeuralISScilab;
import org.kilar.hybridIS.productionIS.ModuleConfigProduction;
import org.kilar.hybridIS.productionIS.ProductionISConcrete;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * @author hkyten
 *
 */
public class ModuleFactory {
	
	static public Module produce(String name, String type, Project parent){
		ModuleConfig cfg;
		Module m;
		if(type.equals(ModuleType.Production)) {
			cfg = new ModuleConfigProduction();
		} else if(type.equals(ModuleType.Neural)) {
			cfg = new ModuleConfigNeural();
		} else {
			throw new RuntimeException();
		}

		cfg.setName(name);
		cfg.setType(type);
		cfg.setOutputLength(parent.getConfig().getOutputLength());
		cfg.setInputLength(parent.getConfig().getInputLength());
		///
		if(type.equals(ModuleType.Production)) {
			((ModuleConfigProduction) cfg).setCode(name + ".code");
			m = new ProductionISConcrete(cfg);
			((ProductionIS) m).setCode("");
		} else if(type.equals(ModuleType.Neural)) {
			((ModuleConfigNeural) cfg).setLayers(new int[0]);
			m = new NeuralISScilab(cfg);
		} else {
			throw new RuntimeException();
		}	
		///
		m.setParent(parent);
		return m;
	}
	
	/**
	 * creates module from given path linked to the project
	 * @param path
	 * @return
	 */
	static public Module produce(String path, Project parent){
		Module m = produce(path);
		m.setParent(parent);
		if(m.getType().equals(ModuleType.Neural)){
			((NeuralIS) m).train(parent.getTrainData(), parent.getTrainOutData());
		}
		return m;
	}
	
	/**
	 * creates module from given path with no link to the project. Some modules could   
	 * @param path
	 * @return
	 */
	static private Module produce(String path){
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
		Logger.info("Пытаюсь считать расширенную конфигурацию модуля...");
		Module module = null;
		///
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
			module = new ProductionISConcrete(config);
			((ProductionIS)module).setCode(Util.getFileTextFull(new File(new File(path).getParent(), config.getCode())));
		} else if(basicConfig.type.equalsIgnoreCase(ModuleType.Fuzzy)){
			ModuleConfigFuzzy config;
			try {
				config = g.fromJson(new FileReader(new File(path)), ModuleConfigFuzzy.class);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				Logger.error("Ошибка чтения файла модуля");
				return null;
			}
			module = new FuzzyISScilab(config);
		} else if(basicConfig.type.equalsIgnoreCase(ModuleType.Hybrid)){
			ModuleConfigHybrid config;
			try {
				config = g.fromJson(new FileReader(new File(path)), ModuleConfigHybrid.class);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				Logger.error("Ошибка чтения файла модуля");
				return null;
			}
			module = new HybridIS(config);
		} else {
			Logger.error("Неизвестный тип модуля");
			return null;
		}
		///
		Logger.info("Готово");
		Logger.info("Модуль " + module.getName() + " загружен");
		return module;
	}
}
