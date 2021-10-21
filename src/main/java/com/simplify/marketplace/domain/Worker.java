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
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.transaction.Transactional;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Worker.
 */
@Entity
@Table(name = "worker")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Worker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "primary_phone")
    private String primaryPhone;

    @Column(name = "email")
    private String email;

    @Column(name = "description")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "gender")
    private String gender;

    @Column(name = "id_proof")
    private String idProof;

    @Column(name = "id_code")
    private String idCode;

    @Column(name = "status")
    private String status;

    @Column(name = "language")
    private String language;

    @Column(name = "worker_location")
    private String workerLocation;

    @Column(name = "created_by")
    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "page1")
    private Boolean page1;

    @Column(name = "page2")
    private Boolean page2;

    @Column(name = "page3")
    private Boolean page3;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true)
    @OneToOne(mappedBy = "worker")
    private Photo photo;

    @OneToMany(mappedBy = "worker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true)
    private Set<File> files = new HashSet<>();

    @OneToMany(mappedBy = "worker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "majorSubject", "minorSubject", "worker" }, allowSetters = true)
    private Set<Education> educations = new HashSet<>();

    @OneToMany(mappedBy = "worker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true)
    private Set<Certificate> certificates = new HashSet<>();

    @OneToMany(mappedBy = "worker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "locations", "company", "worker" }, allowSetters = true)
    private Set<Employment> employments = new HashSet<>();

    @OneToMany(mappedBy = "worker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true)
    private Set<Portfolio> portfolios = new HashSet<>();

    @OneToMany(mappedBy = "worker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true)
    private Set<Refereces> refereces = new HashSet<>();

    @OneToMany(mappedBy = "worker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "locationPrefrences", "fieldValues", "subCategory", "worker" }, allowSetters = true)
    private Set<JobPreference> jobPreferences = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_worker__skill",
        joinColumns = @JoinColumn(name = "worker_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @JsonIgnoreProperties(value = { "workers" }, allowSetters = true)
    private Set<SkillsMaster> skills = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_worker__vmsjobsave",
        joinColumns = @JoinColumn(name = "worker_id"),
        inverseJoinColumns = @JoinColumn(name = "vmsjobsave_id")
    )
    @JsonIgnoreProperties(value = { "workers" }, allowSetters = true)
    private Set<VmsjobSave> vmsjobsaves = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_worker__vmsjobsubmit",
        joinColumns = @JoinColumn(name = "worker_id"),
        inverseJoinColumns = @JoinColumn(name = "vmsjobsubmit_id")
    )
    @JsonIgnoreProperties(value = { "workers" }, allowSetters = true)
    private Set<VmsjobSubmit> vmsjobsubmits = new HashSet<>();

    @Column(name = "candidate_id")
    private String candidate_id;

    public void setId(Long id) {
        this.id = id;
    }

    public Worker id(Long id) {
        this.id = id;
        return this;
    }

    public Worker firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Worker middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Worker lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Worker primaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
        return this;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public Worker description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Worker dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Worker isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Worker user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Worker files(Set<File> files) {
        this.setFiles(files);
        return this;
    }

    public Worker addFile(File file) {
        this.files.add(file);
        file.setWorker(this);
        return this;
    }

    public Worker removeFile(File file) {
        this.files.remove(file);
        file.setWorker(null);
        return this;
    }

    public void setFiles(Set<File> files) {
        if (this.files != null) {
            this.files.forEach(i -> i.setWorker(null));
        }
        if (files != null) {
            files.forEach(i -> i.setWorker(this));
        }
        this.files = files;
    }

    public Worker educations(Set<Education> educations) {
        this.setEducations(educations);
        return this;
    }

    public Worker addEducation(Education education) {
        this.educations.add(education);
        education.setWorker(this);
        return this;
    }

    public Worker removeEducation(Education education) {
        this.educations.remove(education);
        education.setWorker(null);
        return this;
    }

    public void setEducations(Set<Education> educations) {
        if (this.educations != null) {
            this.educations.forEach(i -> i.setWorker(null));
        }
        if (educations != null) {
            educations.forEach(i -> i.setWorker(this));
        }
        this.educations = educations;
    }

    public Worker certificates(Set<Certificate> certificates) {
        this.setCertificates(certificates);
        return this;
    }

    public Worker addCertificate(Certificate certificate) {
        this.certificates.add(certificate);
        certificate.setWorker(this);
        return this;
    }

    public Worker removeCertificate(Certificate certificate) {
        this.certificates.remove(certificate);
        certificate.setWorker(null);
        return this;
    }

    public void setCertificates(Set<Certificate> certificates) {
        if (this.certificates != null) {
            this.certificates.forEach(i -> i.setWorker(null));
        }
        if (certificates != null) {
            certificates.forEach(i -> i.setWorker(this));
        }
        this.certificates = certificates;
    }

    public Worker employments(Set<Employment> employments) {
        this.setEmployments(employments);
        return this;
    }

    public Worker addEmployment(Employment employment) {
        this.employments.add(employment);
        employment.setWorker(this);
        return this;
    }

    public Worker removeEmployment(Employment employment) {
        this.employments.remove(employment);
        employment.setWorker(null);
        return this;
    }

    public void setEmployments(Set<Employment> employments) {
        if (this.employments != null) {
            this.employments.forEach(i -> i.setWorker(null));
        }
        if (employments != null) {
            employments.forEach(i -> i.setWorker(this));
        }
        this.employments = employments;
    }

    public Worker portfolios(Set<Portfolio> portfolios) {
        this.setPortfolios(portfolios);
        return this;
    }

    public Worker addPortfolio(Portfolio portfolio) {
        this.portfolios.add(portfolio);
        portfolio.setWorker(this);
        return this;
    }

    public Worker removePortfolio(Portfolio portfolio) {
        this.portfolios.remove(portfolio);
        portfolio.setWorker(null);
        return this;
    }

    public void setPortfolios(Set<Portfolio> portfolios) {
        if (this.portfolios != null) {
            this.portfolios.forEach(i -> i.setWorker(null));
        }
        if (portfolios != null) {
            portfolios.forEach(i -> i.setWorker(this));
        }
        this.portfolios = portfolios;
    }

    public Worker refereces(Set<Refereces> refereces) {
        this.setRefereces(refereces);
        return this;
    }

    public Worker addRefereces(Refereces refereces) {
        this.refereces.add(refereces);
        refereces.setWorker(this);
        return this;
    }

    public Worker removeRefereces(Refereces refereces) {
        this.refereces.remove(refereces);
        refereces.setWorker(null);
        return this;
    }

    public void setRefereces(Set<Refereces> refereces) {
        if (this.refereces != null) {
            this.refereces.forEach(i -> i.setWorker(null));
        }
        if (refereces != null) {
            refereces.forEach(i -> i.setWorker(this));
        }
        this.refereces = refereces;
    }

    public Worker jobPreferences(Set<JobPreference> jobPreferences) {
        this.setJobPreferences(jobPreferences);
        return this;
    }

    public Worker addJobPreference(JobPreference jobPreference) {
        this.jobPreferences.add(jobPreference);
        jobPreference.setWorker(this);
        return this;
    }

    public Worker removeJobPreference(JobPreference jobPreference) {
        this.jobPreferences.remove(jobPreference);
        jobPreference.setWorker(null);
        return this;
    }

    public void setJobPreferences(Set<JobPreference> jobPreferences) {
        if (this.jobPreferences != null) {
            this.jobPreferences.forEach(i -> i.setWorker(null));
        }
        if (jobPreferences != null) {
            jobPreferences.forEach(i -> i.setWorker(this));
        }
        this.jobPreferences = jobPreferences;
    }

    public Worker skills(Set<SkillsMaster> skillsMasters) {
        this.setSkills(skillsMasters);
        return this;
    }

    public Worker addSkill(SkillsMaster skillsMaster) {
        this.skills.add(skillsMaster);
        skillsMaster.getWorkers().add(this);
        return this;
    }

    public Worker removeSkill(SkillsMaster skillsMaster) {
        this.skills.remove(skillsMaster);
        skillsMaster.getWorkers().remove(this);
        return this;
    }

    public void setSkills(Set<SkillsMaster> skillsMasters) {
        this.skills = skillsMasters;
    }

    @Transactional
    public Set<VmsjobSave> getVmsjobsaves() {
        return this.vmsjobsaves;
    }

    public Worker vmsjobsaves(Set<VmsjobSave> vmsjobSaves) {
        this.setVmsjobsaves(vmsjobSaves);
        return this;
    }

    public Worker addVmsjobsave(VmsjobSave vmsjobSave) {
        this.vmsjobsaves.add(vmsjobSave);
        vmsjobSave.getWorkers().add(this);
        return this;
    }

    @Transactional
    public Worker removeVmsjobsave(VmsjobSave vmsjobSave) {
        this.vmsjobsaves.remove(vmsjobSave);
        vmsjobSave.getWorkers().remove(this);
        return this;
    }

    public void setVmsjobsaves(Set<VmsjobSave> vmsjobSaves) {
        this.vmsjobsaves = vmsjobSaves;
    }

    public Set<VmsjobSubmit> getVmsjobsubmits() {
        return this.vmsjobsubmits;
    }

    public Worker vmsjobsubmits(Set<VmsjobSubmit> vmsjobSubmits) {
        this.setVmsjobsubmits(vmsjobSubmits);
        return this;
    }

    @Transactional
    public Worker addVmsjobsubmit(VmsjobSubmit vmsjobSubmit) {
        this.vmsjobsubmits.add(vmsjobSubmit);
        vmsjobSubmit.getWorkers().add(this);
        return this;
    }

    public Worker removeVmsjobsubmit(VmsjobSubmit vmsjobSubmit) {
        this.vmsjobsubmits.remove(vmsjobSubmit);
        vmsjobSubmit.getWorkers().remove(this);
        return this;
    }

    public void setVmsjobsubmits(Set<VmsjobSubmit> vmsjobSubmits) {
        this.vmsjobsubmits = vmsjobSubmits;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Worker)) {
            return false;
        }
        return id != null && id.equals(((Worker) o).id);
    }

    public Photo getPhoto() {
        return this.photo;
    }

    public Worker photo(Photo photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(Photo photo) {
        if (this.photo != null) {
            this.photo.setWorker(null);
        }
        if (photo != null) {
            photo.setWorker(this);
        }
        this.photo = photo;
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Worker{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", primaryPhone='" + getPrimaryPhone() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
