package com.company.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Entity
@Table(name = "users")
public class UserAccount {

    @Id
    @Column(
            name = "email",
            nullable = false,
            length = 128,
            unique = true
    )
    private String email;

    @Value(value = "${com.company.model.userAccount.enabled}")
    @Column(name = "enabled")
    private boolean enabled;

    @Column(
            name = "password",
            nullable = false,
            length = 60
    )
    private String password;

    @Column(name = "added", nullable = false)
    private Timestamp added;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Embedded
    private Address address;

    @ToString.Exclude
    @OneToMany(
            mappedBy = "userAccount",
            fetch = FetchType.EAGER,
            cascade = CascadeType.REMOVE
    )
    private List<Authority> authorities;

    @ToString.Exclude
    @OneToMany(
            mappedBy = "userAccount",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE
    )
    private List<UserActivity> activities;

    @Autowired
    public void setAddress(Address address) {
        this.address = address;
    }

    @Autowired
    public void setAdded(Timestamp  added) {
        this.added = added;
    }
}
