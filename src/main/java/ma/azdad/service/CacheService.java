package ma.azdad.service;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import ma.azdad.model.App;

@Component
public class CacheService {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CacheManager cacheManager;

	@Autowired
	RestTemplateService restTemplateService;

	@Value("${applicationCode}")
	private String applicationCode;

	public void evictAllCaches() {
		cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}

	public void evictCachePrefixLocal(String prefix) {
		log.info("evictCachePrefix : " + prefix);
		cacheManager.getCacheNames().stream().filter(i -> i.startsWith(prefix)).forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}

	public void evictCachePrefix(String prefix) {
		// evict local
		evictCachePrefixLocal(prefix);
		// evict on other apps
		Arrays.stream(App.values()).filter(i -> !applicationCode.equals(i.getValue())).forEach(i -> restTemplateService.consumRest(i.getLink() + "/rest/cacheEvict/" + prefix, String.class));
	}

	public void evictCache(String... names) {
		Arrays.stream(names).forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}

}
