package com.wallet.service;

import com.wallet.support.WalletData.*;

public class ReversalService {
    private final Store store;
    public ReversalService(Store store) { this.store = store; }

    public void approveDispute(String disputeId) {
        Dispute dispute = store.disputesById.get(disputeId);
        if (dispute == null) throw new IllegalArgumentException("Dispute not found");
        if (dispute.status != DisputeStatus.RAISED) throw new IllegalStateException("Dispute is already processed");
        Transaction txn = store.transactionsById.get(dispute.transactionId);
        if (txn == null || txn.status != TxnStatus.SUCCESS) throw new IllegalStateException("Original transfer is not reversible");
        Wallet sender = store.walletsByUserId.get(txn.senderId);
        Wallet receiver = store.walletsByUserId.get(txn.receiverId);
        if (receiver == null || sender == null) throw new IllegalStateException("Wallet not found");
        if (receiver.balancePaise < txn.amountPaise) {
            txn.status = TxnStatus.PENDING;
            throw new IllegalStateException("Reversal pending: receiver has insufficient balance");
        }
        receiver.debit(txn.amountPaise);
        sender.credit(txn.amountPaise);
        txn.status = TxnStatus.REVERSED;
        dispute.status = DisputeStatus.APPROVED;
    }
    public void rejectDispute(String disputeId) {
        Dispute d = store.disputesById.get(disputeId);
        if (d == null) throw new IllegalArgumentException("Dispute not found");
        if (d.status != DisputeStatus.RAISED) throw new IllegalStateException("Dispute is already processed");
        d.status = DisputeStatus.REJECTED;
    }
}
