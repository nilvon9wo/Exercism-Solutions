public class ParseState {
    private boolean hasActiveList = false;
    public boolean hasActiveList() {
        return this.hasActiveList;
    }
    public boolean hasNoActiveList() {
        return !hasActiveList;
    }
    @SuppressWarnings("UnusedReturnValue")
    public ParseState activateList() {
        this.hasActiveList = true;
        return this;
    }
    @SuppressWarnings("UnusedReturnValue")
    public ParseState deactivateList() {
        this.hasActiveList = false;
        return this;
    }

    private final StringBuilder stringBuilder = new StringBuilder();
    public ParseState append(String text) {
        this.stringBuilder.append(text);
        return this;
    }

    @Override
    public String toString() {
        return this.stringBuilder.toString();
    }
}