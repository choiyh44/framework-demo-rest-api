package kr.co.ensmart.frameworkdemo.common.rest;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import kr.co.ensmart.frameworkdemo.common.token.TokenService;
import kr.co.ensmart.frameworkdemo.common.util.JsonUtils;

public abstract class RestApi {
	private static final Logger logger = LoggerFactory.getLogger(RestApi.class);
	private static final TokenService TOKEN_SERVICE = new TokenService();

	private HttpHeaders requestHeaders;
	private UriComponentsBuilder uriComponentsBuilder;
	private Map<String, Object> uriVariables;

	private long latencyTimes = -1;
	
	private boolean enableTokenAuth = false;

	public static RestApi client(String url) {
		return client(url, true);
	}
	
	public static RestApi client(String url, boolean enableTokenAuth) {
		RestApi restApi = new RestApi() {
		};
		restApi.uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
		restApi.enableTokenAuth = enableTokenAuth;
		restApi.requestHeaders = new HttpHeaders();
		restApi.requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		restApi.requestHeaders.setContentType(MediaType.APPLICATION_JSON);

		return restApi;
	}

	public RestApi addHeader(String key, String value) {
		if (Objects.isNull(requestHeaders)) {
			requestHeaders = new HttpHeaders();
		}
		requestHeaders.add(key, value);
		return this;
	}

	public RestApi setHeader(String key, String value) {
		if (Objects.isNull(requestHeaders)) {
			requestHeaders = new HttpHeaders();
		}
		requestHeaders.set(key, value);
		return this;
	}

	public RestApi queryParam(String name, Object... values) {
		this.uriComponentsBuilder.queryParam(name, values);
		return this;
	}

	public RestApi uriVariable(String name, Object value) {
		if (Objects.isNull(uriVariables)) {
			this.uriVariables = new HashMap<>();
		}
		this.uriVariables.put(name, value);
		return this;
	}

	public MultiValueMap<String, String> getHeaders() {
		return requestHeaders;
	}

	public long getLatencyTimes() {
		return latencyTimes;
	}

	public <T> RestResponse<T> get(Class<T> type) {
		return execute(null, HttpMethod.GET, type);
	}

	public <T> RestResponse<T> get(Type type) {
		return execute(null, HttpMethod.GET, type);
	}
	
	public <T> RestResponse<T> get(ParameterizedTypeReference<T> responseReference) {
		return execute(null, HttpMethod.GET, responseReference.getType());
	}
	
	public <T> RestResponse<T> post(Object request, Class<T> type) {
		return execute(request, HttpMethod.POST, type);
	}
	
	public <T> RestResponse<T> post(Object request, Type type) {
		return execute(request, HttpMethod.POST, type);
	}
	
	public <T> RestResponse<T> post(Object request, ParameterizedTypeReference<T> responseReference) {
		return execute(request, HttpMethod.POST, responseReference.getType());
	}
	
	private RestTemplate restTemplate() {
		return Instance.get();
	}
	
	private void configRequestHeader(HttpHeaders httpHeaders) {
		configAuthorization(httpHeaders);
		Instance.configRequestHeader(httpHeaders);
	}
	
	private void configAuthorization(HttpHeaders httpHeaders) {
		if ( this.enableTokenAuth ) {
			httpHeaders.setBearerAuth(TOKEN_SERVICE.generateToken());
		}
	}

	private <T> RestResponse<T> execute(Object request, HttpMethod method, Type type) {
		if (Objects.nonNull(uriVariables)) {
			this.uriComponentsBuilder.uriVariables(uriVariables);
		}

		URI uri = this.uriComponentsBuilder.build().toUri();

		configRequestHeader(requestHeaders);
		
		RequestEntity<?> requestEntity = new RequestEntity<>(request, requestHeaders, method, uri);

		RestRequestCallback requestCallback = restRequestCallback(restTemplate().httpEntityCallback(requestEntity, type));

		RestResponseExtractor<T> responseExtractor = restResponseExtractor(restTemplate().responseEntityExtractor(type));

		ResponseEntity<T> responseEntity = null;
		Exception exception = null;
		long start = System.currentTimeMillis();
		try {

			responseEntity = restTemplate().execute(requestEntity.getUrl(), method, requestCallback, responseExtractor);

			return new RestResponse<>(responseEntity);
		} catch (RestClientResponseException e) {
			exception = e;
			return new RestResponse<>(e);
		} catch (Exception e) {
			exception = e;
			return new RestResponse<>(e);
		} finally {
			this.latencyTimes = System.currentTimeMillis() - start;

			logging(requestEntity, requestCallback, responseEntity, responseExtractor, exception);
		}
	}

