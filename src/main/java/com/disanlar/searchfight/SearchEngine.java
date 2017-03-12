package com.disanlar.searchfight;

import java.util.concurrent.CompletableFuture;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
interface SearchEngine {

    CompletableFuture<SearchResult> search(String searchTerm);

    String getName();

    SearchEngineType getType();

}
