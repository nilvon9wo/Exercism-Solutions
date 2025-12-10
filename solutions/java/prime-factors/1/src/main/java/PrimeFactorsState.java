public record PrimeFactorsState(long remaining, long factor) {
    public boolean foundFactor() {
        return remaining % factor == 0;
    }

    public boolean hasMoreFactors() {
        return this.remaining() > 1;
    }

    public PrimeFactorsState getNextState() {
        long remaining = this.remaining;
        long factor = this.factor;
        return remaining % factor == 0
                       ? new PrimeFactorsState(remaining / factor, factor)
                       : new PrimeFactorsState(remaining, factor + 1);
    }
}