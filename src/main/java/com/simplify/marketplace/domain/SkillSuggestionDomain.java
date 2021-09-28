package com.simplify.marketplace.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

@Document(indexName = "skillsuggestion")
@Mapping(mappingPath = "/ElasticSearch/skillmapping/mapping.json")
public class SkillSuggestionDomain {

    @Id
    private Long id;

    private String SkillName;

    public String getSkillName() {
        return SkillName;
    }

    public void setSkillName(String skillName) {
        SkillName = skillName;
    }
}
