package com.simplify.marketplace.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CustomUser.
 */
@Entity
@Table(name = "custom_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "customUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customUser" }, allowSetters = true)
    private Set<UserEmail> userEmails = new HashSet<>();

    @OneToMany(mappedBy = "customUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customUser" }, allowSetters = true)
    private Set<UserPhone> userPhones = new HashSet<>();

    @OneToMany(mappedBy = "customUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "location", "customUser" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomUser id(Long id) {
        this.id = id;
        return this;
    }

    public Set<UserEmail> getUserEmails() {
        return this.userEmails;
    }

    public CustomUser userEmails(Set<UserEmail> userEmails) {
        this.setUserEmails(userEmails);
        return this;
    }

    public CustomUser addUserEmail(UserEmail userEmail) {
        this.userEmails.add(userEmail);
        userEmail.setCustomUser(this);
        return this;
    }

    public CustomUser removeUserEmail(UserEmail userEmail) {
        this.userEmails.remove(userEmail);
        userEmail.setCustomUser(null);
        return this;
    }

    public void setUserEmails(Set<UserEmail> userEmails) {
        if (this.userEmails != null) {
            this.userEmails.forEach(i -> i.setCustomUser(null));
        }
        if (userEmails != null) {
            userEmails.forEach(i -> i.setCustomUser(this));
        }
        this.userEmails = userEmails;
    }

    public Set<UserPhone> getUserPhones() {
        return this.userPhones;
    }

    public CustomUser userPhones(Set<UserPhone> userPhones) {
        this.setUserPhones(userPhones);
        return this;
    }

    public CustomUser addUserPhone(UserPhone userPhone) {
        this.userPhones.add(userPhone);
        userPhone.setCustomUser(this);
        return this;
    }

    public CustomUser removeUserPhone(UserPhone userPhone) {
        this.userPhones.remove(userPhone);
        userPhone.setCustomUser(null);
        return this;
    }

    public void setUserPhones(Set<UserPhone> userPhones) {
        if (this.userPhones != null) {
            this.userPhones.forEach(i -> i.setCustomUser(null));
        }
        if (userPhones != null) {
            userPhones.forEach(i -> i.setCustomUser(this));
        }
        this.userPhones = userPhones;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public CustomUser addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public CustomUser addAddress(Address address) {
        this.addresses.add(address);
        address.setCustomUser(this);
        return this;
    }

    public CustomUser removeAddress(Address address) {
        this.addresses.remove(address);
        address.setCustomUser(null);
        return this;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setCustomUser(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setCustomUser(this));
        }
        this.addresses = addresses;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomUser)) {
            return false;
        }
        return id != null && id.equals(((CustomUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomUser{" +
            "id=" + getId() +
            "}";
    }
}
