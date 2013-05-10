package org.kilar.hybridIS.abstractions;

import org.kilar.hybridIS.general.Project;

/**
 * @author hkyten
 * 
 */

public abstract class Module implements CertaintyCalculator {
	protected ModuleConfig config;
	private Project parent = null;
	
	public Module(ModuleConfig config){
		this.config = config;
	}

	public String getName() {
		return config.getName();
	}

	public void setName(String name) {
		config.setName(name);
	}
	
	public void setParent(Project value){
		parent = value;
	}
	
	public ModuleConfig getConfig(){
		return config;
	}
	
	public String getType(){
		return config.getType();
	}
	
	public Project getParent(){
		return parent;
	}
};
