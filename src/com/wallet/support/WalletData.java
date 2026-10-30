package com.wallet.support;

import java.time.LocalDateTime;
import java.util.*;

/**
 * TEMPORARY standalone in-memory data layer for Member 2 practice.
 * Replace/adapt this support layer during team integration with Member 3
 * repositories and Member 4 models. Do not merge this file blindly.
 */
public final class WalletData {
    private WalletData() {}

    public enum KycStatus { BASIC, FULL }
    public enum UserStatus { ACTIVE, BLOCKED }
    public enum TxnType { ADD_MONEY, WITHDRAW, TRANSFER, CASHBACK, REFUND }
    public enum TxnStatus { SUCCESS, FAILED, REVERSED, PENDING }
    public enum DisputeStatus { RAISED, APPROVED, REJECTED }

    public static class User {
        public final String id, name, phone;
        public String pin;
        public KycStatus kycStatus = KycStatus.BASIC;
        public UserStatus status = UserStatus.ACTIVE;
        public User(String id, String name, String phone, String pin) {
            this.id=id; this.name=name; this.phone=phone; this.pin=pin;
        }
    }

    public static class Wallet {
        public final String userId;
        public long balancePaise;
        public Wallet(String userId) { this.userId=userId; }
        public long balanceRupees() { return balancePaise / 100; }
        public void credit(long paise) {
            if (paise <= 0) throw new IllegalArgumentException("Credit must be positive");
            balancePaise = Math.addExact(balancePaise, paise);
        }
        public void debit(long paise) {
            if (paise <= 0) throw new IllegalArgumentException("Debit must be positive");
            if (balancePaise < paise) throw new IllegalStateException("Insufficient wallet balance");
            balancePaise -= paise;
        }
    }

    public static class BankAccount {
        public final String accountNo, userId;
        public long balancePaise;
        public boolean primary;
        public BankAccount(String accountNo, String userId, long balancePaise) {
            this.accountNo=accountNo; this.userId=userId; this.balancePaise=balancePaise;
        }
    }

    public static class Transaction {
        public final String id, referenceId, senderId, receiverId;
        public final TxnType type;
        public final long amountPaise;
        public TxnStatus status;
        public final LocalDateTime createdAt;
        public String note;
        public Transaction(String id, String referenceId, String senderId, String receiverId,
                           TxnType type, long amountPaise, TxnStatus status) {
            this.id=id; this.referenceId=referenceId; this.senderId=senderId;
            this.receiverId=receiverId; this.type=type; this.amountPaise=amountPaise;
            this.status=status; this.createdAt=LocalDateTime.now();
        }
    }

    public static class Dispute {
        public final String id, transactionId, userId, reason;
        public DisputeStatus status = DisputeStatus.RAISED;
        public final LocalDateTime createdAt = LocalDateTime.now();
        public Dispute(String id, String transactionId, String userId, String reason) {
            this.id=id; this.transactionId=transactionId; this.userId=userId; this.reason=reason;
        }
    }

    public static class CashbackRecord {
        public final String id, userId, transactionId;
        public final long amountPaise;
        public final LocalDateTime createdAt = LocalDateTime.now();
        public CashbackRecord(String id, String userId, String transactionId, long amountPaise) {
            this.id=id; this.userId=userId; this.transactionId=transactionId; this.amountPaise=amountPaise;
        }
    }

    public static class Store {
        public final Map<String, User> usersById = new LinkedHashMap<>();
        public final Map<String, Wallet> walletsByUserId = new LinkedHashMap<>();
        public final Map<String, BankAccount> banksByAccountNo = new LinkedHashMap<>();
        public final Map<String, Transaction> transactionsById = new LinkedHashMap<>();
        public final Map<String, Dispute> disputesById = new LinkedHashMap<>();
        public final List<CashbackRecord> cashbackRecords = new ArrayList<>();
        public final Map<String, Integer> pinFailures = new HashMap<>();
        public final Map<String, LocalDateTime> lockedUntil = new HashMap<>();

        public User userByPhone(String phone) {
            return usersById.values().stream().filter(u -> u.phone.equals(phone)).findFirst().orElse(null);
        }
        public List<BankAccount> banksForUser(String userId) {
            List<BankAccount> result = new ArrayList<>();
            for (BankAccount b : banksByAccountNo.values()) if (b.userId.equals(userId)) result.add(b);
            return result;
        }
    }

    public static long rupees(long amount) {
        return Math.multiplyExact(amount, 100L);
    }
}
