package com.vshpynta.booking.service.db;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class MigrationTest {

    private static final String CHANGE_LOG_FILE = "db/changelog/changelog-current.xml";
    private static final String LIQUIBASE_CONTEXT = "";

    @Autowired
    protected DataSource dataSource;

    @Test
    void shouldUpdateAndRollback() throws LiquibaseException, SQLException {
        var liquibase = getLiquibase();

        liquibase.update(LIQUIBASE_CONTEXT);
        liquibase.rollback(liquibase.getDatabase().getRanChangeSetList().size(), LIQUIBASE_CONTEXT);
        liquibase.update(LIQUIBASE_CONTEXT);
    }

    private Liquibase getLiquibase() throws SQLException, LiquibaseException {
        return new Liquibase(
                CHANGE_LOG_FILE,
                new ClassLoaderResourceAccessor(),
                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                        new JdbcConnection(dataSource.getConnection()))
        );
    }
}
