package com.pb.payhub.citidemo.rates;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

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

import com.google.gson.JsonObject;
import com.pb.payhub.citidemo.access.AccessTokenStore;
import com.pb.payhub.citidemo.cfg.CitiVelocityConfig;

@Service
public class RequestSubmitter {
	private Logger logger = LoggerFactory.getLogger("RequestSubmitter");
	@Autowired
	private AccessTokenStore accessTokenStore;
	
	@Autowired
	@Qualifier("ToApi")
	private RestTemplate restToCitiApi;
	
	public void submitPostRequestForrates() {
		
		logger.info("Submitting POST request for RATEs...");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBearerAuth(accessTokenStore.accessToken());
		
		String bodyWithJson = rateRequestBody();

		HttpEntity<String> requestWithPlainBody = new HttpEntity<>(bodyWithJson, headers);
		logger.info("Sending request with body:" + requestWithPlainBody.getBody());
		ResponseEntity<Object> response = restToCitiApi.postForEntity(CitiVelocityConfig.API_ENDPOINT_URL, requestWithPlainBody, Object.class);
		
		Object acknowledgement = response.getBody();
		logger.info("Response for posting request:" + acknowledgement);		
	}
	
	private String rateRequestBody() {
		 JsonObject rateRequest = new JsonObject();
		 rateRequest.addProperty("action", "rate-request");
		 rateRequest.addProperty("senderCompId", CitiVelocityConfig.SENDER_COMP_ID);
		 rateRequest.addProperty("onBehalfOfCompId", CitiVelocityConfig.ON_BEHALF_OF_COMP_ID);
		 rateRequest.addProperty("jsonReqId", UUID.randomUUID().toString());
		 rateRequest.addProperty("sendingTime", new Date().toString());
		 rateRequest.addProperty("symbol", "ALL");
		 rateRequest.addProperty("account", CitiVelocityConfig.ACCOUNT);
		 return rateRequest.toString();
	}
	
	@Bean("ToApi")
	private RestTemplate restTemplate(RestTemplateBuilder builder) {
	    return builder.build();
	}

}
