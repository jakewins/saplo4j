package com.voltvoodoo.saplo4j.utils;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonUtils {

	/**
	 * Used to quickly generate JSONArray's used as parameter containers.
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray jsonParams(Object ... params) {
		JSONArray out = new JSONArray();
		JSONObject current;
		
		for(Object param : params) {
			if(param instanceof ArrayList) { // Hack, Saplo API handles nested lists oddly, this abstracts that away.
				current = new JSONObject();
				current.put("javaClass","java.util.ArrayList");
				current.put("list", param);
				out.add(current);
			} else {
				out.add(param);
			}
		}
		
		return out;
	}
	
}
