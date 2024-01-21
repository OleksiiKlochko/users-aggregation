package users.aggregation.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiDocumentationConfiguration {

    @Bean
    public OpenAPI openAPI() {
        var info = new Info()
                .title("Users Aggregation Service")
                .description("Service for aggregating users data from multiple databases")
                .version("0.0.1");

        return new OpenAPI().info(info);
    }
}
