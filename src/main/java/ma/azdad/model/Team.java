package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity

public class Team extends GenericBean implements Serializable {

	private String name;
	private String comment;
	private TeamType type;
	private Boolean active = true;

	private User user;

	private List<TeamDetail> detailList = new ArrayList<>();

	// tmp
	private Long tmpSize;
	private Long tmpTotalActiveAssignments;
	private String tmpTeamLeaderFullName;
	private TeamDetail tmpTeamLeader;
	private List<TeamDetail> tmpDetailList = new ArrayList<>();

	public Team() {
		super();
	}

	public Team(User user) {
		super();
		this.user = user;
	}

	public Team(Integer id, String name, TeamType type, Boolean active, Long tmpSize, Long tmpTotalActiveAssignments, String teamLeaderFullName) {
		super(id);
		this.name = name;
		this.type = type;
		this.active = active;
		this.tmpSize = tmpSize;
		this.tmpTotalActiveAssignments = tmpTotalActiveAssignments;
		this.tmpTeamLeaderFullName = teamLeaderFullName;
	}

	public void init() {
		for (TeamDetail detail : detailList) {
			detail.init();
			tmpDetailList.add(detail);
		}

	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && name != null)
			result = name.toLowerCase().contains(query);
		if (!result && tmpTeamLeaderFullName != null)
			result = tmpTeamLeaderFullName.toLowerCase().contains(query);
		return result;
	}

	@Transient
	public void initTmpTeamLeader() {
		for (TeamDetail td : detailList)
			if (TeamDetailType.TEAM_LEADER.equals(td.getType())) {
				tmpTeamLeader = td;
				break;
			}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "team", cascade = CascadeType.ALL)
	public List<TeamDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<TeamDetail> detailList) {
		this.detailList = detailList;
	}

	@Column(columnDefinition = "TEXT")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Enumerated(EnumType.ORDINAL)
	public TeamType getType() {
		return type;
	}

	public void setType(TeamType type) {
		this.type = type;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Transient
	public Long getTmpSize() {
		return tmpSize;
	}

	@Transient
	public List<TeamDetail> getTmpDetailList() {
		return tmpDetailList;
	}

	@Transient
	public void setTmpDetailList(List<TeamDetail> tmpDetailList) {
		this.tmpDetailList = tmpDetailList;
	}

	@Transient
	public void setTmpSize(Long tmpSize) {
		this.tmpSize = tmpSize;
	}

	@Transient
	public String getTmpTeamLeaderFullName() {
		return tmpTeamLeaderFullName;
	}

	@Transient
	public void setTmpTeamLeaderFullName(String tmpTeamLeaderFullName) {
		this.tmpTeamLeaderFullName = tmpTeamLeaderFullName;
	}

	@Transient
	public Long getTmpTotalActiveAssignments() {
		return tmpTotalActiveAssignments;
	}

	@Transient
	public TeamDetail getTmpTeamLeader() {
		return tmpTeamLeader;
	}

}
