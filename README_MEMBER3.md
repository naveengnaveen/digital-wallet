# Member 3 – Repository Layer + Data

## Files in this contribution
- `UserRepository.java`
- `WalletRepository.java`
- `BankAccountRepository.java`
- `TransactionRepository.java`
- `DisputeRepository.java`
- `CashbackRepository.java`
- `SeedData.java`
- `docs/ER_diagram.md`
- `docs/SQL_queries.sql`

## Architecture boundary
Repositories perform storage and retrieval only. Business validation, KYC limits,
transfer limits, PIN security, dispute eligibility, transaction filtering policy,
and reward calculations belong to Member 2's service layer.

The current implementation uses in-memory `HashMap` storage. Data disappears
when the Java process stops; this matches the assigned HashMap/List scope.

## Model contract required from Member 4
Before integration, agree on these exact public methods and String identifier types:

| Class | Required methods |
|---|---|
| `User` | `getId()`, `getPhone()` |
| `Wallet` | `getUserId()` |
| `BankAccount` | `getAccountNo()`, `getUserId()` |
| `Transaction` | `getId()`, `getSenderId()`, `getReceiverId()` |
| `Dispute` | `getId()`, `getStatus()` |
| `CashbackRecord` | `getUserId()` |

`Dispute.getStatus()` should return `DisputeStatus`.

If the model layer uses different getter names or ID types, change the repository
code to match the team's agreed model contract. Do not independently change
Member 4's files.

## Repository API for Member 2
- `UserRepository`: `save`, `findById`, `findByPhone`
- `WalletRepository`: `save`, `findByUserId`
- `BankAccountRepository`: `save`, `findByAccountNo`, `findByUser`, `delete`
- `TransactionRepository`: `save`, `findAll`, `findById`, `findBySender`, `findByReceiver`
- `DisputeRepository`: `save`, `findById`, `findByStatus`
- `CashbackRepository`: `save(record, month)`, `findByUserAndMonth`

Single-record lookups return `Optional<T>`. Collection lookups return a new list.

## Seed data
`SeedData` accepts model objects already constructed by the model layer. This is
intentional: Member 3 should not invent constructors or duplicate Member 4's
model implementation. Once Member 4 shares constructors/factory methods, create
the hardcoded sample objects in the agreed bootstrap location and pass them to
`SeedData.loadInitialData(...)`.

## Integration checklist
1. Confirm model getter names and ID types with Member 4.
2. Confirm method signatures with Member 2.
3. Place these files under the existing `src/com/wallet/repository/` folder.
4. Compile with the real `models`, `models/enums`, and `payment` packages.
5. Test save/find/delete behavior.
6. Commit only Member 3 files on branch `m3-repository`.
