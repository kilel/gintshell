package org.kilar.hybridIS.abstractions;

/**
 * @author hkyten
 * 
 */
enum ModuleType {
	Production, Fuzzy, Neural, Hybrid
}

public abstract class Module implements CertaintyCalculator {
	protected int inputLength, outputLength;
	protected ModuleType type;
	protected String name;

	public Module(String name, int inputLength, int outputLength) {
		this.name = name;
		this.inputLength = inputLength;
		this.outputLength = outputLength;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
};
