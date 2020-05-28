package ma.azdad.model;

import java.io.Serializable;


public class CustomerCategory implements Serializable {

	private String category;
	private String image;

	public CustomerCategory(String category, String image) {
		super();
		this.category = category;
		this.image = image;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
