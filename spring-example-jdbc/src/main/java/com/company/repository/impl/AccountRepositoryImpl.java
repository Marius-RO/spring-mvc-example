package com.company.repository.impl;

import com.company.model.UserAccount;
import com.company.repository.AccountRepository;
import com.company.repository.impl.util.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class AccountRepositoryImpl extends AbstractRepository implements AccountRepository {

    public static final String DEF_CREATE_USER_SQL = "INSERT INTO users (username, password, enabled, added) VALUES (?, ?, ?, ?)";
    public static final String DEF_CREATE_USER_ROLE_SQL = "INSERT INTO authorities (username, authority) VALUES (?, ?)";

    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    @Autowired
    public AccountRepositoryImpl(JdbcTemplate jdbcTemplate, JdbcUserDetailsManager jdbcUserDetailsManager) {
        super(jdbcTemplate);
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
    }

    @Override
    protected Class<?> getClassType() {
        return AccountRepositoryImpl.class;
    }

    @Override
    public void insertUser(UserAccount userAccount, String... roles) {
        // TODO: make this as transactional

        // add user
        jdbcTemplate.update(DEF_CREATE_USER_SQL, ps -> {
            ps.setString(1, userAccount.getEmail());
            ps.setString(2, userAccount.getPassword());
            ps.setBoolean(3, userAccount.isEnabled());
            ps.setTimestamp(4, userAccount.getAdded());
        });

        // add roles
        final String username = userAccount.getEmail();
        jdbcTemplate.batchUpdate(DEF_CREATE_USER_ROLE_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, roles[i]);
            }
            @Override
            public int getBatchSize() {
                return roles.length;
            }
        });
    }

    @Override
    public boolean userExists(String email) {
        try{
            jdbcUserDetailsManager.loadUserByUsername(email);
            return true;
        } catch (UsernameNotFoundException ex){
            return false;
        }
    }
}
