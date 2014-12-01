/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.core.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class GkUtils {

	public static List<Byte> toBytesList(String str){
		List<Byte> lst = new ArrayList<Byte>();
		for(byte b : str.getBytes()){
			lst.add(new Byte(b));
		}
		return lst;
	}


	public static List<Byte> toBytesList(byte[] arr){
		List<Byte> lst = new ArrayList<Byte>();
		if(arr != null){
			for(int i = 0; i < arr.length; i++) {
				lst.add(new Byte(arr[i]));
			}
		}
		return lst;
	}

	public static List<Byte> addBytesArray(List<Byte> lst, byte[] arr){
		if(arr != null){
			for(int i = 0; i < arr.length; i++) {
				lst.add(new Byte(arr[i]));
			}
		}
		return lst;
	}

	public static String toString(byte[] data){
		StringBuffer 	buffer = new StringBuffer(data.length);

		for(byte b : data){
				buffer.append((char)b);
		}
		return buffer.toString();
	}
	public static String toString(List<Byte> data){
		StringBuffer 	buffer = new StringBuffer(data.size());
		byte[] 			byteArray = ArrayUtils.toPrimitive(data.toArray(new Byte[]{}));

		for(byte b : byteArray){
				buffer.append((char)b);
		}
		return buffer.toString();
	}

	public static String toStringReplaceCRLF(List<Byte> data){
		String str = GkUtils.toString(data);
		str = StringUtils.replace(str, ""+'\n', "\\n");
		str = StringUtils.replace(str, ""+'\r', "\\r");
		return str;
	}
	public static String toStringReplaceCRLF(byte[] data){
		String str = GkUtils.toString(data);
		str =  StringUtils.replace(str, ""+'\n', "\\n");
		str =  StringUtils.replace(str, ""+'\r', "\\r");
		return str;
	}
}
