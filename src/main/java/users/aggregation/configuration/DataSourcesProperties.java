package users.aggregation.configuration;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties
@Validated
public record DataSourcesProperties(List<@NotNull @Valid DataSource> dataSources) {

    public record DataSource(
            @NotEmpty String name,
            @NotEmpty String strategy,
            @NotEmpty String url,
            @NotEmpty String table,
            @NotEmpty String user,
            @NotEmpty String password,
            @NotNull @Valid Mapping mapping
    ) {

        public record Mapping(
                @NotEmpty String id,
                @NotEmpty String username,
                @NotEmpty String name,
                @NotEmpty String surname
        ) {
        }
    }
}
