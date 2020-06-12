package ma.azdad.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.CompanyType;
import ma.azdad.model.Role;
import ma.azdad.model.User;
import ma.azdad.repos.UserRepos;

@Component
@Transactional
public class UserService {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepos repos;

	@Autowired
	private CacheService cacheService;

	public User findOne(String username) {
		User u = repos.findById(username).get();
		Hibernate.initialize(u.getUserData());
		Hibernate.initialize(u.getRoleList());
		Hibernate.initialize(u.getFileList());
		Hibernate.initialize(u.getHistoryList());
		return u;
	}

	public User findByLogin(String login) {
		User u = repos.findByLogin(login);
		Hibernate.initialize(u.getRoleList());
		return u;
	}

	public User findOneNullable(String username) {
		if (username == null)
			return null;
		return repos.findById(username).get();
	}

	public User save(User user) {
		return repos.save(user);
	}

	public void updatePassword(String username, String password) {
		repos.updatePassword(username, password);
	}

	public List<User> findLight() {
		return repos.findLight();
	}

	public List<User> findLightByRole(Role role) {
		return repos.findLightByRole(role);
	}

	public List<User> findWarehouseManagerList() {
		return findLightByRole(Role.ROLE_ILOGISTICS_WM);
	}

	public List<User> findLightByLineManagerAndStatus(String lineManagerUsername, Boolean active) {
		return repos.findLightByLineManagerAndStatus(lineManagerUsername, active);
	}

	public List<User> findLightByLineManagerAndActive(String lineManagerUsername) {
		return findLightByLineManagerAndStatus(lineManagerUsername, true);
	}

	public List<User> findLightByStatus(Boolean active) {
		return repos.findLightByStatus(active);
	}

	@Cacheable("userService.findAsMap")
	public Map<String, User> findAsMap() {
		return repos.find().stream().collect(Collectors.toMap(i -> i.getUsername(), i -> i));
	}

	public Long countByUsername(String username) {
		return repos.countByUsername(username);
	}

	public List<User> findLightByExternalCompany(User user) {
		if (user == null)
			return null;
		return findLightByExternalCompany(user.getCompanyType(), user.getCustomerId(), user.getSupplierId());
	}

	public List<User> findLightByExternalCompany(CompanyType companyType, Integer customerId, Integer supplierId) {
		if (companyType != null)
			switch (companyType) {
			case CUSTOMER:
				return repos.findLightByCustomer(companyType, customerId);
			case SUPPLIER:
				return repos.findLightBySupplier(companyType, supplierId);
			default:
				break;
			}
		return new ArrayList<User>();
	}

	public List<User> findActiveByCustomerOrSupplier(Integer customerId, Integer supplierId) {
		return repos.findByCustomerOrSupplier(customerId, supplierId, true);
	}

	public Long countByCin(String cin, String username) {
		Long l = username == null ? repos.countByCin(cin) : repos.countByCin(cin, username);
		return l != null ? l : 0;
	}

	public Boolean isCinExists(User user) {
		return countByCin(user.getCin(), user.getUsername()) > 0;
	}

	public Long countByEmail(String email, String username) {
		Long l = username == null ? repos.countByEmail(email) : repos.countByEmail(email, username);
		return l != null ? l : 0;
	}

	public Boolean isEmailExists(User user) {
		return countByEmail(user.getEmail(), user.getUsername()) > 0;
	}

	public Long countByFirstNameAndLastName(String firstName, String lastName, String username) {
		Long l = username == null ? repos.countByFirstNameAndLastName(firstName, lastName) : repos.countByFirstNameAndLastName(firstName, lastName, username);
		return l != null ? l : 0;
	}

	public Boolean isFirstNameAndLastNameExists(User user) {
		return countByFirstNameAndLastName(user.getFirstName(), user.getLastName(), user.getUsername()) > 0;
	}

	public Long countByPhone(String phone, String username) {
		Long l = username == null ? repos.countByPhone(phone) : repos.countByPhone(phone, username);
		return l != null ? l : 0;
	}

	public Boolean isPhoneExists(User user) {
		return countByPhone(user.getPhone(), user.getUsername()) > 0;
	}

	public List<User> findByJob(String job) {
		return repos.findByJob(job);
	}

	public List<User> findLightByTransporter(Integer transporterId) {
		return repos.findLightByTransporter(transporterId);
	}

	public List<User> findLight(String username) {
		return repos.findLight(username);
	}

	@Cacheable("userService.findLight")
	public List<User> findLight(Boolean internal) {
		return repos.findLight(internal);
	}

	@Cacheable("userService.findLightByUser")
	public List<User> findLightByUser(Boolean internal, String userUsername) {
		return repos.findLightByUser(internal, userUsername);
	}

	public Integer findCustomerId(Integer id) {
		return repos.findCustomerId(id);
	}

	public List<User> findByCustomer(Integer customerId) {
		return repos.findByCustomer(customerId);
	}

	public List<User> findBySupplier(Integer supplierId) {
		return repos.findBySupplier(supplierId);
	}

	public List<User> findByCompany(String company) {
		return repos.findByCompany(company);
	}

	public List<User> findLightByProject(Integer projectId) {
		return repos.findLightByProject(projectId);
	}

	public void flush() {
		repos.flush();
	}

	public User saveAndRefresh(User user) {
		save(user);
		return findOne(user.getUsername());
	}

	public void delete(String username) {
		cacheEvict();
		try {
			repos.deleteById(username);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			log.error(dataIntegrityViolationException.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void delete(User a) {
		cacheEvict();
		delete(a.getUsername());
	}

	public void cacheEvict() {
		cacheService.evictCachePrefix("userService");
	}

	@Cacheable("userService.find")
	public List<User> find(Boolean internal) {
		return repos.findByInternal(internal);
	}

	@Cacheable("userService.findByCustomerOrSupplierAndHavingAssignement")
	public List<User> findByCustomerOrSupplierAndHavingAssignement(Integer customerId, Integer supplierId, Integer projectId) {
		return repos.findByCustomerOrSupplierAndHavingAssignement(customerId, supplierId, projectId);
	}

	public User findByEmail(String email) {
		return repos.findByEmail(email);
	}

	public User findByPhone(String phone) {
		return repos.findByPhone(phone);
	}

	public List<User> findLight(Boolean internal, Boolean active) {
		return repos.findLight(internal, active);
	}

	public List<User> findByProjectAssignmentAndUserRoleAndInternal(Integer projectId) {
		return repos.findByProjectAssignmentAndUserRole(projectId, Role.ROLE_ILOGISTICS_USER, true);
	}

	public List<User> findByProjectAssignment(Integer projectId, Boolean internal) {
		return repos.findByProjectAssignment(projectId, internal);
	}

	public List<User> findByProjectDelegation(Integer projectId, Boolean internal) {
		return repos.findByProjectDelegation(projectId, internal);
	}

	public User findLobManagerByDeliveryRequest(Integer deliveryRequestId) {
		return repos.findLobManagerByDeliveryRequest(deliveryRequestId);
	}

}
