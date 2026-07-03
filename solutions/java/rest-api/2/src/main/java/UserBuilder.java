import java.util.ArrayList;
import java.util.List;

public class UserBuilder {
    private String name;
    private final List<Iou> owes = new ArrayList<>();
    private final List<Iou> owedBy = new ArrayList<>();

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder owes(String name, double amount) {
        owes.add(new Iou(name, amount));
        return this;
    }

    public UserBuilder owedBy(String name, double amount) {
        owedBy.add(new Iou(name, amount));
        return this;
    }

    public User build() {
        return new User(name, owes, owedBy);
    }
}
