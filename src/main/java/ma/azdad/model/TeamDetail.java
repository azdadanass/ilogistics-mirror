package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class TeamDetail extends GenericModel<Integer> implements Serializable {

	private Boolean internal;
	private TeamDetailType type;

	private User user;
	private Team team;

	public void init() {
	}

	public TeamDetail() {
		super();
	}

	public TeamDetail(Team team, TeamDetailType type) {
		super();
		this.team = team;
		this.type = type;
		if (TeamCategory.INTERNAL.equals(team.getCategory()))
			this.internal = true;
		else if (TeamCategory.EXTERNAL.equals(team.getCategory()))
			this.internal = false;
	}

	public TeamDetail(TeamDetailType type, Team team) {
		super();
		this.type = type;
		this.team = team;

		if (team.getCategory() != null && !TeamCategory.MIXTE.equals(team.getCategory()))
			this.internal = TeamCategory.INTERNAL.equals(team.getCategory());
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && type != null)
			result = type.getValue().toLowerCase().contains(query);
		return result;
	}

	@Transient
	public Boolean getIsTeamLeader() {
		return TeamDetailType.TEAM_LEADER.equals(type);
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
	public String getUserUsername() {
		return user != null ? user.getUsername() : null;
	}

	@Transient
	public void setUserUsername(String userUsername) {
		if (user == null)
			user = new User();
		user.setUsername(userUsername);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
