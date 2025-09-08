package com.rbox.lineage.application.port.in;

/**
 * 계보 노드 정보를 표현한다.
 */
public record LineageNode(Long id, String name, String sexCd, String spcCd, String bthYmd) {}
