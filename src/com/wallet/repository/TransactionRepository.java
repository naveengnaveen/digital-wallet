package com.wallet.repository;

import com.wallet.payment.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Stores transactions. History sorting/filtering rules belong in TransactionService. */
public class TransactionRepository {
    private final Map<String, Transaction> transactionsById = new HashMap<>();

    public synchronized void save(Transaction transaction) {
        if (transaction == null) throw new IllegalArgumentException("Transaction cannot be null");
        transactionsById.put(transaction.getId(), transaction);
    }

    public synchronized List<Transaction> findAll() {
        return new ArrayList<>(transactionsById.values());
    }

    public synchronized Optional<Transaction> findById(String transactionId) {
        return Optional.ofNullable(transactionsById.get(transactionId));
    }

    public synchronized List<Transaction> findBySender(String senderId) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction txn : transactionsById.values()) {
            if (senderId != null && senderId.equals(txn.getSenderId())) result.add(txn);
        }
        return result;
    }

    public synchronized List<Transaction> findByReceiver(String receiverId) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction txn : transactionsById.values()) {
            if (receiverId != null && receiverId.equals(txn.getReceiverId())) result.add(txn);
        }
        return result;
    }
}
