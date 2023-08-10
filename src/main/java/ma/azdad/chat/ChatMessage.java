package ma.azdad.chat;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ma.azdad.model.GenericModel;
import ma.azdad.model.User;

@Entity
public class ChatMessage extends GenericModel<Integer> {

	private String content;
	private String sender;
	private String receiver;
	private Boolean seen = false;
	private User userSender;
	private User userReceiver;
	private String photo;
	
    private ChatFile file;
	

	private LocalDateTime timestamp = LocalDateTime.now();;
	private MessageType type;

	public enum MessageType {
		CHAT, LEAVE, JOIN
	}

	@Override
	public boolean filter(String query) {
		return contains(query, content);
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

	

	@Column(columnDefinition = "TEXT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@Transient
	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	public User getUserSender() {
		return userSender;
	}

	public void setUserSender(User userSender) {
		this.userSender = userSender;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	public User getUserReceiver() {
		return userReceiver;
	}

	public void setUserReceiver(User userReceiver) {
		this.userReceiver = userReceiver;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean getseen() {
		return seen;
	}

	public void setseen(Boolean seen) {
		this.seen = seen;
	}
	
	
	@Column(columnDefinition = "MEDIUMBLOB")
	@Lob
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@OneToOne(cascade = CascadeType.ALL) // Example relationship, adjust as needed
	public ChatFile getFile() {
		return file;
	}

	public void setFile(ChatFile file) {
		this.file = file;
	}


	
	

}
