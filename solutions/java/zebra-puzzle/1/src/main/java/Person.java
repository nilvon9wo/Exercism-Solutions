import java.util.Objects;

public record Person(Color color, Nationality nationality, Pet pet, Drink drink, Hobby hobby, House house) {

    public boolean noConflict(Person other) {
        Objects.requireNonNull(other, "other");
        return this.color != other.color
               && this.nationality != other.nationality
               && this.pet != other.pet
               && this.drink != other.drink
               && this.hobby != other.hobby
               && this.house != other.house;
    }
}