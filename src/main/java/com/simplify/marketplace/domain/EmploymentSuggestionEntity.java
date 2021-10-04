package com.simplify.marketplace.domain;

public class EmploymentSuggestionEntity {

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

    public EmploymentSuggestionEntity() {
        super();
    }
}
