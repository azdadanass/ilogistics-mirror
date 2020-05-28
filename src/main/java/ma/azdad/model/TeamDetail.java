package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class TeamDetail extends GenericBean implements Serializable {

	private Boolean internal;
	private TeamDetailType type;

	private User user;
	private Team team;

	// tmp
	private String tmpUserUsername;

	public void init() {
		if (user != null)
			tmpUserUsername = user.getUsername();
	}

	public TeamDetail() {
		super();
	}

	public TeamDetail(Team team) {
		super();
		this.team = team;
		if (TeamType.INTERNAL.equals(team.getType()))
			this.internal = true;
		else if (TeamType.EXTERNAL.equals(team.getType()))
			this.internal = false;
	}

	public TeamDetail(TeamDetailType type, Team team) {
		super();
		this.type = type;
		this.team = team;
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && type != null)
			result = type.getValue().toLowerCase().contains(query);
		return result;
	}

	@Transient
	public String getResourceFullName() {
		return user.getFullName();
	}

	@Transient
	public String getResourcePhoto() {
		return user.getPhoto();
	}

	@Transient
	public String getResourcePhone() {
		return user.getPhone();
	}

	@Transient
	public String getResourceEmail() {
		return user.getEmail();
	}

	@Enumerated(EnumType.ORDINAL)
	public TeamDetailType getType() {
		return type;
	}

	public void setType(TeamDetailType type) {
		this.type = type;
	}

	public Boolean getInternal() {
		return internal;
	}

	public void setInternal(Boolean internal) {
		this.internal = internal;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	@Transient
	public String getTmpUserUsername() {
		return tmpUserUsername;
	}

	@Transient
	public void setTmpUserUsername(String tmpUserUsername) {
		this.tmpUserUsername = tmpUserUsername;
	}

}
