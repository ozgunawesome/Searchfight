package services.ozzy.dukeout;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

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
            // Nothing should be printed to stderr
            assertEquals("", err);
        }, "java", ".net");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThan2Arguments() {
        testApplicationWithArguments((out, err) -> {}, "nope");
    }
}
