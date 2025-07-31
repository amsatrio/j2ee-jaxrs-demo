package io.github.amsatrio.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MBiodataDto {
    @JsonProperty("id")
    @NotNull(message = "ID cannot be null for update operations")
    @Min(value = 1, message = "ID must be a positive number")
    private Long id;

    @JsonProperty("fullname")
    @NotBlank(message = "Fullname cannot be empty")
    @Size(max = 255, message = "Fullname must not exceed 255 characters")
    private String fullname;

    @JsonProperty("mobilePhone")
    @NotBlank(message = "Mobile phone cannot be empty")
    @Size(min = 8, max = 15, message = "Mobile phone must be between 8 and 15 characters")
    @Pattern(regexp = "^[0-9]+$", message = "Mobile phone can only contain digits")
    private String mobilePhone;

    @JsonProperty("image")
    // @NotNull(message = "Image cannot be null")
    // @Size(max = 1048576, message = "Image size exceeds 1MB")
    private byte[] image;

    @JsonProperty("imagePath")
    @Size(max = 255, message = "Image path must not exceed 255 characters")
    private String imagePath;

    @JsonProperty("createdBy")
    @NotNull(message = "CreatedBy cannot be null")
    @Min(value = 1, message = "CreatedBy must be a positive number")
    private Long createdBy;

    @JsonProperty("createdOn")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "CreatedOn cannot be null")
    @PastOrPresent(message = "CreatedOn must be in the past or present")
    private Date createdOn;

    @JsonProperty("modifiedBy")
    @Min(value = 1, message = "ModifiedBy must be a positive number")
    private Long modifiedBy;

    @JsonProperty("modifiedOn")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent(message = "ModifiedOn must be in the past or present")
    private Date modifiedOn;

    @JsonProperty("deletedBy")
    @Min(value = 1, message = "DeletedBy must be a positive number")
    private Long deletedBy;

    @JsonProperty("deletedOn")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent(message = "DeletedOn must be in the past or present")
    private LocalDateTime deletedOn;

    @JsonProperty("isDelete")
    @NotNull(message = "IsDelete cannot be null")
    private Boolean isDelete;

    /**
     * Maps a row from a JDBC ResultSet to an MBiodata object.
     * This function assumes the ResultSet's cursor is already positioned at the desired row.
     *
     * @param rs The ResultSet containing the data.
     * @return An MBiodata object populated with data from the current ResultSet row.
     * @throws SQLException If a database access error occurs or the column is not found.
     */
    public static MBiodataDto mapRow(ResultSet rs) throws SQLException {
        MBiodataDto biodata = new MBiodataDto();

        // Retrieve data for each column.
        // Use column names as defined in the SQL CREATE TABLE statement.
        // Handle nullable columns carefully.

        biodata.setId(rs.getLong("id"));
        biodata.setFullname(rs.getString("fullname"));
        biodata.setMobilePhone(rs.getString("mobile_phone")); // Use snake_case for column name

        // For BLOB (image), getBytes() returns null if the column is SQL NULL
        byte[] imageBytes = rs.getBytes("image");
        if (rs.wasNull()) { // Check if the last read column was SQL NULL
            biodata.setImage(null);
        } else {
            biodata.setImage(imageBytes);
        }

        String imagePath = rs.getString("image_path");
        if (rs.wasNull()) {
            biodata.setImagePath(null);
        } else {
            biodata.setImagePath(imagePath);
        }

        biodata.setCreatedBy(rs.getLong("created_by")); // Not nullable
        // For DATETIME to LocalDateTime, getTimestamp() returns null if the column is SQL NULL
        Timestamp createdOnTimestamp = rs.getTimestamp("created_on");
        if (createdOnTimestamp != null) {
            biodata.setCreatedOn(new Date(createdOnTimestamp.toInstant().toEpochMilli()));
        } else {
            // This case should ideally not happen for a NOT NULL column, but good practice for robustness
            biodata.setCreatedOn(null);
        }

        Long modifiedBy = rs.getLong("modified_by");
        if (rs.wasNull()) {
            biodata.setModifiedBy(null);
        } else {
            biodata.setModifiedBy(modifiedBy);
        }

        Timestamp modifiedOnTimestamp = rs.getTimestamp("modified_on");
        if (modifiedOnTimestamp != null) {
            biodata.setModifiedOn(new Date(modifiedOnTimestamp.toInstant().toEpochMilli()));
        } else {
            biodata.setModifiedOn(null);
        }

        Long deletedBy = rs.getLong("deleted_by");
        if (rs.wasNull()) {
            biodata.setDeletedBy(null);
        } else {
            biodata.setDeletedBy(deletedBy);
        }

        Timestamp deletedOnTimestamp = rs.getTimestamp("deleted_on");
        if (deletedOnTimestamp != null) {
            biodata.setDeletedOn(deletedOnTimestamp.toLocalDateTime());
        } else {
            biodata.setDeletedOn(null);
        }

        // For BOOLEAN, getBoolean() returns false for SQL NULL, so use getObject() and cast or check wasNull()
        Object isDeleteObj = rs.getObject("is_delete");
        if (isDeleteObj instanceof Boolean) {
            biodata.setIsDelete((Boolean) isDeleteObj);
        } else if (isDeleteObj == null) {
            // This case should not happen for a NOT NULL column with DEFAULT FALSE
            biodata.setIsDelete(false); // Defaulting to false if somehow null
        } else {
            // Handle other potential types if necessary, e.g., tinyint(1) in MySQL
            biodata.setIsDelete(rs.getBoolean("is_delete"));
        }


        return biodata;
    }
}
