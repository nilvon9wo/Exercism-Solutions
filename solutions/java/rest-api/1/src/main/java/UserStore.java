import java.util.*;

class UserStore {
    private final Map<String, UserState> users = new HashMap<>();

    UserState get(String name) {
        this.ensure(name);
        return users.get(name);
    }

    void put(UserState user) {
        users.put(user.name, user);
    }

    Collection<UserState> getAll() {
        return users.values();
    }

    void ensure(String name) {
        users.putIfAbsent(name, new UserState(name));
    }
}