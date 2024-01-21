package users.aggregation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.utility.DockerImageName;
import users.aggregation.data.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UsersAggregationApplicationTests {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer1 = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.1"))
            .withDatabaseName("data-base-1")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("scripts/postgres-1.sql");

    @Container
    private static final PostgreSQLContainer<?> postgresContainer2 = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.1"))
            .withDatabaseName("data-base-2")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("scripts/postgres-2.sql");

    @Container
    private static final OracleContainer oracleContainer = new OracleContainer(DockerImageName.parse("gvenzl/oracle-free:23.3"))
            .withDatabaseName("database")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("scripts/oracle.sql");

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.2.0"))
            .withDatabaseName("data-base-4")
            .withUsername("testuser-4")
            .withPassword("testpass-4")
            .withInitScript("scripts/mysql.sql");

    //TODO: refactor when RestTestClient will be available https://github.com/spring-projects/spring-framework/issues/31275
    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("data-sources.url.postgres1", postgresContainer1::getJdbcUrl);
        registry.add("data-sources.url.postgres2", postgresContainer2::getJdbcUrl);
        registry.add("data-sources.url.oracle", oracleContainer::getJdbcUrl);
        registry.add("data-sources.url.mysql", mySQLContainer::getJdbcUrl);
    }

    @DisplayName("GET status 200 and list of all users")
    @Test
    void users() {
        String url = "/users";

        ResponseEntity<User[]> users = restTemplate.getForEntity(url, User[].class);

        assertThat(users.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(users.getBody()).containsExactlyInAnyOrder(
                new User.Builder()
                        .id("example-user-id-1")
                        .username("user-1")
                        .name("User")
                        .surname("Userenko")
                        .build(),
                new User.Builder()
                        .id("example-user-id-2")
                        .username("example-user-id-2")
                        .name("Testuser")
                        .surname("Testov")
                        .build(),
                new User.Builder()
                        .id("example-user-id-3")
                        .username("user-3")
                        .name("John")
                        .surname("Smith")
                        .build(),
                new User.Builder()
                        .id("example-user-id-4")
                        .username("user-4")
                        .name("John")
                        .surname("Doe")
                        .build()
        );
    }

    @DisplayName("GET status 200 and list of users filtered by IDs")
    @Test
    void usersFilterByIds() {
        String url = "/users?ids=example-user-id-1,example-user-id-3";

        ResponseEntity<User[]> users = restTemplate.getForEntity(url, User[].class);

        assertThat(users.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(users.getBody()).containsExactlyInAnyOrder(
                new User.Builder()
                        .id("example-user-id-1")
                        .username("user-1")
                        .name("User")
                        .surname("Userenko")
                        .build(),
                new User.Builder()
                        .id("example-user-id-3")
                        .username("user-3")
                        .name("John")
                        .surname("Smith")
                        .build()
        );
    }

    @DisplayName("GET status 200 and list of users filtered by usernames")
    @Test
    void usersFilterByUsernames() {
        String url = "/users?usernames=user-3,user-4";

        ResponseEntity<User[]> users = restTemplate.getForEntity(url, User[].class);

        assertThat(users.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(users.getBody()).containsExactlyInAnyOrder(
                new User.Builder()
                        .id("example-user-id-3")
                        .username("user-3")
                        .name("John")
                        .surname("Smith")
                        .build(),
                new User.Builder()
                        .id("example-user-id-4")
                        .username("user-4")
                        .name("John")
                        .surname("Doe")
                        .build()
        );
    }

    @DisplayName("GET status 200 and list of users filtered by names")
    @Test
    void usersFilterByNames() {
        String url = "/users?names=User,John";

        ResponseEntity<User[]> users = restTemplate.getForEntity(url, User[].class);

        assertThat(users.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(users.getBody()).containsExactlyInAnyOrder(
                new User.Builder()
                        .id("example-user-id-1")
                        .username("user-1")
                        .name("User")
                        .surname("Userenko")
                        .build(),
                new User.Builder()
                        .id("example-user-id-3")
                        .username("user-3")
                        .name("John")
                        .surname("Smith")
                        .build(),
                new User.Builder()
                        .id("example-user-id-4")
                        .username("user-4")
                        .name("John")
                        .surname("Doe")
                        .build()
        );
    }

    @DisplayName("GET status 200 and list of users filtered by surnames")
    @Test
    void usersFilterBySurnames() {
        String url = "/users?surnames=Testov,Doe";

        ResponseEntity<User[]> users = restTemplate.getForEntity(url, User[].class);

        assertThat(users.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(users.getBody()).containsExactlyInAnyOrder(
                new User.Builder()
                        .id("example-user-id-2")
                        .username("example-user-id-2")
                        .name("Testuser")
                        .surname("Testov")
                        .build(),
                new User.Builder()
                        .id("example-user-id-4")
                        .username("user-4")
                        .name("John")
                        .surname("Doe")
                        .build()
        );
    }

    @DisplayName("GET status 200 and list of users filtered by IDs, usernames, names, and surnames")
    @Test
    void usersFilterByAllParameters() {
        String url = "/users?ids=example-user-id-3&usernames=user-3&names=John&surnames=Smith";

        ResponseEntity<User[]> users = restTemplate.getForEntity(url, User[].class);

        assertThat(users.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(users.getBody()).containsExactly(
                new User.Builder()
                        .id("example-user-id-3")
                        .username("user-3")
                        .name("John")
                        .surname("Smith")
                        .build()
        );
    }

    @DisplayName("GET status 200 and empty list for non-existing users filtered by IDs, usernames, names, and surnames")
    @ParameterizedTest
    @ValueSource(strings = {"/users?ids=unknown", "/users?usernames=unknown", "/users?names=unknown", "/users?surnames=unknown"})
    void usersNotFound(String url) {
        ResponseEntity<User[]> users = restTemplate.getForEntity(url, User[].class);

        assertThat(users.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(users.getBody()).isEmpty();
    }

    @DisplayName("GET status 400 for users filtered by empty IDs, usernames, names, and surnames")
    @ParameterizedTest
    @ValueSource(strings = {"/users?ids=", "/users?usernames=", "/users?names=", "/users?surnames="})
    void usersFilterByEmptyRequestParameters(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
