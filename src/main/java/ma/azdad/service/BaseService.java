package ma.azdad.service;

import org.hibernate.Hibernate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Base;

@Component
@Transactional
public class BaseService extends GenericService<Base> {

	@Override
	public Base findOne(Integer id) {
		Base base = super.findOne(id);
		Hibernate.initialize(base.getFileList());
		Hibernate.initialize(base.getHistoryList());
		return base;
	}

	@Async
	public void testAsync(Integer i) {
		for (int j = 0; j < 100; j++) {
			System.out.println(i);
			try {
				Thread.sleep(1 * 1000);
			} catch (InterruptedException e) {
			}
		}

	}

}
