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

    public class SearchResultBuilder {
        private String query;
        private BigInteger results;
        private SearchEngine searchEngine;

        public SearchResultBuilder setQuery(String query) {
            this.query = query;
            return this;
        }

        public SearchResultBuilder setSearchEngine(SearchEngine searchEngine) {
            this.searchEngine = searchEngine;
            return this;
        }

        public SearchResultBuilder setResults(BigInteger results) {
            this.results = results;
            return this;
        }

        public SearchResult createSearchResult() {
            return new SearchResult(query, results, searchEngine);
        }
    }
}
