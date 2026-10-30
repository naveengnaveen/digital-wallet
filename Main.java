import com.wallet.CLI.WalletCli;
import com.wallet.service.BankAccountService;
import com.wallet.service.DisputeService;
import com.wallet.service.ReversalService;
import com.wallet.service.TransactionService;
import com.wallet.service.TransferService;
import com.wallet.service.UserService;
import com.wallet.service.WalletService;

/**
 * M1: entry point. Wires services together and starts the CLI.
 * TODO (after M3 finishes repositories + DataSeeder): create repositories first, pass them into the
 * services, and call DataSeeder.seed(...) before cli.start(). Constructors below will change then.
 */
public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        BankAccountService bankService = new BankAccountService();
        TransactionService txnService = new TransactionService();
        WalletService walletService = new WalletService(txnService);
        TransferService transferService = new TransferService(userService, walletService, txnService);
        DisputeService disputeService = new DisputeService(txnService);
        ReversalService reversalService = new ReversalService(disputeService, txnService);

        new WalletCli(userService, bankService, walletService, transferService,
                txnService, disputeService, reversalService).start();
    }
}
