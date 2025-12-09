public class NetherlandsCurrencyFormatter implements CurrencyFormatterIntf {
    public static final  String SPACE = CurrencyFormatterConstants.SPACE;

    @Override
    public String format(LedgerFormat format, boolean negative, String amount) {
        String signPrefix  = negative
                                     ? CurrencyFormatterConstants.MINUS
                                     : "";

        return format.currency().getSymbol()
                       + SPACE
                       + signPrefix
                       + amount
                       + SPACE;
    }
}
