package com.company.repository.impl;

import com.company.model.UserAccount;
import com.company.repository.AccountRepository;
import com.company.repository.impl.util.AbstractRepository;
import com.company.repository.mappers.user.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.WebApplicationContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class AccountRepositoryImpl extends AbstractRepository implements AccountRepository {

    public static final String DEF_CREATE_USER_SQL = "INSERT INTO users (username, password, enabled, added) VALUES (?, ?, ?, ?)";
    private static final String DEF_CREATE_USER_ROLE_SQL = "INSERT INTO authorities (username, authority) VALUES (?, ?)";
    private static final String DEF_EXTRACT_USER_SQL = "SELECT username, last_name, first_name, added, full_address, phone FROM users WHERE username = ?";
    private static final String DEF_UPDATE_USER_SQL = "UPDATE users " +
                                                      "SET first_name = ?, last_name = ?, full_address = ?, phone = ? " +
                                                      "WHERE username = ?";

    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    @Autowired
    public AccountRepositoryImpl(WebApplicationContext webApplicationContext, JdbcTemplate jdbcTemplate,
                                 JdbcUserDetailsManager jdbcUserDetailsManager) {
        super(webApplicationContext, jdbcTemplate);
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

    @Nullable
    @Override
    public UserAccount getUser(String email) {
        try{
            return jdbcTemplate.queryForObject(DEF_EXTRACT_USER_SQL, new Object[]{email}, webApplicationContext.getBean(UserRowMapper.class));
        } catch (DataAccessException ex){
            return null;
        }
    }

    @Override
    public void updateUser(UserAccount userAccount) {
        Object[] args = {
                userAccount.getFirstName(),
                userAccount.getLastName(),
                userAccount.getAddress() == null ? "" : userAccount.getAddress().getFullAddress(),
                userAccount.getAddress() == null ? "" : userAccount.getAddress().getPhone(),
                userAccount.getEmail()
        };

        jdbcTemplate.update(DEF_UPDATE_USER_SQL, args);
    }

    @Override
    public boolean tryToUpdatePassword(String oldPassword, String newPassword) {
        try {
            jdbcUserDetailsManager.changePassword(oldPassword, newPassword);
            return true;
        }
        catch (AuthenticationException ex){
            return false;
        }
    }

    @Override
    public void deleteUser(String email) {
        jdbcUserDetailsManager.deleteUser(email);
    }
}
