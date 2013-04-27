package org.kilar.hybridIS.productionIS;

import java.util.ArrayList;
import java.util.List;
import org.kilar.hybridIS.general.Logger;
import org.kilar.hybridIS.general.Pair;
import org.kilar.hybridIS.general.Util;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * @author hkyten
 *
 */
public class ProdCodeParser {
	private String[] inputNames, outputNames;
	private String code;
	private int len;
	private List<Double> inValues, outValues;
	private final double INVALID_VALUE = 42;
	private boolean isValidationCheckGoes = false; 

	
	public ProdCodeParser(String code, String[] input, String[] output){
		inputNames = input;
		outputNames = output;
		len = code.length();
		this.code = code;
	}
	
	public boolean isValid(){
		Logger.info("Проверка продукционного модуля");
		List<Double> in = new ArrayList<>();
		isValidationCheckGoes = true;
		int n = inputNames.length;
		for(int i = 0; i < n; ++i)
			in.add(0d);
		try{
			calculate(in);
			Logger.info("Синтаксическая проверка пройдена успешно\n");
			isValidationCheckGoes = false;
			return true;
		} catch (Exception e){
			isValidationCheckGoes = false;
			return false;
		}
	}
	private List<Double> calculate(int start, int end, double scale) throws Exception {
		List<Integer> spl = split(start, end);
		int size = spl.size();
		if(size == 2){ //if only one block in this sector
			int i = spl.get(0);
			//i = ProdCodeUtils.getNextNonSpace(code, i, spl.get(1));
			if(code.charAt(i) == '{'){
				return calcBlock(i, scale);
			} else if(code.substring(i, i + 2).equals("if")){
				return calcIf(i, scale);
			} else {
				return calcStatement(i, scale);
			}	
		}
		//else
		List<Double> temp = new ArrayList<>(),
				out = new ArrayList<>();
		for(int i = 0; i < outputNames.length; ++i)
			out.add(0d);
		
		for(int i = 0; i < size - 1; ++i){
			temp = calculate(spl.get(i), spl.get(i + 1), scale);
			ProdCodeUtils.sumValues(out, temp);
		}
		return out;
	}
	
	
	
	public List<Double> calculate(List<Double> input) throws Exception {
		
		inValues = input;
		for(double d : input){
			if(Math.abs(d) > 1d){
				Logger.error("Значение входного параметра вне [-1, 1] ( "+ d +" )");
				throw new Exception();
			}
		}
		int i = ProdCodeUtils.getNextNonSpace(code, -1, len);
		outValues = calculate(i, len, 1d);
		return outValues; 
	}
	
	private List<Double> calcBlock(int start, double scale) throws Exception {
		int end = ProdCodeUtils.getOtherBracket(code, start);
		return calculate(start+1, end-1, scale);
	}
	
	private List<Double> calcIf(int start, double scale) throws Exception {
		int i = start + 1;
		i = ProdCodeUtils.getNextNonSpace(code, i, len);
		double sc = calcCondition(i);
		i = ProdCodeUtils.getOtherBracket(code, i);
		i = ProdCodeUtils.getNextNonSpace(code, i, len);
		int endIf = getLexemaEnd(i);
		if(isValidationCheckGoes){
			calculate(i, endIf, Math.min(scale, sc));
		} else	if(sc > 0){
			return calculate(i, endIf, Math.min(scale, sc));
		}
		//else
		i++;
		i = ProdCodeUtils.getNextNonSpace(code, i, len);
		if(i + 4 < len && code.subSequence(i, i+4).equals("else")){
			i+=3;
			i = ProdCodeUtils.getNextNonSpace(code, i, len);
			int endElse = getLexemaEnd(i);
			if(isValidationCheckGoes){
				calculate(i, endElse, Math.min(scale, -sc));
			} else {
				return calculate(i, endElse, Math.min(scale, -sc));
			}
		}
		
		return getNewOutVector();
	}
	
	private List<Double> getNewOutVector(){
		return Util.getZeroList(outputNames.length);
	}
	
	private List<Double> calcStatement(int start, double scale) throws Exception {
		List<Double> out = getNewOutVector();
		for(int i = 0; i < outputNames.length; ++i){
			String name = outputNames[i];
			if(code.subSequence(start, start + name.length()).equals(name)){
				start = start + name.length();
				start = ProdCodeUtils.getNextNonSpace(code, start, len);
				// code[start] == '=' now;
				++start;
				int end = start + 1;
				while(code.charAt(end) != ';') ++end;
				double value = Double.parseDouble(code.substring(start, end));
				out.set(i, value*scale);
				return out;
			}
		}
		throw new Exception("Невозможная ошибка в calcStatement");
	}
	
	public List<Integer> split(int start, int end) throws Exception {
		List<Integer> ret = new ArrayList<>();
		// code = prepareCode(code);
		int len = code.length();
		while(start < len && code.charAt(start) < 33)
			++start;
		for (int i = start; i < end; ++i) {
			int next = getLexemaEnd(i);
			if(next == -1){
				Logger.error("Неизвестный оператор, строка " + ProdCodeUtils.getNumStr(code, i));
				Logger.info("Обнаружены ошибки, прекращаю работу");
				throw new Exception();
			}
			while (i < len && next == -1) {
				++i;
				next = getLexemaEnd(i);
			}
			if(next > 0){
				ret.add(i);
				i = next;
			}
			i = ProdCodeUtils.getNextNonSpace(code, i, len);
			--i;
		}
		ret.add(end);
		return ret;
	}
	
