package ma.azdad.chat;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ma.azdad.model.User;
import ma.azdad.repos.ChatMessageRepos;
import ma.azdad.service.ChatMessageService;
import ma.azdad.service.UserService;
import ma.azdad.utils.App;
import ma.azdad.view.SessionView;

@Controller
public class ChatController {

	@Autowired
	ChatMessageRepos chatMessageRepos;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ChatMessageService chatMessageService;
	
	@Value("${application}")
	private String application;

	@MessageMapping("/chat.register")
	@SendTo("/topic/public")
	public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

		return chatMessage;
	}

	@MessageMapping("/chat.send")
	@SendTo("/topic/public")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		User userSender = userService.findByUsername(chatMessage.getSender());
		User userReceiver = userService.findByUsername(chatMessage.getReceiver());
		chatMessage.setUserSender(userSender);
		chatMessage.setUserReceiver(userReceiver);
		chatMessage.setApp(application);
		chatMessageService.save(chatMessage);
		chatMessageService.evictCache();
		return chatMessage;
	}

	

}
