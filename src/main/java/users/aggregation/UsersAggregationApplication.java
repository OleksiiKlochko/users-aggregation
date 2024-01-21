package users.aggregation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class UsersAggregationApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersAggregationApplication.class, args);
    }
}
