package edu.northeastern.cs4500.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

import edu.northeastern.cs4500.utils.DateTimestampSerializer;
import lombok.Getter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.MINIMAL_CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
public abstract class Snippet {

    @Getter
    private String type;

    @Getter
    private String movieID;

    @JsonSerialize(using = DateTimestampSerializer.class)
    @Getter
    private Date createdAt;

    @JsonSerialize(using = DateTimestampSerializer.class)
    @Getter
    private Date updatedAt;

    public Snippet(String type, String movieID, Date createdAt, Date updatedAt) {
        this.type = type;
        this.movieID = movieID;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Snippet() {}
}
