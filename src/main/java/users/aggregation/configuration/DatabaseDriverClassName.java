package users.aggregation.configuration;

import java.util.Optional;

public enum DatabaseDriverClassName {
    //TODO: support more databases
    POSTGRES("org.postgresql.Driver"),
    ORACLE("oracle.jdbc.driver.OracleDriver"),
    MYSQL("com.mysql.cj.jdbc.Driver");

    private final String driver;

    DatabaseDriverClassName(String driver) {
        this.driver = driver;
    }

    public static Optional<String> of(String databaseProviderName) {
        for (DatabaseDriverClassName databaseDriverClassName : values()) {
            if (databaseDriverClassName.name().equalsIgnoreCase(databaseProviderName)) {
                return Optional.of(databaseDriverClassName.driver);
            }
        }
        return Optional.empty();
    }
}
