import java.util.ArrayList;

public class Account {
    private double balance;
    private ArrayList<Transaction> transitionList = new ArrayList<>();

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Invalid balance: " + balance);
        } else {
            this.balance = balance;
        }
    }

    private void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("So tien ban nap vao khong hop le!\n");
            return;
        }
        balance += amount;
        transitionList.add(new Transaction(Transaction.DEPOSIT, amount, balance));
    }

    private void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("So tien ban rut ra khong hop le!\n");
            return;
        }
        if (amount > balance) {
            System.out.println("So tien ban rut vuot qua so du!\n");
            return;
        }

        balance -= amount;
        transitionList.add(new Transaction(Transaction.WITHDRAW, amount, balance));
    }

    public void addTransaction(double amount, String operation) {
        if (operation.equals(Transaction.DEPOSIT)) {
            deposit(amount);
        } else if (operation.equals(Transaction.WITHDRAW)) {
            withdraw(amount);
        } else {
            System.out.println("Yeu cau khong hop le!\n");
        }
    }

    public void printTransaction() {
        for (int i = 0; i < transitionList.size(); i++) {
            if (transitionList.get(i).getOperation().equals(Transaction.DEPOSIT)) {
                System.out.printf("Giao dich %d: Nap tien $%.2f. So du luc nay: $%.2f.\n", i + 1,
                        transitionList.get(i).getAmount(), transitionList.get(i).getBalance());
            } else if (transitionList.get(i).getOperation().equals(Transaction.WITHDRAW)) {
                System.out.printf("Giao dich %d: Rut tien $%.2f. So du luc nay: $%.2f.\n", i + 1,
                        transitionList.get(i).getAmount(), transitionList.get(i).getBalance());
            } else {
                System.out.println("Yeu cau khong hop le!");
            }
        }
    }
}