package ma.azdad.repos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import ma.azdad.GenericTest;
import ma.azdad.rest.DeliveryRequestCostHistory;
import ma.azdad.service.DeliveryRequestDetailService;
import ma.azdad.service.PartNumberPricingService;
import ma.azdad.service.StockRowService;
import ma.azdad.utils.App;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	StockRowRepos stockRowRepos;

	@Autowired
	StockRowService stockRowService;

	@Autowired
	DeliveryRequestDetailService drds;

	@Autowired
	PartNumberPricingService pnps;

	@Test
	@Transactional
	public void test() throws Exception {

		for (DeliveryRequestCostHistory item : getCostHistory(2605, 1)) {
			System.out.println(item.getDeliveryRequestReference());
			System.out.println(item.getDate());

		}
	}

	public List<DeliveryRequestCostHistory> getCostHistory(Integer partNumberId, Integer companyId) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<DeliveryRequestCostHistory[]> response = restTemplate.getForEntity(App.ILOGISTICS.getIp() + "/rest/partNumberPricing/costHistory/" + partNumberId + "/" + companyId, DeliveryRequestCostHistory[].class);
			return Arrays.asList(response.getBody());
		} catch (Exception e) {
			return new ArrayList<DeliveryRequestCostHistory>();
		}
	}

}
