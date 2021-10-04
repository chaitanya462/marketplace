package com.simplify.marketplace.domain;

public class SuggestionEntity {

    private String Id;
    private String Designation;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public SuggestionEntity() {
        super();
    }
}
