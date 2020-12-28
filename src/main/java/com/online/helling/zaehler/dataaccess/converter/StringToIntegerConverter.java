package com.online.helling.zaehler.dataaccess.converter;

import javax.persistence.AttributeConverter;

public class StringToIntegerConverter implements AttributeConverter<Integer, String> {

	@Override
	public Integer convertToEntityAttribute(String attribute) {
		return attribute == null ? null : Integer.valueOf(attribute);
	}

	@Override
	public String convertToDatabaseColumn(Integer dbData) {
		return dbData == null ? null : String.valueOf(dbData);
	}

}
