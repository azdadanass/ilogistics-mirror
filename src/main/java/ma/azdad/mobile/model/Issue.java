package ma.azdad.mobile.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ma.azdad.mobile.ToNotify;
import ma.azdad.model.IssueCategory;
import ma.azdad.model.IssueStatus;
import ma.azdad.model.IssueType;
import ma.azdad.model.Severity;
import ma.azdad.model.User;
import ma.azdad.utils.Public;

public class Issue {
	
	
	private Integer id;
	private String jrRef;
	private Integer jrId;
	private String jrProject;
	private String jrSite;
	private Severity severity;
	private String severityValue;
	private IssueStatus status = IssueStatus.RAISED;
	private String category;
	private String type;
	private Boolean blocking;
	private Boolean permanent;
	private String description;
	private String user1Photo;
	private String comment;

	
    private String endCustomer;
    private Date resolutionDate;
	private String resolutionType;
	private String siteName;
	private Double siteLatitude;
	private Double siteLongitude;
	private String siteGoogleAdress;
	private String toUser;
	private String toUserEmail;
	private String toUserPhone;
	private String toUserCin;
	private String toUserPhoto;
	private String toCompany;
	private String toCompanyLogo;
	
	//ownership
	private String confirmationOwnershipType;
	private ma.azdad.mobile.model.User confirmationOwnershipUser;
	private ma.azdad.mobile.model.IssueSupplier confirmationOwnershipSupplier;
	
	private String assignOwnershipType;
	private ma.azdad.mobile.model.User assignOwnershipUser;
	private ma.azdad.mobile.model.IssueSupplier assignOwnershipSupplier;

	
	
	private Date date1; // raised
	private Date date8; // submitted
	private Date date2; // confirmed
	private Date date3; // assigned
	private Date date4; // resolved
	private Date date5; // ack
	private Date date6; // not resolved
	private Date date7; // closed

	private ma.azdad.mobile.model.User user1;
	private ma.azdad.mobile.model.User user8;
	private ma.azdad.mobile.model.User user2;
	private ma.azdad.mobile.model.User user3;
	private ma.azdad.mobile.model.User user4;
	private ma.azdad.mobile.model.User user5;
	private ma.azdad.mobile.model.User user6;
	private ma.azdad.mobile.model.User user7;
	
	private List<ToNotify> toNotifyList = new ArrayList<>();
	
	
	
