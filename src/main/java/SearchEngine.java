import java.util.function.Consumer;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public interface SearchEngine {

    void search(String searchTerm, Consumer<SearchResult> callback);

    String getName();

    SearchEngineType getType();

}
