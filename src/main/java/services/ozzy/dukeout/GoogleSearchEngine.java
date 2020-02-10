package services.ozzy.dukeout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public class GoogleSearchEngine extends SearchEngine {

    private static final String GOOGLE_API_URL = "https://www.googleapis.com/customsearch/v1?key={key}&cx={cx}&q={q}";

    private final AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
    private final String GOOGLE_API_KEY;
    private final String GOOGLE_API_CX;

    GoogleSearchEngine() {
        super();
        GOOGLE_API_CX = properties.getProperty("search.google.cx");
        GOOGLE_API_KEY = properties.getProperty("search.google.key");
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GoogleRawResultType {
        private static class QueriesType {
            private static class RequestType {
                private BigInteger totalResults;

                BigInteger getTotalResults() {
                    return totalResults;
                }

                public void setTotalResults(BigInteger totalResults) {
                    this.totalResults = totalResults;
                }
            }

            private List<RequestType> request;

            List<RequestType> getRequest() {
                return request;
            }

            public void setRequest(List<RequestType> request) {
                this.request = request;
            }
        }

        private QueriesType queries;

        QueriesType getQueries() {
            return queries;
        }

        public void setQueries(QueriesType queries) {
            this.queries = queries;
        }
    }

    @Override
    public CompletableFuture<SearchResult> search(String searchTerm) {
        CompletableFuture<SearchResult> future = new CompletableFuture<>();

        ListenableFuture<ResponseEntity<GoogleRawResultType>> futureResult = asyncRestTemplate
                .getForEntity(GOOGLE_API_URL, GoogleRawResultType.class, GOOGLE_API_KEY, GOOGLE_API_CX, searchTerm);

        futureResult.addCallback(successResult -> future.complete(new SearchResult.Builder()
                .setQuery(searchTerm)
                .setResults(successResult.getBody().getQueries().getRequest().get(0).getTotalResults())
                .setSearchEngine(this)
                .build()), future::completeExceptionally);

        return future;
    }

    @Override
    public String getName() {
        return "Google";
    }

    @Override
    public SearchEngineType getType() {
        return SearchEngineType.GOOGLE;
    }
}
