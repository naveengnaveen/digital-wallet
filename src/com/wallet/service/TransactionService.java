package com.wallet.service;

import com.wallet.support.WalletData;
import com.wallet.support.WalletData.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class TransactionService {
    private final Store store;
    public TransactionService(Store store) { this.store = store; }

    public List<Transaction> history(String userId) {
        return store.transactionsById.values().stream()
            .filter(t -> userId.equals(t.senderId) || userId.equals(t.receiverId))
            .sorted(Comparator.comparing((Transaction t) -> t.createdAt).reversed())
            .toList();
    }
    public List<Transaction> filter(String userId, TxnType type, TxnStatus status,
                                    LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) throw new IllegalArgumentException("From date must be before to date");
        return history(userId).stream()
            .filter(t -> type == null || t.type == type)
            .filter(t -> status == null || t.status == status)
            .filter(t -> from == null || !t.createdAt.toLocalDate().isBefore(from))
            .filter(t -> to == null || !t.createdAt.toLocalDate().isAfter(to))
            .toList();
    }
    public boolean isDuplicate(String senderId, String receiverId, long amountRupees) {
        return store.transactionsById.values().stream()
            .anyMatch(t -> senderId.equals(t.senderId)
                && java.util.Objects.equals(receiverId, t.receiverId)
                && t.amountPaise == WalletData.rupees(amountRupees)
                && !t.createdAt.isBefore(java.time.LocalDateTime.now().minusMinutes(1)));
    }
}
