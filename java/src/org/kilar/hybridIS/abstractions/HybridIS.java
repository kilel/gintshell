package org.kilar.hybridIS.abstractions;

import java.util.ArrayList;
import java.util.List;

import org.kilar.hybridIS.general.Logger;
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
		try {
			project = new Project(((ModuleConfigHybrid) config).getProjectDir());
		} catch (Exception e) {
			Logger.error("Не могу инициализировать модуль " + getName() + ": не ошибка открытия проекта гибридной ИС");
		}
	}

	@Override
	public List<Double> calculate(List<Double> input) {
		return project.calculate(input);
	}
}