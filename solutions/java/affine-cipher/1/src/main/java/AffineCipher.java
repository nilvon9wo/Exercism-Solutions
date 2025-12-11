public class AffineCipher {
    private final AffineCipherHelper affineCipherHelper;
    private final TransformerLookup transformerLookup;
    public AffineCipher(AffineCipherHelper affineCipherHelper, TransformerLookup transformerLookup) {
        this.affineCipherHelper = affineCipherHelper;
        this.transformerLookup = transformerLookup;
    }

    public AffineCipher() {
        this(new AffineCipherHelper(), new TransformerLookup());
    }

    public String encode(String text, int keyAMultiplier, int keyBShift) {
        this.affineCipherHelper.validateKeyCoprime(keyAMultiplier);
        String sanitizedText = this.affineCipherHelper.sanitizeForEncoding(text);
        String encodedText = this.transformerLookup.get(Coder.ENCODE)
                                     .transformText(sanitizedText, keyAMultiplier, keyBShift);
        return this.affineCipherHelper.groupIntoBlocksOfFive(encodedText);
    }

    public String decode(String text, int keyAMultiplier, int keyBShift) {
        this.affineCipherHelper.validateKeyCoprime(keyAMultiplier);
        String compactText = this.affineCipherHelper.removeSpaces(text);
        return this.transformerLookup.get(Coder.DECODE)
                       .transformText(compactText, keyAMultiplier, keyBShift);
    }
}
