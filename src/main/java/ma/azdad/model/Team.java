package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	private TeamCategory category;
	private String type;
	private Boolean active = true;
	private String membersKey;

	private User user;
	private User teamLeader;

	private List<TeamFile> fileList = new ArrayList<>();
	private List<TeamHistory> historyList = new ArrayList<>();
	private List<TeamDetail> detailList = new ArrayList<>();
	private List<ProjectAssignment> assignmentList = new ArrayList<>();

	// tmp
	private Long tmpSize;
	private Long tmpTotalActiveAssignments;
	private List<TeamDetail> tmpDetailList = new ArrayList<>();

	public Team() {
		super();
	}

	public Team(User user) {
		super();
		this.user = user;
	}

	public Team(Integer id, String name, TeamCategory category, String type, Boolean active, Long tmpSize, Long tmpTotalActiveAssignments, String teamLeaderFullName, String teamLeaderPhoto) {
		super(id);
		this.name = name;
		this.category = category;
		this.type = type;
		this.active = active;
		this.tmpSize = tmpSize;
		this.tmpTotalActiveAssignments = tmpTotalActiveAssignments;
		this.setTeamLeaderFullName(teamLeaderFullName);
		this.setTeamLeaderPhoto(teamLeaderPhoto);
	}

	public void init() {
		for (TeamDetail detail : detailList) {
			detail.init();
			tmpDetailList.add(detail);
		}

	}

	@Override
	public boolean filter(String query) {
		return contains(name, query) || contains(getTeamLeaderFullName(), query);
	}

	public void calculateMembersKey() {
		membersKey = detailList.stream().map(i -> i.getUserUsername() + "," + i.getType().ordinal()).sorted().collect(Collectors.joining(";"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<TeamFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<TeamFile> fileList) {
		this.fileList = fileList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<TeamHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<TeamHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "team", cascade = CascadeType.ALL)
	public List<TeamDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<TeamDetail> detailList) {
		this.detailList = detailList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "team", cascade = CascadeType.ALL)
	public List<ProjectAssignment> getAssignmentList() {
		return assignmentList;
	}

	public void setAssignmentList(List<ProjectAssignment> assignmentList) {
		this.assignmentList = assignmentList;
	}

	@Column(columnDefinition = "TEXT")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Enumerated(EnumType.ORDINAL)
	public TeamCategory getCategory() {
		return category;
	}

	public void setCategory(TeamCategory category) {
		this.category = category;
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

	@ManyToOne(fetch = FetchType.EAGER)
	public User getTeamLeader() {
		return teamLeader;
	}

	public void setTeamLeader(User teamLeader) {
		this.teamLeader = teamLeader;
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
	public String getTeamLeaderFullName() {
		return teamLeader == null ? null : teamLeader.getFullName();
	}

	@Transient
	public void setTeamLeaderFullName(String teamLeaderFullName) {
		if (teamLeader == null)
			teamLeader = new User();
		teamLeader.setFullName(teamLeaderFullName);
	}

	@Transient
	public String getTeamLeaderPhoto() {
		return teamLeader == null ? null : teamLeader.getPhoto();
	}

	@Transient
	public void setTeamLeaderPhoto(String teamLeaderPhoto) {
		if (teamLeader == null)
			teamLeader = new User();
		teamLeader.setPhoto(teamLeaderPhoto);
	}

	@Transient
	public Long getTmpTotalActiveAssignments() {
		return tmpTotalActiveAssignments;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMembersKey() {
		return membersKey;
	}

	public void setMembersKey(String membersKey) {
		this.membersKey = membersKey;
	}

}
