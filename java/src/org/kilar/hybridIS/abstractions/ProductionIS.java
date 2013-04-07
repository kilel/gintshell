package org.kilar.hybridIS.abstractions;

/**
 * @author hkyten
 *
 */
public abstract class ProductionIS extends Module{
	public ProductionIS(String name, int inputLength, int outputLength) {
		super(name, inputLength, outputLength);
		type = ModuleType.Production;
		
	}
}
