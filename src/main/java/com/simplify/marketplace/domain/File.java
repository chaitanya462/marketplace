package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simplify.marketplace.domain.enumeration.FileFormat;
import com.simplify.marketplace.domain.enumeration.FileType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A File.
 */
@Entity
@Table(name = "file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "path")
    private String path;

    @Enumerated(EnumType.STRING)
    @Column(name = "fileformat")
    private FileFormat fileformat;

    @Enumerated(EnumType.STRING)
    @Column(name = "filetype")
    private FileType filetype;

    @Column(name = "tag")
    private String tag;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "is_resume")
    private Boolean isResume;

    @Column(name = "is_profile_pic")
    private Boolean isProfilePic;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "customUser", "files", "educations", "certificates", "employments", "portfolios", "refereces", "jobPreferences", "skills",
        },
        allowSetters = true
    )
    private Worker worker;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public File id(Long id) {
        this.id = id;
        return this;
    }

    public String getPath() {
        return this.path;
    }

    public File path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FileFormat getFileformat() {
        return this.fileformat;
    }

    public File fileformat(FileFormat fileformat) {
        this.fileformat = fileformat;
        return this;
    }

    public void setFileformat(FileFormat fileformat) {
        this.fileformat = fileformat;
    }

    public FileType getFiletype() {
        return this.filetype;
    }

    public File filetype(FileType filetype) {
        this.filetype = filetype;
        return this;
    }

    public void setFiletype(FileType filetype) {
        this.filetype = filetype;
    }

    public String getTag() {
        return this.tag;
    }

    public File tag(String tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public File isDefault(Boolean isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsResume() {
        return this.isResume;
    }

    public File isResume(Boolean isResume) {
        this.isResume = isResume;
        return this;
    }

    public void setIsResume(Boolean isResume) {
        this.isResume = isResume;
    }

    public Boolean getIsProfilePic() {
        return this.isProfilePic;
    }

    public File isProfilePic(Boolean isProfilePic) {
        this.isProfilePic = isProfilePic;
        return this;
    }

    public void setIsProfilePic(Boolean isProfilePic) {
        this.isProfilePic = isProfilePic;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public File createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public File createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public File updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public File updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Worker getWorker() {
        return this.worker;
    }

    public File worker(Worker worker) {
        this.setWorker(worker);
        return this;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof File)) {
            return false;
        }
        return id != null && id.equals(((File) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "File{" +
            "id=" + getId() +
            ", path='" + getPath() + "'" +
            ", fileformat='" + getFileformat() + "'" +
            ", filetype='" + getFiletype() + "'" +
            ", tag='" + getTag() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            ", isResume='" + getIsResume() + "'" +
            ", isProfilePic='" + getIsProfilePic() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
