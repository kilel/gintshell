package org.kilar.hybridIS;

import java.util.Map;


/**
 * defines the main behavior of any intellectual system module
 * 
 * @author hkyten
 * 
 */
public interface ModuleIS {
	/**
	 * 
	 * @param in Map : number of constraint -- certainty in it 
	 * @return Map: number of output value -- certainty in it
	 */
	public Map<Integer, Double> solve(Map<Integer, Double> in);
}
