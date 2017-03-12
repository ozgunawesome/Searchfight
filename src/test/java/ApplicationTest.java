import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Özgün Ayaz on 2017-03-12.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public class ApplicationTest {

    @Test
    public void runApplication() {
        PrintStream out = System.out;

        try {
            Logger.getLogger(this.getClass().getName()).info("Hooking STDOUT to check results, please wait..");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(byteArrayOutputStream));
            Application.main("java", ".net");

            // apparently ".net" will always win since Google will ignore the dot and crush it with 10x results
            // otherwise pretty useless test
            assertTrue(byteArrayOutputStream.toString().contains("Total winner: .net"));

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.setOut(out);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThan2Arguments() {
        try {
            Application.main("nope");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
