package com.simplify.marketplace.domain;

public class SkillsSuggestionEntity {

    private String Id;
    private String skillSuggestion;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSkillSuggestion() {
        return skillSuggestion;
    }

    public void setSkillSuggestion(String skillSuggestion) {
        this.skillSuggestion = skillSuggestion;
    }
}
