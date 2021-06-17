package kr.co.ensmart.frameworkdemo.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonUtils {
	
	public static String string(Object value) {
		try {
			return objectMapper().writeValueAsString(value);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static <T> T object(String json, Class<T> valueType) {
		try {
			return objectMapper().readValue(json, valueType);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static <T> T object(String json, TypeReference<T> valueType) {
		try {
			return objectMapper().readValue(json, valueType);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static ObjectMapper objectMapper() {
		return Instance.objectMapper;
	}
	
	@Component
	static class Instance {
		private static ObjectMapper objectMapper = new ObjectMapper();
		
		@Autowired
		void init(ObjectMapper objectMapper) {
			Instance.objectMapper = objectMapper;
		}
	}
}
