/**
 * 
 */
package org.kilar.hybridIS;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author hkyten
 *
 */
public abstract class HybridIS implements ModuleIS {
	protected List<ModuleIS> modules;
	
	public HybridIS(List<ModuleIS> modules){
		this.modules = modules;
	}
	/* (non-Javadoc)
	 * @see org.kilar.hybridIS.ModuleIS#solve(java.util.Map)
	 */
	@Override
	public Map<Integer, Double> solve(Map<Integer, Double> in) {
		// TODO Auto-generated method stub
		return null;
	}

}
