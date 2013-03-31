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
	public abstract List<Double> calculate(List<List<Double>> input, List<Module> modules);
}
