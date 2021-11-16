package com.company.repository.impl.util;

import com.company.util.AbstractCommonControllerServiceRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;

public abstract class AbstractRepository extends AbstractCommonControllerServiceRepository {

    protected final JdbcTemplate jdbcTemplate;

    public AbstractRepository(WebApplicationContext webApplicationContext, JdbcTemplate jdbcTemplate) {
        super(webApplicationContext);
        this.jdbcTemplate = jdbcTemplate;
    }
}
