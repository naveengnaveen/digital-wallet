package com.wallet.service;

import com.wallet.support.WalletData;
import com.wallet.support.WalletData.*;
import java.util.UUID;

public class WalletService {
    private final Store store;
    public WalletService(Store store) { this.store = store; }

    public Transaction addMoney(String userId, String bankAccountNo, long amountRupees) {
        User user = activeUser(userId);
        validateAmount(amountRupees, 10, 50000);
        BankAccount bank = ownedBank(userId, bankAccountNo);
        long amount = WalletData.rupees(amountRupees);
        if (bank.balancePaise < amount) throw new IllegalStateException("Insufficient bank balance");
        Wallet wallet = wallet(userId);
        bank.balancePaise -= amount;
        wallet.credit(amount);
        return saveTxn(userId, userId, TxnType.ADD_MONEY, amount, TxnStatus.SUCCESS);
    }

    public Transaction withdraw(String userId, String bankAccountNo, long amountRupees) {
        activeUser(userId);
        validateAmount(amountRupees, 10, 50000);
        BankAccount bank = ownedBank(userId, bankAccountNo);
        long amount = WalletData.rupees(amountRupees);
        Wallet wallet = wallet(userId);
        wallet.debit(amount);
        bank.balancePaise = Math.addExact(bank.balancePaise, amount);
        return saveTxn(userId, userId, TxnType.WITHDRAW, amount, TxnStatus.SUCCESS);
    }

    private Transaction saveTxn(String sender, String receiver, TxnType type, long amount, TxnStatus status) {
        String id = UUID.randomUUID().toString();
        Transaction txn = new Transaction(id, UUID.randomUUID().toString(), sender, receiver, type, amount, status);
        store.transactionsById.put(id, txn);
        return txn;
    }
    private Wallet wallet(String id) {
        Wallet w = store.walletsByUserId.get(id);
        if (w == null) throw new IllegalStateException("Wallet not found");
        return w;
    }
    private User activeUser(String id) {
        User u = store.usersById.get(id);
        if (u == null || u.status != UserStatus.ACTIVE) throw new IllegalStateException("User not found or blocked");
        return u;
    }
    private BankAccount ownedBank(String userId, String no) {
        BankAccount b = store.banksByAccountNo.get(no);
        if (b == null || !b.userId.equals(userId)) throw new IllegalArgumentException("Linked bank account not found");
        return b;
    }
    private void validateAmount(long amount, long min, long max) {
        if (amount < min || amount > max) throw new IllegalArgumentException("Amount must be between Rs." + min + " and Rs." + max);
    }
}
