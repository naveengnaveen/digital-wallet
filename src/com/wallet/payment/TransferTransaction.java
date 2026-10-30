package com.wallet.payment;

public class TransferTransaction extends Transaction {
    private Wallet sender;
    private Wallet receiver;

    public TransferTransaction(Wallet sender, Wallet receiver, double amount) {
        super(amount);
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public TxnStatus execute() {
        if (amount <= 0 || !sender.debit(amount)) {
            status = TxnStatus.FAILED;
            return status;
        }
        try {
            receiver.credit(amount);
            status = TxnStatus.SUCCESS;
        } catch (RuntimeException e) {
            sender.credit(amount);
            status = TxnStatus.FAILED;
        }
        return status;
    }

    @Override
    public TxnType getType() { return TxnType.TRANSFER; }
}
