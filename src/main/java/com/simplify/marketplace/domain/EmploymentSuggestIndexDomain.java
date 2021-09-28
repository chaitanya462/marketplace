package com.simplify.marketplace.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

//import org.springframework.data.elasticsearch.annotations.Setting;
//import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "suggestionindex")
@Mapping(mappingPath = "/ElasticSearch/mappings/mapping.json")
//@Setting(settingPath = "/ElasticSearch/settings/setting.json")
public class EmploymentSuggestIndexDomain {

    @Id
    private Long id;

    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
