package com.yan.image.center.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yan.image.center.schema.ImageMain;

@Mapper
public interface ImageMainMapper {

	void insertImageMain(ImageMain imageMain);
	
	void insertBatchImageMain(List<ImageMain> imageMains);
	
	List<ImageMain> findImageMainByUUID(String uuid);
	
	List<ImageMain> findImageMainByMD5(String md5);
	
	List<ImageMain> findImageMainsByCondition(Map<String, Object> condition);
	
	Long countImageMainsByCondition(Map<String, Object> condition);
	
	void updateValidStatusByUUID(ImageMain imageMain);
}
