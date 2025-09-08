package com.rbox.object.adapter.out.persistence.repository;

/**
 * 개체 이미지 정보를 보관하는 엔티티.
 */
public record ObjectImageEntity(
        Long imgId,
        Long objId,
        String url,
        int ordNo
) {}

