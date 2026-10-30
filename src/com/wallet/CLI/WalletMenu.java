package com.wallet.CLI;

import com.wallet.models.User;
import com.wallet.payment.Transaction;
import com.wallet.service.BankAccountService;
import com.wallet.service.WalletService;

import java.util.List;

/** M1: add money and withdraw screens. */
public class WalletMenu {
    private final WalletService walletService;
    private final BankAccountService bankService;
    private final InputHelper in;

    public WalletMenu(WalletService walletService, BankAccountService bankService, InputHelper in) {
        this.walletService = walletService;
        this.bankService = bankService;
        this.in = in;
    }

    public void addMoney(User user) {
        System.out.println("\n--- Add Money ---");
        String accNo = pickAccount(user);
        if (accNo == null) return;
        double amount = in.readAmount("Amount (Rs.): ");
        Transaction t = walletService.addMoney(user.getId(), accNo, amount);
        printResult(t, user);
    }

    public void withdraw(User user) {
        System.out.println("\n--- Withdraw ---");
        String accNo = pickAccount(user);
        if (accNo == null) return;
        double amount = in.readAmount("Amount (Rs.): ");
        Transaction t = walletService.withdraw(user.getId(), accNo, amount);
        printResult(t, user);
    }

    private String pickAccount(User user) {
        List<String> accounts = bankService.listAccounts(user.getId());
        if (accounts.isEmpty()) {
            System.out.println("Link a bank account first.");
            return null;
        }
        System.out.println("Linked accounts:");
        for (int i = 0; i < accounts.size(); i++) System.out.println("  " + (i + 1) + ". " + accounts.get(i));
        return in.readNonEmpty("Account number: ");
    }

    private void printResult(Transaction t, User user) {
        System.out.println("Done. Ref ID: " + t.getRefId() + " | Status: " + t.getStatus());
        System.out.printf("Wallet balance: Rs.%.2f%n", walletService.getBalance(user.getId()));
    }
}
