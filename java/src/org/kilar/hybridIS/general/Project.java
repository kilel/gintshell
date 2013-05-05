package org.kilar.hybridIS.general;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import org.kilar.hybridIS.abstractions.CertaintyCalculator;
import org.kilar.hybridIS.abstractions.Integrator;
import org.kilar.hybridIS.abstractions.IntegratorFactory;
import org.kilar.hybridIS.abstractions.Module;
import org.kilar.hybridIS.abstractions.ModuleFactory;

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
		config.setInputLength(config.getInNames().length);
		config.setOutputLength(config.getOutNames().length);
		
		Logger.info("Начинаю инициализацию модулей");
		modules = new ArrayList<>();
		for(String moduleName : config.getModules()){
			File moduleFile = new File(path, moduleName);
			Module m = ModuleFactory.produce(moduleFile.getPath(), this);
			if(m == null ){
				throw new Exception("Ошибка создания модуля"); 
			}
			m.setName(moduleName);getClass();
			if(m.getConfig().getInputLength() != config.getInputLength() 
					|| m.getConfig().getOutputLength() != config.getOutputLength() ){
				Logger.error("Не совпадает количество входных или выходных параметров в модуле " + m.getName());
				//throw new Exception();
			}
			modules.add(m);
		}
		Logger.info("Инициализация модулей прошла успешно");
		integrator = IntegratorFactory.produce(new File(path, config.getIntegrator()).getPath());
		if(integrator == null){
			throw new Exception("Ошибка создания интегратора");
		}
		integrator.setName(config.getIntegrator());
		Logger.info("Проект успешно открыт");
	}
	
	/**
	 * new project constructor
	 * @param name
	 * @param path
	 */
	public Project(ProjectConfig config) {
		this.config = config; 
	}

	public void addModule(Module module) {
		modules.add(module);
		List<String> mod = new ArrayList<>();
		for(String m : config.getModules()){
			mod.add(m);
		}
		mod.add(module.getName());
		config.setModules(mod.toArray(new String[0]));
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

	public Module getModule(String name) {
		ListIterator<Module> it = getModuleIterator(name);
		if(it == null){
			return null;
		}
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
	
	public Integrator getIntegrator() {
		return this.integrator;
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
	public void setPath(String value){
		path = value;
	}
	
	public String getPath(){
		return path;
	}
	
	@Override
	public List<Double> calculate(List<Double> input) {
		List<List<Double>> integratorInput = new ArrayList<List<Double>>();
		Logger.info("Новое вычисление:");
		Logger.info("x = " + Util.ListToString(input));
		for (Module module : modules) {
			integratorInput.add(module.calculate(input));
		}
		List<Double> ret = integrator.calculate(integratorInput, modules);
		return ret;
	}
	
	/**
	 * Reads data from dataResource
	 * @return
	 */
	public List<List<Double>> calculate(){
		List<List<Double>>  ret = new ArrayList<>(), input = getData();
		for(List<Double> data : input){
			ret.add(calculate(data));
		}
		return ret;
	}
	
	private List<List<Double> > getData(String dataPath, String[] dataNames){
		File source = new File (path, dataPath);
		List<List<Double> > ret = new ArrayList<>();
		List<Double> cur;
		Scanner sc;
		try {
			sc = new Scanner(source);
			sc.useLocale(Locale.US);
		} catch (FileNotFoundException e) {
			Logger.error("Не могу открыть файл с данными для запуска, файл" + source.getPath() + "не найден");
			throw new RuntimeException();
		}
		try{
			int n = sc.nextInt(), k = sc.nextInt();
			if(k != dataNames.length){
				Logger.error("Неверная длина вектора входных данных");
				sc.close();
				throw new RuntimeException();
			}
			//reading data
			for(int cnt = 0; cnt < n; ++cnt){
				cur = new ArrayList<>();
				for(int i = 0; i < k; ++i){
					double data = sc.nextDouble();
					cur.add(data);
				}
				ret.add(cur);
			}
		} catch (InputMismatchException e){
			Logger.error("Неверный формат входных данных");
			sc.close();
			throw new RuntimeException();
		} catch (NoSuchElementException e){
			Logger.error("Не найден очередной элемент данных");
			sc.close();
			throw new RuntimeException();
		}
		sc.close();
		return ret; 
	}
	
	public List<List<Double> > getData() {
		return getData(config.getDataResource(), config.getInNames());	
	}
	
	public List<List<Double> > getTrainData() {
		return getData(config.getTrainData()[0], config.getInNames());	
	}
	
	public List<List<Double> > getTrainOutData() {
		return getData(config.getTrainData()[1], config.getOutNames());	
	}
	
}
