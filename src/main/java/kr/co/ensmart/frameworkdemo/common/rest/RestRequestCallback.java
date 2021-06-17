package kr.co.ensmart.frameworkdemo.common.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.output.TeeOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;

class RestRequestCallback implements RequestCallback {
	private final RequestCallback callback;
	private final ByteArrayOutputStream requestBranch;

	RestRequestCallback(RequestCallback callback) {
		this.callback = callback;
		this.requestBranch = new ByteArrayOutputStream();
	}

	byte[] getBodyAsBytes() {
		return requestBranch.toByteArray();
	}

	String getBodyAsString() {
		return getBodyAsString(StandardCharsets.UTF_8);
	}

	String getBodyAsString(Charset charset) {
		return new String(getBodyAsBytes(), charset);
	}

	@Override
	public void doWithRequest(ClientHttpRequest request) throws IOException {
		callback.doWithRequest(wrap(request));
	}

	private ClientHttpRequest wrap(ClientHttpRequest request) {
		return new ClientHttpRequest() {
			private OutputStream bodyStream;

			private OutputStream bodyStream() throws IOException {
				if (this.bodyStream == null) {
					this.bodyStream = new TeeOutputStream(request.getBody(), requestBranch);
				}
				return this.bodyStream;
			}

			@Override
			public OutputStream getBody() throws IOException {
				return bodyStream();
			}

			@Override
			public HttpHeaders getHeaders() {
				return request.getHeaders();
			}

			@Override
			public URI getURI() {
				return request.getURI();
			}

			@Override
			public String getMethodValue() {
				return request.getMethodValue();
			}

			@Override
			public ClientHttpResponse execute() throws IOException {
				return request.execute();
			}
		};
	}
}
