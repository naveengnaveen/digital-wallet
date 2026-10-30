package com.wallet.repository;

import com.wallet.models.Wallet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Stores one wallet per user. Wallet rules belong in WalletService. */
public class WalletRepository {
    private final Map<String, Wallet> walletsByUserId = new HashMap<>();

    public synchronized void save(Wallet wallet) {
        if (wallet == null) throw new IllegalArgumentException("Wallet cannot be null");
        walletsByUserId.put(wallet.getUserId(), wallet);
    }

    public synchronized Optional<Wallet> findByUserId(String userId) {
        return Optional.ofNullable(walletsByUserId.get(userId));
    }
}
