import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public class GoogleSearchEngineTest {

    @Test
    public void testNameAndType() {
        GoogleSearchEngine engine = new GoogleSearchEngine();
        assertEquals("Google", engine.getName());
        assertEquals(SearchEngineType.GOOGLE, engine.getType());
    }

    @Test
    public void testSearchEngine() throws ExecutionException, InterruptedException {
        GoogleSearchEngine engine = new GoogleSearchEngine();

        SearchResult searchResult = engine.search("java").get();

        assertEquals("java", searchResult.getQuery());
        assertEquals(engine, searchResult.getSearchEngine());
        assertNotNull(searchResult.getResults());
        assertNotEquals(-1, searchResult.getResults().compareTo(BigInteger.ZERO));
    }

}
