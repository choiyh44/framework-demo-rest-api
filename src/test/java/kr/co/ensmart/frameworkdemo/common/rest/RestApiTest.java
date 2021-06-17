package kr.co.ensmart.frameworkdemo.common.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.ensmart.frameworkdemo.common.dto.sample.Sample;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest()
class RestApiTest {

	public static final String localApiServer = "http://localhost:8080";
	
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
