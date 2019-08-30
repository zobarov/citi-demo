package com.pb.payhub.citidemo.cfg;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CitiVelocityConfig {
	//TODO: temporary as just statics:
	public final static String AUTH_ENDPOINT_URL = "https://api.citivelocity.com/markets/cv/api/fx/oauth2/token";
	public final static String REQUEST_STATUS_WSS = "wss://www.streamapi.citivelocity.com/markets/sandbox/fxgateway/ws/request-status?client_id=" + CitiVelocityConfig.CLIENT_ID;
	public final static String API_ENDPOINT_URL = "https://api.citivelocity.com/markets/sandbox/fxgateway/api/v1?client_id=" + CitiVelocityConfig.CLIENT_ID;
	
	public final static String CLIENT_ID = "f362a95c-64ec-481c-92e9-a628758210a3";
	public final static String CLIENT_SECRET = "*****************************";
	
	public static final String SENDER_COMP_ID = "pitneybowes-uat";
	public static final String ON_BEHALF_OF_COMP_ID = "pitneybowes-uat";
	public static final String ACCOUNT = "pitneybowes-uat";
	

}
