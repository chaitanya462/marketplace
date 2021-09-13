package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.simplify.marketplace.domain.enumeration.FileFormat;
import com.simplify.marketplace.domain.enumeration.FileType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A File.
 */
@Entity
@Table(name = "file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
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

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "user", "files", "educations", "certificates", "employments", "portfolios", "refereces", "jobPreferences", "skills" },
        allowSetters = true
    )
    private Worker worker;

    public File id(Long id) {
        this.id = id;
        return this;
    }

    public File path(String path) {
        this.path = path;
        return this;
    }

    public File fileformat(FileFormat fileformat) {
        this.fileformat = fileformat;
        return this;
    }

    public File filetype(FileType filetype) {
        this.filetype = filetype;
        return this;
    }

    public File tag(String tag) {
        this.tag = tag;
        return this;
    }

    public File isDefault(Boolean isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    public File isResume(Boolean isResume) {
        this.isResume = isResume;
        return this;
    }

    public File isProfilePic(Boolean isProfilePic) {
        this.isProfilePic = isProfilePic;
        return this;
    }

    public File worker(Worker worker) {
        this.setWorker(worker);
        return this;
    }

    public File createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public File createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public File updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public File updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
