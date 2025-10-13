package ma.azdad.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Location;
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

	public List<Integer> findIdListByDeliveryRequest(Integer deliveryRequestId) {
		return repos.findIdListByDeliveryRequest(deliveryRequestId);
	}

	public void updateUsedVolumeAndFillPercentage(Integer id) {
		System.out.println("updateUsedVolumeAndFillPercentage : " + id);
		Double totalUsedVolume = repos.findTotalUsedVolume(id);
		Double slotSize = repos.findSlotSize(id);
		repos.updateUsedVolume(id, totalUsedVolume);
		repos.updateFillPercentage(id, totalUsedVolume / slotSize);
	}

	public String findReferenceById(Integer id) {
		return repos.findReferenceById(id);
	}

	public List<ZoneHeight> findAvailableByLocation(Location location, Map<Integer, Double> tmpUsedVolumeMap, Double volume) {
		List<ZoneHeight> result = new ArrayList<ZoneHeight>();
		List<ZoneHeight> list = repos.findByLocation(location.getId());

		System.out.println("map : " + tmpUsedVolumeMap);

		for (ZoneHeight zoneHeight : list) {
			Double usedVolume = zoneHeight.getUsedVolume();
			Double tmpUsedVolume = tmpUsedVolumeMap.getOrDefault(zoneHeight.getId(), 0.0);
			System.out.println("-------------------------------");
			System.out.println(zoneHeight.getReference());
			System.out.println("volume : " + volume);
			System.out.println("usedVolume : " + usedVolume);
			System.out.println("tmpUsedVolume : " + tmpUsedVolume);
			if (location.getSlotSize() >= usedVolume + tmpUsedVolume + volume)
				result.add(zoneHeight);
		}

		return result;
	}
}
