package ma.azdad.chat;

import java.util.Collections;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.model.User;
import ma.azdad.repos.ChatMessageRepos;
import ma.azdad.service.UserService;
import ma.azdad.view.SessionView;

@RestController
public class ChatRest {
	
	@Autowired
	ChatMessageRepos chatMessageRepos;
	
	@Autowired
	UserService userService;
	
	// ... other methods ...

	@GetMapping("/chat/messages/{username}")
	public List<ChatMessage> getLatestChatMessages(@PathVariable String username) {
		
		User user = userService.findByUsername(username);
		
		List<ChatMessage> messages = chatMessageRepos.findByUserSenderOrderByIdDesc(user);
		for (ChatMessage chatMessage : messages) {
			Hibernate.initialize(chatMessage.getUserSender());
		}
		Collections.reverse(messages);
		return messages;
	}
	@GetMapping("/chat/messages/{username}/{receiver}")
	public List<ChatMessage> getLatestChatMessages(@PathVariable String username,@PathVariable String receiver) {
		
		System.out.println("receiver is" +receiver);
		User user = userService.findByUsername(username);
		
		User receiver1 = userService.findByUsername(receiver);
		
		List<ChatMessage> messages = chatMessageRepos.findByUserSenderAndUserReceiverOrderByIdDesc(user, receiver1);
		
		
		for (ChatMessage chatMessage : messages) {
			Hibernate.initialize(chatMessage.getUserSender());
		}
		Collections.reverse(messages);
		return messages;
	}
	
	@GetMapping("/chat/users")
    public List<User> getAllUsers() {
        return userService.findLightByInternalAndActive();
    }
	
	
	@GetMapping("/chat/users/{selecteduser}")
	public String getSelectedUser(@PathVariable String selecteduser) {
		
		User user = userService.findByUsername(selecteduser);
		
		return user.getFullName();
	}
	
	@GetMapping("/chat/users2/{selecteduser}")
	public String getSelectedUser2(@PathVariable String selecteduser) {
		
		User user = userService.findByUsername(selecteduser);
		
		return user.getUsername();
	}
	
	@GetMapping("/chat/photo/{selecteduser}")
    public String getPhoto(@PathVariable String selecteduser) {
		
		User user = userService.findByUsername(selecteduser);
        return user.getPhoto();
    }
	
	@PutMapping("/chat/putSeen/{selecteduser}/{concteduser}")
    public void putSeen(@PathVariable String selecteduser,@PathVariable String concteduser) {
		
		User user = userService.findByUsername(selecteduser);
		List<ChatMessage> list = chatMessageRepos.findByUserReceiverUsernameAndUserSenderUsernameAndSeen(concteduser, selecteduser, false);
		for (ChatMessage chatMessage : list) {
			chatMessage.setseen(true);
			chatMessageRepos.save(chatMessage);
		}
    }
	
	
	
	
	
	@GetMapping("/chat/photo2/{selecteduser}")
    public String getPhoto2(@PathVariable String selecteduser) {
		
		User user = userService.findByFullName(selecteduser);
        return user.getPhoto();
    }
	
	
	

}
