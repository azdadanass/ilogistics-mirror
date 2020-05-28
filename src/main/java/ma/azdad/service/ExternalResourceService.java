package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Affectation;
import ma.azdad.model.Assignment;
import ma.azdad.model.AssignmentDetail;
import ma.azdad.model.ExternalResource;
import ma.azdad.model.ExternalResourceFile;
import ma.azdad.model.ExternalResourceHistory;
import ma.azdad.model.ExternalResourceProjectAssignment;
import ma.azdad.model.User;
import ma.azdad.model.UserData;
import ma.azdad.model.UserFile;
import ma.azdad.model.UserHistory;
import ma.azdad.repos.AffectationRepos;
import ma.azdad.repos.AssignmentRepos;
import ma.azdad.repos.ExternalResourceRepos;
import ma.azdad.repos.UserDataRepos;
import ma.azdad.repos.UserRepos;

@Component
@Transactional
public class ExternalResourceService extends GenericService<ExternalResource> {

	@Autowired
	ExternalResourceRepos externalResourceRepos;

	@Autowired
	UserRepos userRepos;

	@Autowired
	UserDataRepos userDataRepos;

	@Autowired
	AssignmentRepos assignmentRepos;

	@Autowired
	AffectationRepos affectationRepos;

	public void exportToUserScript() throws Exception {
		List<ExternalResource> list = externalResourceRepos.findAll();

		for (ExternalResource er : list) {
			String username = generateUsername(er.getFirstName(), er.getLastName());

			if (username == null)
				throw new Exception("generateUsername error " + er.getFullName());

			User user = new User();
			user.setInternal(false);
			user.setUsername(String.valueOf(er.getId()));
			user.setLogin(username);

			user.setFirstName(er.getFirstName());
			user.setLastName(er.getLastName());
			user.setFullName(er.getFullName());
			user.setJob(er.getJob());
			user.setCin(er.getCin());
			user.setEmail(er.getEmail());
			user.setEmail2(er.getEmail2());
			user.setPassword(er.getPassword());
			user.setActive(er.getActive());
			user.setPhoto(er.getPhoto());
			user.setPhone(er.getPhone());
			user.setPhone2(er.getPhone2());
			user.setGender(er.getGender());
			user.setCompanyType(er.getCompanyType());
			user.setCustomer(er.getCustomer());
			user.setSupplier(er.getSupplier());
			user.setCompany(er.getCompany());
			user.setTransporter(er.getTransporter());
			user.setUser(er.getUser());
			user.setBirthday(er.getBirthday());
			if (!er.getHistoryList().isEmpty())
				user.setDate(er.getHistoryList().get(0).getDate());

			for (ExternalResourceFile ef : er.getFileList()) {
				UserFile us = new UserFile();
				us.setDate(ef.getDate());
				us.setExtension(ef.getExtension());
				us.setType(ef.getType());
				us.setName(ef.getName());
				us.setUser(ef.getUser());
				us.setLink(ef.getLink());
				user.addFile(us);
			}

			for (ExternalResourceHistory eh : er.getHistoryList()) {
				UserHistory uh = new UserHistory();
				uh.setDate(eh.getDate());
				uh.setDescription(eh.getDescription());
				uh.setUser(eh.getUser());
				uh.setStatus(eh.getStatus());
				user.addHistory(uh);
			}

			UserData userData = new UserData();
			userData.setId(er.getId());
			userData.setPassportId(er.getPassportId());
			userData.setPassportExpireDate(er.getPassportExpireDate());
			userData.setDriveLicenceId(er.getDriveLicenceId());
			userData.setDriveLicenceType(er.getDriveLicenceType());
			userData.setDriveLicenceIssuedDate(er.getDriveLicenceIssuedDate());
			userData.setDriveLicenceExpireDate(er.getDriveLicenceExpireDate());
			userData.setBusinessPhone(er.getBusinessPhone());
			userData.setBusinessFax(er.getBusinessFax());
			userData.setHomeAddress(er.getHomeAddress());
			userData.setHomePhone(er.getHomePhone());
			userData.setEmergencyName1(er.getEmergencyName1());
			userData.setEmergencyPhone1(er.getEmergencyPhone1());
			userData.setEmergencyName2(er.getEmergencyName2());
			userData.setEmergencyPhone2(er.getEmergencyPhone2());
			userData.setSkypeId(er.getSkypeId());
			userData.setImId(er.getImId());
			userData.setDescription(er.getDescription());

			user.addUserData(userData);

			user = userRepos.save(user);

			for (ExternalResourceProjectAssignment ea : er.getAssignmentList()) {
				Assignment assignment = new Assignment();
				assignment.setUser(user);
				assignment.setCreationDate(ea.getStartDate());
				assignment.setStartDate(ea.getStartDate());
				assignment.setEndDate(ea.getEndDate());
				assignment.setAssignator(ea.getProject().getManager());
				AssignmentDetail assignmentDetail = new AssignmentDetail();
				assignmentDetail.setAssignment(assignment);
				assignmentDetail.setProject(ea.getProject());
				assignment.getDetailList().add(assignmentDetail);
				assignmentRepos.save(assignment);
			}

			Affectation affectation = new Affectation();
			affectation.setLineManager(user.getUser());
			affectation.setHrManager(userRepos.findOne("l.sbay"));
			affectation.setLogisticManager(userRepos.findOne("a.bassim"));
			affectation.setUser(user);
			affectationRepos.save(affectation);

		}

	}

