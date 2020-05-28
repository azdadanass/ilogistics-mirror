package ma.azdad.service;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheService {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CacheManager cacheManager;
	
	@Autowired
	RestTemplateService restTemplateService;
	
	@Value("${ibuyAddress}")
	private String ibuyAddress;
	
	@Value("${invoiceAddress}")
	private String invoiceAddress;

	public void evictAllCaches() {
		cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}

	public void evictCachePrefix(String prefix) {
		log.info("evictCachePrefix : " + prefix);
		cacheManager.getCacheNames().stream().filter(i -> i.startsWith(prefix)).forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}

	public void evictCache(String... names) {
		Arrays.stream(names).forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}
	
	public void evictCachePrefix(String erp, String prefix) {
		switch (erp) {
		case "ibuy":
			restTemplateService.consumRest(ibuyAddress + "/rest/cacheEvict/" + prefix, String.class);
			break;
		case "invoice":
			restTemplateService.consumRest(invoiceAddress + "/rest/cacheEvict/" + prefix, String.class);
			break;
		default:
			break;
		}
	}

}