package services.ozzy.dukeout;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public class ApplicationTest {

    /**
     * Helper method to test application entry point.
     *
     * @param evaluator         lambda to evaluate the application output
     * @param argumentsToSearch arguments to provide to the application entry point
     */
    public void testApplicationWithArguments(BiConsumer<String, String> evaluator, String... argumentsToSearch) {
        PrintStream defaultStdout = System.out, defaultStderr = System.err;
        String testStdout = null, testStderr = null;

        try {
            Logger.getLogger(this.getClass().getName()).info("Hooking STDOUT and STDERR to check results, please wait..");
            ByteArrayOutputStream stdoutStream = new ByteArrayOutputStream(), stderrStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(stdoutStream));
            System.setErr(new PrintStream(stderrStream));

            Application.main(argumentsToSearch);

            testStdout = stdoutStream.toString().trim();
            testStderr = stderrStream.toString().trim();
            evaluator.accept(testStdout, testStderr);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.setOut(defaultStdout); // restore stdout/stderr
            System.setErr(defaultStderr);

            System.err.println(testStderr);
            System.out.println(testStdout); // print test outputs
        }
    }

    @Test
    public void testApplicationWithTwoArguments() {
        testApplicationWithArguments((out, err) -> {
            // apparently ".net" will always win since Google will ignore the dot and crush it with 10x results
            assertTrue(out.contains("Total winner: .net"));
            // No exceptions in stderr
            assertFalse(err.contains("Exception"));
        }, "java", ".net");
    }

    /**
     * this is a very comprehensive test, going through the Bing API throttling logic, quotes in arguments,
     * URL encoding them, as well as overall stability of the code.
     * The quotation marks shall be taken care of by the shell when invoked through command line.
     */
    @Test
    public void testApplicationWithLotsOfArguments() {
        final String[] argumentsToSearch = {"java", ".net", "python", "javascript", "go", "kotlin", "perl", "ruby", "\"bill gates\"", "\"jeff bezos\"", "folding@home"};

        final AtomicInteger linesThatMatch = new AtomicInteger(0);
        final Pattern pattern = Pattern.compile("(.*): (.*): [0-9]+, (.*): [0-9]+");
        final List<String> argumentsArrayAsList = Arrays.asList(argumentsToSearch);
        final List<String> allSearchEngineNames = Arrays.stream(SearchEngineType.values()).map(SearchEngineType::getName).collect(Collectors.toList());

        testApplicationWithArguments((out, err) -> {
            assertTrue(out.contains("Total winner:")); // the test went all the way successfully and had a winner
            Matcher matcher = pattern.matcher(out); // parse stdout with the regex above
            while (matcher.find()) {
                assertTrue(argumentsArrayAsList.contains(matcher.group(1))); // this search term was in the result list
                assertTrue(allSearchEngineNames.contains(matcher.group(2)));
                assertTrue(allSearchEngineNames.contains(matcher.group(3))); // the two search engines both were in the result list
                linesThatMatch.incrementAndGet();
            }
            assertEquals(argumentsArrayAsList.size(), linesThatMatch.get()); // Returned result count equals input argument count
            assertFalse(err.contains("Exception")); // No exceptions in stderr
        }, argumentsToSearch);
    }

    /**
     * this test should fail right away because at least 2 arguments required
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLessThan2Arguments() {
        testApplicationWithArguments((out, err) -> {}, "nope");
    }
}
