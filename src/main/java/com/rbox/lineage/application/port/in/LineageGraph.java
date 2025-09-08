package com.rbox.lineage.application.port.in;

import java.util.List;
import java.util.Map;

/**
 * 계보 그래프 응답 데이터.
 */
public record LineageGraph(Long root, int depth, List<LineageNode> nodes, List<LineageEdge> edges, Map<String, Object> meta) {}
