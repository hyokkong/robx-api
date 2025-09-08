package com.rbox.lineage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rbox.lineage.application.port.in.LineageGraph;
import com.rbox.lineage.application.service.LineageService;

class LineageServiceTest {
    private LineageService service;

    @BeforeEach
    void setUp() {
        service = new LineageService();
    }

    @Test
    void depth3ShouldReturn15Nodes() {
        LineageGraph g = service.getAncestors(1L, 123L, 3);
        assertEquals(15, g.nodes().size());
        assertEquals(14, g.edges().size());
    }

    @Test
    void depth2ShouldReturn7Nodes() {
        LineageGraph g = service.getAncestors(1L, 123L, 2);
        assertEquals(7, g.nodes().size());
        assertEquals(6, g.edges().size());
    }

    @Test
    void depth1ShouldReturn3Nodes() {
        LineageGraph g = service.getAncestors(1L, 123L, 1);
        assertEquals(3, g.nodes().size());
        assertEquals(2, g.edges().size());
    }
}
