package com.rbox.object.application.port.in;

/**
 * 개체 생성 명령.
 */
public record CreateObjectCommand(String spcCd, String name, String sexCd, String bthYmd) {}

