package services.ozzy.dukeout;

import lombok.Getter;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public enum SearchEngineType {
    GOOGLE("Google"),
    BING_V7("Bing_V7");

    @Getter
    private String name;

    SearchEngineType(String name) {
        this.name = name;
    }
}
