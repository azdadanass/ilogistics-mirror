package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.PackingDetailType;
import ma.azdad.model.PartNumberClass;
import ma.azdad.repos.PackingDetailTypeRepos;

@Component
public class PackingDetailTypeService extends GenericService<Integer, PackingDetailType, PackingDetailTypeRepos> {

	@Cacheable("packingDetailTypeService.findAll")
	public List<PackingDetailType> findAll() {
		return repos.findAll();
	}

	@Cacheable("packingDetailTypeService.find")
	public List<PackingDetailType> find() {
		return repos.find();
	}

	@Cacheable("packingDetailTypeService.findNameListByClassAndActive")
	public List<String> findNameListByClassAndActive(PartNumberClass partNumberClass) {
		return repos.findNameListByClassAndActive(partNumberClass);
	}

	public PackingDetailType findOne(Integer id) {
		PackingDetailType packingDetailType = super.findOne(id);
		return packingDetailType;
	}

	public Long countByNameAndClass(String name, PartNumberClass partNumberClass) {
		return repos.countByNameAndClass(name, partNumberClass);
	}

	@Cacheable("packingDetailTypeService.findImageByNameAndClass")
	public String findImageByNameAndClass(String name, PartNumberClass partNumberClass) {
		return repos.findImageByNameAndClass(name, partNumberClass);
	}

}
