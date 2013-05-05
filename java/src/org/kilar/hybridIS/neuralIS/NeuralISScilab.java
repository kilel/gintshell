package org.kilar.hybridIS.neuralIS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kilar.hybridIS.abstractions.ModuleConfig;
import org.kilar.hybridIS.abstractions.ModuleType;
import org.kilar.hybridIS.abstractions.NeuralIS;
import org.kilar.hybridIS.general.Logger;
import org.kilar.hybridIS.general.ScilabAdapter;
import org.kilar.hybridIS.general.Util;
import org.scilab.modules.types.ScilabDouble;
import org.scilab.modules.types.ScilabInteger;
import org.scilab.modules.types.ScilabMList;
import org.scilab.modules.types.ScilabType;

/**
 * @author trylar
 *
 */
public class NeuralISScilab extends NeuralIS {
	private ScilabDouble speedParam;
	ScilabInteger layers, cycles;
	public ScilabType weights;
	
	public NeuralISScilab(ModuleConfig config) {
		super(config);
		ScilabAdapter.open();
		ModuleConfigNeural curConf = (ModuleConfigNeural) config;
		int[] l = curConf.getLayers();
		int lay[] = new int[l.length + 2];
		lay[0] = config.getInputLength();
		lay[lay.length - 1] = config.getOutputLength();
		for(int i = 0; i < l.length; ++i) {
			lay[i+1] = l[i];
		}
		//Logger.warn("Не установлены весовые коэффициенты нейронной сети " + getName() + ". Устанавливаю по умолчанию");
		String sciInit = "layers = " + Arrays.toString(lay) + ";";
		sciInit += "weights = ann_FF_init(layers);";
		ScilabAdapter.exec(sciInit);
		weights = ScilabAdapter.get("weights");

		layers = TypeTransformerScilab.getInt(lay);
		if(curConf.getStudyRate() != null){
			speedParam = TypeTransformerScilab.getDouble(curConf.getStudyRate());
		} else {
			Logger.warn("Не установлен параметр скорости обучения нейронной сети " + getName() + ". Устанавливаю по умолчанию");
			speedParam = TypeTransformerScilab.getDouble(new double[] {0.5, 0});
			curConf.setStudyRate(new double[] {0.5, 0});
		}
		if(curConf.getStudyCycles() > 100){
			cycles = TypeTransformerScilab.getInt(new int[]{curConf.getStudyCycles()});
		} else {
			cycles = TypeTransformerScilab.getInt(new int[]{1000});
			curConf.setStudyCycles(1000);
		}
		ScilabAdapter.close();
	}
	
	@Override
	public List<Double> calculate(List<Double> input) {
		ScilabAdapter.open();
		String query = "";
		List<Double> ret = new ArrayList<>();
		ScilabAdapter.exec("data = " + Util.ListToScilabVector(input) + ";");
		//ScilabAdapter.exec("data = (data + 1)/ 2;");
		ScilabAdapter.set("weights", weights);
		ScilabAdapter.set("layers", layers);
		ScilabAdapter.exec("result = ann_FF_run(data', layers, weights);");
		ScilabAdapter.exec("result = result * 2 - 1;");
		ScilabDouble sciResult =(ScilabDouble) ScilabAdapter.get("result");
		ScilabAdapter.close();
		for(double[] val : sciResult.getRealPart()){//too unsafe
			ret.add(val[0]);
		}
		Logger.info("Результат вычислений модуля " + getName() + " = " + Util.ListToString(ret));
		return ret;
	}

	@Override
	public void train(List<List<Double>> trainingInput, List<List<Double>> trainingOutput) {
		ScilabAdapter.open();
		String init;
		ScilabAdapter.exec("data = " + Util.ListToScilabArray(trainingInput) + "; ");
		//ScilabAdapter.exec("data = (data + 1)/ 2;");
		ScilabAdapter.exec("out = " + Util.ListToScilabArray(trainingOutput) + ";");
		ScilabAdapter.exec("out = (out + 1)/ 2;");
		ScilabAdapter.set("weights", weights);
		ScilabAdapter.set("layers", layers);
		ScilabAdapter.set("speed", speedParam);
		ScilabAdapter.set("cycles", cycles);
		init = "weights2 = ann_FF_Std_online(data', out', layers, weights, speed, cycles);";
		ScilabAdapter.exec(init);
		weights = ScilabAdapter.get("weights2");
		
		ScilabAdapter.close();
	}
	
	public static void main(String[] argc){
		
	}

}
