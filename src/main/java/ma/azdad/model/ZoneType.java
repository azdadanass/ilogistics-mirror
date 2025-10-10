package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class ZoneType extends GenericModel<Integer> {

	private ZoneCategory category;
	private PartNumberType type;

	public boolean filter(String query) {
		return contains(query, getTypeName());
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return getTypeName();
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public ZoneCategory getCategory() {
		return category;
	}

	public void setCategory(ZoneCategory category) {
		this.category = category;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PartNumberType getType() {
		return type;
	}

	public void setType(PartNumberType type) {
		this.type = type;
	}

	@Transient
	public String getTypeName() {
		return type != null ? type.getName() : null;
	}

	@Transient
	public void setTypeName(String typeName) {
		if (type == null)
			type = new PartNumberType();
		type.setName(typeName);
	}
	
	@Transient
	public Integer getTypeId(){
		return type!=null?type.getId():null;
	}

	@Transient
	public void setTypeId(Integer typeId){
		if(type==null || !typeId.equals(type.getId()))
			type=new PartNumberType();
		type.setId(typeId);
	}

}
