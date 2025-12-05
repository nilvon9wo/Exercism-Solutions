import java.util.stream.Collectors;

public class TextSanitizer {

    String keepLettersAndDigitsLowercase(String input) {
        return input.toLowerCase()
                       .chars()
                       .filter(Character::isLetterOrDigit)
                       .mapToObj(character -> String.valueOf((char) character))
                       .collect(Collectors.joining());
    }
}
