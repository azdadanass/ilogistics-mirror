package ma.azdad.mobile.model;

import ma.azdad.utils.Public;

/**
 * Data class that captures user information for logged in users retrieved from
 * LoginRepository
 */
public class User {

	private String username;
	private String login;
	private String password;
	private String firstName;
	private String lastName;
	private String photo;
	private String email;

	public User() {
		super();
	}

	public User(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public User(String username, String firstName, String lastName, String login,String photo,String email) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.photo = Public.getPublicUrl(photo);
		this.email = email;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}