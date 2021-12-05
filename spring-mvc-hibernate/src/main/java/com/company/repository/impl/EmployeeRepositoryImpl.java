package com.company.repository.impl;

import com.company.config.WebAppSecurityConfig;
import com.company.model.UserAccount;
import com.company.model.UserActivity;
import com.company.repository.EmployeeRepository;
import com.company.repository.impl.util.AbstractRepository;
import com.company.util.Pair;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepositoryImpl extends AbstractRepository implements EmployeeRepository {

    private static final String DEF_EXTRACT_ALL_EMPLOYEES_HQL =
            "SELECT u " +
            "FROM UserAccount u JOIN u.authorities role " +
            "WHERE role.authority = '" + WebAppSecurityConfig.Roles.ROLE_EMPLOYEE + "'";

    @Autowired
    public EmployeeRepositoryImpl(WebApplicationContext webApplicationContext, LocalSessionFactoryBean sessionFactory) {
        super(webApplicationContext, sessionFactory);
    }

    @Override
    protected Class<?> getClassType() {
        return EmployeeRepositoryImpl.class;
    }

    @Override
    public List<UserAccount> getAllEmployees(){
        return super.executeFetchOperation(this::executeGetAllEmployees);
    }

    private List<UserAccount> executeGetAllEmployees(Session session) {
        return session
                .createQuery(DEF_EXTRACT_ALL_EMPLOYEES_HQL, UserAccount.class)
                .list();
    }

    @Override
    public List<Pair<UserAccount, Timestamp>> getAllEmployeesWithLastActivityTimestamp() {
        return super.executeFetchOperation(this::executeGetAllEmployeesWithLastActivityTimestamp);
    }

    private List<Pair<UserAccount, Timestamp>> executeGetAllEmployeesWithLastActivityTimestamp(Session session){
        return session
                .createQuery(DEF_EXTRACT_ALL_EMPLOYEES_HQL, UserAccount.class)
                .list()
                .stream()
                .map(userAccount -> new Pair<>(userAccount, getLastActivity(userAccount.getActivities())))
                .collect(Collectors.toList());
    }

    private Timestamp getLastActivity(List<UserActivity> activities){
        if(activities == null){
            return null;
        }

        return activities.stream()
                .map(UserActivity::getAdded)
                .max(Timestamp::compareTo)
                .orElse(null);
    }

    @Override
    public void deleteEmployeeByEmail(String email) {
        super.executeDeleteOperation(session -> executeDeleteEmployeeByEmail(session, email));
    }

    private void executeDeleteEmployeeByEmail(Session session, String email){
        UserAccount employee = session.get(UserAccount.class, email);
        if(employee != null){
            session.remove(employee);
        }
    }

    @Override
    public List<UserActivity> getEmployeeActivityByEmail(String email) {
        return super.executeFetchOperation(session -> executeGetEmployeeActivityByEmail(session, email));
    }

    private List<UserActivity> executeGetEmployeeActivityByEmail(Session session, String email){
        UserAccount employee = session.get(UserAccount.class, email);
        if(employee != null){
            Hibernate.initialize(employee.getActivities());
        }

        return employee != null ?
                employee.getActivities() :
                new ArrayList<>();
    }


}
