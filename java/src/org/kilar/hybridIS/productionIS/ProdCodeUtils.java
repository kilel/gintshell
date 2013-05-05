package org.kilar.hybridIS.productionIS;

import java.util.List;

import org.kilar.hybridIS.general.Logger;

public class ProdCodeUtils {
	
	public static String prepareCode(String code) {
		code = code.replace("\t", " ");
		int len = 0;
		while (len != code.length()) {
			len = code.length();
			code = code.replaceAll(" ", "");
		}

		return code;
	}
	
	/**
	 * Adds b[i] to a[i] in way: a[i] = a[i] + b[i] - a[i]*b[i];
	 */
	public static  List<Double> sumValues(List<Double> a, List<Double> b){
		
		int n = a.size();
		for(int i = 0; i < n; ++i){
			double ta = a.get(i), tb = b.get(i);
			if(ta * tb < 0){
				a.set(i,ta + tb + ta*tb);
			} else if(ta + tb > 0)
				a.set(i,ta + tb - Math.abs(ta*tb));
			else 
				a.set(i,ta + tb + Math.abs(ta*tb));
		}
		return a;
	}

	public static int getOtherBracket(String code, int position) {
		int depth = 0, direction = 1;
		char otherBracket = 0, thisBracket = code.charAt(position);
		switch (thisBracket) {
		case '(':
			direction = 1;
			otherBracket = ')';
			break;
		case '{':
			direction = 1;
			otherBracket = '}';
			break;
		case ')':
			direction = -1;
			otherBracket = '(';
			break;
		case '}':
			direction = -1;
			otherBracket = '{';
			break;
		}
		int len = code.length();
		for (int i = position; i >= 0 && i < len; i += direction) {
			if (code.charAt(i) == otherBracket)
				--depth;
			else if (code.charAt(i) == thisBracket)
				++depth;
			if (depth == 0)
				return i;
		}

		Logger.error("Неверная скобочная последовательность, строка " + getNumStr(code, position));
		return -1;
	}
	
	public static int getNextNonSpace(String code, int pos, int len){
		if(pos < 0){
			pos = 0;
		} else if(code.charAt(pos) > 32){
			++pos;
		}
		while(pos < len && code.charAt(pos) < 33){
			++pos;
		}
		return pos;
	}
	
	public static int getNumStr(String code, int position) {
		int numStr = 0;
		for (int i = 0; i < position; ++i)
			if (code.charAt(i) == '\n')
				++numStr;
		return numStr;
	}
}
