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
	public ScilabType weigths;
	
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
		Logger.warn("Не установлены весовые коэффициенты нейронной сети " + getName() + ". Устанавливаю по умолчанию");
		String sciInit = "layers = " + Arrays.toString(lay) + ";";
		sciInit += "weights = ann_FF_init(layers);";
		ScilabAdapter.exec(sciInit);
		weigths = ScilabAdapter.get("weights");

		layers = TypeTransformerScilab.getInt(lay);
		if(curConf.getStudyRate() != null){
			speedParam = TypeTransformerScilab.getDouble(curConf.getStudyRate());
		} else {
			Logger.warn("Не установлен параметр скорости обучения нейронной сети " + getName() + ". Устанавливаю по умолчанию");
			speedParam = TypeTransformerScilab.getDouble(new double[] {0.5, 0});
			curConf.setStudyRate(new double[] {0.5, 0});
		}
		if(curConf.getStudyCycles() < 100){
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
		query = "data = " + Util.ListToString(input) + ";";
		ScilabAdapter.set("weights", weigths);
		ScilabAdapter.set("layers", layers);
		query += "result = ann_FF_run(data', layers, weights);";
		ScilabAdapter.exec(query);
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
		String init = "data = " + Util.ListToString(trainingInput) + "; ";
		init += "out = " + Util.ListToString(trainingOutput) + ";";
		ScilabAdapter.set("weights", weigths);
		ScilabAdapter.set("layers", layers);
		ScilabAdapter.set("speed", speedParam);
		ScilabAdapter.set("cycles", cycles);
		init += "weights = ann_FF_Std_online(data', out, layers, weights, speed, cycles);";
		ScilabAdapter.exec(init);
		weigths = ScilabAdapter.get("weights");
		ScilabAdapter.close();
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] argc){
		//System.loadLibrary("javasci2");
		ScilabMList weigths;
		
		/*ScilabAdapter.initialize();
		ScilabAdapter.open();
		String sciInit = "layers = [2,2,1];";
		
		sciInit += "weights = ann_FF_init(layers);";
		ScilabAdapter.exec(sciInit );
		weigths = (ScilabMList) ScilabAdapter.get("weights");
		ScilabAdapter.close();
		try {
			Util.saveObjectToFile(weigths, new File("/home/hkyten/aaaaaaaa"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		

		List<Double> list = new ArrayList<Double>();
		list.add(3d);
		list.add(5d);
		list.add(5d);
		ModuleConfigNeural nc = new ModuleConfigNeural();
		nc.setName("qwe");
		nc.setInputLength(3);
		nc.setOutputLength(3);
		nc.setLayers(new int[]{2,3,4});
		nc.setType(ModuleType.Neural);
		NeuralISScilab neuralISScilab = new NeuralISScilab(nc);
		neuralISScilab.calculate(list);
		//neuralISScilab.train(<<1, 0, 0, 0, 0>, <0, 1, 0, 0, 0>, <0, 0, 1, 0, 0>, <0, 0, 0, 1, 0>, <0, 0, 0, 0, 1>>, <1, 1, 1, 0, 0>);
		
	}

}
