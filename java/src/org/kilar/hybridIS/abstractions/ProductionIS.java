package org.kilar.hybridIS.abstractions;

/**
 * @author hkyten
 *
 */
public abstract class ProductionIS extends Module{
	public ProductionIS(String name) {
		super(name);
		type = ModuleType.Production;
		
	}
}
