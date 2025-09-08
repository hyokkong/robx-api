package com.rbox.search.v2;

/**
 * Parameters for the object search endpoint. It mirrors the listing search
 * but with fewer fields.
 */
public record SearchObjectsReq(
        String spcCd,
        String sexCd,
        String sizeCd,
        String morphMode,
        String morphIds,
        String sort,
        int page,
        int size) {
}

