package com.company.config;

import com.company.controller.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

                .authorizeRequests().antMatchers(getPermitAll()).permitAll()
                .and()
                .authorizeRequests().antMatchers(getPermitOnlyCustomerAndEmployee()).hasAnyAuthority(Roles.ROLE_CUSTOMER, Roles.ROLE_EMPLOYEE)
                .and()
                .authorizeRequests().antMatchers(getPermitOnlyEmployeeAndAdmin()).hasAnyAuthority(Roles.ROLE_EMPLOYEE, Roles.ROLE_ADMIN)
                .and()
                .authorizeRequests().antMatchers(getPermitOnlyAdmin()).hasAuthority(Roles.ROLE_ADMIN)
                .and()
                .authorizeRequests().antMatchers(getPermitOnlyVisitorAndCustomer()).not().hasAnyAuthority(Roles.ROLE_EMPLOYEE, Roles.ROLE_ADMIN)
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin().loginPage(AccountController.PathHandler.FULL_LOGIN_URL).defaultSuccessUrl("/index", true)
                .and()
                .httpBasic()
                .and()
                .logout()
                .and()
                .exceptionHandling().accessDeniedPage(ErrorController.PathHandler.FULL_ERROR_ACCESS_DENIED_URL);
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private String[] getPermitAll(){
        return extractPaths(
                RootController.Security.PERMIT_ALL,
                AccountController.Security.PERMIT_ALL,
                ProductController.Security.PERMIT_ALL,
                ErrorController.Security.PERMIT_ALL
        );
    }

    private String[] getPermitOnlyCustomerAndEmployee(){
        return extractPaths(
                AccountController.Security.PERMIT_ONLY_CUSTOMER_EMPLOYEE
        );
    }

    private String[] getPermitOnlyCustomerAndEmployeeAndAdmin(){
        // this is covered by '.authorizeRequests().anyRequest().authenticated()'
        return extractPaths(
                AccountController.Security.PERMIT_ONLY_CUSTOMER_EMPLOYEE_ADMIN,
                OrderController.Security.PERMIT_ONLY_CUSTOMER_EMPLOYEE_ADMIN
        );
    }

    private String[] getPermitOnlyEmployeeAndAdmin(){
        return extractPaths(
                ProductController.Security.PERMIT_ONLY_EMPLOYEE_ADMIN,
                ErrorController.Security.PERMIT_ONLY_EMPLOYEE_ADMIN
        );
    }

    private String[] getPermitOnlyAdmin(){
        return extractPaths(
                EmployeeController.Security.PERMIT_ONLY_ADMIN,
                CategoryController.Security.PERMIT_ONLY_ADMIN
        );
    }

    private String[] getPermitOnlyVisitorAndCustomer(){
        return extractPaths(
                ProductController.Security.PERMIT_ONLY_VISITOR_CUSTOMER
        );
    }

    private String[] extractPaths(String[]... paths){
        List<String> values = new ArrayList<>();
        for(String[] path : paths){
            values.addAll(Arrays.asList(path));
        }
        return values.toArray(new String[0]);
    }
}
