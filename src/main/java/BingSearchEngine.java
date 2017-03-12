import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.client.AsyncRestTemplate;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public class BingSearchEngine implements SearchEngine {

    private final AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();

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

        future.complete(new SearchResult.Builder()
                .setQuery(searchTerm)
                .setSearchEngine(this)
                .setResults(new BigInteger(32, new Random()).abs())
                .build());

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
