import com.wallet.service.*;
import com.wallet.support.WalletData.*;

/** Small smoke-test demo for the standalone Member 2 module. */
public class Main {
    public static void main(String[] args) {
        Store store = new Store();
        UserService users = new UserService(store);
        BankAccountService banks = new BankAccountService(store);
        WalletService wallets = new WalletService(store);
        TransferService transfers = new TransferService(store);
        RewardService rewards = new RewardService(store);

        User alice = users.register("Alice", "9876543210", "1234");
        User bob = users.register("Bob", "9876543211", "4321");
        banks.link(alice.id, "ALICE-001", 20000);
        wallets.addMoney(alice.id, "ALICE-001", 1000);
        Transaction transfer = transfers.transfer(alice.id, bob.phone, 500);
        CashbackRecord cashback = rewards.rewardFor(transfer);

        System.out.println("Transfer status: " + transfer.status);
        System.out.println("Alice wallet balance (rupees): " + store.walletsByUserId.get(alice.id).balanceRupees());
        System.out.println("Bob wallet balance (rupees): " + store.walletsByUserId.get(bob.id).balanceRupees());
        System.out.println("Cashback awarded (paise): " + (cashback == null ? 0 : cashback.amountPaise));
    }
}
