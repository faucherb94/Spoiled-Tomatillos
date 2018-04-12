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

@Entity
@Table(name = "Groups")
@EntityListeners(AuditingEntityListener.class)
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GroupID")
    @Getter
    @Setter
    private int id;

    @Getter @Setter
    @Column(name = "CreatorID")
    private int creatorID;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private byte[] picture;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimestampSerializer.class)
    @CreatedDate
    @Getter
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimestampSerializer.class)
    @LastModifiedDate
    @Getter
    private Date updatedAt;

    public Group(int creatorID, String name, String description) {
        this.creatorID = creatorID;
        this.name = name;
        this.description = description;
    }

    public Group() {}
}
