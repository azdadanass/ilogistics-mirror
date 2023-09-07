package ma.azdad.model;




import java.time.LocalDateTime;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

public class Conversation implements Comparable<Conversation>{
	
	private User sender;
	private String lastMessage;
	private LocalDateTime time;
	private Boolean read;
	
	
	 @Override
	    public int compareTo(Conversation other) {
	        // Sort by time in descending order
	        return other.getTime().compareTo(this.getTime());
	    }
	 
	 public Boolean contains(String query, Object... objects) {
			for (int i = 0; i < objects.length; i++) {
				Object o = objects[i];
				if (o == null)
					continue;
				if (o instanceof String && ((String) o).toLowerCase().contains(query))
					return true;
				if (o instanceof Double && ((Double) o).toString().contains(query))
					return true;
				if (o instanceof Integer && ((Integer) o).toString().contains(query))
					return true;
				if (o instanceof Date && UtilsFunctions.getFormattedDate(((Date) o)).contains(query))
					return true;
			}
			return false;
		}
	 
	 public boolean filter(String query) {
			return contains(query,sender.getFullName());
		}

	
	
	public Conversation(User sender, String lastMessage, LocalDateTime time, Boolean read) {
		super();
		this.sender = sender;
		this.lastMessage = lastMessage;
		this.time = time;
		this.read = read;
	}
	
	public String getSubMessage() {
        if (lastMessage != null && lastMessage.length() > 35) {
            return lastMessage.substring(0, 35)+"...";
        }
        return lastMessage;
    }
	
	public String getLst() {
        if (lastMessage != null) {
            return "Last Message";
        }else {
        return null;
        }
    }
	
	@Transient
	public String getInternal() {
		if (!sender.getInternal())
			return "(External)";
		else
			return "";
	}
	
	public String getFullName() {
		
		return " "+sender.getFullName();
	}
	
	 public  Date localDateTimeToDate(LocalDateTime localDateTime) {
	        return  Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	    }
	
	

	

	public String formatTimestamp(LocalDateTime timestamp) {
	    LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

	    LocalDateTime yesterday = today.minusDays(1);

	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        if(timestamp!=null) {
	    if (timestamp.isAfter(today)) {
	        // Message is from today, display only the time
	        return timestamp.format(timeFormatter);
	    } else if (timestamp.isAfter(yesterday)) {
	        // Message is from yesterday, display 'Yesterday' and the time
	        return "Yesterday " + timestamp.format(timeFormatter);
	    } else {
	        // Message is from earlier than yesterday, display the date and time
	        return timestamp.format(dateTimeFormatter);
	    }
        }else {
        	return null;
        }
	}


	
	
	public User getsender() {
		return sender;
	}
	public void setsender(User sender) {
		this.sender = sender;
	}
	public String getLastMessage() {
		return lastMessage;
	}
	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public Boolean getread() {
		return read;
	}
	public void setread(Boolean read) {
		this.read = read;
	}

	
	
	
	

	
	
	
}
