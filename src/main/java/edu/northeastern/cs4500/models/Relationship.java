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
@Table(name = "Relationships")
@EntityListeners(AuditingEntityListener.class)
public class Relationship {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RelationshipID")
	@Getter @Setter
	private int id;
	
    @Column(name = "UserID1")
    @Getter
    @Setter
    private int userID1;

    @Getter @Setter
    @Column(name = "UserID2")
    private int userID2;

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

    public Relationship(int uid1, int uid2) {
        this.userID1 = uid1;
        this.userID2 = uid2;
    }

    public Relationship() {}
}
