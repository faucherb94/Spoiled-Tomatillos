package edu.northeastern.cs4500.models;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import lombok.Getter;
import lombok.ToString;

@ToString
public class CriticRating {

    @SerializedName("Source")
    @Getter
    private String source;


    @SerializedName("Value")
    @Getter
    private String value;

    public CriticRating(String source, String value) {
        this.source = source;
        this.value = value;
    }

    public CriticRating() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CriticRating)) return false;
        CriticRating that = (CriticRating) o;

        return Objects.equals(source, that.getSource()) &&
                Objects.equals(value, that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, value);
    }

}
