package com.pb.payhub.citidemo.access;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pb.payhub.citidemo.cfg.CitiVelocityConfig;



@Service
public class AccessTokenStore {
	private Logger logger = LoggerFactory.getLogger("AccessTokenStore");
	@Autowired
	@Qualifier("ToAuth")
	private RestTemplate restToCitiAuth;
	
	private String accessToken;
	
	private final static String PLACEHOLDER_CLIENT_ID = "{client_id}";
	private final static String PLACEHOLDER_CLIENT_SECRET = "{client_secret}";

	
	public String accessToken() {
		if(accessToken == null) {
			logger.info("Access token obtaining here...");
			this.accessToken = reqToken();
		}
		logger.info("Access token [{}]", accessToken);
		return this.accessToken;
	}
	
	/****** Requesting methods ******/
	private String reqToken() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		String placeholdBody = replacePlaceholders(bodyForToken());

		HttpEntity<String> requestWithPlainBody = new HttpEntity<>(placeholdBody, headers);
		
		ResponseEntity<Object> response = restToCitiAuth.postForEntity(CitiVelocityConfig.AUTH_ENDPOINT_URL, requestWithPlainBody, Object.class);
		
		Object linkedMapResp = response.getBody();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>)linkedMapResp;
		return extractToken(map);
	}
	
	private String replacePlaceholders(String body) {
		String replaced = body.replace(PLACEHOLDER_CLIENT_ID, CitiVelocityConfig.CLIENT_ID);
		replaced = replaced.replace(PLACEHOLDER_CLIENT_SECRET, CitiVelocityConfig.CLIENT_SECRET);
		
		return replaced;
	}
	
	private String bodyForToken() {
		String body = "client_id=" + PLACEHOLDER_CLIENT_ID
				+ "&client_secret=" + PLACEHOLDER_CLIENT_SECRET
				+ "&grant_type=client_credentials&scope=/fxapi";
		
		return body;
	}
	
	private String extractToken(Map<String, Object> map) {
			Object token = map.get("access_token");
			
			if(token == null) {
				throw new IllegalStateException("AccessToken is missed");
			}
			return token.toString();
	}
 	
	@Bean("ToAuth")
	private RestTemplate restTemplate(RestTemplateBuilder builder) {
	    return builder.build();
	}

}
