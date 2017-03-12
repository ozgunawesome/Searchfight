import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public class BingSearchEngineTest {

    @Test
    public void testNameAndType() {
        BingSearchEngine engine = new BingSearchEngine();
        assertEquals("Bing", engine.getName());
        assertEquals(SearchEngineType.BING, engine.getType());
    }

    @Test
    public void testStubbedSearchEngine() throws ExecutionException, InterruptedException {
        BingSearchEngine engine = new BingSearchEngine();
        CompletableFuture<SearchResult> futureResult = new CompletableFuture<>();

        engine.search("query", searchResult -> futureResult.complete(searchResult));

        SearchResult searchResult = futureResult.get();

        assertEquals("query", searchResult.getQuery());
        assertEquals(engine, searchResult.getSearchEngine());
        assertNotNull(searchResult.getResults());
        assertNotEquals(-1, searchResult.getResults().compareTo(BigInteger.ZERO));
    }

}
