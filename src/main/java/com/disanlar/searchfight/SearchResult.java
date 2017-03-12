package com.disanlar.searchfight;

import java.math.BigInteger;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
class SearchResult {

    private final String query;
    private final SearchEngine searchEngine;
    private final BigInteger results;

    SearchEngine getSearchEngine() {
        return searchEngine;
    }

    BigInteger getResults() {
        return results;
    }

    String getQuery() {
        return query;
    }

    private SearchResult(String query, BigInteger results, SearchEngine searchEngine) {
        this.query = query;
        this.searchEngine = searchEngine;
        this.results = results;
    }

    static class Builder {
        private String query;
        private BigInteger results;
        private SearchEngine searchEngine;

        Builder setQuery(String query) {
            this.query = query;
            return this;
        }

        Builder setSearchEngine(SearchEngine searchEngine) {
            this.searchEngine = searchEngine;
            return this;
        }

        Builder setResults(BigInteger results) {
            this.results = results;
            return this;
        }

        SearchResult build() {
            return new SearchResult(query, results, searchEngine);
        }
    }
}
