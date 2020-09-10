package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobRequest extends GenericBeanOld implements Serializable {

	private Boolean important = false;
	private Priority priority;
	private JobRequestStatus status = JobRequestStatus.EDITED;
	private Date plannedStartDate;
	private Date plannedCompletionDate;
	private Date plannedAcceptanceDate;
	private Date requestDate;
	private String requestFrom;
	private String description;
	private String smsRef;
	private String rejectionReason;
	private Boolean internalAssignment = true;
	private Boolean needAcceptance = false;

	private Project project;
	private Site site;
	private User requester;
	private User externalRequester;
	private User externalAssignor;
	private Team team;

	private List<JobRequestDeliveryDetail> deliveryDetailList = new ArrayList<>();

	// TIMELINE
	private Date date1; // Edited
	private Date date2; // Requested
	private Date date3; // Approved
	private Date date4; // Assigned
	private Date date5; // Accepted
	private Date date6; // Started
	private Date date7; // Completed
	private Date date8; // Validated
	private Date date9; // Rejected
	private Date date10; // Canceled

	private User user1;
	private User user2;
	private User user3;
	private User user4;
	private User user5;
	private User user6;
	private User user7;
	private User user8;
	private User user9;
	private User user10;

	// tmp
	private Double progress = 0.0;
	private Integer tmpProjectId;
	private String tmpProjectName;
	private Integer tmpSiteId;
	private String tmpSiteName;
	private String tmpRequesterPhoto;
	private String tmpRequesterFullName;
	private String tmpExternalRequesterUsername;
	private String tmpExternalRequesterFullName;
	private List<JobRequestDelivery> deliveryList;
	private Integer tmpCatalogId;
	private String tmpCatalogDetailCategory;
	private Long tmpTotalRaisedIssues;
	private Long tmpTotalRaisedAndBlockingIssues;

	public JobRequest() {
		super();
	}

	public JobRequest(Integer id, JobRequestStatus status, Date plannedStartDate, Date plannedCompletionDate, Date date7, String siteName, String siteGoogleAddress, Double siteLatitude, Double siteLongitude, Long tmpTotalRaisedIssues) {
		super(id);
		this.status = status;
		this.plannedStartDate = plannedStartDate;
		this.plannedCompletionDate = plannedCompletionDate;
		this.date7 = date7;
		this.site = new Site();
		this.site.setName(siteName);
		this.site.setGoogleAddress(siteGoogleAddress);
		this.site.setLatitude(siteLatitude);
		this.site.setLongitude(siteLongitude);
		this.tmpTotalRaisedIssues = tmpTotalRaisedIssues;
	}

	public JobRequest(Integer id, String smsRef, Boolean important, Priority priority, JobRequestStatus status, Date date6, Date date7, Date requestDate, Integer tmpProjectId, String tmpProjectName, String tmpSiteName, String tmpRequesterPhoto, String tmpRequesterFullName, Long tmpTotalRaisedIssues, Long tmpTotalRaisedAndBlockingIssues, String teamName) {
		super(id);
		this.smsRef = smsRef;
		this.important = important;
		this.priority = priority;
		this.status = status;
		this.date6 = date6;
		this.date7 = date7;
		this.requestDate = requestDate;
		this.tmpProjectId = tmpProjectId;
		this.tmpProjectName = tmpProjectName;
		this.tmpSiteName = tmpSiteName;
		this.tmpRequesterPhoto = tmpRequesterPhoto;
		this.tmpRequesterFullName = tmpRequesterFullName;
		this.tmpTotalRaisedIssues = tmpTotalRaisedIssues;
		this.tmpTotalRaisedAndBlockingIssues = tmpTotalRaisedAndBlockingIssues;
		this.setTeamName(teamName);
	}

	public void init() {
		if (project != null)
			tmpProjectId = project.getId();
		if (site != null)
			tmpSiteId = site.getId();
		if (externalRequester != null)
			tmpExternalRequesterUsername = externalRequester.getUsername();

	}

	public void initHistory() {
		date1 = null;
		date2 = null;
		date3 = null;
		date4 = null;
		date5 = null;
		date6 = null;
		date7 = null;
		date8 = null;

		user3 = null;
		user5 = null;
		user6 = null;
		user7 = null;
		user8 = null;
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && getReference() != null)
			result = getReference().toLowerCase().contains(query);
		if (!result && tmpSiteName != null)
			result = tmpSiteName.toLowerCase().contains(query);
		if (!result && tmpProjectName != null)
			result = tmpProjectName.toLowerCase().contains(query);
		if (!result && description != null)
			result = description.toLowerCase().contains(query);
		if (!result && project != null)
			result = project.getName().toLowerCase().contains(query);
		if (!result && site != null)
			result = site.getName().toLowerCase().contains(query);
		if (!result && requester != null)
			result = requester.getFullName().toLowerCase().contains(query);
		if (!result && externalRequester != null)
			result = externalRequester.getFullName().toLowerCase().contains(query);
		return result;
	}

	@Transient
	@JsonIgnore
	public Boolean getHasRaisedIssue() {
		if (tmpTotalRaisedIssues == null)
			return null;
		return tmpTotalRaisedIssues > 0;
	}

	@Transient
	@JsonIgnore
	public Boolean getHasRaisedAndBlockingIssue() {
		if (tmpTotalRaisedIssues == null)
			return null;
		return tmpTotalRaisedAndBlockingIssues > 0;
	}

	// @Transient
	// @JsonIgnore
	// public String getCustomerName() {
	// if (project == null)
	// return null;
	// return project.getCustomerName();
	// }
	//
	// @Transient
	// @JsonIgnore
	// public Integer getCustomerId() {
	// if (project == null)
	// return null;
	// return project.getCustomerId();
	// }
	//
	// @Transient
	// @JsonIgnore
	// public String getCompanyName() {
	// if (project == null)
	// return null;
	// return project.getCompanyName();
	// }

	@Transient
	@JsonIgnore
	public String getProjectName() {
		if (project != null)
			return project.getName();
		return null;
	}

	@Transient
	@JsonIgnore
	public void setProjectName(String name) {
		if (project == null)
			project = new Project();
		project.setName(name);
	}

	@Transient
	@JsonIgnore
	public String getTeamName() {
		if (team != null)
			return team.getName();
		return null;
	}

	@Transient
	@JsonIgnore
	public void setTeamName(String name) {
		if (team == null)
			team = new Team();
		team.setName(name);
	}

	@Transient
	@JsonIgnore
	public Integer getProjectId() {
		if (project != null)
			return project.getId();
		return null;
	}

	@Transient
	@JsonIgnore
	public void setProjectId(Integer projectId) {
		if (project == null)
			project = new Project();
		project.setId(projectId);
	}

	@Transient
	@JsonIgnore
	public Integer getTeamId() {
		if (team != null)
			return team.getId();
		return null;
	}

	@Transient
	@JsonIgnore
	public String getSiteName() {
		if (site != null)
			return site.getName();
		return null;
	}

	@Transient
	@JsonIgnore
	public void setSiteName(String name) {
		if (site == null)
			site = new Site();
		site.setName(name);
	}

	@Transient
	@JsonIgnore
	public String getRequesterFullName() {
		if (requester != null)
			return requester.getFullName();
		return null;
	}

	@Transient
	@JsonIgnore
	public String getExternalRequesterFullName() {
		if (externalRequester != null)
			return externalRequester.getFullName();
		return null;
	}

	@Transient
	@JsonIgnore
	public String getReference() {
		return "JR" + getNumero();
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	public JobRequestStatus getStatus() {
		return status;
	}

	public void setStatus(JobRequestStatus status) {
		this.status = status;
	}

	public Boolean getImportant() {
		return important;
	}

	public void setImportant(Boolean important) {
		this.important = important;
	}

	@Enumerated(EnumType.ORDINAL)
	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public Date getPlannedCompletionDate() {
		return plannedCompletionDate;
	}

	public void setPlannedCompletionDate(Date plannedCompletionDate) {
		this.plannedCompletionDate = plannedCompletionDate;
	}

	public Date getPlannedAcceptanceDate() {
		return plannedAcceptanceDate;
	}

	public void setPlannedAcceptanceDate(Date plannedAcceptanceDate) {
		this.plannedAcceptanceDate = plannedAcceptanceDate;
	}

	@Column(columnDefinition = "TEXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSmsRef() {
		return smsRef;
	}

	public void setSmsRef(String smsRef) {
		this.smsRef = smsRef;
	}

	@Column(columnDefinition = "TEXT")
	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public Boolean getInternalAssignment() {
		return internalAssignment;
	}

	public void setInternalAssignment(Boolean internalAssignment) {
		this.internalAssignment = internalAssignment;
	}

	@Temporal(TemporalType.DATE)
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getRequestFrom() {
		return requestFrom;
	}

	public void setRequestFrom(String requestFrom) {
		this.requestFrom = requestFrom;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public User getRequester() {
		return requester;
	}

	public void setRequester(User requester) {
		this.requester = requester;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getExternalRequester() {
		return externalRequester;
	}

	public void setExternalRequester(User externalRequester) {
		this.externalRequester = externalRequester;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getExternalAssignor() {
		return externalAssignor;
	}

	public void setExternalAssignor(User externalAssignor) {
		this.externalAssignor = externalAssignor;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "jobRequest", cascade = CascadeType.ALL)
	public List<JobRequestDeliveryDetail> getDeliveryDetailList() {
		return deliveryDetailList;
	}

	public void setDeliveryDetailList(List<JobRequestDeliveryDetail> deliveryDetailList) {
		this.deliveryDetailList = deliveryDetailList;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public Date getDate3() {
		return date3;
	}

	public void setDate3(Date date3) {
		this.date3 = date3;
	}

	public Date getDate4() {
		return date4;
	}

	public void setDate4(Date date4) {
		this.date4 = date4;
	}

	public Date getDate5() {
		return date5;
	}

	public void setDate5(Date date5) {
		this.date5 = date5;
	}

	public Date getDate6() {
		return date6;
	}

	public void setDate6(Date date6) {
		this.date6 = date6;
	}

	public Date getDate7() {
		return date7;
	}

	public void setDate7(Date date7) {
		this.date7 = date7;
	}

	public Date getDate8() {
		return date8;
	}

	public void setDate8(Date date8) {
		this.date8 = date8;
	}

	public Date getDate9() {
		return date9;
	}

	public void setDate9(Date date9) {
		this.date9 = date9;
	}

	public Date getDate10() {
		return date10;
	}

	public void setDate10(Date date10) {
		this.date10 = date10;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser3() {
		return user3;
	}

	public void setUser3(User user3) {
		this.user3 = user3;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser4() {
		return user4;
	}

	public void setUser4(User user4) {
		this.user4 = user4;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser5() {
		return user5;
	}

	public void setUser5(User user5) {
		this.user5 = user5;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser6() {
		return user6;
	}

	public void setUser6(User user6) {
		this.user6 = user6;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser7() {
		return user7;
	}

	public void setUser7(User user7) {
		this.user7 = user7;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser8() {
		return user8;
	}

	public void setUser8(User user8) {
		this.user8 = user8;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser9() {
		return user9;
	}

	public void setUser9(User user9) {
		this.user9 = user9;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser10() {
		return user10;
	}

	public void setUser10(User user10) {
		this.user10 = user10;
	}

	public Boolean getNeedAcceptance() {
		return needAcceptance;
	}

	public void setNeedAcceptance(Boolean needAcceptance) {
		this.needAcceptance = needAcceptance;
	}

	@Transient
	public String getTmpProjectName() {
		return tmpProjectName;
	}

	@Transient
	public void setTmpProjectName(String tmpProjectName) {
		this.tmpProjectName = tmpProjectName;
	}

	@Transient
	public String getTmpSiteName() {
		return tmpSiteName;
	}

	@Transient
	public void setTmpSiteName(String tmpSiteName) {
		this.tmpSiteName = tmpSiteName;
	}

	@Transient
	public String getTmpRequesterPhoto() {
		return tmpRequesterPhoto;
	}

	@Transient
	public void setTmpRequesterPhoto(String tmpRequesterPhoto) {
		this.tmpRequesterPhoto = tmpRequesterPhoto;
	}

	@Transient
	public String getTmpRequesterFullName() {
		return tmpRequesterFullName;
	}

	@Transient
	public void setTmpRequesterFullName(String tmpRequesterFullName) {
		this.tmpRequesterFullName = tmpRequesterFullName;
	}

	@Transient
	public String getTmpExternalRequesterFullName() {
		return tmpExternalRequesterFullName;
	}

	@Transient
	public void setTmpExternalRequesterFullName(String tmpExternalRequesterFullName) {
		this.tmpExternalRequesterFullName = tmpExternalRequesterFullName;
	}

	@Transient
	public Integer getTmpProjectId() {
		return tmpProjectId;
	}

	@Transient
	public void setTmpProjectId(Integer tmpProjectId) {
		this.tmpProjectId = tmpProjectId;
	}

	@Transient
	public Integer getTmpSiteId() {
		return tmpSiteId;
	}

	@Transient
	public void setTmpSiteId(Integer tmpSiteId) {
		this.tmpSiteId = tmpSiteId;
	}

	@Transient
	public String getTmpExternalRequesterUsername() {
		return tmpExternalRequesterUsername;
	}

	@Transient
	public void setTmpExternalRequesterUsername(String tmpExternalRequesterUsername) {
		this.tmpExternalRequesterUsername = tmpExternalRequesterUsername;
	}

	@Transient
	public List<JobRequestDelivery> getDeliveryList() {
		return deliveryList;
	}

	@Transient
	public void setDeliveryList(List<JobRequestDelivery> deliveryList) {
		this.deliveryList = deliveryList;
	}

	@Transient
	public Integer getTmpCatalogId() {
		return tmpCatalogId;
	}

	@Transient
	@JsonIgnore
	public void setTmpCatalogId(Integer tmpCatalogId) {
		this.tmpCatalogId = tmpCatalogId;
	}

	@Transient
	public String getTmpCatalogDetailCategory() {
		return tmpCatalogDetailCategory;
	}

	@Transient
	public void setTmpCatalogDetailCategory(String tmpCatalogDetailCategory) {
		this.tmpCatalogDetailCategory = tmpCatalogDetailCategory;
	}

	@Transient
	public Double getProgress() {
		return progress;
	}

	@Transient
	public void setProgress(Double progress) {
		this.progress = progress;
	}

	@Transient
	public Long getTmpTotalRaisedIssues() {
		return tmpTotalRaisedIssues;
	}

	@Transient
	public void setTmpTotalRaisedIssues(Long tmpTotalRaisedIssues) {
		this.tmpTotalRaisedIssues = tmpTotalRaisedIssues;
	}

	@Transient
	public Long getTmpTotalRaisedAndBlockingIssues() {
		return tmpTotalRaisedAndBlockingIssues;
	}

	@Transient
	public void setTmpTotalRaisedAndBlockingIssues(Long tmpTotalRaisedAndBlockingIssues) {
		this.tmpTotalRaisedAndBlockingIssues = tmpTotalRaisedAndBlockingIssues;
	}

}
