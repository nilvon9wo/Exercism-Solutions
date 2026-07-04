class CryptoSquare {
    private final String plaintext;
    CryptoSquare(String plaintext) {
        this.plaintext = this.normalize(plaintext);
    }

    String getCiphertext() {
        if (plaintext.isEmpty()) {
            return "";
        }

        int messageLength = plaintext.length();
        int rowCount = this.calculateRowCount(messageLength);
        int columnCount = this.calculateColumnCount(messageLength);

        CryptoSquareEncoder encoder = new CryptoSquareEncoder(messageLength, rowCount, columnCount);
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            this.appendColumn(encoder.setCurrentColumnIndex(columnIndex));
        }

        return encoder.toString();
    }

    private int calculateRowCount(int messageLength) {
        int rowCount = (int) Math.floor(Math.sqrt(messageLength));
        int columnCount = (int) Math.ceil(Math.sqrt(messageLength));

        if (rowCount * columnCount < messageLength) {
            rowCount++;
        }

        return rowCount;
    }

    private int calculateColumnCount(int messageLength) {
        return (int) Math.ceil(Math.sqrt(messageLength));
    }

    private void appendColumn(CryptoSquareEncoder encoder) {
        if (encoder.getCurrentColumnIndex() > 0) {
            encoder.append(' ');
        }

        for (int rowIndex = 0; rowIndex < encoder.getRowCount(); rowIndex++) {
            this.appendCharacter(encoder.setCurrentRowIndex(rowIndex));
        }
    }

    private void appendCharacter(CryptoSquareEncoder encoder) {
        EncodingStepResult encodingStepResult = encoder.computeNextStep();
        char character = encodingStepResult.isWithinMessageBounds()
            ? plaintext.charAt(encodingStepResult.nextPosition())
            : ' ';
        encoder.append(character);
    }

    private String normalize(String input) {
        return input.replaceAll("[^A-Za-z0-9]", "")
                    .toLowerCase();
    }
}