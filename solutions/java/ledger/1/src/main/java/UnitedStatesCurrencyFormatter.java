public class UnitedStatesCurrencyFormatter implements CurrencyFormatterIntf {
    public static final  String SPACE = CurrencyFormatterConstants.SPACE;
    public static final  String OPEN_PAREN = CurrencyFormatterConstants.OPEN_PAREN;
    public static final  String CLOSE_PAREN = CurrencyFormatterConstants.CLOSE_PAREN;

    @Override
    public String format(LedgerFormat format, boolean negative, String amount) {
        String symbol = format.currency()
                                .getSymbol();
        return negative ? this.formatNegativeAmount(symbol, amount)
                       : this.formatPositiveAmount(symbol, amount);
    }

    private String formatNegativeAmount(String symbol, String amount) {
        return OPEN_PAREN + symbol + amount + CLOSE_PAREN;
    }

    private String formatPositiveAmount(String symbol, String amount) {
        return symbol + amount + SPACE;
    }
}
