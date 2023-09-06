package ma.azdad.repos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.chat.ChatMessage;
import ma.azdad.model.User;

@Repository
public interface ChatMessageRepos extends JpaRepository<ChatMessage, Integer> {

	List<ChatMessage> findByOrderByIdDesc();

	List<ChatMessage> findByUserSenderAndAppOrderByIdDesc(User sender,String app);

	List<ChatMessage> findByUserReceiverAndAppOrderByIdDesc(User rec,String app);

	@Query("from ChatMessage c where (c.userSender=?1 and c.userReceiver=?2 and c.app = ?3) or (c.userSender=?2 and c.userReceiver=?1 and c.app = ?3) order by c.id desc")
	List<ChatMessage> findByUserSenderAndUserReceiverAndAppOrderByIdDesc(User sender, User receiver,String app);

	ChatMessage findTopByUserReceiverUsernameAndUserSenderUsernameAndAppOrderByTimestampDesc(String receiverUsername,
			String senderUsername,String app);

	ChatMessage findTopByUserReceiverUsernameAndUserSenderUsernameAndSeenAndAppOrderByTimestampDesc(String receiverUsername,
			String senderUsername, Boolean seen,String app);

	Long countByUserReceiverUsernameAndSeenAndApp(String receiverUsername, Boolean seen,String app);

	Long countByUserReceiverUsernameAndUserSenderUsernameAndSeenAndApp(String receiverUsername, String senderUsername,
			Boolean seen,String app);

	List<ChatMessage> findByUserReceiverUsernameAndUserSenderUsernameAndSeenAndApp(String receiverUsername,
			String senderUsername, Boolean seen,String app);

	@Query(value = "SELECT * FROM chat_message cm " +
		       "WHERE cm.seen = false " +
		       "AND TIMESTAMPDIFF(HOUR, cm.timestamp, NOW()) >= 1 " +
		       "AND cm.user_receiver_username = ?1 AND cm.user_sender_username = ?2 " +
		       "AND cm.mailed = false "+
		       "AND cm.app = ?3",
		nativeQuery = true)
		List<ChatMessage> findUnseenMessagesByTimestampDifference2(User receiver,User sender,String app);
	
	@Query(value = "SELECT * FROM chat_message cm " +
		       "WHERE cm.seen = false " +
		       "AND TIMESTAMPDIFF(HOUR, cm.timestamp, NOW()) >= 1 " +
		       "AND cm.user_receiver_username = ?1 " +
		       "AND cm.mailed = false "+
		       "AND cm.app = ?2",
		nativeQuery = true)
		List<ChatMessage> findUnseenMessagesByTimestampDifference(User receiver,String app);




	@Modifying
	@Query("update ChatMessage set seen = ?2 where UserReceiver= ?1 and seen=?3 and app= ?4")
	void updateSeen(User receiver, Boolean tr, Boolean fa,String app);

}
