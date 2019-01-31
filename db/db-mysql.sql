
-- 创建image_main表
CREATE TABLE
    image_main
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        uuid VARCHAR(32),
        suffix VARCHAR(6),
        md5 VARCHAR(40) DEFAULT '',
        location VARCHAR(255),
        url VARCHAR(255),
		validStatus VARCHAR(2),
        insertTime DATETIME,
        updateTime DATETIME,
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX idx_image_main_uuid ON image_main (uuid);
CREATE INDEX idx_image_main_md5 ON image_main (md5);

alter table image_main add md5 VARCHAR(40) DEFAULT '' after suffix;
