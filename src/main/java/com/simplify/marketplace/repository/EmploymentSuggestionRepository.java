package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.EmploymentSuggestIndexDomain;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EmploymentSuggestionRepository extends ElasticsearchRepository<EmploymentSuggestIndexDomain, Long> {}