	private String generateUsername(String firstName, String lastName) {
		firstName = firstName.replace(" ", "").toLowerCase();
		lastName = lastName.replace(" ", "").toLowerCase();
		String usernameSuffix = "." + lastName;
		String usernamePrefix = "";
		for (Character character : firstName.toCharArray()) {
			usernamePrefix += character;
			if (userRepos.countByUsername(usernamePrefix + usernameSuffix) == 0)
				return usernamePrefix + usernameSuffix;
		}
		return null;
	}

//	@Override
//	public ExternalResource findOne(Integer id) {
//		ExternalResource externalResource = super.findOne(id);
//		Hibernate.initialize(externalResource.getCustomer());
//		Hibernate.initialize(externalResource.getSupplier());
//		Hibernate.initialize(externalResource.getFileList());
//		Hibernate.initialize(externalResource.getHistoryList());
//		Hibernate.initialize(externalResource.getRoleList());
//		for (ExternalResourceProjectAssignment assignment : externalResource.getAssignmentList())
//			Hibernate.initialize(assignment.getProject());
//		return externalResource;
//	}
//
//	public ExternalResource findOneNullable(Integer id) {
//		if (id == null)
//			return null;
//		return repos.findOne(id);
//	}
//
//	@Cacheable("externalResourceService.findLight")
//	public List<ExternalResource> findLight() {
//		return externalResourceRepos.findLight();
//	}
//
//	public List<ExternalResource> findLightByCompany(ExternalResource externalResource) {
//		return findLightByCompany(externalResource.getCompanyType(), externalResource.getCustomerId(), externalResource.getSupplierId(), externalResource.getCompany());
//	}
//
//	public List<ExternalResource> findLightByCompany(CompanyType companyType, Integer customerId, Integer supplierId, String company) {
//		if (companyType != null)
//			switch (companyType) {
//			case CUSTOMER:
//				return externalResourceRepos.findLightByCustomer(companyType, customerId);
//			case SUPPLIER:
//				return externalResourceRepos.findLightBySupplier(companyType, supplierId);
//			case OTHER:
//				return externalResourceRepos.findLightByCompany(companyType, company);
//			}
//		return null;
//	}
//
//	public Long countByCin(String cin, Integer id) {
//		Long l = id == null ? externalResourceRepos.countByCin(cin) : externalResourceRepos.countByCin(cin, id);
//		return l != null ? l : 0;
//	}
//
//	public Boolean isCinExists(ExternalResource externalResource) {
//		return countByCin(externalResource.getCin(), externalResource.getId()) > 0;
//	}
//
//	public Long countByEmail(String email, Integer id) {
//		Long l = id == null ? externalResourceRepos.countByEmail(email) : externalResourceRepos.countByEmail(email, id);
//		return l != null ? l : 0;
//	}
//
//	public Boolean isEmailExists(ExternalResource externalResource) {
//		return countByEmail(externalResource.getEmail(), externalResource.getId()) > 0;
//	}
//
//	public Long countByFirstNameAndLastName(String firstName, String lastName, Integer id) {
//		Long l = id == null ? externalResourceRepos.countByFirstNameAndLastName(firstName, lastName) : externalResourceRepos.countByFirstNameAndLastName(firstName, lastName, id);
//		return l != null ? l : 0;
//	}
//
//	public Boolean isFirstNameAndLastNameExists(ExternalResource externalResource) {
//		return countByFirstNameAndLastName(externalResource.getFirstName(), externalResource.getLastName(), externalResource.getId()) > 0;
//	}
//
//	public Long countByPhone(String phone, Integer id) {
//		Long l = id == null ? externalResourceRepos.countByPhone(phone) : externalResourceRepos.countByPhone(phone, id);
//		return l != null ? l : 0;
//	}
//
//	public Boolean isPhoneExists(ExternalResource externalResource) {
//		return countByPhone(externalResource.getPhone(), externalResource.getId()) > 0;
//	}
//
//	public List<ExternalResource> findByJob(String job) {
//		return externalResourceRepos.findByJob(job);
//	}
//
//	public List<ExternalResource> findLightByTransporter(Integer transporterId) {
//		return externalResourceRepos.findLightByTransporter(transporterId);
//	}
//
//	public List<ExternalResource> findLight(String username) {
//		return externalResourceRepos.findLight(username);
//	}
//
//	public Integer findCustomerId(Integer id) {
//		return externalResourceRepos.findCustomerId(id);
//	}
//
//	public List<ExternalResource> findByCustomer(Integer customerId) {
//		return externalResourceRepos.findByCustomer(customerId);
//	}
//
//	public List<ExternalResource> findBySupplier(Integer supplierId) {
//		return externalResourceRepos.findBySupplier(supplierId);
//	}
//
//	public List<ExternalResource> findByCompany(String company) {
//		return externalResourceRepos.findByCompany(company);
//	}
//
//	public List<ExternalResource> findLightByProject(Integer projectId) {
//		return externalResourceRepos.findLightByProject(projectId);
//	}
//
//	public ExternalResource findOne(String email) {
//		return findOne(externalResourceRepos.findByEmail(email).getId());
//	}
//
//	@Cacheable("externalResourceService.findAsMap")
//	public Map<Integer, ExternalResource> findAsMap() {
//		return externalResourceRepos.findLight().stream().collect(Collectors.toMap(i -> i.getId(), i -> i));
//	}

}
