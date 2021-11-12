package com.company.repository.impl.util;

import com.company.util.AbstractCommonControllerServiceRepository;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractRepository extends AbstractCommonControllerServiceRepository {

    protected final JdbcTemplate jdbcTemplate;

    public AbstractRepository(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
     }
}
