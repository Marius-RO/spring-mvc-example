package com.company.repository.mappers.user;

import com.company.model.Address;
import com.company.model.UserAccount;
import com.company.repository.mappers.AbstractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserRowMapper extends AbstractMapper implements RowMapper<UserAccount> {

    @Autowired
    public UserRowMapper(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    public UserAccount mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        UserAccount userAccount = webApplicationContext.getBean(UserAccount.class);
        userAccount.setEmail(resultSet.getString("username"));
        userAccount.setLastName(resultSet.getString("last_name"));
        userAccount.setFirstName(resultSet.getString("first_name"));
        userAccount.setAdded(resultSet.getTimestamp("added"));

        Address address = webApplicationContext.getBean(Address.class);
        address.setFullAddress(resultSet.getString("full_address"));
        address.setPhone(resultSet.getString("phone"));
        userAccount.setAddress(address);

        return userAccount;
    }
}
