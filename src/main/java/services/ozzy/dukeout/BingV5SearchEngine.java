package services.ozzy.dukeout;

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
@Deprecated
public class BingV5SearchEngine extends SearchEngine {

    /*
         API v5 is deprecated (except for Azure accounts grandfathered in)
         Use the v7 API for any further development.
     */
    private static final String BING_API_URL = "https://api.cognitive.microsoft.com/bing/v5.0/search?q={q}";
    private static final String BING_API_HEADER = "Ocp-Apim-Subscription-Key";
    private static final String BING_API_KEY = "<secret>";

    private final AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
    private final HttpEntity<String> httpEntity;

    @Deprecated
    BingV5SearchEngine() {
        super();
        HttpHeaders headers = new HttpHeaders();
        headers.set(BING_API_HEADER, BING_API_KEY);
        httpEntity = new HttpEntity<>("parameters", headers);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class BingRawResultType {
        private static class WebPagesType {
            private BigInteger totalEstimatedMatches;

            BigInteger getTotalEstimatedMatches() {
                return totalEstimatedMatches;
            }

            public void setTotalEstimatedMatches(BigInteger totalEstimatedMatches) {
                this.totalEstimatedMatches = totalEstimatedMatches;
            }
        }

        private WebPagesType webPages;

        WebPagesType getWebPages() {
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

        futureResult.addCallback(successResult -> future.complete(new SearchResult.Builder()
                .setQuery(searchTerm)
                .setResults(successResult.getBody().getWebPages().getTotalEstimatedMatches())
                .setSearchEngine(this).build()), future::completeExceptionally);

        return future;
    }

    @Override
    public String getName() {
        return "Bing_V5";
    }

    @Override
    public SearchEngineType getType() {
        return SearchEngineType.BING_V5;
    }
}
