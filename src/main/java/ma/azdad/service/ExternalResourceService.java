package ma.azdad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.ExternalResource;
import ma.azdad.repos.AffectationRepos;
import ma.azdad.repos.ExternalResourceRepos;
import ma.azdad.repos.UserDataRepos;
import ma.azdad.repos.UserRepos;

@Component
@Transactional
public class ExternalResourceService extends GenericServiceOld<ExternalResource> {

	@Autowired
	ExternalResourceRepos externalResourceRepos;

	@Autowired
	UserRepos userRepos;

	@Autowired
	UserDataRepos userDataRepos;

	@Autowired
	AffectationRepos affectationRepos;

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
//	public List<ExternalResource> findLightByExternalCompany(ExternalResource externalResource) {
//		return findLightByExternalCompany(externalResource.getCompanyType(), externalResource.getCustomerId(), externalResource.getSupplierId(), externalResource.getCompany());
//	}
//
//	public List<ExternalResource> findLightByExternalCompany(CompanyType companyType, Integer customerId, Integer supplierId, String company) {
//		if (companyType != null)
//			switch (companyType) {
//			case CUSTOMER:
//				return externalResourceRepos.findLightByCustomer(companyType, customerId);
//			case SUPPLIER:
//				return externalResourceRepos.findLightBySupplier(companyType, supplierId);
//			case OTHER:
//				return externalResourceRepos.findLightByExternalCompany(companyType, company);
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
