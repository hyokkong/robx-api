package com.rbox.object.application.port.in;

/**
 * 이미지 추가 명령.
 */
public record AddImageCommand(String url, int ordNo) {}

