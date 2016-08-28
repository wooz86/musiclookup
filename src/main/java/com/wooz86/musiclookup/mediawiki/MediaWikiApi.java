package com.wooz86.musiclookup.mediawiki;

public interface MediaWikiApi {
    MediaWikiPage getPageByTitle(String pageTitle) throws MediaWikiApiException;
}
