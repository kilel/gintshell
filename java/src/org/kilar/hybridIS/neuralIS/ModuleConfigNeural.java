package org.kilar.hybridIS.neuralIS;

import org.kilar.hybridIS.abstractions.ModuleConfig;

public class ModuleConfigNeural extends ModuleConfig{
	private int[] layers;
	private double[] studyRate;
	private int studyCycles;
	
	public double[] getStudyRate() {
		return studyRate;
	}

	public void setStudyRate(double[] studyRate) {
		this.studyRate = studyRate;
	}

	public int getStudyCycles() {
		return studyCycles;
	}

	public void setStudyCycles(int studyCycles) {
		this.studyCycles = studyCycles;
	}

	public int[] getLayers(){
		return layers;
	}
	
	public void setLayers(int[] value){
		layers = value;
	}
}
