package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tool_type_new")

public class Tooltype implements Serializable {

	private Integer id;
	private String name;
	private Integer minDeadline = 15;
	private Integer maxDeadline = 20;
	private String image;
	private Integer depreciation = 0;
	private Boolean depreciable;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", length = 45)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "min_deadline", columnDefinition = "DOUBLE default 15")
	public Integer getMinDeadline() {
		return minDeadline;
	}

	public void setMinDeadline(Integer minDeadline) {
		this.minDeadline = minDeadline;
	}

	@Column(name = "max_deadline", columnDefinition = "DOUBLE default 20")
	public Integer getMaxDeadline() {
		return maxDeadline;
	}

	public void setMaxDeadline(Integer maxDeadline) {
		this.maxDeadline = maxDeadline;
	}

	@Column(name = "image", length = 500, columnDefinition = "VARCHAR(500) default 'files/no-image.png'")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name = "depreciation", columnDefinition = "INT(3) default '5'")
	public Integer getDepreciation() {
		return depreciation;
	}

	public void setDepreciation(Integer depreciation) {
		this.depreciation = depreciation;
	}

	@Column(name = "depreciable")
	public Boolean getDepreciable() {
		return depreciable;
	}

	public void setDepreciable(Boolean depreciable) {
		this.depreciable = depreciable;
	}

}