package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.PackingDetail;
import ma.azdad.repos.PackingDetailRepos;

@Component
public class PackingDetailService extends GenericService<Integer, PackingDetail, PackingDetailRepos> {

	@Autowired
	PackingDetailRepos packingDetailRepos;

	@Override
	public PackingDetail findOne(Integer id) {
		PackingDetail packingDetail = super.findOne(id);
		// Hibernate.initialize(packingDetail.get..);
		return packingDetail;
	}

	public List<PackingDetail> findByPartNumber(Integer partNumberId) {
		return packingDetailRepos.findByPartNumber(partNumberId);
	}

}
