package users.aggregation.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import users.aggregation.repository.UserRepository;

@Configuration
public class UserRepositoriesConfiguration {
    private final GenericApplicationContext applicationContext;
    private final DataSourcesProperties dataSourcesProperties;

    public UserRepositoriesConfiguration(GenericApplicationContext applicationContext, DataSourcesProperties dataSourcesProperties) {
        this.applicationContext = applicationContext;
        this.dataSourcesProperties = dataSourcesProperties;
    }

    @PostConstruct
    public void configure() {
        dataSourcesProperties.dataSources().forEach(dataSource -> applicationContext.registerBean(
                dataSource.name(),
                UserRepository.class,
                () -> createUserRepository(dataSource)
        ));
    }

    private UserRepository createUserRepository(DataSourcesProperties.DataSource dataSource) {
        JdbcClient jdbcClient = createJdbcClient(dataSource);
        DataSourceMapping dataSourceMapping = createDataSourceMapping(dataSource);
        return new UserRepository(jdbcClient, dataSourceMapping);
    }

    private JdbcClient createJdbcClient(DataSourcesProperties.DataSource dataSource) {
        //TODO: fail fast for now, need to check the expected behavior for this case
        // probably it is OK to skip unrecognized data source
        String driverClassName = DatabaseDriverClassName.of(dataSource.strategy())
                .orElseThrow(() -> new IllegalArgumentException("Unknown database strategy: " + dataSource.strategy()));

        var driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(driverClassName);
        driverManagerDataSource.setUrl(dataSource.url());
        driverManagerDataSource.setUsername(dataSource.user());
        driverManagerDataSource.setPassword(dataSource.password());
        return JdbcClient.create(driverManagerDataSource);
    }

    private DataSourceMapping createDataSourceMapping(DataSourcesProperties.DataSource dataSource) {
        return new DataSourceMapping.Builder()
                .table(dataSource.table())
                .id(dataSource.mapping().id())
                .username(dataSource.mapping().username())
                .name(dataSource.mapping().name())
                .surname(dataSource.mapping().surname())
                .build();
    }
}
