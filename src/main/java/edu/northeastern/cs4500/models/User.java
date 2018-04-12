package edu.northeastern.cs4500.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import edu.northeastern.cs4500.utils.DateTimestampSerializer;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a UserAccount in the DB
 */
@Entity
@Table(name = "UserAccount")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    @Getter @Setter
    private int id;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String firstName;

    @Getter @Setter
    private String lastName;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String role;

    @Getter @Setter
    private String hometown;

    @Column(name = "DisplayPicture")
    @Getter @Setter
    private String picture;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @JsonSerialize(using = DateTimestampSerializer.class)
    @Getter
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @JsonSerialize(using = DateTimestampSerializer.class)
    @Getter
    private Date updatedAt;

    public User(String username, String firstName, String lastName, String email,
                String role, String hometown, String picture) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.hometown = hometown;
        this.picture = picture;
    }

    public User() {}
}
