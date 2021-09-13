//package com.simplify.marketplace.domain;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
//import com.simplify.marketplace.domain.enumeration.DegreeType;
//import com.simplify.marketplace.domain.enumeration.EducationGrade;
//import com.simplify.marketplace.domain.enumeration.MarksType;
//import java.io.Serializable;
//import java.time.LocalDate;
//import javax.persistence.*;
//import lombok.Data;
//import org.hibernate.annotations.Cache;
//import org.hibernate.annotations.CacheConcurrencyStrategy;
//import org.springframework.data.elasticsearch.annotations.DateFormat;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
///**
// * A Education.
// */
//@Entity
//@Table(name = "education")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Data
//public class Education implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "degree_name")
//    private String degreeName;
//
//    @Column(name = "institute")
//    private String institute;
//
//    @Column(name = "year_of_passing")
//    private Integer yearOfPassing;
//
//    @Column(name = "marks")
//    private Float marks;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "marks_type")
//    private MarksType marksType;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "grade")
//    private EducationGrade grade;
//
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonSerialize(using = LocalDateSerializer.class)
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
//    @Column(name = "start_date")
//    private LocalDate startDate;
//
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonSerialize(using = LocalDateSerializer.class)
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
//    @Column(name = "end_date")
//    private LocalDate endDate;
//
//    @Column(name = "is_complete")
//    private Boolean isComplete;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "degree_type")
//    private DegreeType degreeType;
//
//    @Column(name = "description")
//    private String description;
//
//    @Column(name = "created_by")
//    private String createdBy;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonSerialize(using = LocalDateSerializer.class)
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
//    @Column(name = "created_at")
//    private LocalDate createdAt;
//
//    @Column(name = "updated_by")
//    private String updatedBy;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonSerialize(using = LocalDateSerializer.class)
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
//    @Column(name = "updated_at")
//    private LocalDate updatedAt;
//
//    @ManyToOne
//    private SubjectMaster majorSubject;
//
//    @ManyToOne
//    private SubjectMaster minorSubject;
//
//    @ManyToOne
//    @JsonIgnoreProperties(
//        value = { "user", "files", "educations", "certificates", "employments", "portfolios", "refereces", "jobPreferences", "skills" },
//        allowSetters = true
//    )
//    private Worker worker;
//
//    public Education id(Long id) {
//        this.id = id;
//        return this;
//    }
//
//    public Education degreeName(String degreeName) {
//        this.degreeName = degreeName;
//        return this;
//    }
//
//    public Education institute(String institute) {
//        this.institute = institute;
//        return this;
//    }
//
//    public Education yearOfPassing(Integer yearOfPassing) {
//        this.yearOfPassing = yearOfPassing;
//        return this;
//    }
//
//    public Education marks(Float marks) {
//        this.marks = marks;
//        return this;
//    }
//
//    public Education marksType(MarksType marksType) {
//        this.marksType = marksType;
//        return this;
//    }
//
//    public Education grade(EducationGrade grade) {
//        this.grade = grade;
//        return this;
//    }
//
//    public Education startDate(LocalDate startDate) {
//        this.startDate = startDate;
//        return this;
//    }
//
//    public Education endDate(LocalDate endDate) {
//        this.endDate = endDate;
//        return this;
//    }
//
//    public Education isComplete(Boolean isComplete) {
//        this.isComplete = isComplete;
//        return this;
//    }
//
//    public Education degreeType(DegreeType degreeType) {
//        this.degreeType = degreeType;
//        return this;
//    }
//
//    public Education description(String description) {
//        this.description = description;
//        return this;
//    }
//
//    public Education majorSubject(SubjectMaster subjectMaster) {
//        this.setMajorSubject(subjectMaster);
//        return this;
//    }
//
//    public Education minorSubject(SubjectMaster subjectMaster) {
//        this.setMinorSubject(subjectMaster);
//        return this;
//    }
//
//    public Education worker(Worker worker) {
//        this.setWorker(worker);
//        return this;
//    }
//
//    public Education createdBy(String createdBy) {
//        this.createdBy = createdBy;
//        return this;
//    }
//
//    public Education createdAt(LocalDate createdAt) {
//        this.createdAt = createdAt;
//        return this;
//    }
//
//    public Education updatedBy(String updatedBy) {
//        this.updatedBy = updatedBy;
//        return this;
//    }
//
//    public Education updatedAt(LocalDate updatedAt) {
//        this.updatedAt = updatedAt;
//        return this;
//    }
//}
package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.simplify.marketplace.domain.enumeration.DegreeType;
import com.simplify.marketplace.domain.enumeration.EducationGrade;
import com.simplify.marketplace.domain.enumeration.MarksType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Education.
 */
@Entity
@Table(name = "education")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "degree_name")
    private String degreeName;

    @Column(name = "institute")
    private String institute;

    @Column(name = "year_of_passing")
    private Integer yearOfPassing;

    @Column(name = "marks")
    private Float marks;

    @Enumerated(EnumType.STRING)
    @Column(name = "marks_type")
    private MarksType marksType;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade")
    private EducationGrade grade;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date, format = DateFormat.date)
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_complete")
    private Boolean isComplete;

    @Enumerated(EnumType.STRING)
    @Column(name = "degree_type")
    private DegreeType degreeType;

    @Column(name = "description")
    private String description;

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
    private SubjectMaster majorSubject;

    @ManyToOne
    private SubjectMaster minorSubject;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "customUser", "files", "educations", "certificates", "employments", "portfolios", "refereces", "jobPreferences", "skills",
        },
        allowSetters = true
    )
    private Worker worker;

    public Education id(Long id) {
        this.id = id;
        return this;
    }

    public Education degreeName(String degreeName) {
        this.degreeName = degreeName;
        return this;
    }

    public Education institute(String institute) {
        this.institute = institute;
        return this;
    }

    public Education yearOfPassing(Integer yearOfPassing) {
        this.yearOfPassing = yearOfPassing;
        return this;
    }

    public Education marks(Float marks) {
        this.marks = marks;
        return this;
    }

    public Education marksType(MarksType marksType) {
        this.marksType = marksType;
        return this;
    }

    public Education grade(EducationGrade grade) {
        this.grade = grade;
        return this;
    }

    public Education startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public Education endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public Education isComplete(Boolean isComplete) {
        this.isComplete = isComplete;
        return this;
    }

    public Education degreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
        return this;
    }

    public Education description(String description) {
        this.description = description;
        return this;
    }

    public Education majorSubject(SubjectMaster subjectMaster) {
        this.setMajorSubject(subjectMaster);
        return this;
    }

    public Education minorSubject(SubjectMaster subjectMaster) {
        this.setMinorSubject(subjectMaster);
        return this;
    }

    public Education worker(Worker worker) {
        this.setWorker(worker);
        return this;
    }

    public Education createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Education createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Education updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Education updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
