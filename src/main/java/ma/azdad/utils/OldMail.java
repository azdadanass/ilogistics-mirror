package ma.azdad.utils;

import javax.mail.internet.InternetAddress;

public class OldMail {

	private InternetAddress from;
	private String to;
	private InternetAddress[] cc;
	private String bcc;
	private String subject;
	private String message;

	public InternetAddress getFrom() {
		return from;
	}

	public void setFrom(InternetAddress from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public InternetAddress[] getCc() {
		return cc;
	}

	public void setCc(InternetAddress[] cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
