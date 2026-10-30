package com.wallet.service;

import com.wallet.support.WalletData.*;
import java.util.UUID;

public class UserService {
    private final Store store;
    public UserService(Store store) { this.store = store; }

    public User register(String name, String phone, String pin) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (phone == null || !phone.matches("\\d{10}")) throw new IllegalArgumentException("Phone must contain 10 digits");
        if (store.userByPhone(phone) != null) throw new IllegalArgumentException("Phone number already registered");
        validatePin(pin);
        User user = new User(UUID.randomUUID().toString(), name.trim(), phone, pin);
        store.usersById.put(user.id, user);
        store.walletsByUserId.put(user.id, new Wallet(user.id));
        return user;
    }

    public void updateKyc(String userId, KycStatus status) {
        User user = requireUser(userId);
        if (status == null) throw new IllegalArgumentException("KYC status is required");
        user.kycStatus = status;
    }
    public void blockUser(String userId) { requireUser(userId).status = UserStatus.BLOCKED; }
    public void unblockUser(String userId) { requireUser(userId).status = UserStatus.ACTIVE; }

    private User requireUser(String id) {
        User u = store.usersById.get(id);
        if (u == null) throw new IllegalArgumentException("User not found");
        return u;
    }
    private void validatePin(String pin) {
        if (pin == null || !pin.matches("\\d{4}")) throw new IllegalArgumentException("PIN must be exactly 4 digits");
    }
}
