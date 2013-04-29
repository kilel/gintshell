package org.kilar.hybridIS.abstractions;

/**
 * @author hkyten
 *
 */
public abstract class ProductionIS extends Module{
	String code;
	
	public ProductionIS(ModuleConfig config) {
		super(config);
	}
	
	public void setCode(String value){
		code = value;
	}
	
	public String getCode(){
		return code;
	}
	
	public abstract boolean isValid();
	
}
