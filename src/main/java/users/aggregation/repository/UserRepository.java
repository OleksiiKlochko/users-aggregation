package users.aggregation.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.util.CollectionUtils;
import users.aggregation.configuration.DataSourceMapping;
import users.aggregation.data.User;

import java.util.List;
import java.util.stream.Stream;

public class UserRepository {
    private final JdbcClient jdbcClient;
    private final DataSourceMapping dataSourceMapping;
    private final RowMapper<User> userRowMapper;

    public UserRepository(JdbcClient jdbcClient, DataSourceMapping dataSourceMapping) {
        this.jdbcClient = jdbcClient;
        this.dataSourceMapping = dataSourceMapping;

        userRowMapper = (resultSet, rowNum) ->
                new User.Builder()
                        .id(resultSet.getString(dataSourceMapping.id()))
                        .username(resultSet.getString(dataSourceMapping.username()))
                        .name(resultSet.getString(dataSourceMapping.name()))
                        .surname(resultSet.getString(dataSourceMapping.surname()))
                        .build();
    }

    public Stream<User> findUsers(List<String> ids, List<String> usernames, List<String> names, List<String> surnames) {
        var sql = new StringBuilder("SELECT * FROM ").append(dataSourceMapping.table());
        var mapSqlParameterSource = new MapSqlParameterSource();

        if (!CollectionUtils.isEmpty(ids)) {
            sql.append(" WHERE ").append(dataSourceMapping.id()).append(" IN (:ids)");
            mapSqlParameterSource.addValue("ids", ids);
        }
        if (!CollectionUtils.isEmpty(usernames)) {
            sql.append(getAndOrWhere(mapSqlParameterSource)).append(dataSourceMapping.username()).append(" IN (:usernames)");
            mapSqlParameterSource.addValue("usernames", usernames);
        }
        if (!CollectionUtils.isEmpty(names)) {
            sql.append(getAndOrWhere(mapSqlParameterSource)).append(dataSourceMapping.name()).append(" IN (:names)");
            mapSqlParameterSource.addValue("names", names);
        }
        if (!CollectionUtils.isEmpty(surnames)) {
            sql.append(getAndOrWhere(mapSqlParameterSource)).append(dataSourceMapping.surname()).append(" IN (:surnames)");
            mapSqlParameterSource.addValue("surnames", surnames);
        }

        return jdbcClient.sql(sql.toString())
                .paramSource(mapSqlParameterSource)
                .query(userRowMapper)
                .stream();
    }

    private String getAndOrWhere(MapSqlParameterSource mapSqlParameterSource) {
        return mapSqlParameterSource.hasValues() ? " AND " : " WHERE ";
    }
}
