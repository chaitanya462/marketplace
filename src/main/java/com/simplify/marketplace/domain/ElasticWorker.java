package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "elasticsearchworkerindex")
@Mapping(mappingPath = "/ElasticSearch/mappings/mapping.json")
@Setting(settingPath = "/ElasticSearch/settings/setting.json")
public class ElasticWorker implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    private String firstName;

    private String middleName;

    @NotNull
    private String lastName;

    private String primaryPhone;

    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate dateOfBirth;

    private String description;

    private Boolean isActive;

    private String gender;

    private String idProof;

    private String idCode;

    private String status;

    private String language;

    private String workerLocation;

    private String createdBy;

    private String Category;

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate createdAt;

    private String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate updatedAt;

    @JsonIgnoreProperties(value = { "userEmails", "userPhones", "addresses" }, allowSetters = true)
    private User user;

    @JsonIgnoreProperties(value = { "workers" }, allowSetters = true)
    private Set<SkillsMaster> skills = new HashSet<>();

    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true)
    private Set<File> files = new HashSet<>();

    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true)
    private Set<Education> educations = new HashSet<>();

    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true)
    private Set<Certificate> certificates = new HashSet<>();

    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true) // value = { "locations","worker" }
    private Set<Employment> employments = new HashSet<>();

    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true)
    private Set<Portfolio> portfolios = new HashSet<>();

    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true)
    private Set<Refereces> refereces = new HashSet<>();

    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true)
    private Set<JobPreference> jobPreferences = new HashSet<>();

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public User getCustomUser() {
        return user;
    }

    public void setCustomUser(User user) {
        this.user = user;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    public ElasticWorker addFile(File file) {
        this.files.add(file);
        return this;
    }

    public ElasticWorker removeFile(File file) {
        this.files.remove(file);
        file.setWorker(null);
        return this;
    }

    public Set<Education> getEducations() {
        return educations;
    }

    public void setEducations(Set<Education> educations) {
        this.educations = educations;
    }

    public ElasticWorker addEducation(Education education) {
        this.educations.add(education);
        return this;
    }

    public ElasticWorker removeEducation(Education education) {
        this.educations.remove(education);
        education.setWorker(null);
        return this;
    }

    public Set<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(Set<Certificate> certificates) {
        this.certificates = certificates;
    }

    public ElasticWorker addCertificate(Certificate certificate) {
        this.certificates.add(certificate);
        return this;
    }

    public ElasticWorker removeCertificate(Certificate certificate) {
        this.certificates.remove(certificate);
        certificate.setWorker(null);
        return this;
    }

    public Set<Employment> getEmployments() {
        return employments;
    }

    public void setEmployments(Set<Employment> employments) {
        this.employments = employments;
    }

    public ElasticWorker addEmployment(Employment employment) {
        this.employments.add(employment);
        return this;
    }

    public ElasticWorker removeEmployment(Employment employment) {
        this.employments.remove(employment);
        employment.setWorker(null);
        return this;
    }

    public Set<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(Set<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    public ElasticWorker addPortfolio(Portfolio portfolio) {
        this.portfolios.add(portfolio);
        return this;
    }

    public ElasticWorker removePortfolio(Portfolio portfolio) {
        this.portfolios.remove(portfolio);
        portfolio.setWorker(null);
        return this;
    }

    public Set<Refereces> getRefereces() {
        return refereces;
    }

    public void setRefereces(Set<Refereces> refereces) {
        this.refereces = refereces;
    }

    public ElasticWorker addRefereces(Refereces refereces) {
        this.refereces.add(refereces);
        return this;
    }

    public ElasticWorker removeRefereces(Refereces refereces) {
        this.refereces.remove(refereces);
        refereces.setWorker(null);
        return this;
    }

    public Set<JobPreference> getJobPreferences() {
        return jobPreferences;
    }

    public void setJobPreferences(Set<JobPreference> jobPreferences) {
        this.jobPreferences = jobPreferences;
    }

    public ElasticWorker addJobPreference(JobPreference jobPreference) {
        this.jobPreferences.add(jobPreference);
        return this;
    }

    public ElasticWorker removeJobPreference(JobPreference jobPreference) {
        this.jobPreferences.remove(jobPreference);
        jobPreference.setWorker(null);
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdProof() {
        return idProof;
    }

    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getWorkerLocation() {
        return workerLocation;
    }

    public void setWorkerLocation(String workerLocation) {
        this.workerLocation = workerLocation;
    }

    public Set<SkillsMaster> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillsMaster> skills) {
        this.skills = skills;
    }

    public ElasticWorker(
        String id,
        @NotNull String firstName,
        String middleName,
        @NotNull String lastName,
        String primaryPhone,
        String description,
        LocalDate dateOfBirth,
        Boolean isActive,
        User user,
        Set<File> files,
        Set<Education> educations,
        Set<Certificate> certificates,
        Set<Employment> employments,
        Set<Portfolio> portfolios,
        Set<Refereces> refereces,
        Set<JobPreference> jobPreferences,
        Set<SkillsMaster> skills
    ) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.primaryPhone = primaryPhone;
        this.description = description;
        this.dateOfBirth = dateOfBirth;
        this.isActive = isActive;
        this.user = user;
        this.files = files;
        this.educations = educations;
        this.certificates = certificates;
        this.employments = employments;
        this.portfolios = portfolios;
        this.refereces = refereces;
        this.jobPreferences = jobPreferences;
        this.skills = skills;
    }

    public ElasticWorker() {
        super();
    }
}
