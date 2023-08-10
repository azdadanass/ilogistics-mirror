package ma.azdad.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.azdad.model.Currency;
import ma.azdad.repos.ChatMessageRepos;
import ma.azdad.repos.CurrencyRepos;
import ma.azdad.service.GenericService;

@Service
public class ChatService extends GenericService<Integer,ChatMessage, ChatMessageRepos> {
	
	@Autowired
    public ChatMessageRepos chatMessageRepos;

    
    

    public void saveChatMessage(ChatMessage chatMessage) {
        chatMessageRepos.save(chatMessage);
    }

    public List<ChatMessage> getLatestChatMessages() {
        return chatMessageRepos.findByOrderByIdDesc();
    }

}
