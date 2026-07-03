import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UserState {
    final String name;
    final Map<String, Double> owes = new HashMap<>();
    final Map<String, Double> owedBy = new HashMap<>();

    UserState(String name) {
        this.name = name;
    }

    JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("owes", new JSONObject(owes));
        obj.put("owedBy", new JSONObject(owedBy));
        obj.put("balance", balance());
        return obj;
    }

    double balance() {
        double owedByTotal = owedBy.values().stream().mapToDouble(Double::doubleValue).sum();
        double owesTotal = owes.values().stream().mapToDouble(Double::doubleValue).sum();
        return owedByTotal - owesTotal;
    }

    static UserState from(User user) {
        UserState state = new UserState(user.name());
        copyIous(user.owes(), state.owes);
        copyIous(user.owedBy(), state.owedBy);
        return state;
    }

    private static void copyIous(
            List<Iou> ious,
            Map<String, Double> target
    ) {
        for (Iou iou : ious) {
            target.put(iou.name(), iou.amount());
        }
    }
}