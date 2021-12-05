package com.company.util;

import com.company.model.UserAccount;
import com.company.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HibernateUserDetailsManager implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Autowired
    public HibernateUserDetailsManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = accountRepository.getUser(username);
        if(userAccount == null){
            throw new UsernameNotFoundException("Username does not exists");
        }

        return userDetailsConversion(userAccount);
    }

    private UserDetails userDetailsConversion(UserAccount userAccount){
        List<GrantedAuthority> authoritiesList = userAccount.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());
        return new User(userAccount.getEmail(), userAccount.getPassword(), authoritiesList);
    }
}
