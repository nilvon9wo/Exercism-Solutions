class LedgerService {
    private final UserStore store;

    LedgerService(UserStore store) {
        this.store = store;
    }

    void addUser(String name) {
        store.ensure(name);
    }

    void recordIou(String lender, String borrower, double amount) {
        UserState lenderState = store.get(lender);
        UserState borrowerState = store.get(borrower);
        applyIou(lenderState, borrowerState, amount);
    }

    private void applyIou(UserState lenderState, UserState borrowerState, double amount) {
        amount = this.netExistingOppositeDebt(lenderState, borrowerState, amount);
        amount = this.netExistingOppositeDebt(borrowerState, lenderState, amount);
        this.applyRemainingDebt(lenderState, borrowerState, amount);
    }

    private double netExistingOppositeDebt(UserState fromState, UserState toState, double amount) {
        String fromName = fromState.name;
        String toName = toState.name;
        double existing = toState.owes.getOrDefault(fromName, 0.0);
        if (existing <= 0) {
            return amount;
        }

        if (existing > amount) {
            double remaining = existing - amount;
            toState.owes.put(fromName, remaining);
            fromState.owedBy.put(toName, remaining);
            return 0.0;
        }

        amount -= existing;
        toState.owes.remove(fromName);
        fromState.owedBy.remove(toName);
        return amount;
    }

    private void applyRemainingDebt(
            UserState lenderState,
            UserState borrowerState,
            double amount
    ) {
        if (amount <= 0) {
            return;
        }

        lenderState.owedBy.put(borrowerState.name, amount);
        borrowerState.owes.put(lenderState.name, amount);
    }
}