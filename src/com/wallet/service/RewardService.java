package com.wallet.service;

import com.wallet.support.WalletData;
import com.wallet.support.WalletData.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class RewardService {
    private final Store store;
    public RewardService(Store store) { this.store = store; }

    public CashbackRecord rewardFor(Transaction txn) {
        if (txn == null || txn.status != TxnStatus.SUCCESS || txn.type != TxnType.TRANSFER)
            throw new IllegalArgumentException("Cashback applies only to successful transfers");
        if (txn.amountPaise < WalletData.rupees(500)) return null;
        User user = store.usersById.get(txn.senderId);
        if (user == null) throw new IllegalArgumentException("User not found");
        long monthPaid = store.cashbackRecords.stream()
            .filter(r -> r.userId.equals(user.id)
                && r.createdAt.getMonth() == LocalDateTime.now().getMonth()
                && r.createdAt.getYear() == LocalDateTime.now().getYear())
            .mapToLong(r -> r.amountPaise).sum();
        long reward = Math.min(txn.amountPaise / 100, WalletData.rupees(20)); // 1%, capped at Rs.20
        long remaining = Math.max(0, WalletData.rupees(100) - monthPaid);
        reward = Math.min(reward, remaining);
        if (reward <= 0) return null;
        Wallet wallet = store.walletsByUserId.get(user.id);
        if (wallet == null) throw new IllegalStateException("Wallet not found");
        wallet.credit(reward);
        CashbackRecord record = new CashbackRecord(UUID.randomUUID().toString(), user.id, txn.id, reward);
        store.cashbackRecords.add(record);
        return record;
    }
}
