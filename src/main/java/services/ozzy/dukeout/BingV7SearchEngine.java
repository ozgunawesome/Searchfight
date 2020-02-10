package services.ozzy.dukeout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Özgün Ayaz on 2019-02-10.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public class BingV7SearchEngine extends SearchEngine {

    private final String bingApiURL;
    private final AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
    private final HttpEntity<String> httpEntity;

    BingV7SearchEngine() {
        super();
        bingApiURL = properties.getProperty("search.bing_v7.endpoint");

        // Set required HTTP header for Bing authentication
        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", properties.getProperty("search.bing_v7.key"));
        httpEntity = new HttpEntity<>("parameters", headers);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    private static class BingRawResultType {
        @Data
        private static class WebPagesType {
            private BigInteger totalEstimatedMatches;
        }
        private WebPagesType webPages;
    }

    @Override
    public CompletableFuture<SearchResult> search(String searchTerm) {

        CompletableFuture<SearchResult> future = new CompletableFuture<>();

        ListenableFuture<ResponseEntity<BingRawResultType>> futureResult = asyncRestTemplate
                .exchange(bingApiURL, HttpMethod.GET, httpEntity, BingRawResultType.class, searchTerm);

        futureResult.addCallback(successResult -> future.complete(new SearchResult.Builder()
                .setQuery(searchTerm)
                .setResults(successResult.getBody().getWebPages().getTotalEstimatedMatches())
                .setSearchEngine(this).build()), future::completeExceptionally);

        return future;
    }

    @Override
    public String getName() {
        return "Bing_V7";
    }

    @Override
    public SearchEngineType getType() {
        return SearchEngineType.BING_V7;
    }
}
