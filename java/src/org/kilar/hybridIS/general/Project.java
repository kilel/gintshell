package org.kilar.hybridIS.general;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.kilar.hybridIS.abstractions.*;

/**
 * @author hkyten
 * 
 */
public class Project implements CertaintyCalculator{
	private String name;
	private Integrator integrator;
	private List<Module> modules;
	private Map<String, Integer> inputDataNames, outputDataNames;
	
	public Project(String name) {
		setName(name);
		modules = new LinkedList<Module>();
	}

	public void addModule(Module module) {
		modules.add(module);
	}

	public void delModule(String name) {
		getModuleIterator(name).remove();
	}

	ListIterator<Module> getModuleIterator(String name) {
		ListIterator<Module> it = modules.listIterator();
		while (it.hasNext()) {
			Module module = it.next();
			if (module.getName().equals(name)) {
				return it;
			}
		}
		return null;
	}

	Module getModule(String name) {
		ListIterator<Module> it = getModuleIterator(name);
		if (it.hasNext()) {
			it.next();
			return it.previous();
		} else if (it.hasPrevious()) {
			it.previous();
			return it.next();
		} else
			return modules.get(0);
	}

	public void setIntegrator(Integrator newIntegrator) {
		this.integrator = newIntegrator;
	}

	public String getIntegratorName() {
		return this.integrator.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		this.name = newName;
	}


	@Override
	public List<Double> calculate(List<Double> input, int inputLength, int outputLength) {
		HybridIS hybrid = new HybridIS(getName(), inputLength, outputLength, modules, integrator);
		List<Double> result = hybrid.calculate(input, inputLength, outputLength);
		
		return result;
	}
}
