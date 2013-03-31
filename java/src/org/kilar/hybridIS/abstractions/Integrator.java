package org.kilar.hybridIS.abstractions;

import java.util.List;

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
	protected String name = "Abstract integrator";
	public abstract List<Double> calculate(List<List<Double>> input, List<Module> modules);
	public Integrator(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
}
