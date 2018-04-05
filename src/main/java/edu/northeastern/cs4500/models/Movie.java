package edu.northeastern.cs4500.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Movie {

    @Getter @Setter
    private String title;

    @Getter @Setter
    private int year;

    @Getter @Setter
    private String rated;

    @Getter @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMMM yyyy")
    private Date released;

    @Getter @Setter
    private String runtime;

    @Getter @Setter
    private List<String> genres;

    @Getter @Setter
    private List<String> directors;

    @Getter @Setter
    private List<String> writers;

    @Getter @Setter
    private List<String> actors;

    @Getter @Setter
    private String plot;

    @Getter @Setter
    private List<String> languages;

    @Getter @Setter
    private List<String> countries;

    @Getter @Setter
    private String awards;

    @Getter @Setter
    private String poster;

    @Getter @Setter
    private List<CriticRating> ratings;

    @Getter @Setter
    @JsonProperty("imdbID")
    private String imdbID;

    @Getter @Setter
    private String boxOffice;

    @Getter @Setter
    private String production;

    @Getter @Setter
    private String website;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie that = (Movie) o;

        return Objects.equals(title, that.getTitle()) &&
                Objects.equals(year, that.getYear()) &&
                Objects.equals(rated, that.getRated()) &&
                Objects.equals(released, that.getReleased()) &&
                Objects.equals(runtime, that.getRuntime()) &&
                Objects.equals(genres, that.getGenres()) &&
                Objects.equals(directors, that.getDirectors()) &&
                Objects.equals(writers, that.getWriters()) &&
                Objects.equals(actors, that.getActors()) &&
                Objects.equals(plot, that.getPlot()) &&
                Objects.equals(languages, that.getLanguages()) &&
                Objects.equals(countries, that.getCountries()) &&
                Objects.equals(awards, that.getAwards()) &&
                Objects.equals(poster, that.getPoster()) &&
                Objects.equals(ratings, that.getRatings()) &&
                Objects.equals(imdbID, that.getImdbID()) &&
                Objects.equals(boxOffice, that.getBoxOffice()) &&
                Objects.equals(production, that.getProduction()) &&
                Objects.equals(website, that.getWebsite());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, imdbID, poster);
    }
}
