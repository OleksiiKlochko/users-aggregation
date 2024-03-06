package users.aggregation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UsersAggregationApplicationTests {

    @Container
    private static final PostgreSQLContainer<?> postgres1 = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.2"))
            .withDatabaseName("data-base-1")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("scripts/postgres-1.sql");

    @Container
    private static final PostgreSQLContainer<?> postgres2 = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.2"))
            .withDatabaseName("data-base-2")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("scripts/postgres-2.sql");

    @Container
    private static final OracleContainer oracle = new OracleContainer(DockerImageName.parse("gvenzl/oracle-free:23.3"))
            .withDatabaseName("database")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("scripts/oracle.sql");

    @Container
    private static final MySQLContainer<?> mySQL = new MySQLContainer<>(DockerImageName.parse("mysql:8.3.0"))
            .withDatabaseName("data-base-4")
            .withUsername("testuser-4")
            .withPassword("testpass-4")
            .withInitScript("scripts/mysql.sql");

    @Autowired
    private WebTestClient webTestClient;

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("data-sources.url.postgres1", postgres1::getJdbcUrl);
        registry.add("data-sources.url.postgres2", postgres2::getJdbcUrl);
        registry.add("data-sources.url.oracle", oracle::getJdbcUrl);
        registry.add("data-sources.url.mysql", mySQL::getJdbcUrl);
    }

    @DisplayName("GET status 200 and list of all users")
    @Test
    void users() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(User.class).hasSize(4).contains(
                        new User()
                                .id("example-user-id-1")
                                .username("user-1")
                                .name("User")
                                .surname("Userenko"),
                        new User()
                                .id("example-user-id-2")
                                .username("example-user-id-2")
                                .name("Testuser")
                                .surname("Testov"),
                        new User()
                                .id("example-user-id-3")
                                .username("user-3")
                                .name("John")
                                .surname("Smith"),
                        new User()
                                .id("example-user-id-4")
                                .username("user-4")
                                .name("John")
                                .surname("Doe")
                );
    }

    @DisplayName("GET status 200 and list of users filtered by IDs")
    @Test
    void usersFilterByIds() {
        webTestClient.get().uri("/users?ids=example-user-id-1,example-user-id-3")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(User.class).hasSize(2).contains(
                        new User()
                                .id("example-user-id-1")
                                .username("user-1")
                                .name("User")
                                .surname("Userenko"),
                        new User()
                                .id("example-user-id-3")
                                .username("user-3")
                                .name("John")
                                .surname("Smith")
                );
    }

    @DisplayName("GET status 200 and list of users filtered by usernames")
    @Test
    void usersFilterByUsernames() {
        webTestClient.get().uri("/users?usernames=user-3,user-4")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(User.class).hasSize(2).contains(
                        new User()
                                .id("example-user-id-3")
                                .username("user-3")
                                .name("John")
                                .surname("Smith"),
                        new User()
                                .id("example-user-id-4")
                                .username("user-4")
                                .name("John")
                                .surname("Doe")
                );
    }

    @DisplayName("GET status 200 and list of users filtered by names")
    @Test
    void usersFilterByNames() {
        webTestClient.get().uri("/users?names=User,John")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(User.class).hasSize(3).contains(
                        new User()
                                .id("example-user-id-1")
                                .username("user-1")
                                .name("User")
                                .surname("Userenko"),
                        new User()
                                .id("example-user-id-3")
                                .username("user-3")
                                .name("John")
                                .surname("Smith"),
                        new User()
                                .id("example-user-id-4")
                                .username("user-4")
                                .name("John")
                                .surname("Doe")
                );
    }

    @DisplayName("GET status 200 and list of users filtered by surnames")
    @Test
    void usersFilterBySurnames() {
        webTestClient.get().uri("/users?surnames=Testov,Doe")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(User.class).hasSize(2).contains(
                        new User()
                                .id("example-user-id-2")
                                .username("example-user-id-2")
                                .name("Testuser")
                                .surname("Testov"),
                        new User()
                                .id("example-user-id-4")
                                .username("user-4")
                                .name("John")
                                .surname("Doe")
                );
    }

    @DisplayName("GET status 200 and list of users filtered by IDs, usernames, names, and surnames")
    @Test
    void usersFilterByAllParameters() {
        webTestClient.get().uri("/users?ids=example-user-id-3&usernames=user-3&names=John&surnames=Smith")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(User.class).hasSize(1).contains(
                        new User()
                                .id("example-user-id-3")
                                .username("user-3")
                                .name("John")
                                .surname("Smith")
                );
    }

    @DisplayName("GET status 200 and empty list for non-existing users filtered by IDs, usernames, names, and surnames")
    @ParameterizedTest
    @ValueSource(strings = {"/users?ids=unknown", "/users?usernames=unknown", "/users?names=unknown", "/users?surnames=unknown"})
    void usersNotFound(String uri) {
        webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(User.class).hasSize(0);
    }

    @DisplayName("GET status 400 for users filtered by empty IDs, usernames, names, and surnames")
    @ParameterizedTest
    @ValueSource(strings = {"/users?ids=", "/users?usernames=", "/users?names=", "/users?surnames="})
    void usersFilterByEmptyRequestParameters(String uri) {
        webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }
}
