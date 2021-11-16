package com.company.config;

import com.company.repository.impl.AccountRepositoryImpl;
import com.company.util.GlobalCategoriesInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Objects;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.company"})
@PropertySources( value = {
            @PropertySource(value = "classpath:connectivity.properties"),
            @PropertySource(value = "classpath:default-values.properties")
    }
)
public class WebAppMvcConfig implements WebMvcConfigurer {

    private final Environment environment;
    private final WebApplicationContext webApplicationContext;

    @Autowired
    public WebAppMvcConfig(Environment environment, WebApplicationContext webApplicationContext) {
        this.environment = environment;
        this.webApplicationContext = webApplicationContext;
    }

    @Bean(name = "viewResolver")
    public InternalResourceViewResolver configViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/view/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean(name = "dataSource")
    public DataSource getDataSource(){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(environment.getProperty("dataSource.url"));
        driverManagerDataSource.setUsername(environment.getProperty("dataSource.username"));
        driverManagerDataSource.setPassword(environment.getProperty("dataSource.password"));
        driverManagerDataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("dataSource.driver")));
        return driverManagerDataSource;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate getJdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(getDataSource());
        return jdbcTemplate;
    }

    @Bean(name = "jdbcUserDetailsManager")
    public JdbcUserDetailsManager getJdbcUserDetailsManager(){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(getDataSource());
        jdbcUserDetailsManager.setCreateUserSql(AccountRepositoryImpl.DEF_CREATE_USER_SQL);
        return jdbcUserDetailsManager;
    }


    @Bean(name = "messageSource")
    public MessageSource getValidationMessageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("validation");
        return messageSource;
    }

    @Bean(name = "validator")
    public LocalValidatorFactoryBean getLocalValidator(){
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(getValidationMessageSource());
        return localValidatorFactoryBean;
    }

    @Override
    public Validator getValidator() {
        return getLocalValidator();
    }

    @Bean(name = "timestamp ")
    public Timestamp getCurrentTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webApplicationContext.getBean(GlobalCategoriesInterceptor.class));
    }
}
