package com.wallet.CLI;

import com.wallet.models.User;
import com.wallet.payment.Transaction;
import com.wallet.service.TransferService;
import com.wallet.service.WalletService;

/** M1: send money screen (receiver phone, amount, PIN). */
public class TransferMenu {
    private final TransferService transferService;
    private final WalletService walletService;
    private final InputHelper in;

    public TransferMenu(TransferService transferService, WalletService walletService, InputHelper in) {
        this.transferService = transferService;
        this.walletService = walletService;
        this.in = in;
    }

    public void send(User user) {
        System.out.println("\n--- Send Money ---");
        String receiverPhone = in.readPhone("Receiver phone: ");
        double amount = in.readAmount("Amount (Rs.): ");
        String pin = in.readPin("Enter your PIN: ");
        Transaction t = transferService.transfer(user.getId(), receiverPhone, amount, pin);
        System.out.println("Transfer " + t.getStatus() + ". Ref ID: " + t.getRefId());
        System.out.printf("Amount: Rs.%.2f | To: %s%n", t.getAmount(), receiverPhone);
        System.out.printf("Wallet balance: Rs.%.2f%n", walletService.getBalance(user.getId()));
    }
}
