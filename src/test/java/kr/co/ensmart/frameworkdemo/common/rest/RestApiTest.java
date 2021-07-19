package kr.co.ensmart.frameworkdemo.common.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.DispatcherServlet;

import kr.co.ensmart.frameworkdemo.common.dto.sample.Sample;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest()
class RestApiTest {

	public static final String localApiServer = "http://localhost:8081";
	
    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    protected Environment environment;
    
    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;
    protected MockHttpSession httpSession;
    
    @BeforeEach
    public void setUp(){
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        httpSession = new MockHttpSession();
        
        request.setSession(httpSession);
        request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
        
        //RequestContextThreadLocal.setRequestContext(new RequestContext(request));
        ApplicationContextWrapper.setApplicationContext(applicationContext);
    }

	@Test
	void get() {
		RestResponse<Sample> response = RestApi.client(localApiServer+ "/api/samples/{id}")
				.uriVariable("id", "2")
				.get(Sample.class);
		
		assertEquals(false, response.hasError());
		assertEquals(false, response.hasResponseError());
		assertEquals(false, response.hasUnkownError());
		
		Sample result = response.getBody();
		
//		assertEquals(SampleRestDto.SUCCESS_CODE, demo.getReturnCode());
		
		log.debug("ID: {}", result.getId());
	}

}
