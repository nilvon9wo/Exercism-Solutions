public class CryptoSquareEncoder {
    public CryptoSquareEncoder(
            int messageLength,
            int rowCount,
            int columnCount
    ) {
        this.messageLength = messageLength;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    private final int messageLength;

    private final int rowCount;
    public int getRowCount() {
        return this.rowCount;
    }

    private int currentRowIndex;
    public CryptoSquareEncoder setCurrentRowIndex(int currentRowIndex) {
        this.currentRowIndex = currentRowIndex;
        return this;
    }

    private final int columnCount;

    private int currentColumnIndex;
    public CryptoSquareEncoder setCurrentColumnIndex(int currentColumnIndex) {
        this.currentColumnIndex = currentColumnIndex;
        return this;
    }
    public int getCurrentColumnIndex() {
        return this.currentColumnIndex;
    }

    public EncodingStepResult computeNextStep() {
        int nextCharacterIndex = computeLinearIndex();
        boolean isWithinMessageBounds = nextCharacterIndex < messageLength;
        return new EncodingStepResult(nextCharacterIndex, isWithinMessageBounds);
    }

    private int computeLinearIndex() {
        return this.currentRowIndex * this.columnCount + this.currentColumnIndex;
    }

    private final StringBuilder ciphertext = new StringBuilder();
    public CryptoSquareEncoder append(char character) {
        ciphertext.append(character);
        return this;
    }

    @Override
    public String toString() {
        return this.ciphertext.toString();
    }
}