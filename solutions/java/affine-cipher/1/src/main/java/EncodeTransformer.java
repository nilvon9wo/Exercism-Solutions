import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

public class EncodeTransformer implements AffineTransformerIntf {
    private final AffineCipherHelper affineCipherHelper;
    private EncodeTransformer(AffineCipherHelper affineCipherHelper) {
        this.affineCipherHelper = affineCipherHelper;
    }

    @SuppressWarnings("unused")
    public EncodeTransformer() {
        this(new AffineCipherHelper());
    }

    public static final int ALPHABET_SIZE = AffineCipherHelper.ALPHABET_SIZE;

    @Override
    public String transformText(String text, int keyAMultiplier, int keyBShift) {
        return text.chars()
                       .mapToObj(character -> this.transformCharacter((char) character, keyAMultiplier, keyBShift))
                       .collect(Collectors.joining());
    }

    private String transformCharacter(char characterValue, int keyAMultiplier, int keyBShift) {
        IntUnaryOperator encodeOperation = index -> (keyAMultiplier * index + keyBShift) % ALPHABET_SIZE;
        return this.affineCipherHelper.transformCharacter(characterValue, encodeOperation);
    }
}