DIGITAL WALLET AND UPI PAYMENT APP (C28)
Console application, Java, in-memory storage.

HOW TO RUN
----------
From the C28/ folder:

  Linux / Mac:
    mkdir -p out
    javac -d out Main.java $(find src -name "*.java")
    java -cp out Main

  Windows (PowerShell):
    mkdir out
    javac -d out Main.java (Get-ChildItem -Recurse src -Filter *.java).FullName
    java -cp out Main

Requires JDK 17 or later.

LAYERS
------
CLI -> Service -> Repository -> Models / Payment
Each layer calls only the layer below it.

TEAM OWNERSHIP
--------------
M1  CLI layer         : Main.java, README.txt, docs/System_diagram, src/com/wallet/CLI/*
M2  Service layer     : src/com/wallet/service/*
M3  Repository + data : src/com/wallet/repository/*, DataSeeder, docs/ER_diagram, docs/SQL_queries.sql
M4  Models + Payment  : src/com/wallet/models/*, src/com/wallet/payment/*

STUBS (temporary)
-----------------
The files below currently hold simple fake code so the CLI compiles and can be demoed.
The owner replaces them with the real implementation (keep the public method signatures):
  service/*          -> M2
  models/User, Customer, Admin, enums/*  -> M4
  payment/Transaction                    -> M4
  exception/WalletException              -> keep as the base exception

DEMO LOGINS WHILE STUBS ARE IN USE
----------------------------------
  Customer : register a new user from the main menu
  Admin    : phone 9999999999, PIN 0000

TEST SCENARIOS
--------------
1. Register user A and user B (phone 10 digits, PIN 4 digits). Duplicate phone must be rejected.
2. Login as A. Link a bank account. Linking a 4th account must be rejected.
3. Add money below Rs.10 and above Rs.50,000 must be rejected. Valid amount must succeed.
4. Withdraw more than the wallet balance must be rejected.
5. Send money to B using B's phone. Self-transfer, unknown receiver, wrong PIN must be rejected.
6. Transfer above Rs.10,000 must be rejected. Total above Rs.25,000 in a day must be rejected.
7. Send the same amount to the same receiver twice within 1 minute: second must be rejected.
8. Transfer of Rs.500 or more must give cashback (1%, max Rs.20 per txn, Rs.100 per month).
9. View history: latest first. Try filters by type, status and date range (yyyy-MM-dd).
10. Raise a dispute on a Success transfer. Login as Admin and approve the reversal.
11. Reversal when receiver balance is low must stay Pending. Use "Retry pending reversals" later.
12. Admin blocks a user: that user must not be able to log in or transact.
