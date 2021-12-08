package com.company.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Entity
@Table(name = "user_activities")
public class UserActivity {

    public interface Tags {
        String ADD_PRODUCT = "ADD_PRODUCT";
        String UPDATE_PRODUCT = "UPDATE_PRODUCT";
        String DELETE_PRODUCT = "DELETE_PRODUCT";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "tag", nullable = false)
    private String tag;

    @Lob
    @Column(name = "before_value", nullable = false)
    private String before;

    @Lob
    @Column(name = "after_value", nullable = false)
    private String after;

    @Column(name = "added", nullable = false)
    private Timestamp added;

    @ManyToOne
    @JoinColumn(name="fk_email", nullable = false)
    private UserAccount userAccount;
}
