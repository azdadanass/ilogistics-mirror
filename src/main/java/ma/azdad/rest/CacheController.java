package ma.azdad.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.service.CacheService;

@RestController
public class CacheController {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CacheService cacheService;

	@GetMapping("/rest/cacheEvict")
	public void cacheEvict() {
		cacheService.evictAllCaches();
	}

	@GetMapping("/rest/cacheEvict/{cacheName}")
	public void cacheEvict(@PathVariable String cacheName) {
		cacheService.evictCachePrefix(cacheName);
	}

}
