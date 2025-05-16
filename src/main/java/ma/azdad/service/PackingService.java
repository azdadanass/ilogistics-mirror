package ma.azdad.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.Packing;
import ma.azdad.model.PackingDetail;
import ma.azdad.model.PartNumber;
import ma.azdad.repos.PackingRepos;
import ma.azdad.repos.PartNumberRepos;

@Component
public class PackingService extends GenericService<Integer, Packing, PackingRepos> {

	@Autowired
	PackingRepos packingRepos;

	@Autowired
	PartNumberRepos partNumberRepos;
	
	@Autowired
	PackingDetailService packingDetailService;

	@Override
	public Packing findOne(Integer id) {
		Packing packing = super.findOne(id);
		Hibernate.initialize(packing.getPartNumber());
		Hibernate.initialize(packing.getPartNumber().getUser());
		Hibernate.initialize(packing.getDetailList());
		return packing;
	}

	public Boolean isNameExists(Integer partNumberId, String name, Integer id) {
		return ObjectUtils.firstNonNull(id != null ? packingRepos.countByPartNumberAndName(partNumberId, name, id) : packingRepos.countByPartNumberAndName(partNumberId, name), 0l) > 0;
	}

	public List<Packing> findByPartNumber(Integer partNumberId) {
		return packingRepos.findByPartNumber(partNumberId);
	}

	public List<Packing> findByPartNumberAndActive(Integer partNumberId) {
		List<Packing> result = packingRepos.findByPartNumberAndActive(partNumberId);
		result.forEach(p -> initialize(p.getDetailList()));
		return result;
	}

	public void createPackingForPartNumberWithoutPackingList() {
		List<PartNumber> list = partNumberRepos.findWithoutPackingList();
		for (PartNumber partNumber : list) {
			System.out.println("pn --> " + partNumber.getId());
			Packing packing = new Packing();
			packing.setName("DEFAULT");
			PackingDetail packingDetail = new PackingDetail(partNumber);
			packingDetail.setType("Box");
			packing.addDetail(packingDetail);
			packing.calculateFields();
			partNumber.addPacking(packing);
			partNumberRepos.save(partNumber);
		}
	}

	public void updateActive(Integer id, Boolean active) {
		packingRepos.updateActive(id, active);
	}

	public Long countByPartNumber(Integer partNumberId) {
		return ObjectUtils.firstNonNull(packingRepos.countByPartNumber(partNumberId), 0l);
	}

	public void updateName(Integer id, String name) {
		packingRepos.updateName(id, name);
	}
	
	public Boolean hasSerialNumber(Integer id) {
		return packingDetailService.countByPackingAndHasSerialnumber(id)>0;
		
	}

}
