import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

class RestApi {
    private final UserStore store;
    private final LedgerService ledger;

    RestApi(UserStore store, User... initialUsers) {
        this.store = store;
        this.ledger = new LedgerService(store);
        Arrays.stream(initialUsers)
              .forEach(u -> store.put(UserState.from(u)));
    }

    RestApi(User... initialUsers) {
        this(new UserStore(), initialUsers);
    }

    String get(String url) {
        return this.get(url, new JSONObject());
    }

    String get(String url, JSONObject payload) {
        if ("/users".equals(url)) {
            List<UserState> selectedUsers = (payload.isEmpty())
                                            ? new ArrayList<>(store.getAll())
                                            : this.extractUsersFromPayload(payload);
            return this.buildUsersResponse(selectedUsers);
        }

        throw new IllegalArgumentException("Unknown endpoint");
    }

    private List<UserState> extractUsersFromPayload(final JSONObject payload) {
        JSONArray requestedUsers = payload.getJSONArray("users");
        List<UserState> resultStates = new ArrayList<>();
        for (int i = 0; i < requestedUsers.length(); i++) {
            resultStates.add(store.get(requestedUsers.getString(i)));
        }

        return resultStates;
    }

    String post(String url, JSONObject payload) {
        return switch (url) {
            case "/add" -> this.handleAddUser(payload);
            case "/iou" -> this.handleIou(payload);
            default -> throw new IllegalArgumentException("Unknown endpoint");
        };
    }

    private String handleAddUser(final JSONObject payload) {
        String userName = payload.getString("user");
        ledger.addUser(userName);
        return store.get(userName)
                    .toJson()
                    .toString();
    }

    private String handleIou(final JSONObject payload) {
        ledger.recordIou(
                payload.getString("lender"),
                payload.getString("borrower"),
                payload.getDouble("amount")
        );

        String lender = payload.getString("lender");
        String borrower = payload.getString("borrower");
        List<UserState> affectedUsers = this.getAffectedUsers(lender, borrower);
        return this.buildUsersResponse(affectedUsers);
    }

    private List<UserState> getAffectedUsers(final String lender, final String borrower) {
        return Arrays.asList(
                store.get(lender),
                store.get(borrower)
        );
    }

    private String buildUsersResponse(final List<UserState> users) {
        users.sort(Comparator.comparing(user -> user.name));
        return this.toJson(users)
                   .toString();
    }

    private JSONObject toJson(final List<UserState> users) {
        JSONArray usersArray = new JSONArray();
        for (UserState userState : users) {
            usersArray.put(userState.toJson());
        }

        return new JSONObject().put("users", usersArray);
    }
}