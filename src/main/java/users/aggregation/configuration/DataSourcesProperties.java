package users.aggregation.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties
public record DataSourcesProperties(List<DataSource> dataSources) {

    public record DataSource(
            String name,
            String strategy,
            String url,
            String table,
            String user,
            String password,
            Mapping mapping
    ) {

        public record Mapping(
                String id,
                String username,
                String name,
                String surname
        ) {
        }
    }
}
