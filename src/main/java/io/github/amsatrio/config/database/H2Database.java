package io.github.amsatrio.config.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import io.github.amsatrio.annotation.H2Qualifier;

@H2Qualifier
@ApplicationScoped
public class H2Database implements DatabaseProvider {

    @Resource(name = "jdbc/H2DataSource")
    private DataSource dataSource;

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    @Override
    public void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
        }
    }

    // Overloaded close method for PreparedStatement
    public void close(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }

    @Override
    public void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }
    }

}
