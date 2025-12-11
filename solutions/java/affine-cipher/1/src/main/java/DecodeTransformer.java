import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

public class DecodeTransformer implements AffineTransformerIntf {
    private final AffineCipherHelper affineCipherHelper;
    private DecodeTransformer(AffineCipherHelper affineCipherHelper) {
        this.affineCipherHelper = affineCipherHelper;
    }

    @SuppressWarnings("unused")
    public DecodeTransformer() {
        this(new AffineCipherHelper());
    }

    public static final int ALPHABET_SIZE = AffineCipherHelper.ALPHABET_SIZE;
    
    @Override
    public String transformText(String text, int keyAMultiplier, int keyBShift) {
        int inverseA = this.affineCipherHelper.findMultiplierThatUndoesKeyAMultiplier(keyAMultiplier);
        return text.chars()
                       .mapToObj(c -> this.transformCharacter((char) c, inverseA, keyBShift))
                       .collect(Collectors.joining());
    }

    private String transformCharacter(char characterValue, int inverseAMultiplier, int keyBShift) {
        IntUnaryOperator decodeOperation = index -> {
            int adjustedShift = index - (keyBShift % ALPHABET_SIZE);
            int normalizedValue = (adjustedShift % ALPHABET_SIZE + ALPHABET_SIZE) % ALPHABET_SIZE;
            return (inverseAMultiplier * normalizedValue) % ALPHABET_SIZE;
        };
        return this.affineCipherHelper.transformCharacter(characterValue, decodeOperation);
    }
}
