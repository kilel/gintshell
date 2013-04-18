package org.kilar.hybridIS.neuralIS;

import java.util.List;

import org.kilar.hybridIS.abstractions.ModuleConfig;

public class ModuleConfigNeural extends ModuleConfig{
	private Integer[] layers;
	
	public Integer[] getLayers(){
		return layers;
	}
	
	public void setLayers(Integer[] value){
		layers = value;
	}
}
