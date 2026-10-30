package com.wallet.payment;

public abstract class Transaction {
    protected double amount;
    protected TxnStatus status;

    public Transaction(double amount) {
        this.amount = amount;
        this.status = TxnStatus.PENDING;
    }

    public abstract TxnStatus execute();
    public abstract TxnType getType();

    public double getAmount() { return amount; }
    public TxnStatus getStatus() { return status; }
}
