import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

public class Dice {
	private final RandomGenerator random;
	private Dice(RandomGenerator random) {
		this.random = random;
	}

	public Dice() {
		this(new Random());
	}

	private static final int NUMBER_OF_DICE = 4;
	private static final int DIE_MIN = 1;
	private static final int DIE_MAX_EXCLUSIVE = 7;  // 7 because Random.ints upper bound is exclusive

	public List<Integer> roll() {
		return random.ints(NUMBER_OF_DICE, DIE_MIN, DIE_MAX_EXCLUSIVE)
				       .boxed()
				       .collect(Collectors.toList());
	}
}
