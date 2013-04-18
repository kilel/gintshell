package org.kilar.hybridIS.productionIS;

import org.kilar.hybridIS.abstractions.ModuleConfig;

public class ModuleConfigProduction extends ModuleConfig{
	private String code;
	
	public String getCode(){
		return code;
	}
	
	public void setCode(String value){
		code = value;
	}
}
