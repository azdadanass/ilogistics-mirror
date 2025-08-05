package ma.azdad.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.Transporter;
import ma.azdad.repos.TransporterRepos;

@Component
public class TransporterService extends GenericService<Integer, Transporter, TransporterRepos> {

	@Autowired
	TransporterRepos transporterRepos;

	@Override
	public Transporter findOne(Integer id) {
		Transporter transporter = super.findOne(id);
		Hibernate.initialize(transporter.getFileList());
		Hibernate.initialize(transporter.getHistoryList());
		Hibernate.initialize(transporter.getVehicleList());
		Hibernate.initialize(transporter.getSupplier());
		Hibernate.initialize(transporter.getCompany());
		transporter.getUserList().forEach(i->{
			initialize(i.getCompany());
			initialize(i.getCustomer());
			initialize(i.getSupplier());
		});
		return transporter;
	}

	public List<Transporter> findLight() {
		return transporterRepos.findLight();
	}

}
