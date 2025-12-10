import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public class Robot {
    private final RandomGenerator random;
    public Robot(RandomGenerator random) {
        this.random = random;
    }

    public Robot() {
        this(new Random());
    }
    private static final Set<String> USED_NAMES = new HashSet<>();

    private static final int LETTER_COUNT = 26;
    private static final int MAX_NUMBER = 1000;
    private static final int DIGIT_COUNT = 3;
    private static final char FIRST_LETTER = 'A';

    private String name;
    public String getName() {
        if (name == null) {
            this.name = this.generateUniqueName();
        }

        return this.name;
    }

    public void reset() {
        this.name = null;
    }

    private String generateUniqueName() {
        return Stream.generate(this::randomName)
                       .filter(USED_NAMES::add)
                       .findFirst()
                       .orElseThrow();
    }

    private String randomName() {
        return randomLetters() + randomDigits();
    }

    private String randomLetters() {
        return "" + this.getRandomLetter() + this.getRandomLetter();
    }

    private char getRandomLetter() {
        return (char) (FIRST_LETTER + this.random.nextInt(LETTER_COUNT));
    }

    private String randomDigits() {
        int number = this.random.nextInt(MAX_NUMBER); // 0..999
        return String.format("%0" + DIGIT_COUNT + "d", number);
    }
}
