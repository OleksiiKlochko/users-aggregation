package users.aggregation.data;

public record User(String id, String username, String name, String surname) {

    public static final class Builder {
        private String id;
        private String username;
        private String name;
        private String surname;

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

        public User build() {
            return new User(id, username, name, surname);
        }
    }
}
