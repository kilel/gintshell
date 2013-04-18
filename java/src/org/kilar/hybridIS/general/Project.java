package org.kilar.hybridIS.general;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.kilar.hybridIS.abstractions.*;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * @author hkyten
 * 
 */
public class Project implements CertaintyCalculator{
	private String path;
	private ProjectConfig config;
	private Integrator integrator;
	private List<Module> modules;
	
	/**
	 * existing project opening
	 * @param path
	 * @throws Exception 
	 */
	public Project(String path) throws Exception {
		this.path = path;
		Logger.info("Пытаюсь прочитать структуру проекта " + path);
		File f = new File(path, "config");
		if(!f.exists()){
			Logger.error("Не найден конфигурационный файл");
			return;
		}
		Gson g = new Gson();
		
		try {
			config = g.fromJson(new FileReader(f), ProjectConfig.class);
		} catch (JsonSyntaxException e) {
			Logger.error("Ошибка парсинга конфигурационного файла");
			return;
		} catch (JsonIOException e) {
			Logger.error("Ошибка чтения конфигурационного файла");
			return;
		} catch (FileNotFoundException e) {
			// checked e.printStackTrace();
		}
		Logger.info("Начинаю инициализацию модулей");
		modules = new ArrayList<>();
		for(String moduleName : config.getModules()){
			File moduleFile = new File(path, moduleName);
			Module m = ModuleFactory.produce(moduleFile.getPath(), this);
			if(m == null ){
				throw new Exception(); 
			}
			if(m.getConfig().getInputLength() != config.getInputLength() 
					|| m.getConfig().getOutputLength() != config.getOutputLength() ){
				Logger.error("Не совпадает количество входных или выходных параметров!");
				throw new Exception();
			}
			modules.add(m);
		}
		Logger.info("Инициализация модулей прошла успешно");
		integrator = IntegratorFactory.produce(new File(path, config.getIntegrator()).getPath());
		if(integrator == null){
			throw new Exception();
		}
		Logger.info("Проект успешно открыт");
	}
	
	/**
	 * new project constructor
	 * @param name
	 * @param path
	 */
	public Project(String name, String path, int inSize, int outSize) {
		setName(name);
		this.path = path;
		modules = new LinkedList<Module>();
		config.setInNames(new String[0]);
		config.setOutNames(new String[0]);
		config.setOutputLength(outSize);
		config.setInputLength(inSize);
	}

	public void addModule(Module module) {
		modules.add(module);
	}

	public void delModule(String name) {
		getModuleIterator(name).remove();
	}

	ListIterator<Module> getModuleIterator(String name) {
		ListIterator<Module> it = modules.listIterator();
		while (it.hasNext()) {
			Module module = it.next();
			if (module.getName().equals(name)) {
				return it;
			}
		}
		return null;
	}

	Module getModule(String name) {
		ListIterator<Module> it = getModuleIterator(name);
		if (it.hasNext()) {
			it.next();
			return it.previous();
		} else if (it.hasPrevious()) {
			it.previous();
			return it.next();
		} else
			return modules.get(0);
	}

	public void setIntegrator(Integrator newIntegrator) {
		this.integrator = newIntegrator;
	}

	public String getIntegratorName() {
		return this.integrator.getName();
	}

	public String getName() {
		return config.getName();
	}

	public void setName(String newName) {
		config.setName(newName);
	}

	public ProjectConfig getConfig(){
		return config;
	}
	
	public List<Module> getModules(){
		return modules;
	}
	
	public String[] getInDataNames(){
		return config.getInNames();
	}
	
	public String[] getOutDataNames(){
		return config.getOutNames();
	}
	
	public int getInputLength(){
		return config.getInputLength();
	}
	
	public int getOutputLength(){
		return config.getOutputLength();
	}
	public void setPath(File value){
		try {
			path = value.getCanonicalPath();
		} catch (IOException e) {
			Logger.error("Не могу сменить путь проекта на " + value.getPath());
			return;
		}
	}
	
	public File getPath(){
		return new File(path);
	}
	
	@Override
	public List<Double> calculate(List<Double> input) {
		List<List<Double>> integratorInput = new ArrayList<List<Double>>();
		for (Module module : modules) {
			integratorInput.add(module.calculate(input));
		}
		List<Double> ret = integrator.calculate(integratorInput, modules);
		return ret;
	}
}
