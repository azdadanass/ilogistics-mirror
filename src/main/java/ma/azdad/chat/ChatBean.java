package ma.azdad.chat;


import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;


import org.springframework.beans.factory.annotation.Autowired;

import ma.azdad.repos.ChatMessageRepos;

@ManagedBean
@ViewScoped
public class ChatBean implements Serializable {

    private List<ChatMessage> messages;

    @Autowired
    private ChatMessageRepos chatMessageRepos;

    @PostConstruct
    public void init() {
        // Load the messages from the database when the bean is initialized
        messages = chatMessageRepos.findByOrderByIdDesc();
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }
}

