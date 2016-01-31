package org.goko.core.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

public class BigDecimalUtils {
		
	private static final BigDecimal SQRT_DIG = new BigDecimal(10);
	private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());
	/**
	 * Uses Newton Raphson to compute the square root of a BigDecimal.
	 * 
	 * @author Luciano Culacciatti 
	 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
	 */
	
	private static BigDecimal sqrtNewtonRaphson  (BigDecimal c, BigDecimal xn, BigDecimal precision){
	    BigDecimal fx = xn.pow(2).add(c.negate());
	    BigDecimal fpx = xn.multiply(new BigDecimal(2));
	    BigDecimal xn1 = fx.divide(fpx,2*SQRT_DIG.intValue(),RoundingMode.HALF_DOWN);
	    xn1 = xn.add(xn1.negate());
	    BigDecimal currentSquare = xn1.pow(2);
	    BigDecimal currentPrecision = currentSquare.subtract(c);
	    currentPrecision = currentPrecision.abs();
	    if (currentPrecision.compareTo(precision) <= -1){
	        return xn1;
	    }
	    return sqrtNewtonRaphson(c, xn1, precision);
	}

	
	public static BigDecimal sqrt(BigDecimal c, int scale){
		double d = Math.sqrt(c.doubleValue());
	 //   return sqrtNewtonRaphson(c,new BigDecimal(1),new BigDecimal(1).divide(SQRT_PRE));
		 return new BigDecimal( Math.sqrt(c.doubleValue())).setScale(scale, RoundingMode.HALF_DOWN);
	}
	
	public static String toString(BigDecimal value){
		DecimalFormat df = (DecimalFormat)NumberFormat.getNumberInstance();
		
		DecimalFormatSymbols sym = df.getDecimalFormatSymbols();
		sym.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(sym);
		df.setGroupingUsed(false);
		df.setParseBigDecimal(true);		
		
		return df.format(value);
	}
	
	public static BigDecimal parse(String value) throws GkException{
		try {
			DecimalFormat df = (DecimalFormat)NumberFormat.getNumberInstance();
			DecimalFormatSymbols sym = df.getDecimalFormatSymbols();
			sym.setDecimalSeparator('.');
			df.setDecimalFormatSymbols(sym);
			df.setGroupingUsed(false);
			df.setParseBigDecimal(true);		
			return (BigDecimal) df.parse(value);
		} catch (ParseException e) {
			throw new GkTechnicalException(e);
		}		
	}
	
	public static boolean isBigDecimal(String value){
		DecimalFormat df = (DecimalFormat)NumberFormat.getNumberInstance();
		df.setGroupingUsed(false);
		DecimalFormatSymbols sym = df.getDecimalFormatSymbols();
		
		String decimalSepStr = ".";//""+sym.getDecimalSeparator();		
		if(".".equals(decimalSepStr)){
			decimalSepStr = "\\"+decimalSepStr;
		}
		String integerPart = "-?[0-9]+";
		String decimalPart = "("+decimalSepStr+"[0-9]+)?";
		
		String numberRegex = integerPart+decimalPart;		
		Pattern pattern = Pattern.compile(numberRegex);
		return pattern.matcher(value).matches();
	}
	
//	public static BigDecimal parseUser(String value) throws GkException{
//		try {  
//			DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.US);			
//          	df.setParseBigDecimal(true);
//          	df.setGroupingUsed(false);
//			return (BigDecimal) df.parseObject(value);
//		} catch (ParseException e) {
//			throw new GkTechnicalException(e);
//		}
//	}
//	
//	public static String formatUser(BigDecimal value) throws GkException{
//		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		return df.format(value);
//	}	
//	
//	public static boolean isBigDecimal(String value){
//		//return new BigDecimalValidator().isValid(value, Locale.US);
//		try {  
//			DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.US);			
//          	df.setParseBigDecimal(true);          	
//          	df.setGroupingUsed(false);          	
//			df.parseObject(value);
//			return true;
//		} catch (ParseException e) {
//			return false;
//		}
//	}
}