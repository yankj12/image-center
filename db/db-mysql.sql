
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



-- 创建image_ref表
CREATE TABLE
    image_ref
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        uuid VARCHAR(32) DEFAULT '',
        md5 VARCHAR(40) DEFAULT '',
        displayName VARCHAR(255) DEFAULT '',
        suffix VARCHAR(6) DEFAULT '',
        userCode VARCHAR(20) DEFAULT '',
        category VARCHAR(20) DEFAULT '',
		validStatus VARCHAR(2)DEFAULT '1',
        insertTime DATETIME,
        updateTime DATETIME,
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX idx_image_ref_uuid ON image_ref (uuid);
CREATE INDEX idx_image_ref_md5 ON image_ref (md5);


-- 创建image_tag表
CREATE TABLE
    image_tag
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        md5 VARCHAR(40) DEFAULT '',
        tagName VARCHAR(40) DEFAULT '',
		validStatus VARCHAR(2)DEFAULT '1',
        insertTime DATETIME,
        updateTime DATETIME,
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX idx_image_tag_md5 ON image_tag (md5);

alter table image_ref add suffix VARCHAR(6) DEFAULT '' after displayName;
