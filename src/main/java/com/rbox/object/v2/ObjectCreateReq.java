package com.rbox.object.v2;

/**
 * Request body for creating an object in the v2 API.
 * Only spcCd and sexCd are required.
 */
public record ObjectCreateReq(
        String spcCd,
        String name,
        String sexCd,
        String ownerType
) {}
