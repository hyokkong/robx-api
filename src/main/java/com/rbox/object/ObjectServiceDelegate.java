package com.rbox.object;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.rbox.object.application.port.in.ObjectUseCase;

/**
 * Delegate that exposes versioned object services.
 * Allows callers to obtain the proper implementation based on API version.
 */
@Service
@RequiredArgsConstructor
public class ObjectServiceDelegate {
    private final @Qualifier("objectV1Service") ObjectUseCase v1Service;
    private final @Qualifier("objectV2Service") com.rbox.object.v2.ObjectService v2Service;

    /**
     * Returns the v1 object service.
     */
    public ObjectUseCase v1() {
        return v1Service;
    }

    /**
     * Returns the v2 object service.
     */
    public com.rbox.object.v2.ObjectService v2() {
        return v2Service;
    }
}
