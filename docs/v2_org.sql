CREATE TABLE IF NOT EXISTS tb_org (
  org_id   BIGINT AUTO_INCREMENT COMMENT '조직ID',
  org_nm   VARCHAR(120) NOT NULL COMMENT '조직명',
  org_tp   VARCHAR(20) NOT NULL DEFAULT 'SHOP' COMMENT '유형(SHOP/TEAM)',
  stat_cd  VARCHAR(20) NOT NULL DEFAULT 'ACTV' COMMENT '상태',
  reg_dt   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록',
  PRIMARY KEY pk_tb_org (org_id),
  KEY idx_tb_org_tp (org_tp, stat_cd)
) ENGINE=InnoDB COMMENT='조직(샵/팀)';

CREATE TABLE IF NOT EXISTS tb_org_usr (
  org_id   BIGINT NOT NULL COMMENT '조직ID',
  usr_id   BIGINT NOT NULL COMMENT '사용자ID',
  role_cd  VARCHAR(20) NOT NULL COMMENT '역할(OWNER/ADMIN/EDITOR/VIEWER)',
  reg_dt   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록',
  PRIMARY KEY pk_tb_org_usr (org_id, usr_id),
  KEY idx_tb_org_usr_role (role_cd)
) ENGINE=InnoDB COMMENT='조직-사용자';

-- 리소스에 조직 소유 필드 추가 (NULL 허용)
ALTER TABLE tb_obj      ADD COLUMN own_org_id BIGINT NULL COMMENT '소유조직ID' AFTER own_usr_id;
ALTER TABLE tb_mkt_lst  ADD COLUMN own_org_id BIGINT NULL COMMENT '소유조직ID' AFTER obj_id;
ALTER TABLE tb_auc      ADD COLUMN own_org_id BIGINT NULL COMMENT '소유조직ID' AFTER obj_id;
