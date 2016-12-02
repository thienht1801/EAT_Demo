package com.predix.iot.bms.timeseries.websocket;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TsWebSocketEndpoint extends Endpoint {

	private static Logger log = LoggerFactory.getLogger(TsWebSocketEndpoint.class);
	
	Session curSession;

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		this.curSession = session;
	}

	/**
	 * Define the behavior of the client message handler when the session is closed
	 * 
	 * @param session: WebSocket session
	 * @param closeReason: Provide details of session closing
	 */
	@OnClose
	@Override
	public void onClose(Session session, CloseReason closeReason) {
		log.debug("Client: Session " + session.getId() + " closed because of " + closeReason.getReasonPhrase()); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
