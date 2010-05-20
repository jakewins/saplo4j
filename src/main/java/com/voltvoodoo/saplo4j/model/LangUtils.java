package com.voltvoodoo.saplo4j.model;

import com.voltvoodoo.saplo4j.exception.SaploGeneralException;
import com.voltvoodoo.saplo4j.model.Language;

public class LangUtils {
	public static String lang2String(Language lang) throws SaploGeneralException {
		switch(lang){
		case SWEDISH:
			return "se";
		case ENGLISH:
			return "en";
		}
		
		throw new SaploGeneralException("Unrecognized language: " + lang + ".");
	}
}
