package org.kilar.hybridIS.productionIS;

import java.util.List;

import org.kilar.hybridIS.abstractions.ModuleConfig;
import org.kilar.hybridIS.abstractions.ProductionIS;
import org.kilar.hybridIS.general.Logger;
import org.kilar.hybridIS.general.ProjectConfig;

public class ProductionISConcrete extends ProductionIS {

	public ProductionISConcrete(ModuleConfig config) {
		super(config);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Double> calculate(List<Double> input) {
		ProjectConfig con = config.getParent().getConfig();
		ProdCodeParser parser = new ProdCodeParser(((ModuleConfigProduction)config).getCode(), con.getInNames(), con.getOutNames());
		try {
			return parser.calculate(input);
		} catch (Exception e) {
			Logger.error("Ошибка в модуле " +  getName());
			return null;
		}
	}

}
