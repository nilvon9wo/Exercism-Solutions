import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

record Allergies(int score) {

    private static final int ALLERGEN_MASK = 0xFF;

    Allergies(int score) {
        // mask out any bits outside of valid allergens
        this.score = score & ALLERGEN_MASK;
    }

    List<Allergen> getList() {
        return Stream.of(Allergen.values())
                       .filter(this::isAllergicTo)
                       .collect(Collectors.toList());
    }

    boolean isAllergicTo(Allergen allergen) {
        int allergenFlag = this.score & allergen.getScore();
        return allergenFlag != 0;
    }
}
