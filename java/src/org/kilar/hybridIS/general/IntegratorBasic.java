package org.kilar.hybridIS.general;

import java.util.ArrayList;
import java.util.List;

import org.kilar.hybridIS.abstractions.Integrator;
import org.kilar.hybridIS.abstractions.IntegratorConfig;
import org.kilar.hybridIS.abstractions.Module;

public class IntegratorBasic extends Integrator {

	public IntegratorBasic(IntegratorConfig config) {
		super(config);
	}

	@Override
	public List<Double> calculate(List<List<Double>> input, List<Module> modules) {
		int n = input.size();
		if(n == 0)
			return new ArrayList<>();
		int m = modules.get(0).getConfig().getOutputLength();
		List<Double> res = new ArrayList<>();
		double s = 0;
		for(int j = 0; j < m; ++j){
			s = 0;
			for(int i = 0; i < n; ++i){
				s += input.get(i).get(j);
			}
			res.add(s/n);
		}
		
		return res;
	}

}
