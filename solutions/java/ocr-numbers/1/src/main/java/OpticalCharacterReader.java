import java.util.ArrayList;
import java.util.List;

class OpticalCharacterReader {
    private final OcrGrid grid;
    private final OcrDigitCatalog catalog;
    OpticalCharacterReader(OcrGrid grid, OcrDigitCatalog catalog) {
        this.grid = grid;
        this.catalog = catalog;
    }

    OpticalCharacterReader() {
        this(new OcrGrid(), new OcrDigitCatalog());
    }

    String parse(List<String> input) {
        this.validate(input);
        List<String[]> digits = this.grid.extractDigitCells(input);

        List<String> outputDigits = new ArrayList<>();
        StringBuilder currentRow = new StringBuilder();
        int widthInDigits = input.get(0).length() / 3;
        for (int i = 0; i < digits.size(); i++) {
            if (i > 0 && i % widthInDigits == 0) {
                outputDigits.add(currentRow.toString());
                currentRow.setLength(0);
            }

            currentRow.append(catalog.match(digits.get(i)));
        }

        outputDigits.add(currentRow.toString());
        return String.join(",", outputDigits);
    }

    private void validate(List<String> input) {
        if (input.size() % 4 != 0) {
            throw new IllegalArgumentException("Number of input rows must be a positive multiple of 4");
        }

        int width = input.get(0).length();
        for (String row : input) {
            if (row.length() != width || width % 3 != 0) {
                throw new IllegalArgumentException("Number of input columns must be a positive multiple of 3");
            }
        }
    }
}