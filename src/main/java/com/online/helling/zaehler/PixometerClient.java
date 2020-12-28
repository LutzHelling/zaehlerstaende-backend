package com.online.helling.zaehler;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class PixometerClient extends RestTemplate implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String TOKEN = "5d3a3974081cbc4ede33ff389e8f402d2ce9e28f";
	private static final String ENDPOINT_PREFIX = "https://pixometer.io/api/v1/";

	public PixometerClient() {
		addAuthentication();
	}

	public String getReadings(int medium, Date lastAblesung) {
		String endpoint = ENDPOINT_PREFIX + "readings/";
		if (lastAblesung != null) {
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTimeInMillis(lastAblesung.getTime());
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String filter = "?physical_medium=" + getMediumExpression(medium) + "&reading_date_after="
					+ df.format(cal.getTime());
			endpoint += filter;
		}
		ResponseEntity<String> response = this.exchange(endpoint, HttpMethod.GET, null, String.class);

		return response.getBody();
	}

	private String getMediumExpression(int medium) {
		switch (medium) {
		case 1:
			return "electricity";
		case 2:
			return "gas";
		case 3:
			return "water";
		}
		return null;
	}

	private void addAuthentication() {
		List<ClientHttpRequestInterceptor> interceptors = Collections
				.singletonList(new BasicAuthorizationInterceptor());
		setRequestFactory(new InterceptingClientHttpRequestFactory(getRequestFactory(), interceptors));
	}

	public static class BasicAuthorizationInterceptor implements ClientHttpRequestInterceptor {

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
				throws IOException {

			List<MediaType> mediaTypes = new ArrayList<>();
			mediaTypes.add(MediaType.APPLICATION_JSON);
			request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			request.getHeaders().add("Content-Encoding", "gzip");
			request.getHeaders().setAccept(mediaTypes);
			request.getHeaders().add("Authorization", "Token " + TOKEN);
			return execution.execute(request, body);
		}
	}
}
