package com.rbox.search.v2;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * Very lightweight service backing the v2 search endpoints.
 * <p>
 * It does not query a database; instead it returns empty results so that
 * the API surface is in place for further evolution.
 */
@Service
public class SearchService {
    public SearchResult searchListings(SearchListingsReq req) {
        return new SearchResult(Collections.emptyList(), 0, req.page(), req.size());
    }

    public SearchResult searchObjects(SearchObjectsReq req) {
        return new SearchResult(Collections.emptyList(), 0, req.page(), req.size());
    }

    public record SearchResult(List<Map<String, Object>> content, long total, int page, int size) {}
}

