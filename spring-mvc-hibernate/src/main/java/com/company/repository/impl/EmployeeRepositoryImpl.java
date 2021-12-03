package com.company.repository.impl;

import com.company.config.WebAppSecurityConfig;
import com.company.model.UserAccount;
import com.company.model.UserActivity;
import com.company.repository.EmployeeRepository;
import com.company.repository.impl.util.AbstractRepository;
import com.company.repository.mappers.user.UserActivityResultSetExtractor;
import com.company.repository.mappers.user.UserSampleWithLastTimestampResultSetExtractor;
import com.company.repository.mappers.user.UsersResultSetExtractor;
import com.company.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class EmployeeRepositoryImpl extends AbstractRepository implements EmployeeRepository {

    private static final String DEF_EXTRACT_ALL_EMPLOYEES_SQL = "SELECT username, added, first_name, last_name, full_address, phone " +
                                                                "FROM users JOIN authorities USING(username) " +
                                                                "WHERE authority = '" + WebAppSecurityConfig.Roles.ROLE_EMPLOYEE + "'";

    private static final String DEF_EXTRACT_EMPLOYEE_ACTIVITY_SQL = "SELECT * FROM user_activities WHERE fk_username = ?";
    private static final String DEF_DELETE_EMPLOYEE_BY_EMAIL_SQL = "DELETE FROM users WHERE username = ?";

    private static final String DEF_EXTRACT_ALL_EMPLOYEES_WITH_LAST_ACTIVITY_TIMESTAMP_SQL
            = "SELECT DISTINCT u.username, first_name, last_name, (" +
                                                                    "SELECT added " +
                                                                    "FROM user_activities ua " +
                                                                    "WHERE ua.fk_username = u.username " +
                                                                    "ORDER BY ua.added DESC " +
                                                                    "LIMIT 1 " +
                                                                  ") AS added " +
             "FROM users u JOIN authorities a ON (u.username = a.username) " +
             "WHERE a.authority = '" + WebAppSecurityConfig.Roles.ROLE_EMPLOYEE + "'";

    @Autowired
    public EmployeeRepositoryImpl(WebApplicationContext webApplicationContext, JdbcTemplate jdbcTemplate) {
        super(webApplicationContext, jdbcTemplate);
    }

    @Override
    protected Class<?> getClassType() {
        return EmployeeRepositoryImpl.class;
    }

    @Override
    public List<UserAccount> getAllEmployees(){
        return jdbcTemplate.query(DEF_EXTRACT_ALL_EMPLOYEES_SQL, webApplicationContext.getBean(UsersResultSetExtractor.class));
    }

    @Override
    public List<Pair<UserAccount, Timestamp>> getAllEmployeesWithLastActivityTimestamp() {
        return jdbcTemplate.query(DEF_EXTRACT_ALL_EMPLOYEES_WITH_LAST_ACTIVITY_TIMESTAMP_SQL,
                webApplicationContext.getBean(UserSampleWithLastTimestampResultSetExtractor.class));
    }

    @Override
    public void deleteEmployeeByEmail(String email) {
        jdbcTemplate.update(DEF_DELETE_EMPLOYEE_BY_EMAIL_SQL, email);
    }

    @Override
    public List<UserActivity> getEmployeeActivityByEmail(String email) {
        return jdbcTemplate.query(DEF_EXTRACT_EMPLOYEE_ACTIVITY_SQL, ps -> ps.setString(1, email),
                webApplicationContext.getBean(UserActivityResultSetExtractor.class));
    }
}
