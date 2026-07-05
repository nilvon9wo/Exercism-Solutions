import java.util.ArrayList;
import java.util.List;

final class KeyedOptionsParser {
    public static Result<KeyedOptions> parse(Input input) {
        Result<String> keyResult = KeyParser.parse(input);
        if (!keyResult.isValid()) {
            return Result.failure(keyResult.getException());
        }

        List<String> options = new ArrayList<>();
        while (input.current() == Symbol.OPTION_START.value()) {
            Result<String> optionResult = OptionParser.parse(input);
            if (!optionResult.isValid()) {
                return Result.failure(optionResult.getException());
            }

            options.add(optionResult.getValue());
            input.moveNext();
        }

        return Result.success(new KeyedOptions(keyResult.getValue(), options));
    }
}