	public int getLexemaEnd(int position) {
		
		if (code.charAt(position) == '{') {
			return ProdCodeUtils.getOtherBracket(code, position);
		}
		for (String in : outputNames) {
			if (position + in.length() < len && code.substring(position, position + in.length()).equals(in)) {
				return getEndStatement(position, in);
			}
		}
		if (position + 2 < len && code.substring(position, position + 2).equals("if")) {
			return getEndIf(position);
		}
		return -1;
	}
	
	/**
	 * tries to find end of statement "input = dValue ;" expression
	 * @param position start parse from
	 * @param inputName name of statement
	 * @return ';' index
	 */
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
		int i = position + 1; //code[i] == 'f' of "if"
		i = ProdCodeUtils.getNextNonSpace(code, i, len); //trying to find '('
		if (code.charAt(i) != '(') {
			Logger.error("Некорректная запись оператора if, строка " + ProdCodeUtils.getNumStr(code, position));
			return -1;
		}
		int endCond = ProdCodeUtils.getOtherBracket(code, i); 
		if (endCond < 0) {
			return -1;
		}
		//check conditions
		if(!isValidIfCondition(i)){
			Logger.error("Ошибка в условиях оператора if, строка " + ProdCodeUtils.getNumStr(code, i));
			return -1;
		}
		i = endCond;
		i = ProdCodeUtils.getNextNonSpace(code, i, len);
		i = getLexemaEnd(i);
		if (i < 0) {
			Logger.error("Некорректное тело оператора if, строка " + ProdCodeUtils.getNumStr(code, i));
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
		return inValues.get(getInputValueIndex(in));
	}
	
	private int getInputValueIndex(String in) throws Exception{
		//[TODO] to map?
		for(int i = 0; i < inputNames.length; ++i){
			if(inputNames[i].equals(in)){
				return i;
			}
		}
		Logger.error("Не существует входной переменной с именем \""+ in +"\"");
		throw new Exception();
	}
	/**
	 * @input start -- start parsing index, 
	 * Term = Term || term | Term && Term | (Term) | inputName 
	 * <br>
	 * tip: (TERM && TERM)
	 * <br>&nbsp;&nbsp;&nbsp;/|\
	 * 	@param start -- index in code before term.  
	 *  
	 */
	private Pair<Integer, Double> getTerm(int start) throws Exception{
		start = ProdCodeUtils.getNextNonSpace(code, start, len);
		double res = 0;
		if(code.charAt(start) == '('){
			res = calcCondition(start);
			start = ProdCodeUtils.getOtherBracket(code, start);
		} else {
			int s = start;
			while(code.charAt(start) > 32 && code.charAt(start) != ')' &&  code.charAt(start) != '|' && code.charAt(start) != '&')
				++start;
			res = getInputValue(code.substring(s, start));
		}
		return (new Pair<Integer, Double>(start - 1, res));
	}
	
	private double calcCondition(int start) throws Exception{
		double left = 0;
		int position;
		char operation = 0;
		Pair<Integer, Double> pair = getTerm(start);
		left = pair.second;
		position = pair.first;
		
		while(true){
			position = pair.first;
			position = ProdCodeUtils.getNextNonSpace(code, position, len);
			operation = code.charAt(position);
			if(operation == ')')
				return left;
			
			switch(operation){
			case '|':
				if(code.substring(position, position + 2).equals("||")){
					// operation is valid
				} else {
					Logger.error("Недопустимая условная операция, строка " + ProdCodeUtils.getNumStr(code, position));
					throw new Exception();
				}
				break;
			case '&':
				if(code.substring(position, position + 2).equals("&&")){
					// operation is valid
				} else {
					Logger.error("Недопустимая условная операция, строка " + ProdCodeUtils.getNumStr(code, position));
					throw new Exception();
				}
				break;
			default:
				Logger.error("Недопустимая условная операция, строка " + ProdCodeUtils.getNumStr(code, position));
				throw new Exception();
			}
			position++;
			//position = ProdCodeUtils.getNextNonSpace(code, position, len);
			// now can read next term
			pair = getTerm(position);
			if(operation == '|')
				left = Math.max(left, pair.second);
			else
				left = Math.min(left, pair.second);
		}
	}
	
	private boolean isValidIfCondition(int start){
		try{
			calcCondition(start);
			return true;
		} catch (Exception e){
			Logger.error("Невозможно вычислить условие, строка " + ProdCodeUtils.getNumStr(code, start));
			return false;
		}
	}
	
	@Override
	public String toString(){
		String out = "\n";
		for(int i = 0; i < outputNames.length; ++i){
			out += outputNames[i] + " : " + outValues.get(i) + "\n";
		}
		return out;
	
	}
		
	
}
