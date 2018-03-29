package edu.northeastern.cs4500.models;

import java.util.Objects;

import lombok.Getter;

public class MovieRatingSnippet extends Snippet {

    @Getter
    private int rating;

    public MovieRatingSnippet(MovieRating r) {
        super("rating", r.getMovieID(), r.getCreatedAt(), r.getUpdatedAt());
        this.rating = r.getRating();
    }

    public MovieRatingSnippet() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieRatingSnippet)) return false;
        MovieRatingSnippet that = (MovieRatingSnippet) o;

        return Objects.equals(rating, that.getRating()) &&
                Objects.equals(getType(), that.getType()) &&
                Objects.equals(getMovieID(), that.getMovieID()) &&
                Objects.equals(getCreatedAt(), that.getCreatedAt()) &&
                Objects.equals(getUpdatedAt(), that.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(rating, getType(), getMovieID(), getCreatedAt(), getUpdatedAt());
    }
}
