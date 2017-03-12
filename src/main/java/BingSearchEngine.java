import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public class BingSearchEngine implements SearchEngine {

    private static final String BING_API_URL = "https://api.cognitive.microsoft.com/bing/v5.0/search?q={q}";
    private static final String BING_API_HEADER = "Ocp-Apim-Subscription-Key";
    private static final String BING_API_KEY = "027774d2d68545c29da1196b52a02cfc";

    private final AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
    private final HttpEntity<String> httpEntity;

    public BingSearchEngine() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(BING_API_HEADER, BING_API_KEY);
        httpEntity = new HttpEntity<>("parameters", headers);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class BingRawResultType {
        private static class WebPagesType {
            private BigInteger totalEstimatedMatches;

            public BigInteger getTotalEstimatedMatches() {
                return totalEstimatedMatches;
            }

            public void setTotalEstimatedMatches(BigInteger totalEstimatedMatches) {
                this.totalEstimatedMatches = totalEstimatedMatches;
            }
        }

        private WebPagesType webPages;

        public WebPagesType getWebPages() {
            return webPages;
        }

        public void setWebPages(WebPagesType webPages) {
            this.webPages = webPages;
        }
    }

    @Override
    public CompletableFuture<SearchResult> search(String searchTerm) {

        CompletableFuture<SearchResult> future = new CompletableFuture<>();

        ListenableFuture<ResponseEntity<BingRawResultType>> futureResult = asyncRestTemplate
                .exchange(BING_API_URL, HttpMethod.GET, httpEntity, BingRawResultType.class, searchTerm);

        futureResult.addCallback(successResult -> {
            future.complete(new SearchResult.Builder()
                    .setQuery(searchTerm)
                    .setResults(successResult.getBody().getWebPages().getTotalEstimatedMatches())
                    .setSearchEngine(this).build());
        }, future::completeExceptionally);

        return future;
    }

    @Override
    public String getName() {
        return "Bing";
    }

    @Override
    public SearchEngineType getType() {
        return SearchEngineType.BING;
    }
}
