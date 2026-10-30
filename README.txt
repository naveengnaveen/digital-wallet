C28 - Member 2 Service Layer (standalone practice version)

This ZIP contains:
- 9 service classes with working starter business logic.
- WalletData.java: TEMPORARY in-memory models/store so Member 2 can compile and practise before teammates finish.
- Main.java: small smoke-test demo.
- No external libraries required. Java 17 or later is recommended.

Run from this folder:
    javac -d out Main.java src/com/wallet/support/WalletData.java src/com/wallet/service/*.java
    java -cp out Main

IMPORTANT TEAM INTEGRATION NOTES:
1. WalletData.java is only a temporary local support layer. Member 3 will own repositories/data storage; Member 4 will own the final models/enums.
2. Do not overwrite your teammates' files or merge this temporary WalletData.java as the final shared model.
3. Before integration, agree on constructors, method signatures, return types, and exception strategy with Members 3 and 4.
4. Business rules here are a learning baseline. Confirm the assignment's exact interpretation with your team (especially reversal, cashback eligibility, and daily/monthly limits).
5. PIN values are plain text in this practice version. In a real wallet application, store a secure salted hash and never log or expose PINs.

Git workflow (after copying into the team's C28 repo):
    git checkout -b m2-service
    git status
    git add src/com/wallet/service/
    git commit -m "M2: implement service layer"
    git push -u origin m2-service

Only add WalletData.java / Main.java to your team branch if the team explicitly agrees. The shared project already has its own Main.java and Member 4 models.
