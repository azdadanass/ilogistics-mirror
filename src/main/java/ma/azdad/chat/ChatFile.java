package ma.azdad.chat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import ma.azdad.model.GenericModel;

@Entity
public class ChatFile extends GenericModel<Integer>  {

	private String filename;
	private String data;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Column(columnDefinition = "MEDIUMBLOB")
	@Lob
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	// Constructors, getters, setters

}
