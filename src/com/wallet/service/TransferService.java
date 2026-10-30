package com.wallet.service;

import com.wallet.support.WalletData;
import com.wallet.support.WalletData.*;
import java.time.LocalDate;
import java.util.UUID;

public class TransferService {
    private final Store store;
    public TransferService(Store store) { this.store = store; }

    public Transaction transfer(String senderId, String receiverPhone, long amountRupees) {
        User sender = activeUser(senderId);
        User receiver = store.userByPhone(receiverPhone);
        if (receiver == null || receiver.status != UserStatus.ACTIVE) throw new IllegalArgumentException("Active receiver not found");
        if (sender.id.equals(receiver.id)) throw new IllegalArgumentException("Self-transfer is not allowed");
        if (amountRupees < 1 || amountRupees > 10000) throw new IllegalArgumentException("Transfer must be Rs.1 to Rs.10,000");
        long amount = WalletData.rupees(amountRupees);
        long todayTotal = store.transactionsById.values().stream()
            .filter(t -> senderId.equals(t.senderId) && t.type == TxnType.TRANSFER
                && t.status == TxnStatus.SUCCESS && t.createdAt.toLocalDate().equals(LocalDate.now()))
            .mapToLong(t -> t.amountPaise).sum();
        if (todayTotal + amount > WalletData.rupees(25000)) throw new IllegalStateException("Daily transfer limit Rs.25,000 exceeded");

        if (sender.kycStatus == KycStatus.BASIC) {
            long monthTotal = store.transactionsById.values().stream()
                .filter(t -> senderId.equals(t.senderId) && t.type == TxnType.TRANSFER
                    && t.status == TxnStatus.SUCCESS
                    && t.createdAt.getMonth() == java.time.LocalDateTime.now().getMonth()
                    && t.createdAt.getYear() == java.time.LocalDateTime.now().getYear())
                .mapToLong(t -> t.amountPaise).sum();
            if (monthTotal + amount > WalletData.rupees(20000)) throw new IllegalStateException("Basic KYC monthly limit Rs.20,000 exceeded");
        }

        Wallet from = store.walletsByUserId.get(senderId);
        Wallet to = store.walletsByUserId.get(receiver.id);
        if (from == null || to == null) throw new IllegalStateException("Wallet not found");
        from.debit(amount);
        try { to.credit(amount); }
        catch (RuntimeException ex) { from.credit(amount); throw ex; }

        String id = UUID.randomUUID().toString();
        Transaction txn = new Transaction(id, UUID.randomUUID().toString(), senderId, receiver.id,
                TxnType.TRANSFER, amount, TxnStatus.SUCCESS);
        store.transactionsById.put(id, txn);
        return txn;
    }
    private User activeUser(String id) {
        User u = store.usersById.get(id);
        if (u == null || u.status != UserStatus.ACTIVE) throw new IllegalStateException("User not found or blocked");
        return u;
    }
}
