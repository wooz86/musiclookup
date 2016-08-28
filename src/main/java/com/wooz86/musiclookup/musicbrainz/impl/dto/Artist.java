package com.wooz86.musiclookup.musicbrainz.impl.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist implements Serializable {

    @JsonProperty("release-groups")
    private List<ReleaseGroup> releaseGroups = new ArrayList<>();
    private List<Relation> relations = new ArrayList<>();

    public List<ReleaseGroup> getReleaseGroups() {
        return releaseGroups;
    }

    public void setReleaseGroups(List<ReleaseGroup> releaseGroups) {
        this.releaseGroups = releaseGroups;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "releaseGroups='" + releaseGroups + '\'' +
                ", relations='" + relations + '\'' +
                '}';
    }
}
