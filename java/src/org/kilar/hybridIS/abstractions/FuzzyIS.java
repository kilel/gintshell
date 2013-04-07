package org.kilar.hybridIS.abstractions;

/**
 * @author trylar
 * 
 */
public abstract class FuzzyIS extends Module {
	public FuzzyIS(String name, int inputLength, int outputLength) {
		super(name, inputLength, outputLength);
		type = ModuleType.Fuzzy;
	}
}
