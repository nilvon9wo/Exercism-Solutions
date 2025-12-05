enum GiftType {

    Partridge("{placeholder} in a Pear Tree", "Partridges"),
    Dove("Turtle {placeholder}", "Doves"),
    Hen("French {placeholder}", "Hens"),
    Bird("Calling {placeholder}", "Birds"),
    Ring("Gold {placeholder}", "Rings"),
    Goose("{placeholder}-a-Laying", "Geese"),
    Swan("{placeholder}-a-Swimming", "Swans"),
    Maid("{placeholder}-a-Milking", "Maids"),
    Lady("{placeholder} Dancing", "Ladies"),
    Lord("{placeholder}-a-Leaping", "Lords"),
    Piper("{placeholder} Piping", "Pipers"),
    Drummer("{placeholder} Drumming", "Drummers");

    private final String descriptionTemplate;
    private final String plural;

    GiftType(String descriptionTemplate, String plural) {
        this.descriptionTemplate = descriptionTemplate;
        this.plural = plural;
    }

    String toPhrase(int currentGiftNumber) {
        boolean isSingular = currentGiftNumber == 1;
        String giftDescription = this.getDescription(isSingular);
        String phrase = !isSingular
                                ? Number.fromInt(currentGiftNumber).toLowercaseString() + " " + giftDescription
                                : ArticleUtils.prependIndefiniteArticle(giftDescription);

        if (currentGiftNumber > 1) {
            phrase += ",";
        }

        return phrase;
    }

    private String getDescription(boolean isSingular) {
        String giftWord = isSingular
                                  ? this.name()
                                  : this.plural;
        return this.descriptionTemplate.replace("{placeholder}", giftWord);
    }
}