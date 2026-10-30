package com.wallet.payment;

public class RefundTransaction extends Transaction {
    private Wallet wallet;

    public RefundTransaction(Wallet wallet, double amount) {
        super(amount);
        this.wallet = wallet;
    }

    @Override
    public TxnStatus execute() {
        if (amount <= 0) {
            status = TxnStatus.FAILED;
            return status;
        }
        try {
            wallet.credit(amount);
            status = TxnStatus.SUCCESS;
        } catch (RuntimeException e) {
            status = TxnStatus.FAILED;
        }
        return status;
    }

    @Override
    public TxnType getType() { return TxnType.REFUND; }
}
