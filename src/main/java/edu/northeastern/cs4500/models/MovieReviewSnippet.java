package edu.northeastern.cs4500.models;

import java.util.Objects;

import lombok.Getter;

public class MovieReviewSnippet extends Snippet {

    @Getter
    private String review;

    public MovieReviewSnippet(MovieReview r) {
        super("review", r.getMovieID(), r.getCreatedAt(), r.getUpdatedAt());
        this.review = r.getReview();
    }

    public MovieReviewSnippet() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieReviewSnippet)) return false;
        MovieReviewSnippet that = (MovieReviewSnippet) o;

        return Objects.equals(review, that.getReview()) &&
                Objects.equals(getType(), that.getType()) &&
                Objects.equals(getMovieID(), that.getMovieID()) &&
                Objects.equals(getCreatedAt(), that.getCreatedAt()) &&
                Objects.equals(getUpdatedAt(), that.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(review, getType(), getMovieID(), getCreatedAt(), getUpdatedAt());
    }
}
