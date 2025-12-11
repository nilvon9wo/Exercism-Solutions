import java.util.EnumMap;
import java.util.Map;

public class TransformerLookup {
    private final Map<Coder, AffineTransformerIntf> transformerCache = new EnumMap<>(Coder.class);

    private static final Map<Coder, Class<? extends AffineTransformerIntf>> TRANSFORMER_TYPES;
    static {
        TRANSFORMER_TYPES = new EnumMap<>(Coder.class);
        TRANSFORMER_TYPES.put(Coder.ENCODE, EncodeTransformer.class);
        TRANSFORMER_TYPES.put(Coder.DECODE, DecodeTransformer.class);
    }

    public AffineTransformerIntf get(Coder coderType) {
        return this.transformerCache.computeIfAbsent(coderType, this::tryCreateTransformer);
    }

    private AffineTransformerIntf tryCreateTransformer(Coder key) {
        Class<? extends AffineTransformerIntf> transformerClass = TRANSFORMER_TYPES.get(key);
        if (transformerClass == null) {
            throw new IllegalArgumentException("Unsupported coder type: " + key);
        }

        return this.tryCreateTransformer(key, transformerClass);
    }

    private AffineTransformerIntf tryCreateTransformer(
            Coder key,
            Class<? extends AffineTransformerIntf> transformerClass
    ) {
        try {
            return transformerClass.getDeclaredConstructor()
                           .newInstance();
        }
        catch (Exception ex) {
            throw new RuntimeException("Failed to instantiate transformer for " + key, ex);
        }
    }
}
