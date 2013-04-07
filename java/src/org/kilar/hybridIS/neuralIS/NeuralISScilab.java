package org.kilar.hybridIS.neuralIS;

import java.util.ArrayList;
import java.util.List;

import org.kilar.hybridIS.abstractions.NeuralIS;
import org.kilar.hybridIS.general.ScilabAdapter;
import org.scilab.modules.javasci.JavasciException;
import org.scilab.modules.javasci.Scilab;
import org.scilab.modules.types.ScilabType;

/**
 * @author trylar
 *
 */
public class NeuralISScilab extends NeuralIS {
	
	private Scilab scilab;
	
	public NeuralISScilab(String name, int inputLength, int outputLength, List<Integer> sizeOfLayers) {
		super(name, inputLength, outputLength);
		if (ScilabAdapter.isExistScilab() == false){
			new ScilabAdapter();
		}
		scilab = ScilabAdapter.getScilab();
		
		String str = null;
		str += "rand('seed', 0);";
		str += "N = [";
		str += Integer.toString(inputLength);
		for (int size : sizeOfLayers){
			str += ", " + Integer.toString(size);
		}
		str += ", " + Integer.toString(outputLength);
		str += "];";
		str += "W = ann_FF_init(N)";
		
		scilab.exec(str);
	}

	@Override
	public List<Double> calculate(List<Double> input, int inputLength, int outputLength) {
		
		String str = null;
		List<Double> output = null;
		
		str += "";
		
		scilab.exec(str);
		
		try {
			ScilabType out = scilab.get("output");
			output = (List<Double>) out.getSerializedObject();
		} catch (JavasciException e) {
			e.printStackTrace();
		}
		
		return output;
	}

	@Override
	public void train(List<List<Double>> trainingInput,
			List<Double> trainingOutput) {
		
		try {
			ScilabType W = scilab.get("W");
		} catch (JavasciException e) {
			e.printStackTrace();
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
			str += scilab.get("N") + ", ";
			str += scilab.get("W") + ", ";
		} catch (JavasciException e) {
			e.printStackTrace();
		}

		str += "[0.5, 0.05], ";
		
		str += "350";
		
		str += ");";
		
		scilab.exec(str);
		
	}
	
	public static void main(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(3);
		list.add(5);
		list.add(5);
		NeuralISScilab neuralISScilab = new NeuralISScilab("qwert", 5, 5, list);
		//neuralISScilab.train(<<1, 0, 0, 0, 0>, <0, 1, 0, 0, 0>, <0, 0, 1, 0, 0>, <0, 0, 0, 1, 0>, <0, 0, 0, 0, 1>>, <1, 1, 1, 0, 0>);
		
	}

}
