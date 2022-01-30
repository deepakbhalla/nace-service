package com.nace.config;

import java.util.Objects;

import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nace.constants.NaceDetailsConstants;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

/**
 * EhCache Configuration.
 * 
 * @author Deepak Bhalla
 *
 */
@Configuration
public class EhCacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return CacheManager.create();
    }

    @Bean
    public EhCacheCacheManager naceEhCacheConfigManager() {

        Cache existingCache = cacheManager().getCache(NaceDetailsConstants.NACE_CACHE_NAME);

        if (Objects.isNull(existingCache)) {
            CacheConfiguration ehCacheConfig = new CacheConfiguration().eternal(false).maxEntriesLocalHeap(0)
                    .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU).name(NaceDetailsConstants.NACE_CACHE_NAME)
                    .timeToLiveSeconds(Long.valueOf(86400));

            Cache cache = new Cache(ehCacheConfig);
            cacheManager().addCache(cache);
        }
        return new EhCacheCacheManager(cacheManager());
    }
}
