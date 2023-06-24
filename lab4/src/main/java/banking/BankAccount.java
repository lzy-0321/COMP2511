package banking;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance must be >= 0");
        }
        this.balance = initialBalance;
    }

    /**
     * Deposit money into the account
     *
     * @param amount - The amount to deposit
     * @pre amount > 0
     * @post balance = old balance + amount
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be > 0");
        }
        balance += amount;
    }

    /**
     * Withdraw money from the account
     *
     * @param amount - The amount to withdraw
     * @pre amount > 0 AND amount <= balance
     * @post balance = old balance - amount
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be > 0");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds for withdrawal");
        }
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}

class LoggedBankAccount extends BankAccount {
    private List<String> logs;

    public LoggedBankAccount(double initialBalance) {
        super(initialBalance);
        this.logs = new ArrayList<>();
    }

    /**
     * Deposit money into the account and log the action
     *
     * @param amount - The amount to deposit
     * @pre amount > 0
     * @post balance = old balance + amount AND a log has been created
     */
    @Override
    public void deposit(double amount) {
        super.deposit(amount);
        logs.add("Deposit: " + amount);
    }

    /**
     * Withdraw money from the account and log the action
     *
     * @param amount - The amount to withdraw
     * @pre amount > 0 AND amount <= balance
     * @post balance = old balance - amount AND a log has been created
     */
    @Override
    public void withdraw(double amount) {
        super.withdraw(amount);
        logs.add("Withdrawal: " + amount);
    }

    public List<String> getLogs() {
        return logs;
    }
}
