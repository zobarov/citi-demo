package com.pb.payhub.citidemo.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pb.payhub.citidemo.access.AccessTokenStore;
import com.pb.payhub.citidemo.rates.RequestSubmitter;
import com.pb.payhub.citidemo.rates.SocketConnector;

@Service
public class RatesObtainingWorkflow {
	private Logger logger = LoggerFactory.getLogger("com.pb.payhub.citidemo");
	@Autowired
	private AccessTokenStore accessTokenStore;
	@Autowired
	private SocketConnector socketConnector;
	@Autowired
	private RequestSubmitter requestSubmitter;
	
	public void flow( ) {
		logger.info("Starting rates workflow for execution...");
		String token = accessTokenStore.accessToken();
		logger.info("Obtained token: " + token);
		logger.info("Establishing socket connection...");
		socketConnector.establishConnection();
		logger.info("Submitting rates request as POST...");
		requestSubmitter.submitPostRequestForrates();
		logger.info("Listening the socket...");
		socketConnector.isCurrentSessionOpen();
		
	}

}
