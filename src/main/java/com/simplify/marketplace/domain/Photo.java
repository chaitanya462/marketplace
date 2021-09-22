package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Photo.
 */
@Entity
@Table(name = "photo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "mimetype")
    private String mimetype;

    @Lob
    @Column(name = "pic")
    private byte[] pic;

    @Column(name = "pic_content_type")
    private String picContentType;

    @JsonIgnoreProperties(
        value = {
            "user", "files", "educations", "certificates", "employments", "portfolios", "refereces", "jobPreferences", "skills", "photo",
        },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private Worker worker;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Photo id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Photo name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimetype() {
        return this.mimetype;
    }

    public Photo mimetype(String mimetype) {
        this.mimetype = mimetype;
        return this;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public byte[] getPic() {
        return this.pic;
    }

    public Photo pic(byte[] pic) {
        this.pic = pic;
        return this;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public String getPicContentType() {
        return this.picContentType;
    }

    public Photo picContentType(String picContentType) {
        this.picContentType = picContentType;
        return this;
    }

    public void setPicContentType(String picContentType) {
        this.picContentType = picContentType;
    }

    public Worker getWorker() {
        return this.worker;
    }

    public Photo worker(Worker worker) {
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
        if (!(o instanceof Photo)) {
            return false;
        }
        return id != null && id.equals(((Photo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Photo{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", mimetype='" + getMimetype() + "'" +
            ", pic='" + getPic() + "'" +
            ", picContentType='" + getPicContentType() + "'" +
            "}";
    }
}
