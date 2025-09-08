package com.rbox.object.application.port.in;

/**
 * 개체 수정 명령.
 */
public record UpdateObjectCommand(String name, String sexCd, String bthYmd) {}

