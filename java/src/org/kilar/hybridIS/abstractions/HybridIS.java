package org.kilar.hybridIS.abstractions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hkyten
 * 
 */
public class HybridIS extends Module {
	protected List<Module> modules;
	protected Integrator integrator;

	public HybridIS(String name, List<Module> modules, Integrator integrator) {
		super(name);
		this.modules = modules;
		this.integrator = integrator;
		type = ModuleType.Hybrid;
		
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