package io.github.amsatrio.config.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface DatabaseProvider {
    
    Connection getConnection() throws SQLException;

    void close(Connection connection);
    void close(Statement statement);
    void close(PreparedStatement preparedStatement);
    void close(ResultSet resultSet);
}
