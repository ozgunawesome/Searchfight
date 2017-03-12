import java.math.BigInteger;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public class GoogleSearchEngine implements SearchEngine {

    @Override
    public void search(String searchTerm, Consumer<SearchResult> callback) {

        // method stub!
        callback.accept(new SearchResult.Builder()
                .setQuery(searchTerm)
                .setSearchEngine(this)
                .setResults(new BigInteger(32, new Random()).abs())
                .build());

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
