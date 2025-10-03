package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.ZoneHeight;
import ma.azdad.repos.ZoneHeightRepos;

@Component
public class ZoneHeightService extends GenericService<Integer, ZoneHeight, ZoneHeightRepos> {

	@Cacheable("zoneHeightService.findAll")
	public List<ZoneHeight> findAll() {
		return repos.findAll();
	}

	public ZoneHeight findOne(Integer id) {
		ZoneHeight zoneHeight = super.findOne(id);
		return zoneHeight;
	}

	public List<ZoneHeight> findByLocation(Integer locationId) {
		return repos.findByLocation(locationId);
	}

}
