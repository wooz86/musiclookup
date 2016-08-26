package com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseGroup implements Serializable {

    private UUID id;

    @JsonProperty("primary-type")
    private String primaryType;

    private String title;

    private String image;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ReleaseGroup{" +
                "primaryType=" + primaryType +
                ", title=" + title +
                ", id=" + id +
                '}';
    }
}
