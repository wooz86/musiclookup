package com.wooz86.musiclookup.artist.infrastructure.mediawiki;

public class MediaWikiPage {
    private final String id;
    private final String title;
    private final String extract;

    public MediaWikiPage(String id, String title, String extract) {
        this.id = id;
        this.title = title;
        this.extract = extract;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getExtract() {
        return extract;
    }
}
