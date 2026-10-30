package com.wallet.payment;

public class WithdrawTransaction extends Transaction {
    private Wallet wallet;
    private BankAccount bank;

    public WithdrawTransaction(Wallet wallet, BankAccount bank, double amount) {
        super(amount);
        this.wallet = wallet;
        this.bank = bank;
    }

    @Override
    public TxnStatus execute() {
        if (amount <= 0 || !wallet.debit(amount)) {
            status = TxnStatus.FAILED;
            return status;
        }
        try {
            bank.credit(amount);
            status = TxnStatus.SUCCESS;
        } catch (RuntimeException e) {
            wallet.credit(amount);
            status = TxnStatus.FAILED;
        }
        return status;
    }

    @Override
    public TxnType getType() { return TxnType.WITHDRAW; }
}
