package com.rbox.org.v2;

/**
 * Request body for adding a member to an organization.
 */
public record OrgMemberAddReq(
        Long userId,
        String roleCd) {
}
