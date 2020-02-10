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

        List<SearchEngine> searchEngines = Arrays.asList(new GoogleSearchEngine(), new BingV7SearchEngine());
        List<CompletableFuture<SearchResult>> futureResults = new ArrayList<>();

        for (SearchEngine searchEngine : searchEngines) {
            for (String searchTerm : args) {
                futureResults.add(searchEngine.search(searchTerm));
            }
        }

        CompletableFuture.allOf(futureResults.toArray(new CompletableFuture[futureResults.size()])).thenRun(() -> {

            // map result futures to actual results.
            List<SearchResult> finalResults = futureResults.stream()
                    .map(x -> x.getNow(null)) // never absent
                    .collect(Collectors.toList());

            // Search engine output per query
            finalResults.stream().collect(groupingBy(SearchResult::getQuery)).forEach((query, resultList) ->
                    System.out.println(query + ": " + resultList.stream()
                            .map(x -> x.getSearchEngine().getName() + ": " + x.getResults())
                            .collect(Collectors.joining(", "))));

            // Winners for each search engine
            finalResults.stream().collect(groupingBy(SearchResult::getSearchEngine)).forEach((engine, resultList) ->
                    System.out.println(engine.getName() + " winner: " + resultList.stream()
                            .max(comparing(SearchResult::getResults)).get().getQuery()));

            // Total winner
            System.out.println("Total winner: " + finalResults.stream().collect(groupingBy(SearchResult::getQuery,
                    mapping(SearchResult::getResults, reducing(BigInteger.ZERO, BigInteger::add))))
                    .entrySet().stream().max(comparing(Map.Entry::getValue)).get().getKey());

        }).get();
    }
}
