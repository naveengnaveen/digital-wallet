package com.wallet.repository;

import com.wallet.models.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Stores and retrieves users. User validation belongs in UserService. */
public class UserRepository {
    private final Map<String, User> usersById = new HashMap<>();
    private final Map<String, User> usersByPhone = new HashMap<>();

    public synchronized void save(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");

        User previous = usersById.put(user.getId(), user);
        if (previous != null && previous.getPhone() != null) {
            usersByPhone.remove(previous.getPhone());
        }
        if (user.getPhone() != null) {
            usersByPhone.put(user.getPhone(), user);
        }
    }

    public synchronized Optional<User> findById(String userId) {
        return Optional.ofNullable(usersById.get(userId));
    }

    public synchronized Optional<User> findByPhone(String phone) {
        return Optional.ofNullable(usersByPhone.get(phone));
    }
}
