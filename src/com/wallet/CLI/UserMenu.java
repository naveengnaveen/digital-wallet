package com.wallet.CLI;

import com.wallet.models.User;
import com.wallet.service.BankAccountService;
import com.wallet.service.UserService;

import java.util.List;

/** M1: register, login, profile/KYC and bank-account screens. No business logic here. */
public class UserMenu {
    private final UserService userService;
    private final BankAccountService bankService;
    private final InputHelper in;

    public UserMenu(UserService userService, BankAccountService bankService, InputHelper in) {
        this.userService = userService;
        this.bankService = bankService;
        this.in = in;
    }

    public void register() {
        System.out.println("\n--- Register ---");
        String name = in.readNonEmpty("Name: ");
        String phone = in.readPhone("Phone (10 digits): ");
        String email = in.readEmail("Email: ");
        String pin = in.readPin("Set 4-digit PIN: ");
        User u = userService.register(name, phone, email, pin);
        System.out.println("Registered successfully. User ID: " + u.getId() + " (wallet created with Rs.0)");
    }

    public User login() {
        System.out.println("\n--- Login ---");
        String phone = in.readPhone("Phone: ");
        String pin = in.readPin("PIN: ");
        User u = userService.login(phone, pin);
        System.out.println("Welcome, " + u.getName() + "!");
        return u;
    }

    public User adminLogin() {
        System.out.println("\n--- Admin Login ---");
        String phone = in.readPhone("Admin phone: ");
        String pin = in.readPin("PIN: ");
        User a = userService.loginAdmin(phone, pin);
        System.out.println("Welcome, " + a.getName() + " (Admin)");
        return a;
    }

    public void profile(User user) {
        System.out.println("\n--- My Profile ---");
        System.out.println(userService.getProfile(user.getId()));
        showAccounts(user);
    }

    public void linkBank(User user) {
        System.out.println("\n--- Link Bank Account ---");
        String bank = in.readNonEmpty("Bank name: ");
        String accNo = in.readNonEmpty("Account number: ");
        bankService.link(user.getId(), bank, accNo);
        System.out.println("Bank account linked.");
    }

    public void unlinkBank(User user) {
        System.out.println("\n--- Unlink Bank Account ---");
        if (!showAccounts(user)) return;
        String accNo = in.readNonEmpty("Account number to unlink: ");
        bankService.unlink(user.getId(), accNo);
        System.out.println("Bank account unlinked.");
    }

    public void setPrimary(User user) {
        System.out.println("\n--- Set Primary Account ---");
        if (!showAccounts(user)) return;
        String accNo = in.readNonEmpty("Account number to make primary: ");
        bankService.setPrimary(user.getId(), accNo);
        System.out.println("Primary account updated.");
    }

    /** Prints linked accounts. Returns false if there are none. */
    boolean showAccounts(User user) {
        List<String> accounts = bankService.listAccounts(user.getId());
        if (accounts.isEmpty()) {
            System.out.println("No linked bank accounts.");
            return false;
        }
        System.out.println("Linked accounts:");
        for (int i = 0; i < accounts.size(); i++) System.out.println("  " + (i + 1) + ". " + accounts.get(i));
        return true;
    }
}
