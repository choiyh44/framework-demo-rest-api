package kr.co.ensmart.frameworkdemo.common.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.input.TeeInputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

class RestResponseExtractor<T> implements ResponseExtractor<ResponseEntity<T>> {
	private final ResponseExtractor<ResponseEntity<T>> extractor;
	private final ByteArrayOutputStream bodyBranch;

	public RestResponseExtractor(ResponseExtractor<ResponseEntity<T>> extractor) {
		this.extractor = extractor;
		this.bodyBranch = new ByteArrayOutputStream();
	}

	byte[] getBodyAsBytes() {
		return bodyBranch.toByteArray();
	}

	String getBodyAsString() {
		return getBodyAsString(StandardCharsets.UTF_8);
	}

	String getBodyAsString(Charset charset) {
		return new String(getBodyAsBytes(), charset);
	}

	@Override
	public ResponseEntity<T> extractData(ClientHttpResponse response) throws IOException {
		return extractor.extractData(wrap(response));
	}

	private ClientHttpResponse wrap(ClientHttpResponse response) {
		return new ClientHttpResponse() {
			private InputStream bodyStream;

			private InputStream bodyStream() throws IOException {
				if (this.bodyStream == null) {
					this.bodyStream = new TeeInputStream(response.getBody(), bodyBranch, true);
				}
				return this.bodyStream;
			}

			@Override
			public HttpHeaders getHeaders() {
				return response.getHeaders();
			}

			@Override
			public InputStream getBody() throws IOException {
				return bodyStream();
			}

			@Override
			public String getStatusText() throws IOException {
				return response.getStatusText();
			}

			@Override
			public HttpStatus getStatusCode() throws IOException {
				return response.getStatusCode();
			}

			@Override
			public int getRawStatusCode() throws IOException {
				return response.getRawStatusCode();
			}

			@Override
			public void close() {
				try {
					response.close();
				} finally {
					try {
						bodyBranch.close();
					} catch (Exception e) {
						// to do nothing
					}
				}
			}
		};
	}
}
