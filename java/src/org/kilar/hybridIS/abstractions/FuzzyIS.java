package org.kilar.hybridIS.abstractions;

/**
 * @author trylar
 * 
 */
public abstract class FuzzyIS extends Module {
	public FuzzyIS(String name) {
		super(name);
		type = ModuleType.Fuzzy;
	}
}
