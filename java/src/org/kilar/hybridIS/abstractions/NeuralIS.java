package org.kilar.hybridIS.abstractions;
import java.util.List;
/**
 * @author trylar
 *
 */
public abstract class NeuralIS extends Module{
	public NeuralIS(ModuleConfig config) {
		super(config);
	}
	public abstract void train(List<List<Double>> trainingInput, List<Double> trainingOutput);
}
