package services.ozzy.dukeout;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class NumberFormatUtilTest {

    /**
     * unit test for the number formatter working, changing locales correctly
     */
    @Test
    public void testFormatter() {
        NumberFormatUtil.changeLocale("sv", "SE");
        assertEquals("1 000 000", NumberFormatUtil.format(new BigInteger("1000000")));
        NumberFormatUtil.changeLocale("en","US");
        assertEquals("1,000,000", NumberFormatUtil.format(new BigInteger("1000000")));
    }

    @Test
    public void testCorrectThousandSeparator() {
        NumberFormatUtil.changeLocale("de", "DE");
        assertEquals('.', NumberFormatUtil.groupingSeparator());
    }

}
