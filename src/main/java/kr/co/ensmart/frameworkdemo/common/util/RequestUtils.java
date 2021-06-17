package kr.co.ensmart.frameworkdemo.common.util;

import java.util.Enumeration;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public abstract class RequestUtils {
	
	public static String requestQueryString() {
		HttpServletRequest request = request();
		return Objects.nonNull(request) ? request.getQueryString() : null;
	}
	
	public static HttpHeaders requestHeaders() {
		HttpServletRequest request = request();
		if ( Objects.isNull(request) ) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		Enumeration<String> names = request.getHeaderNames();
		while(names.hasMoreElements()) {
			String name = names.nextElement();
			if ( StringUtils.hasText(name) ) {
				Enumeration<String> values = request.getHeaders(name);
				while(values.hasMoreElements()) {
					String value = values.nextElement();
					headers.add(name, value);
				}
			}
		}
		
		return HttpHeaders.readOnlyHttpHeaders(headers);
	}
	
	public static HttpServletRequest request() {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if ( attributes instanceof ServletRequestAttributes ) {
			return ((ServletRequestAttributes)attributes).getRequest();
		}
		return null;
	}

}
