public class BankAccount {

    private enum AccountState { CLOSED, OPEN }

    private AccountState state = AccountState.CLOSED;
    private int balance = 0;

    // Opens the account, fails if already open
    synchronized void open() throws BankAccountActionInvalidException {
        if (state == AccountState.OPEN) {
            throw new BankAccountActionInvalidException("Account already open");
        }
        balance = 0;
        state = AccountState.OPEN;
    }

    // Closes the account, fails if not open
    synchronized void close() throws BankAccountActionInvalidException {
        if (state == AccountState.CLOSED) {
            throw new BankAccountActionInvalidException("Account not open");
        }
        state = AccountState.CLOSED;
    }

    // Returns balance, only if open
    synchronized int getBalance() throws BankAccountActionInvalidException {
        ensureAccountIsOpen();
        return balance;
    }

    // Deposit into account, only if open and non-negative
    synchronized void deposit(int amount) throws BankAccountActionInvalidException {
        ensureAccountIsOpen();
        if (amount < 0) {
            throw new BankAccountActionInvalidException("Cannot deposit or withdraw negative amount");
        }
        balance += amount;
    }

    // Withdraw from account, only if open and sufficient funds
    synchronized void withdraw(int amount) throws BankAccountActionInvalidException {
        ensureAccountIsOpen();
        if (amount < 0) {
            throw new BankAccountActionInvalidException("Cannot deposit or withdraw negative amount");
        }
        if (amount > balance) {
            throw new BankAccountActionInvalidException("Cannot withdraw more money than is currently in the account");
        }
        balance -= amount;
    }

    // Helper to validate that account is open
    private void ensureAccountIsOpen() throws BankAccountActionInvalidException {
        if (state != AccountState.OPEN) {
            throw new BankAccountActionInvalidException("Account closed");
        }
    }
}
