package kr.co.ensmart.frameworkdemo.common.token;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public abstract class TokenUtils {
	
	public static String resolveToken(HttpServletRequest request) {
		final String prefix = "Bearer ";
		String authorization = request.getHeader("Authorization");
		if ( StringUtils.hasText(authorization) && authorization.startsWith(prefix) ) {
			return authorization.substring(prefix.length());
		}
		return null;
	}
	
}
