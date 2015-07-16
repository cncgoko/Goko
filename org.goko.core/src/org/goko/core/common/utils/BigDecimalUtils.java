package org.goko.core.common.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

public class BigDecimalUtils {
	/**
	 * Compute the square root of x to a given scale, x >= 0. Use Newton's
	 * algorithm.
	 * 
	 * @param x
	 *            the value of x
	 * @param scale
	 *            the desired scale of the result
	 * @return the result value
	 */
	public static BigDecimal sqrt(BigDecimal x, int scale) {
		// Check that x >= 0.
		if (x.signum() < 0) {
			throw new IllegalArgumentException("x < 0");
		}

		// n = x*(10^(2*scale))
		BigInteger n = x.movePointRight(scale << 1).toBigInteger();

		// The first approximation is the upper half of n.
		int bits = (n.bitLength() + 1) >> 1;
		BigInteger ix = n.shiftRight(bits);
		BigInteger ixPrev;

		// Loop until the approximations converge
		// (two successive approximations are equal after rounding).
		do {
			ixPrev = ix;

			// x = (x + n/x)/2
			ix = ix.add(n.divide(ix)).shiftRight(1);

			Thread.yield();
		} while (ix.compareTo(ixPrev) != 0);

		return new BigDecimal(ix, scale);
	}
	
	public static String toString(BigDecimal value){
		DecimalFormat df = (DecimalFormat)NumberFormat.getNumberInstance();
		df.setGroupingUsed(false);
		return df.format(value);
	}
	
	public static BigDecimal parse(String value) throws GkException{
		try {
			DecimalFormat df = (DecimalFormat)NumberFormat.getNumberInstance();
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
		
		String decimalSepStr = ""+sym.getDecimalSeparator();		
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