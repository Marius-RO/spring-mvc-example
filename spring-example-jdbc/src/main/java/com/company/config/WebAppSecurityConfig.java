package com.company.config;

import com.company.controller.AccountController;
import com.company.controller.CategoryController;
import com.company.controller.RootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity(debug = false)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    public interface Roles {
        String ROLE_ADMIN = "ROLE_ADMIN";
        String ROLE_CUSTOMER = "ROLE_CUSTOMER";
        String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";
    }

    private final DataSource dataSource;

    @Autowired
    public WebAppSecurityConfig(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                // Account controller
                .authorizeRequests().antMatchers(AccountController.Security.PERMIT_ALL).permitAll()
                .and()
                .authorizeRequests().antMatchers(RootController.Security.PERMIT_ALL).permitAll()
                .and()
                // CategoryController
                .authorizeRequests().antMatchers(CategoryController.Security.PERMIT_ONLY_ADMIN).hasAuthority(Roles.ROLE_ADMIN)
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/account/login").defaultSuccessUrl("/index", true)
                .and()
                .httpBasic()
                .and()
                .logout()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder getPasswordEncoder(){
        //return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }
}
