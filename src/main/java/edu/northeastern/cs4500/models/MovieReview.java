package edu.northeastern.cs4500.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"updatedAt"})
public class MovieReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReviewID")
    @Getter @Setter
    private int id;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Getter @Setter
    private String movieID;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Getter @Setter
    private int userID;

    @Getter @Setter
    private String review;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
    @Getter
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Getter
    private Date updatedAt;

    public MovieReview(String movieID, int userID, String review) {
        this.movieID = movieID;
        this.userID = userID;
        this.review = review;
    }

    public MovieReview() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieReview)) return false;
        MovieReview that = (MovieReview)o;

        return Objects.equals(id, that.getId()) &&
                Objects.equals(movieID, that.getMovieID()) &&
                Objects.equals(userID, that.getUserID()) &&
                Objects.equals(review, that.getReview()) &&
                Objects.equals(createdAt, that.getCreatedAt()) &&
                Objects.equals(updatedAt, that.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movieID, userID, review, createdAt, updatedAt);
    }

}
