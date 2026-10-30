package com.wallet.service;

import com.wallet.support.WalletData.*;
import java.util.UUID;

public class BankAccountService {
    private final Store store;
    public BankAccountService(Store store) { this.store = store; }

    public BankAccount link(String userId, String accountNo, long openingBalanceRupees) {
        requireActiveUser(userId);
        if (accountNo == null || accountNo.isBlank()) throw new IllegalArgumentException("Account number is required");
        if (openingBalanceRupees < 0) throw new IllegalArgumentException("Balance cannot be negative");
        if (store.banksForUser(userId).size() >= 3) throw new IllegalStateException("Maximum 3 bank accounts allowed");
        if (store.banksByAccountNo.containsKey(accountNo)) throw new IllegalArgumentException("Account number already linked");
        BankAccount bank = new BankAccount(accountNo, userId, Math.multiplyExact(openingBalanceRupees, 100L));
        bank.primary = store.banksForUser(userId).isEmpty();
        store.banksByAccountNo.put(accountNo, bank);
        return bank;
    }

    public void setPrimary(String userId, String accountNo) {
        BankAccount selected = requireOwnedBank(userId, accountNo);
        for (BankAccount b : store.banksForUser(userId)) b.primary = false;
        selected.primary = true;
    }

    public void unlink(String userId, String accountNo) {
        requireOwnedBank(userId, accountNo);
        store.banksByAccountNo.remove(accountNo);
        var remaining = store.banksForUser(userId);
        if (!remaining.isEmpty() && remaining.stream().noneMatch(b -> b.primary)) remaining.get(0).primary = true;
    }

    private BankAccount requireOwnedBank(String userId, String accountNo) {
        BankAccount b = store.banksByAccountNo.get(accountNo);
        if (b == null || !b.userId.equals(userId)) throw new IllegalArgumentException("Bank account not found for this user");
        return b;
    }
    private void requireActiveUser(String userId) {
        User u = store.usersById.get(userId);
        if (u == null || u.status != UserStatus.ACTIVE) throw new IllegalStateException("User not found or blocked");
    }
}
