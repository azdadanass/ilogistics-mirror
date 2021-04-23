package ma.azdad.service;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import ma.azdad.utils.App;

@Component
public class CacheService {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	RestTemplateService restTemplateService;

	@Value("${applicationCode}")
	private String applicationCode;

	@Autowired
	CacheManager cacheManager;

	public void evictAllCaches() {
		cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}

	public void evictCache(String prefix) {
		cacheManager.getCacheNames().stream().filter(i -> i.startsWith(prefix)).forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}

	public void evictCache(String... prefixTab) {
		Arrays.stream(prefixTab).forEach(this::evictCache);
	}

	@Async
	public void evictCacheOthers(String prefix) {
		Arrays.stream(App.values()).filter(i -> i.getCacheable() && !applicationCode.equals(i.getValue())).forEach(i -> restTemplateService.consumRest(i.getHttpLink() + "/rest/evictCache/" + prefix, String.class));
	}

}
