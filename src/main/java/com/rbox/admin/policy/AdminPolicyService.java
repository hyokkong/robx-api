package com.rbox.admin.policy;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class AdminPolicyService {
    private final Clock clock;

    private final Map<String, MarketPolicy> marketPolicies = new ConcurrentHashMap<>();
    private final Map<String, BreedingPolicy> breedingPolicies = new ConcurrentHashMap<>();
    private final Map<String, QualityPolicy> qualityPolicies = new ConcurrentHashMap<>();
    private final Map<String, Map<String, SizePolicy>> sizePolicies = new ConcurrentHashMap<>();

    private final List<AuditLog> auditLogs = new CopyOnWriteArrayList<>();
    private final AtomicLong audSeq = new AtomicLong();

    public AdminPolicyService(Clock clock) {
        this.clock = clock;
    }

    public Paged<MarketPolicy> listMarketPolicies(String useYn, int page, int size) {
        List<MarketPolicy> list = new ArrayList<>(marketPolicies.values());
        if (useYn != null) {
            list = list.stream().filter(p -> useYn.equals(p.useYn())).toList();
        }
        int total = list.size();
        int from = Math.max(0, (page - 1) * size);
        int to = Math.min(from + size, total);
        List<MarketPolicy> content = from >= total ? List.of() : list.subList(from, to);
        return new Paged<>(content, total, page, size);
    }

    public MarketPolicy upsertMarketPolicy(String polCd, MarketPolicyRequest req, Long uid) {
        MarketPolicy old = marketPolicies.get(polCd);
        MarketPolicy newer = new MarketPolicy(polCd, req.polVal(), req.useYn(), clock.instant());
        marketPolicies.put(polCd, newer);
        audit(uid, "POL_UPSERT", "tb_mkt_pol", polCd, Map.of("old", old, "new", newer));
        return newer;
    }

    public void deleteMarketPolicy(String polCd, Long uid) {
        MarketPolicy old = marketPolicies.remove(polCd);
        audit(uid, "POL_DELETE", "tb_mkt_pol", polCd, Map.of("old", old, "new", null));
    }

    public BreedingPolicy getBreedingPolicy(String polCd) {
        return breedingPolicies.get(polCd);
    }

    public BreedingPolicy upsertBreedingPolicy(String polCd, BreedingPolicyRequest req, Long uid) {
        if (req.etaMin() > req.etaMax()) throw new IllegalArgumentException("etaMin <= etaMax");
        BreedingPolicy old = breedingPolicies.get(polCd);
        BreedingPolicy newer = new BreedingPolicy(polCd, req.etaMin(), req.etaMax(), req.cdlDay(), req.layGap());
        breedingPolicies.put(polCd, newer);
        audit(uid, "POL_UPSERT", "tb_brd_pol", polCd, Map.of("old", old, "new", newer));
        return newer;
    }

    public QualityPolicy getQualityPolicy(String polCd) {
        return qualityPolicies.get(polCd);
    }

    public QualityPolicy upsertQualityPolicy(String polCd, QualityPolicyRequest req, Long uid) {
        if (req.g1Min() > req.g2Min() || req.g2Min() > req.g3Min())
            throw new IllegalArgumentException("g1<=g2<=g3");
        QualityPolicy old = qualityPolicies.get(polCd);
        QualityPolicy newer = new QualityPolicy(polCd, req.g1Min(), req.g2Min(), req.g3Min(), req.rptPnlA(), req.rptPnlB());
        qualityPolicies.put(polCd, newer);
        audit(uid, "POL_UPSERT", "tb_qual_pol", polCd, Map.of("old", old, "new", newer));
        return newer;
    }

    public List<SizePolicy> listSizePolicies(String spcCd) {
        return new ArrayList<>(sizePolicies.getOrDefault(spcCd, Map.of()).values());
    }

    public SizePolicy upsertSizePolicy(String spcCd, String szCd, SizePolicyRequest req, Long uid) {
        if (req.ageMin() != null && req.ageMax() != null && req.ageMin() > req.ageMax())
            throw new IllegalArgumentException("ageMin <= ageMax");
        if (req.wtMin() != null && req.wtMax() != null && req.wtMin() > req.wtMax())
            throw new IllegalArgumentException("wtMin <= wtMax");
        Map<String, SizePolicy> map = sizePolicies.computeIfAbsent(spcCd, k -> new ConcurrentHashMap<>());
        SizePolicy old = map.get(szCd);
        SizePolicy newer = new SizePolicy(spcCd, szCd, req.ageMin(), req.ageMax(), req.wtMin(), req.wtMax(), req.adjTp());
        map.put(szCd, newer);
        audit(uid, "SIZ_UPSERT", "tb_siz_pol", spcCd + ":" + szCd, Map.of("old", old, "new", newer));
        return newer;
    }

    public void deleteSizePolicy(String spcCd, String szCd, Long uid) {
        Map<String, SizePolicy> map = sizePolicies.get(spcCd);
        if (map == null) return;
        SizePolicy old = map.remove(szCd);
        audit(uid, "SIZ_DELETE", "tb_siz_pol", spcCd + ":" + szCd, Map.of("old", old, "new", null));
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    private void audit(Long uid, String actTp, String tgtTb, String tgtId, Map<String, Object> diff) {
        long id = audSeq.incrementAndGet();
        auditLogs.add(new AuditLog(id, uid, actTp, tgtTb, tgtId, diff, Instant.now(clock)));
    }
}
