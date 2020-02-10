package services.ozzy.dukeout;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public class Application {

    public static void main(String... args) throws ExecutionException, InterruptedException {

        if (args.length < 2) {
            throw new IllegalArgumentException("At least 2 arguments are required.");
        }

        // Search engine instances.
        // Since the underlying REST caller is async, the results will be fetched asynchronously.
        List<SearchEngine> searchEngines = Arrays.asList(new GoogleSearchEngine(), new BingV7SearchEngine());

        // Empty list that will contain the search results
        List<CompletableFuture<SearchResult>> futureResults = new ArrayList<>();

        // Create a future with the search result for each term from each search engine
        for (SearchEngine searchEngine : searchEngines) {
            for (String searchTerm : args) {
                futureResults.add(searchEngine.search(searchTerm));
            }
        }

        // Create a CompletableFuture with all search engine/query combinations.
        CompletableFuture.allOf(futureResults.toArray(new CompletableFuture[0])).thenRun(() -> {

            // map result futures to actual results.
            List<SearchResult> finalResults = futureResults.stream()
                    .map(x -> x.getNow(null)) // never absent, but checked exceptions :(
                    .collect(Collectors.toList());

            // Search engine output per query
            finalResults.stream()
                    .collect(groupingBy(SearchResult::getQuery)) // group by query ie. Map<String, List<SearchResult>>
                    .forEach((query, resultList) ->
                            System.out.println(query + ": " + resultList.stream()
                                    .map(x -> x.getSearchEngine().getName() + ": " + x.getResults())
                                    .collect(Collectors.joining(", "))));

            // Winners for each search engine
            finalResults.stream()
                    .collect(groupingBy(SearchResult::getSearchEngine)) // group by search engine, ie. Map<SearchEngine, List<SearchResult>>
                    .forEach((engine, resultList) -> System.out.println(
                            engine.getName() + " winner: " +
                                    resultList.stream()
                                            .max(comparing(SearchResult::getResults))
                                            .orElseThrow(RuntimeException::new)
                                            .getQuery()));

            // Total winner
            System.out.println("Total winner: " +
                    finalResults.stream().collect( // into a Map<String, BigInteger>
                            groupingBy(SearchResult::getQuery, // use search query as map key
                                    mapping(SearchResult::getResults, // get result counts
                                            reducing(BigInteger.ZERO, BigInteger::add)))) // add result counts together
                            .entrySet().stream() // get the map's underlying Set<Entry<String, BigInteger>> and
                            .max(Map.Entry.comparingByValue()).orElseThrow(RuntimeException::new) // get Entry<> that has max value
                            .getKey()); // get the key for Entry<> which will be the search query (see above)

        }).get(); // Wait for all searches to complete (which will then execute the lambda above)
    }
}
