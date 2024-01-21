package users.aggregation.configuration;

public record DataSource(String name, String strategy, String url, String table, String user, String password, Mapping mapping) {

    public record Mapping(String id, String username, String name, String surname) {
    }
}
