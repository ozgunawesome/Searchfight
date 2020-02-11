package services.ozzy.dukeout;

import lombok.Getter;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
@Getter
public enum SearchEngines {
    GOOGLE("Google", new GoogleSearchEngine()),
    BING_V7("Bing_V7", new BingV7SearchEngine());

    private String name;
    private SearchEngine instance;

    SearchEngines(String name, SearchEngine instance) {
        this.name = name;
        this.instance = instance;
    }
}
