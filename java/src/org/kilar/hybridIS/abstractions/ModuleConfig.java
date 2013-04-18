package org.kilar.hybridIS.abstractions;

public class ModuleConfig {
	
	protected int inputLength;
	protected int outputLength;
	protected String name;
	protected String type;
	
	public ModuleConfig(){
		name = "";
		type = "";
		inputLength = 0;
		outputLength = 0;
	}
	
	public ModuleConfig(String name, String type, int inputLength, int outputLength){
		this.name = name;
		this.type = type;
		this.inputLength = inputLength;
		this.outputLength = outputLength;
	}
	
	public int getInputLength(){
		return inputLength;
	}
	
	public int getOutputLength(){
		return outputLength;
	}
	
	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}
	
	public void setInputLength(int value){
		inputLength = value;
	}
	
	public void setOutputLength(int value){
		outputLength = value;
	}
	
	public void setName(String value){
		name = value;
	}
	
	public void setType(String value){
		type = value;
	}
}
