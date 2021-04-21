package ma.azdad.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.ToNotify;
import ma.azdad.repos.ToNotifyRepos;

@Component
public class ToNotifyService extends GenericService<Integer, ToNotify, ToNotifyRepos> {

	@Autowired
	ToNotifyRepos toNotifyRepos;

	@Override
	public ToNotify findOne(Integer id) {
		ToNotify toNotify = super.findOne(id);
		return toNotify;
	}

	public List<ToNotify> findByUser(String username) {
		List<ToNotify> result = toNotifyRepos.findByUser(username);
		result.stream().filter(i -> !i.getInternalResource().getInternal()).forEach(i -> {
			Hibernate.initialize(i.getUser().getSupplier());
			Hibernate.initialize(i.getUser().getCustomer());
		});
		return result;
	}

	public Long countByUserAndInternalResource(String username, String internalResourceUsername) {
		return Optional.ofNullable(toNotifyRepos.countByUserAndInternalResource(username, internalResourceUsername)).orElse(0l);
	}

}
