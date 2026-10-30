package com.wallet.service;

import com.wallet.support.WalletData.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class DisputeService {
    private final Store store;
    public DisputeService(Store store) { this.store = store; }

    public Dispute raise(String userId, String transactionId, String reason) {
        Transaction txn = store.transactionsById.get(transactionId);
        if (txn == null) throw new IllegalArgumentException("Transaction not found");
        if (!userId.equals(txn.senderId)) throw new IllegalArgumentException("Only the sender can raise this dispute");
        if (txn.type != TxnType.TRANSFER || txn.status != TxnStatus.SUCCESS)
            throw new IllegalStateException("Only successful transfers can be disputed");
        if (txn.createdAt.isBefore(LocalDateTime.now().minusHours(24)))
            throw new IllegalStateException("Dispute must be raised within 24 hours");
        if (reason == null || reason.isBlank()) throw new IllegalArgumentException("Dispute reason is required");
        Dispute dispute = new Dispute(UUID.randomUUID().toString(), transactionId, userId, reason.trim());
        store.disputesById.put(dispute.id, dispute);
        return dispute;
    }
}
