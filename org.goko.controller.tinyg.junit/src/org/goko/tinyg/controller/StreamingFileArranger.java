/**
 * 
 */
package org.goko.tinyg.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

/**
 * A simple utility class to easily rearrange TinyG input and output
 * @author Psyko
 * @date 5 juil. 2016
 */
public class StreamingFileArranger {
	public static void main(String[] args) {
		File file = new File("C:/Users/Psyko/Documents/tmp1.txt");

	    try {

	        Scanner sc = new Scanner(file);
	        List<String> lstGc = new ArrayList<String>();
	        List<String> lstR  = new ArrayList<String>();
	        
	        while (sc.hasNextLine()) {
	            String line = sc.nextLine();
	            if(StringUtils.startsWith(StringUtils.trim(line), "{\"gc\"")){
	            	lstGc.add(line);
	            }else if(StringUtils.startsWith(StringUtils.trim(line), "{\"r\":{\"gc")){
	            	lstR.add(line);
	            }else{
	            	//throw new IllegalAccessError("Unknown line "+line);
	            }
	        }
	        int imax = Math.max(lstGc.size(), lstR.size());
	        for (int i = 0; i < imax; i++) {
	        	if(lstGc.size() > i){
	        		System.out.println(lstGc.get(i));
	        	}else{
	        		System.out.println("++++++++ NO MORE GC ++++++++");
	        	}
				if(lstR.size() > i){
				//	System.out.println(lstR.get(i));
				}else{
				//	System.out.println("++++++++ NO MORE R ++++++++");
				}
				
			}
	        
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	}
}
