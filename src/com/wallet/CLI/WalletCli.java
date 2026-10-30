package com.wallet.CLI;

import com.wallet.exception.WalletException;
import com.wallet.models.User;
import com.wallet.service.BankAccountService;
import com.wallet.service.DisputeService;
import com.wallet.service.ReversalService;
import com.wallet.service.TransactionService;
import com.wallet.service.TransferService;
import com.wallet.service.UserService;
import com.wallet.service.WalletService;

/**
 * M1: main loop and menu routing. Talks to the service layer only (never to repositories).
 * Every WalletException thrown by a service is caught here and shown as a readable message.
 */
public class WalletCli {
    private final InputHelper in = new InputHelper();
    private final WalletService walletService;

    private final UserMenu userMenu;
    private final WalletMenu walletMenu;
    private final TransferMenu transferMenu;
    private final HistoryMenu historyMenu;
    private final AdminMenu adminMenu;

    public WalletCli(UserService userService, BankAccountService bankService, WalletService walletService,
                     TransferService transferService, TransactionService txnService,
                     DisputeService disputeService, ReversalService reversalService) {
        this.walletService = walletService;
        this.userMenu = new UserMenu(userService, bankService, in);
        this.walletMenu = new WalletMenu(walletService, bankService, in);
        this.transferMenu = new TransferMenu(transferService, walletService, in);
        this.historyMenu = new HistoryMenu(txnService, disputeService, in);
        this.adminMenu = new AdminMenu(disputeService, reversalService, userService, in);
    }

    public void start() {
        while (true) {
            System.out.println("\n===== DIGITAL WALLET =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            int choice = in.readInt("Choose: ", 1, 4);
            if (choice == 4) {
                System.out.println("Goodbye!");
                return;
            }
            try {
                switch (choice) {
                    case 1 -> userMenu.register();
                    case 2 -> userSession(userMenu.login());
                    case 3 -> adminMenu.run(userMenu.adminLogin());
                    default -> { }
                }
            } catch (WalletException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void userSession(User user) {
        while (true) {
            System.out.printf("%n===== USER MENU  |  %s  |  Balance: Rs.%.2f =====%n",
                    user.getName(), walletService.getBalance(user.getId()));
            System.out.println("1. My Profile / KYC status");
            System.out.println("2. Link Bank Account");
            System.out.println("3. Unlink Bank Account");
            System.out.println("4. Set Primary Account");
            System.out.println("5. Add Money");
            System.out.println("6. Withdraw");
            System.out.println("7. Send Money");
            System.out.println("8. Transaction History");
            System.out.println("9. Raise Dispute");
            System.out.println("10. Logout");
            int choice = in.readInt("Choose: ", 1, 10);
            if (choice == 10) {
                System.out.println("Logged out.");
                return;
            }
            try {
                switch (choice) {
                    case 1 -> userMenu.profile(user);
                    case 2 -> userMenu.linkBank(user);
                    case 3 -> userMenu.unlinkBank(user);
                    case 4 -> userMenu.setPrimary(user);
                    case 5 -> walletMenu.addMoney(user);
                    case 6 -> walletMenu.withdraw(user);
                    case 7 -> transferMenu.send(user);
                    case 8 -> historyMenu.showHistory(user);
                    case 9 -> historyMenu.raiseDispute(user);
                    default -> { }
                }
            } catch (WalletException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
