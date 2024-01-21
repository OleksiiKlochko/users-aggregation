package users.aggregation.configuration;

public record DataSourceMapping(String table, String id, String username, String name, String surname) {

    public static final class Builder {
        private String table;
        private String id;
        private String username;
        private String name;
        private String surname;

        public Builder table(String table) {
            this.table = table;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public DataSourceMapping build() {
            return new DataSourceMapping(table, id, username, name, surname);
        }
    }
}
