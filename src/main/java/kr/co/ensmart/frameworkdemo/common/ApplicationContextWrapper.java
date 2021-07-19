package kr.co.ensmart.frameworkdemo.common;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ContextLoaderListener;

public final class ApplicationContextWrapper {

	private static final ThreadLocal<ApplicationContext> THREAD_LOCAL_APPLICATION_CONTEXT = new ThreadLocal<ApplicationContext>() {
        ApplicationContext applicationContext;
        @Override
        protected ApplicationContext initialValue() {
            return null;
        };
        @Override
        public void set(ApplicationContext value) {
            applicationContext = value;
        };
        @Override
        public ApplicationContext get() {
            return applicationContext;
        };
        @Override
        public void remove() {
            applicationContext = null;
        };
    };
    
    private ApplicationContextWrapper() {
    	throw new UnsupportedOperationException();
	}

    public static final <T> T getBean(Class<T> clazz) {

        ApplicationContext applicationContext = getCurrentApplicationContext();
        return applicationContext.getBean(clazz);
    }

    public static final Object getBean(String beanName) {

        ApplicationContext applicationContext = getCurrentApplicationContext();
        return applicationContext.getBean(beanName);
    }

    public static final Resource getResource(String resource) {
        ApplicationContext applicationContext = getCurrentApplicationContext();

        return applicationContext.getResource(resource);
    }

    public static final ApplicationContext getApplicationContext() {
        return getCurrentApplicationContext();
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        THREAD_LOCAL_APPLICATION_CONTEXT.set(applicationContext);
    }

    private static ApplicationContext getCurrentApplicationContext() {
        ApplicationContext applicationContext = ContextLoaderListener.getCurrentWebApplicationContext();
        applicationContext = applicationContext == null ? THREAD_LOCAL_APPLICATION_CONTEXT.get() : applicationContext;

        return applicationContext;
    }
}
