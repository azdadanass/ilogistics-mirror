package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.IbuyPayment;
import ma.azdad.repos.IbuyPaymentRepos;

@Component
public class IbuyPaymentService extends GenericService<Integer, IbuyPayment, IbuyPaymentRepos> {

	@Cacheable("ibuyPaymentService.findAll")
	public List<IbuyPayment> findAll() {
		return repos.findAll();
	}

	public IbuyPayment findOne(Integer id) {
		IbuyPayment ibuyPayment = super.findOne(id);

		return ibuyPayment;
	}

	@Cacheable("ibuyPaymentService.findByPo")
	public List<IbuyPayment> findByPo(Integer poId) {
		return repos.findByPo(poId);
	}

}
