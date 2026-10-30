package com.wallet.repository;

import com.wallet.models.BankAccount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Stores linked bank accounts. Link limits and unlink rules belong in BankAccountService. */
public class BankAccountRepository {
    private final Map<String, BankAccount> accountsByNumber = new HashMap<>();

    public synchronized void save(BankAccount account) {
        if (account == null) throw new IllegalArgumentException("Bank account cannot be null");
        accountsByNumber.put(account.getAccountNo(), account);
    }

    public synchronized Optional<BankAccount> findByAccountNo(String accountNo) {
        return Optional.ofNullable(accountsByNumber.get(accountNo));
    }

    public synchronized List<BankAccount> findByUser(String userId) {
        List<BankAccount> result = new ArrayList<>();
        for (BankAccount account : accountsByNumber.values()) {
            if (userId != null && userId.equals(account.getUserId())) result.add(account);
        }
        return result;
    }

    public synchronized boolean delete(String accountNo) {
        return accountsByNumber.remove(accountNo) != null;
    }
}
