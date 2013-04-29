package org.kilar.hybridIS.general;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;

public class ProjectConfig {
	private String name;
	private String[] modules;
	private String[] outNames;
	private String[] inNames;
	private String integrator;
	private String dataResource;
	private String[] trainData;
	private int inputLength;
	private int outputLength;
	
	public static void main(String[] argc){
		Gson g = new Gson();
		File f = new File("/home/hkyten/workspace/gintshell/java/sampleProject/config");
		try {
			ProjectConfig p = g.fromJson(new FileReader(f), ProjectConfig.class);
			p.inputLength = 5;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String[] getTrainData() {
		return trainData;
	}

	public void setTrainData(String[] testData) {
		this.trainData = testData;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDataResource(){
		return dataResource;
	}
	
	public String getIntegrator(){
		return integrator;
	}
	
	public String[] getModules(){
		return modules;
	}
	
	public String[] getOutNames(){
		return outNames;
	}
	
	public String[] getInNames(){
		return inNames;
	}
	
	public int getInputLength(){
		return inputLength;
	}
	
	public int getOutputLength(){
		return outputLength;
	}
	
	public void setName(String value){
		name = value;
	}
	
	public void setDataResource(String value){
		dataResource = value;
	}
	
	public void setIntegrator(String value){
		integrator = value;
	}
	
	public void setModules(String[] value){
		modules = value;
	}
	
	public void setInNames(String[] value){
		inNames = value;
	}
	
	public void setOutNames(String[] value){
		outNames = value;
	}
	
	public void setInputLength(int value){
		inputLength = value;
	}
	
	public void setOutputLength(int value){
		outputLength = value;
	}
	
}
