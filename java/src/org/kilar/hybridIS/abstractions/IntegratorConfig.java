package org.kilar.hybridIS.abstractions;

public class IntegratorConfig {
	private String name;
	private String type;
	
	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}
	
	public void setName(String value){
		name = value;
	}
	
	public void setType(String value){
		type = value;
	}
}
