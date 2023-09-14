package ma.azdad.service;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.chat.ChatMessage;
import ma.azdad.model.Bu;
import ma.azdad.model.Conversation;
import ma.azdad.model.Role;
import ma.azdad.model.User;

import ma.azdad.repos.ChatMessageRepos;

@Component
public class ChatMessageService extends GenericService<Integer, ChatMessage, ChatMessageRepos> {
	
	@Autowired
	UserRoleService useRoleService;
	
	@Autowired
	UserService userService;
	
	
	 @Value("${application}")
	 private String application;
	
	
	 public  LocalDateTime getLargerDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
	        return dateTime1.compareTo(dateTime2) >= 0 ? dateTime1 : dateTime2;
	 }
	 
	 public  LocalDateTime getLatestDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
	        return dateTime1.isAfter(dateTime2) ? dateTime1 : dateTime2;
	    }
	 
	 public ChatMessage getLastMessage(ChatMessage message1, ChatMessage message2) {
	        LocalDateTime message1Time = message1.getTimestamp();
	        LocalDateTime message2Time = message2.getTimestamp();

	        LocalDateTime latestTime = getLatestDateTime(message1Time, message2Time);

	        // Return the ChatMessage with the latest timestamp
	        return latestTime.equals(message1Time) ? message1 : message2;
	    }
	 
	 
	
	@Cacheable("chatMessageService.findTop10UserConversations")
	public List<Conversation> findTop10UserConversations(String username) {

		List<User> users = userService.findLightByActive(username);
		List<Conversation> conversations = new ArrayList<>();
		for (User user : users) {
			
			if(useRoleService.isHavingRole(user.getUsername(), Role.ROLE_CRM)) {

			ChatMessage messageReceived = repos.findTopByUserReceiverUsernameAndUserSenderUsernameAndAppOrderByTimestampDesc(username, user.getUsername(),application); 
			ChatMessage messageSent = repos.findTopByUserReceiverUsernameAndUserSenderUsernameAndAppOrderByTimestampDesc(user.getUsername(),username,application);
			 
			if( messageReceived!=null && messageSent!=null ) 	
			conversations.add(new Conversation(user, getLastMessage(messageReceived, messageSent).getContent(), getLargerDateTime(messageReceived.getTimestamp(),messageSent.getTimestamp()), null));
			else if( messageReceived!=null && messageSent==null ) 	
				conversations.add(new Conversation(user, messageReceived.getContent(), messageReceived.getTimestamp(), null));
			else if( messageReceived==null && messageSent!=null ) 	
				conversations.add(new Conversation(user, messageSent.getContent(), messageSent.getTimestamp(), null));
			
			}

		}
			
			
		Collections.sort(conversations);
		

		if(conversations.size()>=10)
			return conversations.subList(0, 10);
		else
			return conversations;
	}
	
	 @Cacheable("chatMessageService.findAllUserConversations")
	public List<Conversation> findAllUserConversations(String username) {

		List<User> users = userService.findLightByActive(username);
		List<Conversation> conversations = new ArrayList<>();
		for (User user : users) {

			if(useRoleService.isHavingRole(user.getUsername(), Role.ROLE_CRM)) {
			ChatMessage messageReceived = repos.findTopByUserReceiverUsernameAndUserSenderUsernameAndAppOrderByTimestampDesc(username, user.getUsername(),application); 
			ChatMessage messageSent = repos.findTopByUserReceiverUsernameAndUserSenderUsernameAndAppOrderByTimestampDesc(user.getUsername(),username,application );
			 
			if( messageReceived!=null && messageSent!=null ) 	
			conversations.add(new Conversation(user, getLastMessage(messageReceived, messageSent).getContent(), getLargerDateTime(messageReceived.getTimestamp(),messageSent.getTimestamp()), null));
			else if( messageReceived!=null && messageSent==null ) 	
				conversations.add(new Conversation(user, messageReceived.getContent(), messageReceived.getTimestamp(), null));
			else if( messageReceived==null && messageSent!=null ) 	
				conversations.add(new Conversation(user, messageSent.getContent(), messageSent.getTimestamp(), null));
			
			}

		}
		Collections.sort(conversations);
		

			return conversations;
	}
	
	
	/*
	 * public List<ChatMessage> findTop10UserConversations(String userName){
	 * 
	 * return repos.findt
	 * 
	 * }
	 */

}
