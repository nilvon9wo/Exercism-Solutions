import java.util.*;
import java.util.stream.Collectors;

class HandshakeCalculator {
	private static final int REVERSE_CODE = 0b10000;
	private static final Map<Integer, Signal> SIGNAL_MAP = new LinkedHashMap<>(){{
		put(0b00001, Signal.WINK);
		put(0b00010, Signal.DOUBLE_BLINK);
		put(0b00100, Signal.CLOSE_YOUR_EYES);
		put(0b01000, Signal.JUMP);
	}};

    List<Signal> calculateHandshake(int number) {
	    List<Signal> result = this.collectSignals(number);
	    if ((number & REVERSE_CODE) != 0) {
		    Collections.reverse(result);
	    }

	    return result;
    }

	private List<Signal> collectSignals(int number) {
		return SIGNAL_MAP.entrySet()
				       .stream()
				       .filter(entry -> this.isSignalBitSet(number, entry))
				       .map(Map.Entry::getValue)
				       .collect(Collectors.toList());
	}

	private boolean isSignalBitSet(int number, Map.Entry<Integer, Signal> entry) {
		Integer signal = entry.getKey();
		return (number & signal) != 0;
	}
}
