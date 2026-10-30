package com.wallet.repository;

import com.wallet.models.CashbackRecord;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Stores cashback records by user and month. Cashback calculation belongs in RewardService. */
public class CashbackRepository {
    private final Map<String, List<CashbackRecord>> recordsByUserMonth = new HashMap<>();

    private String createKey(String userId, YearMonth month) {
        return userId + "::" + month;
    }

    public synchronized void save(CashbackRecord record, YearMonth month) {
        if (record == null) throw new IllegalArgumentException("Cashback record cannot be null");
        if (month == null) throw new IllegalArgumentException("Month cannot be null");

        String key = createKey(record.getUserId(), month);
        recordsByUserMonth.computeIfAbsent(key, ignored -> new ArrayList<>()).add(record);
    }

    public synchronized List<CashbackRecord> findByUserAndMonth(String userId, YearMonth month) {
        if (userId == null || month == null) return new ArrayList<>();
        return new ArrayList<>(recordsByUserMonth.getOrDefault(
                createKey(userId, month), new ArrayList<>()));
    }
}
