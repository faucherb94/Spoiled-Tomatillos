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
@Table(name = "GroupMemberships")
@EntityListeners(AuditingEntityListener.class)
public class GroupMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MembershipID")
    @Getter
    @Setter
    private int id;

    @Getter @Setter
    @Column(name = "GroupID")
    private int groupID;

    @Getter @Setter
    @Column(name = "UserID")
    private int userID;

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

    public GroupMembership(int groupID, int userID) {
        this.groupID = groupID;
        this.userID = userID;
    }

    public GroupMembership() {}
}
