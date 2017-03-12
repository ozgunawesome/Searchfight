import java.math.BigInteger;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public class SearchResult {

    private String query;
    private SearchEngine searchEngine;
    private BigInteger results;

    public SearchEngine getSearchEngine() {
        return searchEngine;
    }

    public BigInteger getResults() {
        return results;
    }

    public String getQuery() {
        return query;
    }

    private SearchResult(String query, BigInteger results, SearchEngine searchEngine) {
        this.query = query;
        this.searchEngine = searchEngine;
        this.results = results;
    }

    public static class Builder {
        private String query;
        private BigInteger results;
        private SearchEngine searchEngine;

        public Builder setQuery(String query) {
            this.query = query;
            return this;
        }

        public Builder setSearchEngine(SearchEngine searchEngine) {
            this.searchEngine = searchEngine;
            return this;
        }

        public Builder setResults(BigInteger results) {
            this.results = results;
            return this;
        }

        public SearchResult build() {
            return new SearchResult(query, results, searchEngine);
        }
    }
}
