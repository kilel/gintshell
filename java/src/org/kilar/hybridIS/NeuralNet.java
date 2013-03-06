/**
 * 
 */
package org.kilar.hybridIS;

import java.util.List;
import java.util.Map;

/**
 * @author hkyten
 *
 */
public abstract class NeuralNet implements ModuleIS {
	protected List<Integer> neuronLayers;
	
	public NeuralNet(List<Integer> neuronLayers){
		this.neuronLayers = neuronLayers;
	}
	
	public void train(List<Map<Integer, Double> > in){
		
	}
	/* (non-Javadoc)
	 * @see org.kilar.hybridIS.ModuleIS#solve(java.util.Map)
	 */
	@Override
	public Map<Integer, Double> solve(Map<Integer, Double> in) {
		// TODO 
		return null;
	}

}
