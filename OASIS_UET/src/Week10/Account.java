import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    public static final String CHECKING = "CHECKING";
    public static final String SAVINGS = "SAVINGS";
    protected long accountNumber;
    protected double balance;
    protected List<Transaction> transactionList;

    /**
     * Account constructor
     */
    public Account() {
        this.accountNumber = 0;
        this.balance = 0;
        this.transactionList = new ArrayList<>();
    }

    /**
     *
     * @param accountNumber
     * @param balance
     */
    public Account(long accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactionList = new ArrayList<>();
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return this.balance;
    }

    /**
     *
     * @param amount
     * @throws InsufficientFundsException
     * @throws InvalidFundingAmountException
     */
    public void doWithdrawing(double amount) throws InsufficientFundsException,
            InvalidFundingAmountException {
        if (amount < 0) {
            throw new InvalidFundingAmountException(amount);
        } else if (amount > this.balance) {
            throw new InsufficientFundsException(amount);
        } else {
            this.balance -= amount;
        }
    }

    /**
     *
     * @param amount
     * @throws InvalidFundingAmountException
     */
    public void doDepositing(double amount) throws InvalidFundingAmountException {
        if (amount < 0) {
            throw new InvalidFundingAmountException(amount);
        } else {
            this.balance += amount;
        }
    }

    /**
     *
     * @return
     */
    public String getTransactionHistory() {
        StringBuilder transactionHistory = new StringBuilder();
        transactionHistory.append(String.format(
                "Lịch sử giao dịch của tài khoản %d:",
                this.accountNumber)
        );
        for (Transaction transaction : transactionList) {
            transactionHistory.append("\n");
            transactionHistory.append(transaction.getTransactionSummary());
        }
        return transactionHistory.toString();
    }

    public void addTransaction(Transaction transaction) {
        this.transactionList.add(transaction);
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Account) {
            Account another = (Account) o;
            return this.accountNumber == another.getAccountNumber();
        }
        return false;
    }

    public abstract void withdraw(double amount);

    public abstract void deposit(double amount);
}