package com.example.project_tracker.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                "projects",       // Must match the name in @Cacheable("projects")
                "projectById",    // Also used in getProjectById
                "tasks"           // Used in @CacheEvict
        );
    }
}
