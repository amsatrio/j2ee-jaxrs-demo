package io.github.amsatrio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.github.amsatrio.annotation.H2Qualifier;
import io.github.amsatrio.config.database.DatabaseProvider;
import io.github.amsatrio.dto.MBiodataDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class MBiodataDao {
    @Inject
    @H2Qualifier
    private DatabaseProvider databaseProvider;

    @PostConstruct
    public void init() {
        Connection connection = null;
        try {
            connection = databaseProvider.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS m_biodata (\n" + //
                    "    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Auto increment',\n" + //
                    "    fullname VARCHAR(255),\n" + //
                    "    mobile_phone VARCHAR(15),\n" + //
                    "    image BLOB,\n" + //
                    "    image_path VARCHAR(255),\n" + //
                    "    created_by BIGINT NOT NULL,\n" + //
                    "    created_on DATETIME NOT NULL,\n" + //
                    "    modified_by BIGINT,\n" + //
                    "    modified_on DATETIME,\n" + //
                    "    deleted_by BIGINT,\n" + //
                    "    deleted_on DATETIME,\n" + //
                    "    is_delete BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'default FALSE',\n" + //
                    "    PRIMARY KEY (id)\n" + //
                    ");");
            log.info("Book table created or already exists.");
        } catch (SQLException e) {
            log.error("Error initializing database: " + e.getMessage(), e);
        } finally {

            databaseProvider.close(connection);

        }
    }

    public MBiodataDto findById(Long id) {
        String sql = "SELECT * FROM m_biodata WHERE id = ?";
        Connection connection = null;
        try {
            connection = databaseProvider.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) { // Move cursor to the first (and only) row
                    return MBiodataDto.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            databaseProvider.close(connection);

        }
        return null; // No record found
    }

    public List<MBiodataDto> findAll() throws SQLException {
        List<MBiodataDto> biodataList = new ArrayList<>();
        String sql = "SELECT * FROM m_biodata";

        Connection connection = null;
        try {
            connection = databaseProvider.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                biodataList.add(MBiodataDto.mapRow(rs));
            }
        } finally {

            databaseProvider.close(connection);

        }
        return biodataList;
    }

    public int deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM m_biodata WHERE id = ?";

        Connection connection = null;
        try {
            connection = databaseProvider.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id); // Set the ID parameter
            return statement.executeUpdate(); // Returns number of affected rows
        } finally {

            databaseProvider.close(connection);

        }
    }

    public int update(MBiodataDto biodata) throws SQLException {
        // SQL UPDATE statement.
        // We update all fields that are typically mutable, including modified_by/on and
        // is_delete.
        String sql = "UPDATE m_biodata SET " +
                "fullname = ?, mobile_phone = ?, image = ?, image_path = ?, " +
                "modified_by = ?, modified_on = ?, deleted_by = ?, deleted_on = ?, is_delete = ? " +
                "WHERE id = ?";
        Connection connection = null;
        try {
            connection = databaseProvider.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set parameters for the PreparedStatement
            statement.setString(1, biodata.getFullname());
            statement.setString(2, biodata.getMobilePhone());
            statement.setBytes(3, biodata.getImage());
            statement.setString(4, biodata.getImagePath());
            statement.setLong(5, biodata.getModifiedBy() != null ? biodata.getModifiedBy() : 0); // Handle null for
                                                                                                 // primitive long
            if (biodata.getModifiedBy() == null)
                statement.setNull(5, java.sql.Types.BIGINT); // Explicitly set null if needed
            else
                statement.setLong(5, biodata.getModifiedBy());

            statement.setTimestamp(6,
                    biodata.getModifiedOn() != null ? Timestamp.valueOf(biodata.getModifiedOn()) : null);

            if (biodata.getDeletedBy() == null)
                statement.setNull(7, java.sql.Types.BIGINT);
            else
                statement.setLong(7, biodata.getDeletedBy());

            statement.setTimestamp(8,
                    biodata.getDeletedOn() != null ? Timestamp.valueOf(biodata.getDeletedOn()) : null);

            statement.setBoolean(9, biodata.getIsDelete() != null ? biodata.getIsDelete() : false);

            statement.setLong(10, biodata.getId()); // WHERE clause condition

            // Execute the update statement
            return statement.executeUpdate(); // Returns number of affected rows
        } finally {
            databaseProvider.close(connection);
        }
    }

    public MBiodataDto create(MBiodataDto biodata) throws SQLException {
        // SQL INSERT statement.
        // We omit 'id' as it's auto-incremented.
        // 'created_on' and 'is_delete' have defaults, but we set them explicitly for
        // clarity
        // and to match the Java object's state.
        String sql = "INSERT INTO m_biodata (" +
                "fullname, mobile_phone, image, image_path, created_by, created_on, is_delete" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Use try-with-resources to ensure PreparedStatement and ResultSet are closed
        // automatically.
        // Statement.RETURN_GENERATED_KEYS is crucial to get the auto-generated 'id'.
        Connection connection = null;
        try {
            connection = databaseProvider.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set parameters for the PreparedStatement
            statement.setString(1, biodata.getFullname());
            statement.setString(2, biodata.getMobilePhone());
            statement.setBytes(3, biodata.getImage()); // Handles null automatically
            statement.setString(4, biodata.getImagePath()); // Handles null automatically
            statement.setLong(5, biodata.getCreatedBy());
            // Convert LocalDateTime to Timestamp for JDBC
            statement.setTimestamp(6,
                    biodata.getCreatedOn() != null ? Timestamp.from(biodata.getCreatedOn().toInstant()) : null);
            statement.setBoolean(7, biodata.getIsDelete() != null ? biodata.getIsDelete() : false); // Default to false
                                                                                                    // if null

            // Execute the insert statement
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating biodata failed, no rows affected.");
            }

            // Retrieve the auto-generated ID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    biodata.setId(generatedKeys.getLong(1)); // Set the generated ID back to the object
                } else {
                    throw new SQLException("Creating biodata failed, no ID obtained.");
                }
            }
        } finally {
            databaseProvider.close(connection);
        }
        return biodata;
    }
}
