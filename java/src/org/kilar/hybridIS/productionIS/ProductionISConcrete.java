package org.kilar.hybridIS.productionIS;

import java.util.List;

import org.kilar.hybridIS.abstractions.ModuleConfig;
import org.kilar.hybridIS.abstractions.ProductionIS;
import org.kilar.hybridIS.general.Logger;
import org.kilar.hybridIS.general.ProjectConfig;
import org.kilar.hybridIS.general.Util;

public class ProductionISConcrete extends ProductionIS {

	public ProductionISConcrete(ModuleConfig config) {
		super(config);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Double> calculate(List<Double> input) {
		ProjectConfig con = getParent().getConfig();
		ProdCodeParser parser = new ProdCodeParser(getCode(), con.getInNames(), con.getOutNames());
		try {
			return parser.calculate(input);
		} catch (Exception e) {
			Logger.error("Ошибка в модуле " +  getName());
			return Util.getZeroList(getConfig().getOutputLength());
		}
	}

}
