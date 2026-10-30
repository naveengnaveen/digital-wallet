package com.wallet.CLI;

import com.wallet.models.User;
import com.wallet.models.enums.TxnStatus;
import com.wallet.models.enums.TxnType;
import com.wallet.payment.Transaction;
import com.wallet.service.DisputeService;
import com.wallet.service.TransactionService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** M1: transaction history (with filters) and raise-dispute screens. */
public class HistoryMenu {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final TransactionService txnService;
    private final DisputeService disputeService;
    private final InputHelper in;

    public HistoryMenu(TransactionService txnService, DisputeService disputeService, InputHelper in) {
        this.txnService = txnService;
        this.disputeService = disputeService;
        this.in = in;
    }

    public void showHistory(User user) {
        System.out.println("\n--- Transaction History ---");
        System.out.println("Filters (press Enter to skip any)");
        TxnType type = readType();
        TxnStatus status = readStatus();
        LocalDate from = in.readDateOrBlank("From date (yyyy-MM-dd): ");
        LocalDate to = in.readDateOrBlank("To date   (yyyy-MM-dd): ");

        List<Transaction> list = txnService.getHistory(user.getId(), type, status, from, to);
        if (list.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        System.out.printf("%-12s %-10s %12s  %-9s %s%n", "RefID", "Type", "Amount", "Status", "Date");
        System.out.println("-".repeat(64));
        for (Transaction t : list) {
            System.out.printf("%-12s %-10s %12.2f  %-9s %s%n",
                    t.getRefId(), t.getType(), t.getAmount(), t.getStatus(), t.getTimestamp().format(FMT));
        }
    }

    public void raiseDispute(User user) {
        System.out.println("\n--- Raise Dispute ---");
        String ref = in.readNonEmpty("Transaction Ref ID: ");
        String id = disputeService.raise(user.getId(), ref);
        System.out.println("Dispute raised. Dispute ID: " + id + " (waiting for admin approval)");
    }

    private TxnType readType() {
        while (true) {
            String s = in.readLine("Type (ADD_MONEY/WITHDRAW/TRANSFER/CASHBACK/REFUND): ").toUpperCase();
            if (s.isEmpty()) return null;
            try { return TxnType.valueOf(s); }
            catch (IllegalArgumentException e) { System.out.println("  Unknown type."); }
        }
    }

    private TxnStatus readStatus() {
        while (true) {
            String s = in.readLine("Status (SUCCESS/FAILED/REVERSED/PENDING): ").toUpperCase();
            if (s.isEmpty()) return null;
            try { return TxnStatus.valueOf(s); }
            catch (IllegalArgumentException e) { System.out.println("  Unknown status."); }
        }
    }
}
