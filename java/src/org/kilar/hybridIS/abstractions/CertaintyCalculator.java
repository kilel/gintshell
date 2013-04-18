/**
 * 
 */
package org.kilar.hybridIS.abstractions;

import java.util.List;

/**
 * @author hkyten
 *
 */
public interface CertaintyCalculator {
	/**
	 * @param input certainty vector
	 * @return certainty of output variables vector
	 */
	public List<Double> calculate(List<Double> input);
}
