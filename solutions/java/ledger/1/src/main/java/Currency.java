public enum Currency {
    USD("$"),
    EUR("€");

    private final String symbol;

    Currency(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public static Currency fromCode(String code) {
        try {
            return Currency.valueOf(code);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency");
        }
    }
}
