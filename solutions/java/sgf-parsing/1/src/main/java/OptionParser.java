import java.lang.IllegalArgumentException;

final class OptionParser {
    public static Result<String> parse(Input input) {
        return input.current() != Symbol.OPTION_START.value()
               ? Result.failure(new IllegalArgumentException("Input is missing option."))
               : !input.moveNext()
                     ? Result.failure(new IllegalArgumentException("Option is missing contents."))
                     : input.takeUntil(Symbol.OPTION_END.value(), "Option");
    }
}