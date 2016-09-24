package com.wooz86.musicbrainz.impl.dto;

import com.google.api.client.util.Key;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Artist {

    @Key("release-groups")
    private List<ReleaseGroup> releaseGroups = new ArrayList<>();

    @Key
    private List<Relation> relations = new ArrayList<>();

    public List<ReleaseGroup> getReleaseGroups() {
        return releaseGroups;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public List<ReleaseGroup> getReleaseGroupByType(String releaseGroupType) {
        List<ReleaseGroup> releaseGroupsFound = getReleaseGroups()
                .stream()
                .filter(p -> p.getPrimaryType().equals(releaseGroupType))
                .collect(Collectors.toList());

        if (releaseGroupsFound.isEmpty()) {
            throw new IllegalArgumentException("No release groups with type \"" + releaseGroupType + "\" found.");
        }

        return releaseGroupsFound;
    }

    public List<Relation> getRelationsByType(String relationType) {
        List<Relation> relationsFound = getRelations()
                .stream()
                .filter(p -> p.getType().equals(relationType))
                .collect(Collectors.toList());

        if (relationsFound.isEmpty()) {
            throw new IllegalArgumentException("No relations with type \"" + relationType + "\" found.");
        }

        return relationsFound;
    }
}
