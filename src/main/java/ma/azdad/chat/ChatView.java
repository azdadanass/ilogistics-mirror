package ma.azdad.chat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.transaction.Transactional;

import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.Conversation;
import ma.azdad.model.Currency;
import ma.azdad.model.Role;
import ma.azdad.model.User;
import ma.azdad.repos.ChatMessageRepos;
import ma.azdad.repos.CurrencyRepos;
import ma.azdad.repos.UserRepos;
import ma.azdad.service.ChatMessageService;
import ma.azdad.service.CurrencyService;
import ma.azdad.service.UserRoleService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;
import ma.azdad.view.GenericView;
import ma.azdad.view.RepeatPaginator;
import ma.azdad.view.SessionView;
import ma.azdad.view.UserView;

@ManagedBean
@Component
@Scope("view")
public class ChatView extends GenericView<Integer, ChatMessage, ChatMessageRepos, ChatService> {

	@Autowired
	private SessionView sessionView;
	@Autowired
	private UserView userView;
	@Autowired
	UserRepos userRepos;
	@Autowired
	ChatMessageRepos chatRepos;
	
	@Autowired
	UserRoleService useRoleService;
	
	@Autowired
	ChatMessageService chatMessageService;
	
	 @Value("${application}")
	 private String application;

	String username;
	Integer check = 0;
	Long totalMNotSeen;
	
	private String searchBean2 = "";
	private RepeatPaginator messagePaginator;

	private List<Conversation> conversations = new ArrayList<>();
	private List<Conversation> top10Conversations = new ArrayList<>();
	private List<Conversation> allConversations = new ArrayList<>();
	private List<Conversation> filterallConversations = new ArrayList<>();

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
		totalMNotSeen = chatRepos.countByUserReceiverUsernameAndSeenAndApp(sessionView.getUsername(), false,application);
	}

	@Override
	protected void initParameters() {
		super.initParameters();
		if (getParameter("username") != null)
			username = getParameter("username");

		if (getParameter("check") != null)
			check = getIntegerParameter("check");
		top10Conversations=findTop10UserConversations(sessionView.getUsername());
		allConversations=filterallConversations=findAllUserConversations(sessionView.getUsername());
		messagePaginator = new RepeatPaginator(allConversations);

	}

	@Override
	public void refreshList() {

		if (isPage("chat") && pageIndex != null) {
			switch (pageIndex) {
			case 1:
				conversations = findUserConversations(sessionView.getUsername());
				break;
			case 2:
				conversations = userView.findOnlineUserConversations(sessionView.getUsername());
				break;

			default:
				break;
			}
		}

	}
	
	public List<Conversation> findUserConversations(String username) {

		List<User> users = userView.findLightByActive(username);
		List<Conversation> conversations = new ArrayList<>();
		for (User user : users) {

			if(useRoleService.isHavingRole(user.getUsername(), Role.ROLE_ILOGISTICS)) {
			
			conversations.add(new Conversation(user, null, null, null));
			
		}

		}

		return conversations;
	}
	
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
	
	 public List<Conversation> findTop10UserConversations(String username) {

			
			
			
			return chatMessageService.findTop10UserConversations(username);
	}
	
	
	 public List<Conversation> findAllUserConversations(String username) {

			
			return chatMessageService.findAllUserConversations(username);
	}

	public String getJavascriptUsername() {

		if (username != null) {
			System.out.print(username + " is in js");
			return "var senderUsername = '" + username + "';";
		} else {
			return null;
		}
	}

	public Boolean isPage() {
		return ("/" + "chat" + ".xhtml").equals(currentPath) || ("/" + "chat2" + ".xhtml").equals(currentPath)
				|| ("/" + "conversations" + ".xhtml").equals(currentPath);
	}

	public List<Conversation> findMessagesNotSeen() {

		List<User> users = userView.findLightByActive(sessionView.getUsername());
		List<Conversation> conversations = new ArrayList<>();
		for (User user : users) {
			
			if(useRoleService.isHavingRole(user.getUsername(), Role.ROLE_ILOGISTICS)) {

			ChatMessage messageReceived = chatRepos
					.findTopByUserReceiverUsernameAndUserSenderUsernameAndSeenAndAppOrderByTimestampDesc(
							sessionView.getUsername(), user.getUsername(), false,application);
			if (messageReceived != null)
				conversations.add(new Conversation(user, null, null, null));

		}
			
		}
		return conversations;

	}

	public Long countTotalNotSeen() {

		return chatRepos.countByUserReceiverUsernameAndSeenAndApp(sessionView.getUsername(), false,application);

	}

	public Long countNotSeen(String user) {

		return chatRepos.countByUserReceiverUsernameAndUserSenderUsernameAndSeenAndApp(sessionView.getUsername(), user,
				false,application);

	}

	public void updateSeen() {
		if (check == 1) {
			User user = userRepos.findByUsername(username);
			List<ChatMessage> list = chatRepos.findByUserReceiverUsernameAndUserSenderUsernameAndSeenAndApp(
					sessionView.getUsername(), user.getUsername(), false,application);
			for (ChatMessage chatMessage : list) {
				chatMessage.setseen(true);
				chatRepos.save(chatMessage);
				chatMessageService.evictCache();
			}

		}

	}
	
	public void filterBean2(String query) {
		
		List<Conversation> list = new ArrayList<Conversation>();
		query = query.toLowerCase().trim();
		for (Conversation bean : filterallConversations) {
			if (bean.filter(query))
				list.add(bean);
		}
		allConversations = list;
		messagePaginator = new RepeatPaginator(allConversations);
	}

	

	// save
	public Boolean canSave() {
		return sessionView.getIsAdmin();
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {

		return true;
	}

	// delete
	public Boolean canDelete() {
		return true;
	}

	public String delete() {
		if (!canDelete())
			return null;
		try {
			service.delete(model);
		} catch (DataIntegrityViolationException e) {
			FacesContextMessages.ErrorMessages("Can not delete this item (contains childs)");
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages("Error !");
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}
		return addParameters(listPage, "faces-redirect=true");
	}

	@Cacheable("currencyView.findAll")
	public List<ChatMessage> findAll() {
		return service.findAll();
	}

	// getters & setters
	public ChatMessage getModel() {
		return model;
	}

	public void setModel(ChatMessage model) {
		this.model = model;
	}

	public List<Conversation> getConversations() {
		return conversations;
	}

	public void setConversations(List<Conversation> conversations) {
		this.conversations = conversations;
	}

	public Long getTotalMNotSeen() {
		return totalMNotSeen;
	}

	public void setTotalMNotSeen(Long totalMNotSeen) {
		this.totalMNotSeen = totalMNotSeen;
	}

	public List<Conversation> getTop10Conversations() {
		return top10Conversations;
	}

	public void setTop10Conversations(List<Conversation> top10Conversations) {
		this.top10Conversations = top10Conversations;
	}

	public List<Conversation> getAllConversations() {
		return allConversations;
	}

	public void setAllConversations(List<Conversation> allConversations) {
		this.allConversations = allConversations;
	}
	
	public String getSearchBean2() {
		return searchBean2;
	}

	public void setSearchBean2(String searchBean2) {
		this.searchBean2 = searchBean2;
		filterBean2(searchBean2);
	}
	
	public RepeatPaginator getMessagePaginator() {
		return messagePaginator;
	}

	public void setMessagePaginator(RepeatPaginator messagePaginator) {
		this.messagePaginator = messagePaginator;
	}
	
	
	

}
