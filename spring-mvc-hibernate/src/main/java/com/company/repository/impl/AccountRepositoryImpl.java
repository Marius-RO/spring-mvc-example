package com.company.repository.impl;

import com.company.model.Authority;
import com.company.model.UserAccount;
import com.company.repository.AccountRepository;
import com.company.repository.impl.util.AbstractRepository;
import com.company.util.IncorrectPasswordException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AccountRepositoryImpl extends AbstractRepository implements AccountRepository {

    private static final String DEF_UPDATE_USER_PROFILE_HQL =
            "UPDATE UserAccount u " +
            "SET u.firstName = ?1, u.lastName = ?2, u.address.fullAddress = ?3, u.address.phone = ?4 " +
            "WHERE u.email = ?5";

    private static final String DEF_UPDATE_USER_PASSWORD_HQL =
            "UPDATE UserAccount u " +
            "SET u.password = ?1" +
            "WHERE u.email = ?2";


    @Autowired
    public AccountRepositoryImpl(WebApplicationContext webApplicationContext, LocalSessionFactoryBean sessionFactory) {
        super(webApplicationContext, sessionFactory);
    }

    @Override
    protected Class<?> getClassType() {
        return AccountRepositoryImpl.class;
    }

    @Override
    public void insertUser(UserAccount userAccount, String... roles) {
        super.executeInsertOperation(session -> executeInsertUser(session, userAccount, roles));
    }

    private void executeInsertUser(Session session, UserAccount userAccount, String... roles){
        List<Authority> authorityList = createAuthorities(userAccount, roles);

        session.save(userAccount);
        for(Authority auth : authorityList){
            session.save(auth);
        }
    }

    private List<Authority> createAuthorities(UserAccount userAccount, String... roles){
        return Arrays.stream(roles)
                .map(role -> new Authority(role, userAccount))
                .collect(Collectors.toList());
    }

    @Override
    public boolean userExists(String email) {
        return getUser(email) != null;
    }

    @Nullable
    @Override
    public UserAccount getUser(String email) {
        return super.executeFetchOperation(session -> session.get(UserAccount.class, email));
    }

    @Override
    public void updateUser(UserAccount userAccount) {
        super.executeUpdateOperation(session -> executeUserUpdate(session, userAccount));
    }

    private void executeUserUpdate(Session session, UserAccount userAccount){
        session.createQuery(DEF_UPDATE_USER_PROFILE_HQL)
                .setParameter(1, userAccount.getFirstName())
                .setParameter(2, userAccount.getLastName())
                .setParameter(3, userAccount.getAddress().getFullAddress())
                .setParameter(4, userAccount.getAddress().getPhone())
                .setParameter(5, userAccount.getEmail())
                .executeUpdate();
    }

    @Override
    public boolean tryToUpdatePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("User is not logged in");
        }

        UserAccount userAccount = getUser(currentUser.getName());
        if(userAccount == null){
            throw new UsernameNotFoundException("User does not exists");
        }

        PasswordEncoder passwordEncoder = webApplicationContext.getBean(PasswordEncoder.class);
        if(!passwordEncoder.matches(oldPassword, userAccount.getPassword())){
            throw new IncorrectPasswordException("The old password is not correct");
        }

        super.executeUpdateOperation(session -> updatePassword(session, passwordEncoder, newPassword , userAccount));
        return true;
    }

    private void updatePassword(Session session, PasswordEncoder passwordEncoder, String newPassword, UserAccount userAccount){
        session.createQuery(DEF_UPDATE_USER_PASSWORD_HQL)
                .setParameter(1, passwordEncoder.encode(newPassword))
                .setParameter(2, userAccount.getEmail())
                .executeUpdate();
    }

    @Override
    public void deleteUser(String email) {
        UserAccount userAccount = getUser(email);
        if(userAccount == null){
            return;
        }

        super.executeDeleteOperation(session -> session.remove(userAccount));
    }
}
