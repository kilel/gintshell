package org.kilar.hybridIS.general;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import org.kilar.hybridIS.abstractions.*;
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
public class Project implements CertaintyCalculator{
	private String path;
	private ProjectConfig config;
	private Integrator integrator;
	private List<Module> modules;
	/** input name -> number in list  */
	private Map<String, Integer> inputToNum;
	
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
		if(config.getInNames().length != config.getInputLength() 
				|| config.getOutNames().length != config.getOutputLength()){
			Logger.error("Длина входных или выходных данных не соответствует длине, указанной в параметрах");
			throw new Exception();
		}
		Logger.info("Начинаю инициализацию модулей");
		modules = new ArrayList<>();
		for(String moduleName : config.getModules()){
			File moduleFile = new File(path, moduleName);
			Module m = ModuleFactory.produce(moduleFile.getPath(), this);
			if(m == null ){
				throw new Exception(); 
			}
			m.setName(moduleName);
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
		integrator.setName(config.getIntegrator());
		inputToNum = new HashMap<>();
		for(int i = 0; i < getInputLength(); ++i){
			inputToNum.put(getInDataNames()[i], i);
		}
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
	
	public List<List<Double> > getData() throws RuntimeException{
		File source = new File (path, config.getDataResource());
		List<List<Double> > ret = new ArrayList<>();
		List<Double> cur = new ArrayList<>();
		/** router[i] = p <==> current input name [i]  == real input name [k]  */
		int[] router = new int[getInputLength()];
		Scanner sc;
		try {
			sc = new Scanner(source);
		} catch (FileNotFoundException e) {
			Logger.error("Не могу открыть файл с данными для запуска, файл" + source.getPath() + "не найден");
			throw new RuntimeException();
		}
		try{
			int n = sc.nextInt(), k = sc.nextInt();
			if(k != getInputLength()){
				Logger.error("Неверная длина вектора входных данных");
				sc.close();
				throw new RuntimeException();
			}
			//reading names
			Set<Integer> set = new HashSet<>();
			for(int i = 0; i < k; ++i){
				String name = sc.next();
				Integer route = inputToNum.get(name);
				if(route == null){
					Logger.error("Неизвестная входная переменная (" + name + ")");
					sc.close();
					throw new RuntimeException();
				}
				router[i] = route;
				if(set.contains(route)){
					Logger.error("Переопределение входной переменной (" + name + ")");
					sc.close();
					throw new RuntimeException();
				}
				set.add(route);
			}
			//reading data
			for(int cnt = 0; cnt < n; ++cnt){
				cur.clear();
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
	
}
