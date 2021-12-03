package com.company.repository.mappers.user;

import com.company.model.Address;
import com.company.model.UserAccount;
import com.company.repository.mappers.AbstractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UsersResultSetExtractor extends AbstractMapper implements ResultSetExtractor<List<UserAccount>> {

    @Autowired
    public UsersResultSetExtractor(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    public List<UserAccount> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<UserAccount> users = new ArrayList<>();

        while(resultSet.next()){
            UserAccount user = webApplicationContext.getBean(UserAccount.class);
            user.setEmail(resultSet.getString("username"));
            user.setAdded(resultSet.getTimestamp("added"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));

            Address address = webApplicationContext.getBean(Address.class);
            address.setFullAddress(resultSet.getString("full_address"));
            address.setPhone(resultSet.getString("phone"));
            user.setAddress(address);

            users.add(user);
        }

        return users;
    }
}
