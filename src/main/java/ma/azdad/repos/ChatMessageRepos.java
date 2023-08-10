package ma.azdad.repos;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.chat.ChatMessage;
import ma.azdad.model.User;


@Repository
public interface ChatMessageRepos  extends JpaRepository<ChatMessage, Integer>{
	
	List<ChatMessage> findByOrderByIdDesc();
	List<ChatMessage>  findByUserSenderOrderByIdDesc(User sender);
	List<ChatMessage>  findByUserReceiverOrderByIdDesc(User rec);
	@Query("from ChatMessage c where (c.userSender=?1 and c.userReceiver=?2) or (c.userSender=?2 and c.userReceiver=?1) order by c.id desc")
	List<ChatMessage>  findByUserSenderAndUserReceiverOrderByIdDesc(User sender,User receiver);
				
	ChatMessage  findTopByUserReceiverUsernameAndUserSenderUsernameOrderByTimestampDesc(String receiverUsername, String senderUsername);
	ChatMessage  findTopByUserReceiverUsernameAndUserSenderUsernameAndSeenOrderByTimestampDesc(String receiverUsername, String senderUsername,Boolean seen);
	
	Long  countByUserReceiverUsernameAndSeen(String receiverUsername,Boolean seen);
	Long  countByUserReceiverUsernameAndUserSenderUsernameAndSeen(String receiverUsername, String senderUsername,Boolean seen);
	List<ChatMessage>  findByUserReceiverUsernameAndUserSenderUsernameAndSeen(String receiverUsername, String senderUsername,Boolean seen);
	
	
	
	
	
	@Modifying
	@Query("update ChatMessage set seen = ?2 where UserReceiver= ?1 and seen=?3")
	void updateSeen(User  receiver, Boolean tr,Boolean fa);
	
	

}
