package org.kilar.hybridIS.abstractions;

import java.util.ArrayList;
import java.util.List;

import org.kilar.hybridIS.general.Project;

/**
 * @author hkyten
 * 
 */
public class HybridIS extends Module {
	protected List<Module> modules;
	protected Integrator integrator;
	protected Project project;

	public HybridIS(ModuleConfig config) {
		super(config);
		project = new Project(((ModuleConfigHybrid) config).getProjectDir());
	}

	@Override
	public List<Double> calculate(List<Double> input) {
		return project.calculate(input);
	}
}