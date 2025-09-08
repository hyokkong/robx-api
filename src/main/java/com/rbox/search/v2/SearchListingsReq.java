package com.rbox.search.v2;

import java.math.BigDecimal;

/**
 * Parameters for the listing search endpoint. Only a subset of the
 * documented fields are captured to keep the example concise.
 */
public record SearchListingsReq(
        String type,
        String spcCd,
        String sexCd,
        String sizeCd,
        BigDecimal priceMin,
        BigDecimal priceMax,
        String morphMode,
        String morphCat,
        String morphIds,
        Boolean hasImage,
        Long ownerUserId,
        Long ownerOrgId,
        String sort,
        int page,
        int size) {
}

