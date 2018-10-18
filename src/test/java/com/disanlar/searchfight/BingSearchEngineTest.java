package com.disanlar.searchfight;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
@Ignore
public class BingSearchEngineTest {

    @Test
    public void testNameAndType() {
        BingSearchEngine engine = new BingSearchEngine();
        Assert.assertEquals("Bing", engine.getName());
        Assert.assertEquals(SearchEngineType.BING, engine.getType());
    }

    @Test
    public void testBingSearchEngine() throws ExecutionException, InterruptedException {
        BingSearchEngine engine = new BingSearchEngine();
        SearchResult searchResult = engine.search("query").get();

        Assert.assertEquals("query", searchResult.getQuery());
        Assert.assertEquals(engine, searchResult.getSearchEngine());
        assertNotNull(searchResult.getResults());
        Assert.assertNotEquals(-1, searchResult.getResults().compareTo(BigInteger.ZERO));
    }

}
