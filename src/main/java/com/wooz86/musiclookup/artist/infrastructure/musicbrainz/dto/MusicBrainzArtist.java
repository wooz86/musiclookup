package com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicBrainzArtist implements Serializable {

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

    public String getWikipediaPageUrl() {
        Url url = getWikipediaRelation().getUrl();
        return url.getResource();
    }

    private Relation getWikipediaRelation() {
        return getRelationsByType("wikipedia").get(0);
    }

    // @todo Skeptic if this is really allowed to be here in this DataStructure object
    private List<Relation> getRelationsByType(String relationType) {
        List<Relation> relationsFound = relations
                .stream()
                .filter(p -> p.getType().equals(relationType))
                .collect(Collectors.toList());

        if (relationsFound.isEmpty()) {
            throw new IllegalArgumentException("No relations with type \"" + relationType + "\" found.");
        }

        return relationsFound;
    }

    @Override
    public String toString() {
        return "MusicBrainzArtist{" +
                "releaseGroups='" + releaseGroups + '\'' +
                ", relations='" + relations + '\'' +
                '}';
    }
}
