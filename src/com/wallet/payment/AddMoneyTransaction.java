package com.wallet.payment;

public class AddMoneyTransaction extends Transaction {
    private BankAccount bank;
    private Wallet wallet;

    public AddMoneyTransaction(BankAccount bank, Wallet wallet, double amount) {
        super(amount);
        this.bank = bank;
        this.wallet = wallet;
    }

    @Override
    public TxnStatus execute() {
        if (amount <= 0 || !bank.debit(amount)) {
            status = TxnStatus.FAILED;
            return status;
        }
        try {
            wallet.credit(amount);
            status = TxnStatus.SUCCESS;
        } catch (RuntimeException e) {
            bank.credit(amount);
            status = TxnStatus.FAILED;
        }
        return status;
    }

    @Override
    public TxnType getType() { return TxnType.ADD_MONEY; }
}
