package org.kilar.hybridIS.abstractions;

import java.util.List;

class IntegratorType{
	static public final String Basic = "basic"; 
}

/**
 * @author hkyten
 * 
 */
public abstract class Integrator {

	/**
	 * @param input
	 *            list of certainties from modules
	 * @return integrated certainty
	 */
	protected IntegratorConfig config;
	public abstract List<Double> calculate(List<List<Double>> input, List<Module> modules);
	public Integrator(IntegratorConfig config){
		this.config = config;
	}
	public String getName(){
		return config.getName();
	}
	
	public void setName(String value){
		config.setName(value);
	}
	
	public IntegratorConfig getConfig(){
		return config;
	}
}
