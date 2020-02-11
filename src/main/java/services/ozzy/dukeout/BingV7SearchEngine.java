package services.ozzy.dukeout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.Data;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * Created by Özgün Ayaz on 2019-02-10.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public class BingV7SearchEngine extends SearchEngine {

    private final String bingApiURL;
    private final HttpEntity<String> httpEntity;

    /**
     * Bing API has a limit of 3 TPS in the free tier. Using the retry module from the
     * open-source resilience4j library for 10 retries with 1 sec in between here.
     */
    final RetryConfig retryConfig = RetryConfig.<ResponseEntity<BingRawResultType>>custom()
            .maxAttempts(10)
            .waitDuration(Duration.ofMillis(1000))
            .retryOnResult((response) -> response.getStatusCode().equals(HttpStatus.TOO_MANY_REQUESTS)) // HTTP 429
            .retryOnException(e -> e instanceof RestClientException)
            .retryExceptions(IOException.class, TimeoutException.class)
            .build();

    /**
     * Also not using the async RestTemplate from the parent class, instead wrapping the synchronous
     * REST client in my own thread pool here.
     */
    private final ExecutorService executorService = Executors.newCachedThreadPool();

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

        return CompletableFuture.supplyAsync(
                Retry.decorateSupplier( // resilience4j has this helper static method to add retry logic to lambda functions
                        Retry.of(this.getName(), retryConfig),
                        () -> new RestTemplate().exchange(bingApiURL, HttpMethod.GET, httpEntity, BingRawResultType.class, searchTerm)),
                executorService) // this will return an async CompletableFuture<ResponseEntity<BingRawSearchResult>>
                .thenApply(result -> new SearchResult.Builder()  // once the request succeeds, we map it to our common SearchResult value type here
                        .setQuery(searchTerm)
                        .setResults(result.getBody().getWebPages().getTotalEstimatedMatches())
                        .setSearchEngine(this).build());
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
