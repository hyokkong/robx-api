package com.rbox.lineage.application.port.in;

/**
 * 계보 조회용 유스케이스.
 */
public interface LineageUseCase {
    /**
     * 주어진 개체의 조상 계보를 조회한다.
     * 
     * @param uid    로그인 사용자 ID
     * @param rootId 루트 개체 ID
     * @param depth  조회 깊이(1~3)
     * @return 계보 그래프
     */
    LineageGraph getAncestors(Long uid, Long rootId, int depth);
}
