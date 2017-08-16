package utils;

import annotations.DatabasePassword;
import annotations.DatabasePort;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.dbcp.BasicDataSource;

@Singleton
public final class ProductionConnectionProvider extends PooledConnectionProvider {
    private static final String jdbcDriver = "com.mysql.jdbc.Driver";
    private static final String dbHost = "localhost";
    private static final String dbUser = "developer";

    private static String dbPort;
    private final String dbPassword;

    @Inject
    public ProductionConnectionProvider(@DatabasePassword String dbPassword, @DatabasePort String dbPort) {
        super();
        this.dbPassword = dbPassword;
        this.dbPort = dbPort;
        configure();
    }

    @Override
    protected void configure(BasicDataSource dataSource) {
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);
        dataSource.setUrl(String.format("jdbc:mysql://%s:%s/%s", dbHost, dbPort, Statements.DB_NAME));

        dataSource.setMinIdle(5);
    }
}
