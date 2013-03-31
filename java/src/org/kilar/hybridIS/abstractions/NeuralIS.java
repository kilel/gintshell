package org.kilar.hybridIS.abstractions;
import java.util.List;
/**
 * @author trylar
 *
 */
public abstract class NeuralIS extends Module{
	public NeuralIS(String name) {
		super(name);
		type = ModuleType.Neural;
	}
	public abstract void train(List<List<Double>> trainingInput, List<Double> trainingOutput);
}
