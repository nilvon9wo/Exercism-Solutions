public enum Animal {
    FLY,
    SPIDER("It wriggled and jiggled and tickled inside her.", true),
    BIRD("How absurd to swallow a bird!"),
    CAT("Imagine that, to swallow a cat!"),
    DOG("What a hog, to swallow a dog!"),
    GOAT("Just opened her throat and swallowed a goat!"),
    COW("I don't know how she swallowed a cow!"),
    HORSE("She's dead, of course!");

    private final String remark;
    private final boolean hasExtendVerse;

    Animal(String remark, boolean hasExtendVerse) {
        this.remark = remark;
        this.hasExtendVerse = hasExtendVerse;
    }

    Animal(String remark) {
        this(remark, false);
    }

    Animal() {
        this(null, false);
    }

    public String getName() {
        return name()
                .toLowerCase(); // derive name from enum constant
    }

    public String getRemark() {
        return remark;
    }

    public boolean hasExtendVerse() {
        return hasExtendVerse;
    }

    public String indefiniteForm() {
        return EnglishArticles.prefixArticle(getName());
    }

    public String verseExtension() {
        if (!hasExtendVerse || remark == null || remark.isBlank()) {
            throw new IllegalStateException(getName() + " does not have extended verse");
        }
        return remark.replaceFirst("It", "that");
    }
}
