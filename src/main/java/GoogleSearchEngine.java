import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public class GoogleSearchEngine implements SearchEngine {

    private static final RestTemplate restTemplate = new RestTemplate();

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GoogleRawResultType {
        private static class QueriesType {
            private static class RequestType {
                private String totalResults;

                public String getTotalResults() {
                    return totalResults;
                }

                public void setTotalResults(String totalResults) {
                    this.totalResults = totalResults;
                }
            }

            private List<RequestType> request;

            public List<RequestType> getRequest() {
                return request;
            }

            public void setRequest(List<RequestType> request) {
                this.request = request;
            }
        }

        private QueriesType queries;

        public QueriesType getQueries() {
            return queries;
        }

        public void setQueries(QueriesType queries) {
            this.queries = queries;
        }
    }

    @Override
    public void search(String searchTerm, Consumer<SearchResult> callback) {
        try {
            GoogleRawResultType rawResultType = restTemplate.getForObject(
                    "https://www.googleapis.com/customsearch/v1?key={key}&cx={cx}&q={q}",
                    GoogleRawResultType.class,
                    Constants.GOOGLE_API_KEY,
                    Constants.GOOGLE_API_CX,
                    URLEncoder.encode(searchTerm, "UTF-8"));

            callback.accept(new SearchResult.Builder()
                    .setQuery(searchTerm)
                    .setSearchEngine(this)
                    .setResults(new BigInteger(rawResultType.getQueries().getRequest().get(0).getTotalResults()))
                    .build());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
