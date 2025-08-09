package models.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Currency {
    GalacticCoin("GTC",1),
    StarDollar("SUD",2),
    QuantumNote("QTR",5);

    private final String pattern;
    private final int rate;

    Currency(String pattern, int rate) {
        this.pattern = pattern;
        this.rate = rate;
    }

    public String getPattern() {
        return pattern;
    }

    public int getRate() {
        return rate;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }

    public static Currency fromPattern(String pattern) {
        for (Currency c : Currency.values()) {
            if (c.getPattern().equals(pattern)) {
                return c;
            }
        }
        return null;
    }
}
