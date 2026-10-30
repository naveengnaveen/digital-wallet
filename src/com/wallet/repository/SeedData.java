package com.wallet.repository;

import com.wallet.models.User;
import com.wallet.models.Wallet;
import com.wallet.models.BankAccount;
import java.util.List;

/**
 * Inserts prepared sample objects into repositories.
 *
 * Member 4 owns model constructors, so this class deliberately receives
 * constructed objects rather than assuming constructor signatures.
 */
public final class SeedData {
    private SeedData() {}

    public static void loadUsers(List<User> users, UserRepository repository) {
        for (User user : users) repository.save(user);
    }

    public static void loadWallets(List<Wallet> wallets, WalletRepository repository) {
        for (Wallet wallet : wallets) repository.save(wallet);
    }

    public static void loadBankAccounts(
            List<BankAccount> accounts, BankAccountRepository repository) {
        for (BankAccount account : accounts) repository.save(account);
    }

    /** Convenience method for the three initial data groups assigned to Member 3. */
    public static void loadInitialData(
            List<User> users,
            List<Wallet> wallets,
            List<BankAccount> bankAccounts,
            UserRepository userRepository,
            WalletRepository walletRepository,
            BankAccountRepository bankAccountRepository) {

        loadUsers(users, userRepository);
        loadWallets(wallets, walletRepository);
        loadBankAccounts(bankAccounts, bankAccountRepository);
    }
}
