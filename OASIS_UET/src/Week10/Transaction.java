public class Transaction {
    public static final int TYPE_DEPOSIT_CHECKING = 0;
    public static final int TYPE_WITHDRAW_CHECKING = 1;
    public static final int TYPE_DEPOSIT_SAVINGS = 2;
    public static final int TYPE_WITHDRAW_SAVINGS = 3;
    private final int type;
    private final double amount;
    private final double initialBalance;
    private final double finalBalance;

    public Transaction() {
        this.type = 0;
        this.amount = 0;
        this.initialBalance = 0;
        this.finalBalance = 0;
    }

    /**
     *
     * @param type
     * @param amount
     * @param initialBalance
     * @param finalBalance
     */
    public Transaction(int type, double amount, double initialBalance, double finalBalance) {
        this.type = type;
        this.amount = amount;
        this.initialBalance = initialBalance;
        this.finalBalance = finalBalance;
    }

    /**
     *
     * @param type
     * @return
     */
    private String getTransactionTypeString(int type) {
        if (type == 0) {
            return "Nạp tiền vãng lai";
        }
        if (type == 1) {
            return "Rút tiền vãng lai";
        }
        if (type == 2) {
            return "Nạp tiền tiết kiệm";
        }
        if (type == 3) {
            return "Rút tiền tiết kiệm";
        }
        return "";
    }

    /**
     *
     * @return
     */
    public String getTransactionSummary() {
        return String.format(
                "- Kiểu giao dịch: %s. Số dư ban đầu: $%.2f. "
                        + "Số tiền: $%.2f. Số dư cuối: $%.2f.",
                getTransactionTypeString(this.type),
                this.initialBalance,
                this.amount,
                this.finalBalance
        );
    }
}