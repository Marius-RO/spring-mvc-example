package com.company.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserActivity {

    public interface Tags {
        String ADD_PRODUCT = "ADD_PRODUCT";
        String UPDATE_PRODUCT = "UPDATE_PRODUCT";
        String DELETE_PRODUCT = "DELETE_PRODUCT";
    }

    private int id;
    private String fkUserEmail;
    private String tag;
    private String before;
    private String after;
    private Timestamp added;
}
