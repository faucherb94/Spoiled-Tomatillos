package edu.northeastern.cs4500.models;

import com.google.gson.annotations.SerializedName;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResult {

    @SerializedName("Title")
    private String title;

    public String getTitle() {
        return this.title;
    }

    @SerializedName("Year")
    private String year;

    public String getYear() {
        return this.year;
    }

    private String imdbID;

    @JsonProperty("imdbID")
    public String getIMDBID() {
        return this.imdbID;
    }

    @SerializedName("Type")
    private String type;

    public String getType() {
        return this.type;
    }

    @SerializedName("Poster")
    private String poster;

    public String getPoster() {
        return this.poster;
    }

    public SearchResult(String title, String year, String imdbID, String type, String poster) {
        this.title = title;
        this.year = year;
        this.imdbID = imdbID;
        this.type = type;
        this.poster = poster;
    }

    @Override
    public String toString() {
        return "Result:\nTitle: " + this.title + "\nYear: " + this.year + "\nIMDB ID: " +
                this.imdbID + "\nType: " + this.type + "\nPoster: " + this.poster + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        SearchResult other = (SearchResult)obj;
        return this.title.equals(other.title) &&
                this.year.equals(other.year) &&
                this.imdbID.equals(other.imdbID) &&
                this.type.equals(other.type) &&
                this.poster.equals(other.poster);
    }

    @Override
    public int hashCode() {
        return this.imdbID.hashCode();
    }
}
