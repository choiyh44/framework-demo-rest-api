package kr.co.ensmart.frameworkdemo.common.token;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

public class TokenFilter implements Filter
{
	final byte[] body = "{\"code\":\"9999\", \"message\":\"invalid token\"}".getBytes();
	final Set<String> ignorePathPrefixSet = new HashSet<>(Arrays.asList("/health/check", "/health/prometheus"));
	
	private TokenService tokenService = new TokenService();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
    	String path = ((HttpServletRequest) servletRequest).getRequestURI();
    	if ( isIgnoringPath(path) ) {
    		filterChain.doFilter(servletRequest, servletResponse);
    	} else {
    		String token = TokenUtils.resolveToken((HttpServletRequest) servletRequest);
    		
    		if ( StringUtils.hasText(token) && tokenService.verifyToken(token) ) {
    			filterChain.doFilter(servletRequest, servletResponse);
    		} else {
    			HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
    			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
    			httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    			httpServletResponse.getOutputStream().write(body);
    			httpServletResponse.getOutputStream().flush();
    		}
    	}
    }
    
    private boolean isIgnoringPath(String path) {
    	if( StringUtils.hasText(path) ) {
    		for (String prefix : ignorePathPrefixSet) {
				if ( path.startsWith(prefix) ) {
					return true;
				}
			}
    	}
    	return false;
    }

    public static FilterRegistrationBean<Filter> tokenFilterRegistration() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new TokenFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName(TokenFilter.class.getSimpleName());
        filterRegistrationBean.setOrder(1);

        return filterRegistrationBean;
    }
    
}
