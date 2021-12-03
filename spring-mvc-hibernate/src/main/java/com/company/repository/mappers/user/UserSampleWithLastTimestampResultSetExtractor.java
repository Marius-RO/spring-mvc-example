package com.company.repository.mappers.user;

import com.company.model.UserAccount;
import com.company.repository.mappers.AbstractMapper;
import com.company.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserSampleWithLastTimestampResultSetExtractor extends AbstractMapper
        implements ResultSetExtractor<List<Pair<UserAccount, Timestamp>>> {

    @Autowired
    public UserSampleWithLastTimestampResultSetExtractor(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    public List<Pair<UserAccount, Timestamp>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Pair<UserAccount, Timestamp>> values = new ArrayList<>();

        while(resultSet.next()){
            UserAccount user = webApplicationContext.getBean(UserAccount.class);
            user.setEmail(resultSet.getString(1));
            user.setFirstName(resultSet.getString(2));
            user.setLastName(resultSet.getString(3));

            values.add(new Pair<>(user, resultSet.getTimestamp(4)));
        }

        return values;
    }
}
