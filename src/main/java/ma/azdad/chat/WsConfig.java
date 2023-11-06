package ma.azdad.chat;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

//@Configuration
//@EnableWebSocketMessageBroker
public class WsConfig implements WebSocketMessageBrokerConfigurer{

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ilogistics-endPoint").withSockJS();
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("/ilogistics");
	}
	
	 @Override
	    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
	        // Set the buffer size limit to your desired value (in bytes)
	        registration.setMessageSizeLimit(6*1024 * 1024); // 1 MB as an example
	    }
}
