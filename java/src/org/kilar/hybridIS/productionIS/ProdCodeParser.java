package org.kilar.hybridIS.productionIS;

import java.util.ArrayList;
import java.util.List;
import org.kilar.hybridIS.general.Logger;

/**
 * @author hkyten
 *
 */
public class ProdCodeParser {
	private List<String> inputNames, outputNames;
	private String code;
	private int len;
	
	public ProdCodeParser(String code, List<String> input, List<String> output){
		inputNames = input.subList(0, input.size());
		outputNames = output.subList(0, output.size());
		len = code.length();
		this.code = code;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> split() {
		List<String> ret = new ArrayList<>();
		// code = prepareCode(code);
		int len = code.length();
		for (int i = 0; i < len; ++i) {
			int next = getLexemaEnd(i);
			if(next == -1){
				Logger.error("Неизвестный оператор, строка "	+ ProdCodeUtils.getNumStr(code, i));
				Logger.info("Обнаружены ошибки, прекращаю работу");
				return null;
			}
			while (i < len && next == -1) {
				++i;
				next = getLexemaEnd(i);
			}
			if(next > 0){
				ret.add(code.substring(i, next+1));
				i = next;
			}
			i = ProdCodeUtils.getNextNonSpace(code, i, len);
			--i;
		}
		return ret;
	}
	
	public int getLexemaEnd(int position) {
		
		if (code.charAt(position) == '{') {
			return ProdCodeUtils.getOtherBracket(code, position);
		}
		for (String in : inputNames) {
			if (position + in.length() < len && code.substring(position, position + in.length()).equals(in)) {
				return getEndStatement(position, in);
			}
		}
		if (position + 2 < len && code.substring(position, position + 2).equals("if")) {
			return getEndIf(position);
		}
		return -1;
	}
	
	private int getEndStatement(int position, String inputName){
		String in = inputName;
		int len = code.length();
		int i = position + in.length() -1 ;
		i = ProdCodeUtils.getNextNonSpace(code, i, len);
		if (code.charAt(i) != '=') {
			Logger.error("Некорректная запись оператора уверенности, строка " + ProdCodeUtils.getNumStr(code, position));
			return -1;
		}
		++i;
		int j = i + 1;
		while (j < len && code.charAt(j) != ';')
			++j;
		try {
			double z = Double.parseDouble(code.substring(i, j));
			if (Math.abs(z) > 1)
				throw new Exception();
		} catch (Exception e) {
			Logger.error("Некорректная запись числа уверенности, строка "	+ ProdCodeUtils.getNumStr(code, i));
			return -1;
		}
		return j;
	}
	private int getEndIf(int position){
		int len = code.length();
		int i = position + 1;
		i = ProdCodeUtils.getNextNonSpace(code, i, len);
		if (code.charAt(i) != '(') {
			Logger.error("Некорректная запись оператора if, строка " + ProdCodeUtils.getNumStr(code, position));
			return -1;
		}
		i = ProdCodeUtils.getOtherBracket(code, i);
		if (i < 0) {//[TODO]
			return -1;
		}
		i = ProdCodeUtils.getNextNonSpace(code, i, len);
		i = getLexemaEnd(i);
		if (i < 0) {
			Logger.error("Некорректное тело оператора if, строка " + ProdCodeUtils.getNumStr(code, position));
			return -1;
		}
		int endIf = i;
		i = ProdCodeUtils.getNextNonSpace(code, i, len);
		if (i + 4 < len && code.substring(i, i + 4).equals("else")) {
			i = i + 3;
			i = ProdCodeUtils.getNextNonSpace(code, i, len);
			int elseStart = i;
			i = getLexemaEnd(i);
			if (i < 0) {
				Logger.error("Некорректное тело оператора else, строка " + ProdCodeUtils.getNumStr(code, elseStart));
				return -1;
			}
			return i;
		} else {
			return endIf;
		}
	}
	
	private double getInputValue(String in) throws Exception{
		//[TODO]
		return 0.5;
	}
	
	private double calcCondition(int start) throws Exception{
		double ret = 0, left = 0, right = 0;
		int end = ProdCodeUtils.getOtherBracket(code, start), position = ProdCodeUtils.getNextNonSpace(code, start, len);
		char operation = 0;
		if(code.charAt(position) == '('){
			left = calcCondition(position);
			position = ProdCodeUtils.getOtherBracket(code, position);
			position = ProdCodeUtils.getNextNonSpace(code, position, len);
		} else {
			int s = position;
			while(code.charAt(position) > 32 && code.charAt(position) != ')' &&  code.charAt(position) != '|' && code.charAt(position) != '&')
				++position;
			position = ProdCodeUtils.getNextNonSpace(code, position, len);
			operation = code.charAt(position);
			switch(operation){
			case ')':
				left = getInputValue(code.substring(s, position));
				return left;
			case '|':
				if(code.substring(position, position + 2).equals("||")){
					//[TODO]
				} else {
					throw new Exception();
				}
				break;
			case '&':
				if(code.substring(position, position + 2).equals("&&")){
					//[TODO]
				} else {
					throw new Exception();
				}
				break;
			}
		}
		
		
		return ret;
	}
	
	private boolean checkIfCondition(int start){
		try{
			calcCondition(start);
			return true;
		} catch (Exception e){
			Logger.error("Невозможно вычислить условие, строка " + ProdCodeUtils.getNumStr(code, start));
			return false;
		}
		
	}
}
