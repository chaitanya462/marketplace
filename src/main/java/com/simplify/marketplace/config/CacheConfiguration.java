package com.simplify.marketplace.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.simplify.marketplace.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.simplify.marketplace.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.simplify.marketplace.domain.User.class.getName());
            createCache(cm, com.simplify.marketplace.domain.Authority.class.getName());
            createCache(cm, com.simplify.marketplace.domain.User.class.getName() + ".authorities");
            createCache(cm, com.simplify.marketplace.domain.Otp.class.getName());
            createCache(cm, com.simplify.marketplace.domain.OtpAttempt.class.getName());
            createCache(cm, com.simplify.marketplace.domain.CustomUser.class.getName());
            createCache(cm, com.simplify.marketplace.domain.CustomUser.class.getName() + ".userEmails");
            createCache(cm, com.simplify.marketplace.domain.CustomUser.class.getName() + ".userPhones");
            createCache(cm, com.simplify.marketplace.domain.CustomUser.class.getName() + ".addresses");
            createCache(cm, com.simplify.marketplace.domain.UserEmail.class.getName());
            createCache(cm, com.simplify.marketplace.domain.UserPhone.class.getName());
            createCache(cm, com.simplify.marketplace.domain.Address.class.getName());
            createCache(cm, com.simplify.marketplace.domain.Location.class.getName());
            createCache(cm, com.simplify.marketplace.domain.Location.class.getName() + ".locationPrefrences");
            createCache(cm, com.simplify.marketplace.domain.Client.class.getName());
            createCache(cm, com.simplify.marketplace.domain.Worker.class.getName());
            createCache(cm, com.simplify.marketplace.domain.Worker.class.getName() + ".files");
            createCache(cm, com.simplify.marketplace.domain.Worker.class.getName() + ".educations");
            createCache(cm, com.simplify.marketplace.domain.Worker.class.getName() + ".certificates");
            createCache(cm, com.simplify.marketplace.domain.Worker.class.getName() + ".employments");
            createCache(cm, com.simplify.marketplace.domain.Worker.class.getName() + ".portfolios");
            createCache(cm, com.simplify.marketplace.domain.Worker.class.getName() + ".refereces");
            createCache(cm, com.simplify.marketplace.domain.Worker.class.getName() + ".jobPreferences");
            createCache(cm, com.simplify.marketplace.domain.Worker.class.getName() + ".skills");
            createCache(cm, com.simplify.marketplace.domain.File.class.getName());
            createCache(cm, com.simplify.marketplace.domain.Education.class.getName());
            createCache(cm, com.simplify.marketplace.domain.SubjectMaster.class.getName());
            createCache(cm, com.simplify.marketplace.domain.Certificate.class.getName());
            createCache(cm, com.simplify.marketplace.domain.Employment.class.getName());
            createCache(cm, com.simplify.marketplace.domain.Employment.class.getName() + ".locations");
            createCache(cm, com.simplify.marketplace.domain.SkillsMaster.class.getName());
            createCache(cm, com.simplify.marketplace.domain.SkillsMaster.class.getName() + ".workers");
            createCache(cm, com.simplify.marketplace.domain.Portfolio.class.getName());
            createCache(cm, com.simplify.marketplace.domain.Refereces.class.getName());
            createCache(cm, com.simplify.marketplace.domain.JobPreference.class.getName());
            createCache(cm, com.simplify.marketplace.domain.JobPreference.class.getName() + ".locationPrefrences");
            createCache(cm, com.simplify.marketplace.domain.JobPreference.class.getName() + ".fieldValues");
            createCache(cm, com.simplify.marketplace.domain.Category.class.getName());
            createCache(cm, com.simplify.marketplace.domain.Category.class.getName() + ".categories");
            createCache(cm, com.simplify.marketplace.domain.Category.class.getName() + ".fields");
            createCache(cm, com.simplify.marketplace.domain.LocationPrefrence.class.getName());
            createCache(cm, com.simplify.marketplace.domain.Field.class.getName());
            createCache(cm, com.simplify.marketplace.domain.Field.class.getName() + ".fieldValues");
            createCache(cm, com.simplify.marketplace.domain.FieldValue.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
