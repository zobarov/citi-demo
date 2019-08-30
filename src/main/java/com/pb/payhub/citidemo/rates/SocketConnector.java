package com.pb.payhub.citidemo.rates;

import java.net.URI;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pb.payhub.citidemo.access.AccessTokenStore;
import com.pb.payhub.citidemo.cfg.CitiVelocityConfig;

@Service
@WebSocket
public class SocketConnector {
	private Logger logger = LoggerFactory.getLogger("SocketConnector");
	@Autowired
	private AccessTokenStore accessTokenStore;
	
	private Session currentSession;
	
	public void establishConnection() {
		logger.info("Establishing WSS socket connection for listening.");
		CountDownLatch closeLatch = new CountDownLatch(1);

		SslContextFactory sslContextFactory = new SslContextFactory(); 
		sslContextFactory.setEndpointIdentificationAlgorithm("HTTPS"); 
		HttpClient httpClient = new HttpClient(sslContextFactory); 
		WebSocketClient client = new WebSocketClient(httpClient); 
		client.setMaxIdleTimeout(1800000);// Web socket will be closed after 30 minutes of inactivity 
		try {
			httpClient.start();
			client.start();
			ClientUpgradeRequest request = new ClientUpgradeRequest(); 
			request.setHeader("Authorization ", Collections.singletonList("Bearer " + accessTokenStore.accessToken()));
			Future<Session> future = client.connect(this, URI.create(CitiVelocityConfig.REQUEST_STATUS_WSS), request); 
			Session session = (Session) future.get();
			this.currentSession = session;
			logger.info("Session is opened: " + currentSession.isOpen());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isCurrentSessionOpen() {
		return this.currentSession.isOpen();
	}
	
	@OnWebSocketConnect
	public void onConnect(Session session) {
		logger.info("onConnect() invoked");
		logger.info("Is session open? " + session.isOpen());

		try
        {
			/*
			String requestJson = rateRequest().toString();
			logger.info("Formed request json: {}", requestJson);
            Future<Void> fut = session.getRemote().sendStringByFuture(requestJson);
            fut.get(2000, TimeUnit.SECONDS); // wait for send to complete.
            
            */
        } catch (Throwable t)
        {
            t.printStackTrace();
        }
		
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		logger.info("onClose() invoked");
		logger.info("Status Code : " + statusCode + "; Reason: " + reason);
		if (statusCode == 1001) {
			//String token = accessTokenSrv.recieveAccessToken();
			
			logger.info("Open socket connection here again?");
			//openSocketConnection(accessTokenStorage.token());
		}
	}

	@OnWebSocketError
	public void onError(Throwable cause) {
		logger.info("onError() invoked " + cause);
	}

	@OnWebSocketMessage
	public void onMessage(String msg) {
		logger.info("onMessage() invoked");
		logger.info("Received a message via socket " + msg);
	}

}
