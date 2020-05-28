package ma.azdad.service;

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
		User u = repos.findOne(username);
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
		return repos.findOne(username);
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

	public List<User> findLightByLineManagerAndStatus(String lineManagerUsername, Boolean contractActive) {
		return repos.findLightByLineManagerAndStatus(lineManagerUsername, contractActive);
	}

	public List<User> findLightByLineManagerAndActive(String lineManagerUsername) {
		return findLightByLineManagerAndStatus(lineManagerUsername, true);
	}

	public List<User> findLightByStatus(Boolean contractActive) {
		return repos.findLightByStatus(contractActive);
	}

	@Cacheable("userService.findAsMap")
	public Map<String, User> findAsMap() {
		return repos.find().stream().collect(Collectors.toMap(i -> i.getUsername(), i -> i));
	}

	public Long countByUsername(String username) {
		return repos.countByUsername(username);
	}

	public List<User> findLightByCompany(User user) {
		if (user == null)
			return null;
		return findLightByCompany(user.getCompanyType(), user.getCustomerId(), user.getSupplierId(), user.getCompany());
	}

	public List<User> findLightByCompany(CompanyType companyType, Integer customerId, Integer supplierId, String company) {
		if (companyType != null)
			switch (companyType) {
			case CUSTOMER:
				return repos.findLightByCustomer(companyType, customerId);
			case SUPPLIER:
				return repos.findLightBySupplier(companyType, supplierId);
			case OTHER:
				return repos.findLightByCompany(companyType, company);
			}
		return null;
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
			repos.delete(username);
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
}
