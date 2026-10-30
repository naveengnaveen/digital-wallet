package com.wallet.service;

import com.wallet.support.WalletData.*;
import java.time.LocalDateTime;

public class PinSecurityService {
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_HOURS = 24;
    private final Store store;
    public PinSecurityService(Store store) { this.store = store; }

    public boolean verify(String userId, String enteredPin) {
        User user = store.usersById.get(userId);
        if (user == null) throw new IllegalArgumentException("User not found");
        LocalDateTime lockedUntil = store.lockedUntil.get(userId);
        if (lockedUntil != null && LocalDateTime.now().isBefore(lockedUntil))
            throw new IllegalStateException("PIN locked until " + lockedUntil);
        if (user.pin.equals(enteredPin)) {
            store.pinFailures.remove(userId);
            store.lockedUntil.remove(userId);
            return true;
        }
        int attempts = store.pinFailures.getOrDefault(userId, 0) + 1;
        store.pinFailures.put(userId, attempts);
        if (attempts >= MAX_ATTEMPTS) {
            store.lockedUntil.put(userId, LocalDateTime.now().plusHours(LOCK_HOURS));
            throw new IllegalStateException("3 wrong attempts. PIN locked for 24 hours");
        }
        return false;
    }
}
