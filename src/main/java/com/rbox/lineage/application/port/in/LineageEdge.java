package com.rbox.lineage.application.port.in;

/**
 * 계보 간선(from->to 관계) 정보를 표현한다.
 */
public record LineageEdge(Long from, Long to, String rel) {}
