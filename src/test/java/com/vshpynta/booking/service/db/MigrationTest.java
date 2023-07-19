package com.vshpynta.booking.service.db;

import com.playtika.test.common.spring.DockerPresenceBootstrapConfiguration;
import com.playtika.test.mariadb.EmbeddedMariaDBBootstrapConfiguration;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest(
        classes = {
                DockerPresenceBootstrapConfiguration.class,
                EmbeddedMariaDBBootstrapConfiguration.class,
                MigrationTest.MigrationTestConfiguration.class
        },
        properties = {
                "embedded.mariadb.database=booking_db"
        }
)
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

    @EnableAutoConfiguration
    @Configuration
    static class MigrationTestConfiguration {

    }
}
