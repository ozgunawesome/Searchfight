# Dukeout

Search some terms in some search engines, see which term gets more results.

### How to use

* Rename `secrets_template.properties` to `secrets.properties` and fill out API related info.
* Compile with `mvn package`
* Invoke from command line with `java -jar path/to/Dukeout.jar <arguments>`
* At least 2 arguments are required

### Tests?

Yep. `mvn test`

### More search engines?

Why not? Extend the `SearchEngine` abstract class.

Google and Bing API implementations included by default.

### Licensing

This work is licensed under a [Creative Commons Attribution-ShareAlike 4.0 International License](https://creativecommons.org/licenses/by-sa/4.0/).