package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.SkillSuggestionDomain;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SkillsSuggestionRepo extends ElasticsearchRepository<SkillSuggestionDomain, Long> {}
