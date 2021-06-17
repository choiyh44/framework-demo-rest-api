package kr.co.ensmart.frameworkdemo.common.rest;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;

public class RestResponse<T> {
	private final ResponseEntity<T> responseEntity;
	private final Exception exception;
	private final RestClientResponseException responseException;

	public RestResponse(ResponseEntity<T> responseEntity) {
		this(responseEntity, null);
	}

	public RestResponse(Exception exception) {
		this(null, exception);
	}

	public RestResponse(RestClientResponseException exception) {
		this(null, exception);
	}

	public RestResponse(ResponseEntity<T> responseEntity, Exception exception) {
		this.responseEntity = responseEntity;
		this.exception = exception;
		this.responseException = null;
	}

	public RestResponse(ResponseEntity<T> responseEntity, RestClientResponseException exception) {
		this.responseEntity = responseEntity;
		this.exception = null;
		this.responseException = exception;
	}

	public MultiValueMap<String, String> getHeaders() {
		return hasResponseError() ? responseException.getResponseHeaders() : responseEntity.getHeaders();
	}

	public int getStatusCode() {
		if (Objects.nonNull(this.responseEntity)) {
			return this.responseEntity.getStatusCodeValue();
		} else if (hasResponseError()) {
			return this.responseException.getRawStatusCode();
		}
		return -1;
	}

	public String getStatusText() {
		if (Objects.nonNull(this.responseEntity)) {
			return this.responseEntity.getStatusCode().getReasonPhrase();
		} else if (hasResponseError()) {
			return this.responseException.getStatusText();
		}
		return null;
	}

	public T getBody() {
		return Objects.nonNull(this.responseEntity) ? this.responseEntity.getBody() : null;
	}

	public String getBodyAsErrorString() {
		return hasResponseError() ? responseException.getResponseBodyAsString() : null;
	}

	public boolean hasError() {
		return hasUnkownError() || hasResponseError();
	}

	public boolean hasUnkownError() {
		return Objects.nonNull(exception);
	}

	public boolean hasResponseError() {
		return Objects.nonNull(responseException);
	}

	public Exception getException() {
		return Objects.nonNull(this.exception) ? this.exception : this.responseException;
	}

}
