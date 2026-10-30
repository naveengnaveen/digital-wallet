package com.wallet.repository;

import com.wallet.models.Dispute;
import com.wallet.models.enums.DisputeStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Stores disputes. Eligibility and approval rules belong in DisputeService. */
public class DisputeRepository {
    private final Map<String, Dispute> disputesById = new HashMap<>();

    public synchronized void save(Dispute dispute) {
        if (dispute == null) throw new IllegalArgumentException("Dispute cannot be null");
        disputesById.put(dispute.getId(), dispute);
    }

    public synchronized Optional<Dispute> findById(String disputeId) {
        return Optional.ofNullable(disputesById.get(disputeId));
    }

    public synchronized List<Dispute> findByStatus(DisputeStatus status) {
        List<Dispute> result = new ArrayList<>();
        for (Dispute dispute : disputesById.values()) {
            if (status == dispute.getStatus()) result.add(dispute);
        }
        return result;
    }
}
