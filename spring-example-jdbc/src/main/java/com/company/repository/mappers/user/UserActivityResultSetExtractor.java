package com.company.repository.mappers.user;

import com.company.model.UserActivity;
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
public class UserActivityResultSetExtractor extends AbstractMapper implements ResultSetExtractor<List<UserActivity>> {

    @Autowired
    public UserActivityResultSetExtractor(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    public List<UserActivity> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<UserActivity> activitiesList = new ArrayList<>();

        while(resultSet.next()){
            UserActivity activity = webApplicationContext.getBean(UserActivity.class);
            activity.setId(resultSet.getInt(1));
            activity.setTag(resultSet.getString(2));
            activity.setBefore(resultSet.getString(3));
            activity.setAfter(resultSet.getString(4));
            activity.setAdded(resultSet.getTimestamp(5));
            activity.setFkUserEmail(resultSet.getString(6));

            activitiesList.add(activity);
        }

        return activitiesList;
    }
}
