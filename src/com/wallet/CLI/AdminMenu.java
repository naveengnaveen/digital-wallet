package com.wallet.CLI;

import com.wallet.exception.WalletException;
import com.wallet.models.User;
import com.wallet.service.DisputeService;
import com.wallet.service.ReversalService;
import com.wallet.service.UserService;

import java.util.List;

/** M1: admin screens (disputes, reversal approval, block/unblock, KYC upgrade). */
public class AdminMenu {
    private final DisputeService disputeService;
    private final ReversalService reversalService;
    private final UserService userService;
    private final InputHelper in;

    public AdminMenu(DisputeService disputeService, ReversalService reversalService,
                     UserService userService, InputHelper in) {
        this.disputeService = disputeService;
        this.reversalService = reversalService;
        this.userService = userService;
        this.in = in;
    }

    public void run(User admin) {
        while (true) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. View all disputes");
            System.out.println("2. Approve reversal");
            System.out.println("3. Block user");
            System.out.println("4. Unblock user");
            System.out.println("5. Upgrade user KYC to Full");
            System.out.println("6. Retry pending reversals");
            System.out.println("7. Logout");
            int choice = in.readInt("Choose: ", 1, 7);
            if (choice == 7) return;
            try {
                handle(choice, admin);
            } catch (WalletException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void handle(int choice, User admin) {
        switch (choice) {
            case 1 -> {
                List<String> disputes = disputeService.listAll();
                if (disputes.isEmpty()) System.out.println("No disputes.");
                else disputes.forEach(d -> System.out.println("  " + d));
            }
            case 2 -> {
                String id = in.readNonEmpty("Dispute ID: ");
                System.out.println(reversalService.approve(admin.getId(), id));
            }
            case 3 -> {
                userService.blockUser(in.readPhone("User phone to block: "));
                System.out.println("User blocked.");
            }
            case 4 -> {
                userService.unblockUser(in.readPhone("User phone to unblock: "));
                System.out.println("User unblocked.");
            }
            case 5 -> {
                userService.upgradeKyc(in.readPhone("User phone: "));
                System.out.println("KYC upgraded to FULL.");
            }
            case 6 -> System.out.println(reversalService.retryPending() + " pending reversal(s) completed.");
            default -> { }
        }
    }
}
