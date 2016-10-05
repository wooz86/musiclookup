package com.wooz86.mediawiki;

import java.net.URISyntaxException;

public interface MediaWikiApi {
    MediaWikiPage getPageByTitle(String pageTitle) throws MediaWikiApiException, URISyntaxException;
}
