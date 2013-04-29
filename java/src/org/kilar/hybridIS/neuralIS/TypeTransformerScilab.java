package org.kilar.hybridIS.neuralIS;

import java.util.Arrays;

import org.kilar.hybridIS.general.ScilabAdapter;
import org.scilab.modules.types.ScilabDouble;
import org.scilab.modules.types.ScilabInteger;
import org.scilab.modules.types.ScilabMList;
import org.scilab.modules.types.ScilabString;

public class TypeTransformerScilab {
	static public ScilabDouble getDouble(double[] real){
		double [][] t = new double[1][];
		t[0] = real;
		ScilabDouble ret = new ScilabDouble(null, t, new double[0][], true);
		return ret;
	}
	
	static public ScilabInteger getInt(int[] layers){
		int [][] t = new int[1][];
		t[0] = layers;
		ScilabInteger two = new ScilabInteger(t, true);
		return two;
	}
	
	static public ScilabMList gеtWeights(double[] weigths, int[] layers){
		ScilabMList ret = new ScilabMList();
		ScilabString one = new ScilabString(null, new String[][] {{"hm","dims","entries"}}, true);
		ret.add(one);
		ret.add(getInt(layers));
		ret.add(getDouble(weigths));
		return ret;
		//TODO
	}
	
	static public void main(String[] argc){
		//gеtWeights(null, null);
	}

}
