package com.company.repository.impl.util;

import com.company.util.AbstractCommonControllerServiceRepository;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractRepository extends AbstractCommonControllerServiceRepository {

    @Deprecated
    protected JdbcTemplate jdbcTemplate;

    // TODO: make this variable final
    private LocalSessionFactoryBean sessionFactory;

    @Deprecated
    public AbstractRepository(WebApplicationContext webApplicationContext, JdbcTemplate jdbcTemplate) {
        super(webApplicationContext);
        this.jdbcTemplate = jdbcTemplate;
    }

    public AbstractRepository(WebApplicationContext webApplicationContext, LocalSessionFactoryBean sessionFactory) {
        super(webApplicationContext);
        this.sessionFactory = sessionFactory;
    }

    protected <R> R executeFetchOperation(Function<Session, R> function){
        Session session = openNewSession();
        R result = function.apply(session);
        session.close();
        return result;
    }

    protected void executeInsertOperation(Consumer<Session> consumer){
        executeInsertDeleteUpdateOperations(consumer);
    }

    protected void executeUpdateOperation(Consumer<Session> consumer){
        executeInsertDeleteUpdateOperations(consumer);
    }

    protected void executeDeleteOperation(Consumer<Session> consumer){
        executeInsertDeleteUpdateOperations(consumer);
    }

    private void executeInsertDeleteUpdateOperations(Consumer<Session> consumer){
        Session session = openNewSession();
        session.beginTransaction();

        consumer.accept(session);

        session.getTransaction().commit();
        session.close();
    }

    private Session openNewSession(){
        return Objects.requireNonNull(sessionFactory.getObject()).openSession();
    }

}
