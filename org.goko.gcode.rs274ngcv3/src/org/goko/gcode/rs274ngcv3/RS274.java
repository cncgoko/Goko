/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.goko.gcode.rs274ngcv3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.gcode.rs274ngcv3.config.RS274Preference;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;

public class RS274 {
	public final static String MOTION_MODE_RAPID = "g0";
	public final static String MOTION_MODE_RAPID_EXTENDED = "g00";
	public final static String MOTION_MODE_CONTROLLED = "g1" ;
	public final static String MOTION_MODE_CONTROLLED_EXTENDED = "g01" ;
	public final static String MOTION_MODE_ARC_CW = "g2" ;
	public final static String MOTION_MODE_ARC_CW_EXTENDED = "g02" ;
	public final static String MOTION_MODE_ARC_CCW = "g3" ;
	public final static String MOTION_MODE_ARC_CCW_EXTENDED = "g03" ;


	public static String getTokenLetter(GCodeToken token){
		return StringUtils.substring(token.getValue(), 0,1);
	}
	public static boolean isUniqueTokenByLetter(String letter, Collection<GCodeToken> lstTokens) throws GkException{
		return findUniqueTokenByLetter(letter, lstTokens) != null;
	}

	public static GCodeToken findUniqueTokenByLetter(String letter, Collection<GCodeToken> lstTokens) throws GkException{
		GCodeToken uniqueToken = null;
		for (GCodeToken token : lstTokens) {
			if(StringUtils.equalsIgnoreCase(letter, getTokenLetter(token))){
				if(uniqueToken != null){
					throw new GkTechnicalException("Multiple GCode word with letter "+letter+" found in token list.");
				}
				uniqueToken = token;
			}
		}
		return uniqueToken;
	}

	public static GCodeToken removeUniqueTokenByLetter(String letter, Collection<GCodeToken> lstTokens) throws GkException{
		GCodeToken uniqueToken = null;
		for (GCodeToken token : lstTokens) {
			if(StringUtils.equalsIgnoreCase(letter, getTokenLetter(token))){
				if(uniqueToken != null){
					throw new GkTechnicalException("Multiple GCode word with letter "+letter+" found in token list.");
				}
				uniqueToken = token;
			}
		}
		if(uniqueToken != null){
			lstTokens.remove(uniqueToken);
		}
		return uniqueToken;
	}
	public static GCodeToken getUniqueTokenByLetter(String letter, Collection<GCodeToken> lstTokens) throws GkException{
		GCodeToken uniqueToken = findUniqueTokenByLetter(letter, lstTokens);
		if(uniqueToken == null){
			throw new GkTechnicalException("No token for letter "+letter+" found in "+lstTokens);
		}
		return uniqueToken;
	}

	public static List<GCodeToken> findTokenByLetter(String letter, Collection<GCodeToken> lstTokens) throws GkException{
		List<GCodeToken> tokens = new ArrayList<GCodeToken>();
		for (GCodeToken token : lstTokens) {
			if(StringUtils.equalsIgnoreCase(getTokenLetter(token), letter)){
				tokens.add(token);
			}
		}
		return tokens;
	}

	public static boolean isToken(String strToken, Collection<GCodeToken> lstTokens)throws GkException{
		return findToken(strToken, lstTokens) != null;
	}

	public static GCodeToken removeToken(String strToken, Collection<GCodeToken> lstTokens)throws GkException{
		GCodeToken token = findToken(strToken, lstTokens);
		if(token != null){
			lstTokens.remove(token);
		}
		return token;
	}
	public static GCodeToken findToken(String strToken, Collection<GCodeToken> lstTokens)throws GkException{
		GCodeToken uniqueToken = null;
		for (GCodeToken token : lstTokens) {
			if(StringUtils.equalsIgnoreCase(strToken,token.getValue())){
				if(uniqueToken != null){
					throw new GkTechnicalException("Multiple GCode word "+strToken+" found in token list.");
				}
				uniqueToken = token;
			}
		}
		return uniqueToken;
	}

	public static String getTokenValue(GCodeToken token) {
		return StringUtils.substring(token.getValue(), 1);
	}

	public static String getTokenValueByLetter(String letter, Collection<GCodeToken> lstTokens) throws GkException {
		return getTokenValue( getUniqueTokenByLetter(letter, lstTokens) );
	}

	public static BigDecimal buildBigDecimal(String value) throws GkException{
		BigDecimal bigDecimal = new BigDecimal(value);
		if(RS274Preference.getInstance().isDecimalTruncateEnabled()){
			int decimalCount = RS274Preference.getInstance().getDecimalCount();
			if(bigDecimal.scale() > decimalCount){
				bigDecimal = bigDecimal.setScale(decimalCount,RoundingMode.DOWN);
			}
		}
		return  bigDecimal;
	}

	public static String toString(List<GCodeToken> lstTokens) {
		StringBuffer buffer = new StringBuffer();
		if(CollectionUtils.isNotEmpty(lstTokens)){
			for (GCodeToken gCodeToken : lstTokens) {
				buffer.append(gCodeToken.getValue());
				buffer.append(" ");
			}
		}
		return buffer.toString();
	}


}
