package ma.azdad.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Transporter;
import ma.azdad.repos.TransporterRepos;

@Component
@Transactional
public class TransporterService extends GenericService<Transporter> {
	
	
	@Autowired
	TransporterRepos transporterRepos;
	
	
	@Override
	public Transporter findOne(Integer id) {
		Transporter transporter = super.findOne(id);
		Hibernate.initialize(transporter.getFileList());
		Hibernate.initialize(transporter.getHistoryList());
		Hibernate.initialize(transporter.getVehicleList());
		Hibernate.initialize(transporter.getSupplier());
		Hibernate.initialize(transporter.getDriverList());
		return transporter;
	}
	
	
	public List<Transporter> findLight(){
		return transporterRepos.findLight();
	}

}

