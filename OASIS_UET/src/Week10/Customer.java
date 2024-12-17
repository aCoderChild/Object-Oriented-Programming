package Week16.Banking_System;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer {
    private long idNumber;
    private String fullName;
    private final List<Account> accountList;

    /**
     *
     */
    public Customer() {
        this.idNumber = 0;
        this.fullName = "";
        this.accountList = new ArrayList<>();
    }

    /**
     *
     * @param idNumber
     * @param fullName
     */
    public Customer(long idNumber, String fullName) {
        this.idNumber = idNumber;
        this.fullName = fullName;
        this.accountList = new ArrayList<>();
    }

    public long getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(long idNumber) {
        this.idNumber = idNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public String getCustomerInfo() {
        return String.format(
                "Số CMND: %d." + " Họ tên: %s.",
                this.idNumber,
                this.fullName
        );
    }

    /**
     *
     * @param account
     */
    public void addAccount(Account account) {
        for (Account accountCompare : accountList) {
            if (accountCompare.equals(account)) {
                accountCompare = account;
                return;
            }
        }
        accountList.add(account);
    }

    /**
     *
     * @param account
     */
    public void removeAccount(Account account) {
        for (Account accountCompare : this.accountList) {
            if (Objects.equals(accountCompare, account)) {
                this.accountList.remove(accountCompare);
                break;
            }
        }
    }
}