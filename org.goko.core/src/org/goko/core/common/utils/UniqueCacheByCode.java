/**
 * 
 */
package org.goko.core.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;

/**
 * @author PsyKo
 * @date 8 févr. 2016
 */
public class UniqueCacheByCode<T extends ICodeBean> extends CacheByCode<T> {

	/** (inheritDoc)
	 * @see org.goko.core.common.utils.CacheByCode#add(org.goko.core.common.utils.ICodeBean)
	 */
	@Override
	public void add(T element) throws GkException {
		if(exist(element.getCode())){
			element.setCode(generateUniqueCode(element.getCode()));
		}
		super.add(element);
	}

	/**
	 * Generate a unique code using the given code as a base
	 * @param code the current code
	 * @return the newly created code 
	 * @throws GkException GkException 
	 */
	private String generateUniqueCode(String code) throws GkException {
		String generatedCode = StringUtils.defaultString(code);
		Pattern pattern = Pattern.compile("(.*)\\((.*)\\)");
		Matcher matcher = pattern.matcher(generatedCode);
		// We check if we have a number at the end		
		if(!matcher.find()){
			generatedCode += "(1)";
		}
		
		int copy = 1;
		matcher = pattern.matcher(generatedCode);
		while(exist(generatedCode) && copy < 100){
			copy++;
			StringBuffer sb = new StringBuffer();
			matcher.find();			
			matcher.appendReplacement(sb, "$1("+String.valueOf(copy)+")"); 
			matcher.appendTail(sb);
				
			generatedCode = sb.toString();
			matcher = pattern.matcher(generatedCode);
		}		
		return generatedCode;
	}	
}