	private RestRequestCallback restRequestCallback(RequestCallback callback) {
		return new RestRequestCallback(callback);
	}

	private <T> RestResponseExtractor<T> restResponseExtractor(ResponseExtractor<ResponseEntity<T>> extractor) {
		return new RestResponseExtractor<>(extractor);
	}

	private <T> void logging(RequestEntity<?> requestEntity, RestRequestCallback requestCallback,
			ResponseEntity<T> responseEntity, RestResponseExtractor<T> responseExtractor, Exception e) {
		if (Objects.isNull(e)) {
			logging(requestEntity, requestCallback, responseEntity, responseExtractor);
		} else if (e instanceof RestClientResponseException) {
			RestClientResponseException re = (RestClientResponseException) e;
			logging(requestEntity.getUrl(), requestEntity.getMethod(), requestEntity.getHeaders(),
					requestCallback.getBodyAsString(), re.getRawStatusCode(), re.getStatusText(),
					re.getResponseHeaders(), re.getResponseBodyAsString(), e);
		} else {
			logging(requestEntity.getUrl(), requestEntity.getMethod(), requestEntity.getHeaders(),
					requestCallback.getBodyAsString(), -1, null, null, null, e);
		}
	}

	private <T> void logging(RequestEntity<?> requestEntity, RestRequestCallback requestCallback,
			ResponseEntity<T> responseEntity, RestResponseExtractor<T> responseExtractor) {
		logging(requestEntity.getUrl(), requestEntity.getMethod(), requestEntity.getHeaders(),
				requestCallback.getBodyAsString(), responseEntity.getStatusCodeValue(),
				responseEntity.getStatusCode().getReasonPhrase(), responseEntity.getHeaders(),
				responseExtractor.getBodyAsString(), null);
	}

	private void logging(URI url, HttpMethod method, HttpHeaders reqHeaders, String reqBody, int statusCode,
						 String statusText, HttpHeaders resHeaders, String resBody, Exception e) {
		StringBuilder sb = new StringBuilder().append('\n');
		sb.append("##################################################################").append('\n');
		sb.append("# [REST_API] latency-time: ").append(this.latencyTimes).append(" ms\n");
		sb.append("##[Request]#######################################################").append('\n');
		sb.append("# URL    : ").append(url).append('\n');
		sb.append("# Method : ").append(method).append('\n');
		sb.append("# Headers: ").append(reqHeaders).append('\n');
		sb.append("# Body   : ").append(reqBody).append('\n');
		sb.append("##[Response]######################################################").append('\n');
		sb.append("# Code   : ").append(statusCode).append(' ').append(statusText).append('\n');
		sb.append("# Headers: ").append(resHeaders).append('\n');
		sb.append("# Body   : ").append(resBody).append('\n');
		sb.append("##################################################################");

		if (Objects.isNull(e)) {
			if (logger.isDebugEnabled()) {
				logger.debug(sb.toString());
			}
		} else {
			sb.append('\n').append("# Exception : ");
			logger.error(sb.toString(), e);
		}
	}

	@Component
	static class Instance {
		private static RestTemplate restTemplate;
		static void set(RestTemplate restTemplate) {
			Instance.restTemplate = restTemplate;
		}
		static RestTemplate get() {
			return Instance.restTemplate;
		}

		private static ClientInfoResolver clientInfoResolver;
		static void configRequestHeader(HttpHeaders httpHeaders) {
			if ( Objects.nonNull(Instance.clientInfoResolver) ) {
				ClientInfo clientInfo = Instance.clientInfoResolver.resolve();
				if ( Objects.nonNull(clientInfo) ) {
					httpHeaders.set(ClientInfo.CLIENT_INFO_HEADER_NAME, JsonUtils.string(clientInfo));
				}
			}
		}
		
		@Autowired
		public void init(RestTemplate restTemplate) {
			Instance.restTemplate = restTemplate;
		}
		
		@Autowired(required = false)
		public void init(ClientInfoResolver clientInfoResolver) {
			Instance.clientInfoResolver = clientInfoResolver;
		}
	}
}
