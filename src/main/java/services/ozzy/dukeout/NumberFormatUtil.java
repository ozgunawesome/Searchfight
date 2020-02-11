package services.ozzy.dukeout;

import java.math.BigInteger;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Özgün Ayaz on 2019-02-11.
 * License: CC BY-SA 4.0
 * For more info visit https://creativecommons.org/licenses/by-sa/4.0/
 */
public class NumberFormatUtil {

    private static NumberFormatUtil instance = null;
    private NumberFormat formatter;
    private Locale locale;

    private NumberFormatUtil() {
        this.locale = Locale.getDefault();
        this.formatter = NumberFormat.getNumberInstance(locale);
    }

    static void changeLocale(String language, String country) {
        getInstance().locale = new Locale(language, country);
        getInstance().formatter = NumberFormat.getNumberInstance(getInstance().locale);
    }

    static NumberFormatUtil getInstance() {
        if (instance == null) {
            instance = new NumberFormatUtil();
        }
        return instance;
    }

    static String format(BigInteger number) {
        return getInstance().formatter.format(number.longValue());
    }

    static char groupingSeparator() {
        return DecimalFormatSymbols.getInstance(getInstance().locale).getGroupingSeparator();
    }

}
