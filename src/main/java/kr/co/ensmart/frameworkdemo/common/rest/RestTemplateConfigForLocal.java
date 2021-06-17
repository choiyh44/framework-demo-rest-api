package kr.co.ensmart.frameworkdemo.common.rest;

import java.time.Duration;
import java.util.function.Supplier;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import kr.co.ensmart.frameworkdemo.common.util.SkipSSL;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("local")
@Configuration
public class RestTemplateConfigForLocal {
	@Autowired
	private RestTemplateProperties properties;

	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
		log.info("[COMMON] creating Local RestTemplate");
        return builder
                .setConnectTimeout(Duration.ofSeconds(properties.getConnectTimeout()))
                .setReadTimeout(Duration.ofSeconds(properties.getReadTimeout()))
                .requestFactory(createClientHttpRequestFactory())
                .build();
    }
	
	@Component
	@ConfigurationProperties(prefix="hmg.rest-template")
	@Setter
	@Getter
	@ToString
	static public class RestTemplateProperties {
		private long connectTimeout = 5; // default 5 seconds
		private long readTimeout = 30;   // default 30 seconds
	}
	
	private Supplier<ClientHttpRequestFactory> createClientHttpRequestFactory() {
		try {
			CloseableHttpClient httpClient = HttpClients.custom()
					.setSSLContext(SkipSSL.crateSSLContext())
					.setSSLHostnameVerifier(SkipSSL.createHostnameVerifier())
					.build();

			return () -> new HttpComponentsClientHttpRequestFactory(httpClient);
		} catch (Exception e) {
			log.warn("[COMMON] Always Skip TLS failed", e);
			return () -> new HttpComponentsClientHttpRequestFactory();
		}
	}

}
