-- Digital Wallet – Member 3 SQL documentation
-- These are illustrative queries; align table/column names with the final schema.

-- Query 1: Find a user by phone number.
SELECT user_id, name, phone, kyc_status, user_status
FROM users
WHERE phone = :phone;

-- Query 2: Get the transaction history for a user (sent or received).
SELECT transaction_id, sender_id, receiver_id, amount,
       txn_type, txn_status, created_at
FROM transactions
WHERE sender_id = :user_id OR receiver_id = :user_id
ORDER BY created_at DESC;

-- Query 3: Calculate cashback earned by a user in a month.
SELECT user_id, SUM(amount) AS total_cashback
FROM cashback_records
WHERE user_id = :user_id
  AND created_at >= :month_start
  AND created_at < :next_month_start
GROUP BY user_id;
