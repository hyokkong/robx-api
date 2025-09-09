package com.rbox.org.v2;

/**
 * Request body for creating an organization in v2 APIs.
 */
public record OrgCreateReq(
        String orgNm,
        String orgTp) {
}
