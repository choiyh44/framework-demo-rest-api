package kr.co.ensmart.frameworkdemo.common;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;
    
	@Override
	public void afterPropertiesSet() throws Exception {
		ApplicationContextWrapper.setApplicationContext(applicationContext);
	}

}

