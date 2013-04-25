package org.kilar.hybridIS.neuralIS;

import java.util.ArrayList;
import java.util.List;

import org.kilar.hybridIS.abstractions.ModuleType;
import org.kilar.hybridIS.abstractions.NeuralIS;
import org.kilar.hybridIS.general.Logger;
import org.kilar.hybridIS.general.ScilabAdapter;
import org.kilar.hybridIS.general.Util;
import org.scilab.modules.javasci.JavasciException;
import org.scilab.modules.javasci.Scilab;
import org.scilab.modules.types.ScilabTList;
import org.scilab.modules.types.ScilabType;

/**
 * @author trylar
 *
 */
public class NeuralISScilab extends NeuralIS {
	
	public NeuralISScilab(ModuleConfigNeural config) {
		super(config);
		ScilabAdapter.initialize();
		ScilabAdapter.open();
		String str = null;
		str += "rand('seed', 0);";
		str += "N = [";
		str += Integer.toString(config.getInputLength());
		for (int size : config.getLayers()){
			str += ", " + Integer.toString(size);
		}
		str += ", " + Integer.toString(config.getOutputLength());
		str += "];";
		str += "W = ann_FF_init(N)";
		
		ScilabAdapter.exec(str);
		ScilabAdapter.close();
	}

	@Override
	public List<Double> calculate(List<Double> input) {
		String str = "";
		List<Double> output = null;
		
		str = "output = 1";
		
		ScilabAdapter.exec(str);

		/*try {
			ScilabType out = ScilabAdapter.get("output");
			//output = (List<Double>) out.getSerializedObject();
			//[TODO] not working
		} catch (JavasciException e) {
			Logger.error("Общая ошибка работы нейросети");
			throw new RuntimeException();
		}*/
		
		//return output;
		return Util.getZeroList(config.getOutputLength());
	}

	@Override
	public void train(List<List<Double>> trainingInput,
			List<Double> trainingOutput) {
		
		
		try {
			ScilabType W = ScilabAdapter.get("W");
		} catch (JavasciException e1) {
			Logger.error("Общая ошибка работы нейросети");
			throw new RuntimeException();
		}
		
		
		String str = null;
		
		str += "W = ann_FF_Std_online([";
		
		for (int i=0; i<trainingInput.size(); ++i){
			List<Double> column = trainingInput.get(i);
			for (int j=0; j<column.size(); ++j){
				Double d = column.get(j);
				str += Double.toString(d);
				if (j != column.size()-1)
					str += ",";
			}
			if (i != trainingInput.size()-1)
				str += ";";
		}
		str += "], [";
		
		for (Double d : trainingOutput){
			str += Double.toString(d) + " ";
		}
		str += "], ";
		
		try {
			str += ScilabAdapter.get("N") + ", ";
			str += ScilabAdapter.get("W") + ", ";
		} catch (JavasciException e) {
			Logger.error("Общая ошибка работы нейросети");
			throw new RuntimeException();
		}

		str += "[0.5, 0.05], ";
		
		str += "350";
		
		str += ");";
		
		ScilabAdapter.exec(str);
		
	}
	
	public static void main(String[] argc){
		//System.loadLibrary("javasci2");
		List<Integer> list = new ArrayList<Integer>();
		list.add(3);
		list.add(5);
		list.add(5);
		ModuleConfigNeural nc = new ModuleConfigNeural();
		nc.setName("qwe");
		nc.setInputLength(3);
		nc.setOutputLength(3);
		nc.setLayers(list.toArray(new Integer[0]));
		nc.setType(ModuleType.Neural);
		NeuralISScilab neuralISScilab = new NeuralISScilab(nc);
		//neuralISScilab.train(<<1, 0, 0, 0, 0>, <0, 1, 0, 0, 0>, <0, 0, 1, 0, 0>, <0, 0, 0, 1, 0>, <0, 0, 0, 0, 1>>, <1, 1, 1, 0, 0>);
		
	}

}