	public Issue() {
		super();
	}
	public Issue(Integer id, Severity severity, IssueStatus status, String category, String type, Boolean blocking,
			Boolean permanent, String description,String jrRef,String jrProject,String jrSite,String user1Photo) {
		super();
		this.id = id;
		this.severity = severity;
		this.status = status;
		this.category = category;
		this.type = type;
		this.blocking = blocking;
		this.permanent = permanent;
		this.description = description;
		this.jrRef = jrRef;
		this.jrProject = jrProject;
		this.jrSite = jrSite;
		this.user1Photo = Public.getPublicUrl(user1Photo);
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Severity getSeverity() {
		return severity;
	}
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	public IssueStatus getStatus() {
		return status;
	}
	public void setStatus(IssueStatus status) {
		this.status = status;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getBlocking() {
		return blocking;
	}
	public void setBlocking(Boolean blocking) {
		this.blocking = blocking;
	}
	public Boolean getPermanent() {
		return permanent;
	}
	public void setPermanent(Boolean permanent) {
		this.permanent = permanent;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getJrRef() {
		return jrRef;
	}
	public void setJrRef(String jrRef) {
		this.jrRef = jrRef;
	}
	public String getJrProject() {
		return jrProject;
	}
	public void setJrProject(String jrProject) {
		this.jrProject = jrProject;
	}
	public String getJrSite() {
		return jrSite;
	}
	public void setJrSite(String jrSite) {
		this.jrSite = jrSite;
	}
	public Date getDate1() {
		return date1;
	}
	public void setDate1(Date date1) {
		this.date1 = date1;
	}
	public Date getDate8() {
		return date8;
	}
	public void setDate8(Date date8) {
		this.date8 = date8;
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
	public ma.azdad.mobile.model.User getUser1() {
		return user1;
	}
	public void setUser1(ma.azdad.mobile.model.User user1) {
		this.user1 = user1;
	}
	public ma.azdad.mobile.model.User getUser8() {
		return user8;
	}
	public void setUser8(ma.azdad.mobile.model.User user8) {
		this.user8 = user8;
	}
	public ma.azdad.mobile.model.User getUser2() {
		return user2;
	}
	public void setUser2(ma.azdad.mobile.model.User user2) {
		this.user2 = user2;
	}
	public ma.azdad.mobile.model.User getUser3() {
		return user3;
	}
	public void setUser3(ma.azdad.mobile.model.User user3) {
		this.user3 = user3;
	}
	public ma.azdad.mobile.model.User getUser4() {
		return user4;
	}
	public void setUser4(ma.azdad.mobile.model.User user4) {
		this.user4 = user4;
	}
	public ma.azdad.mobile.model.User getUser5() {
		return user5;
	}
	public void setUser5(ma.azdad.mobile.model.User user5) {
		this.user5 = user5;
	}
	public ma.azdad.mobile.model.User getUser6() {
		return user6;
	}
	public void setUser6(ma.azdad.mobile.model.User user6) {
		this.user6 = user6;
	}
	public ma.azdad.mobile.model.User getUser7() {
		return user7;
	}
	public void setUser7(ma.azdad.mobile.model.User user7) {
		this.user7 = user7;
	}
	public String getUser1Photo() {
		return user1Photo;
	}
	public void setUser1Photo(String user1Photo) {
		this.user1Photo = user1Photo;
	}
	public String getEndCustomer() {
		return endCustomer;
	}
	public void setEndCustomer(String endCustomer) {
		this.endCustomer = endCustomer;
	}
	public Date getResolutionDate() {
		return resolutionDate;
	}
	public void setResolutionDate(Date resolutionDate) {
		this.resolutionDate = resolutionDate;
	}
	public String getResolutionType() {
		return resolutionType;
	}
	public void setResolutionType(String resolutionType) {
		this.resolutionType = resolutionType;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public Double getSiteLatitude() {
		return siteLatitude;
	}
	public void setSiteLatitude(Double siteLatitude) {
		this.siteLatitude = siteLatitude;
	}
	public Double getSiteLongitude() {
		return siteLongitude;
	}
	public void setSiteLongitude(Double siteLongitude) {
		this.siteLongitude = siteLongitude;
	}
	public String getSiteGoogleAdress() {
		return siteGoogleAdress;
	}
	public void setSiteGoogleAdress(String siteGoogleAdress) {
		this.siteGoogleAdress = siteGoogleAdress;
	}
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	public String getToUserEmail() {
		return toUserEmail;
	}
	public void setToUserEmail(String toUserEmail) {
		this.toUserEmail = toUserEmail;
	}
	public String getToUserPhone() {
		return toUserPhone;
	}
	public void setToUserPhone(String toUserPhone) {
		this.toUserPhone = toUserPhone;
	}
	public String getToUserCin() {
		return toUserCin;
	}
	public void setToUserCin(String toUserCin) {
		this.toUserCin = toUserCin;
	}
	public String getToUserPhoto() {
		return toUserPhoto;
	}
	public void setToUserPhoto(String toUserPhoto) {
		this.toUserPhoto = toUserPhoto;
	}
	public String getToCompany() {
		return toCompany;
	}
	public void setToCompany(String toCompany) {
		this.toCompany = toCompany;
	}
	public String getToCompanyLogo() {
		return toCompanyLogo;
	}
	public void setToCompanyLogo(String toCompanyLogo) {
		this.toCompanyLogo = toCompanyLogo;
	}
	public String getSeverityValue() {
		return severityValue;
	}
	public void setSeverityValue(String severityValue) {
		this.severityValue = severityValue;
	}
	public String getConfirmationOwnershipType() {
		return confirmationOwnershipType;
	}
	public void setConfirmationOwnershipType(String confirmationOwnershipType) {
		this.confirmationOwnershipType = confirmationOwnershipType;
	}
	public ma.azdad.mobile.model.User getConfirmationOwnershipUser() {
		return confirmationOwnershipUser;
	}
	public void setConfirmationOwnershipUser(ma.azdad.mobile.model.User confirmationOwnershipUser) {
		this.confirmationOwnershipUser = confirmationOwnershipUser;
	}
	public ma.azdad.mobile.model.IssueSupplier getConfirmationOwnershipSupplier() {
		return confirmationOwnershipSupplier;
	}
	public void setConfirmationOwnershipSupplier(ma.azdad.mobile.model.IssueSupplier confirmationOwnershipSupplier) {
		this.confirmationOwnershipSupplier = confirmationOwnershipSupplier;
	}
	public String getAssignOwnershipType() {
		return assignOwnershipType;
	}
	public void setAssignOwnershipType(String assignOwnershipType) {
		this.assignOwnershipType = assignOwnershipType;
	}
	public ma.azdad.mobile.model.User getAssignOwnershipUser() {
		return assignOwnershipUser;
	}
	public void setAssignOwnershipUser(ma.azdad.mobile.model.User assignOwnershipUser) {
		this.assignOwnershipUser = assignOwnershipUser;
	}
	public ma.azdad.mobile.model.IssueSupplier getAssignOwnershipSupplier() {
		return assignOwnershipSupplier;
	}
	public void setAssignOwnershipSupplier(ma.azdad.mobile.model.IssueSupplier assignOwnershipSupplier) {
		this.assignOwnershipSupplier = assignOwnershipSupplier;
	}
	public List<ToNotify> getToNotifyList() {
		return toNotifyList;
	}
	public void setToNotifyList(List<ToNotify> toNotifyList) {
		this.toNotifyList = toNotifyList;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getJrId() {
		return jrId;
	}
	public void setJrId(Integer jrId) {
		this.jrId = jrId;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	

}
