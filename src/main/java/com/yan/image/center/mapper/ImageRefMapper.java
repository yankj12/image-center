package com.yan.image.center.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yan.image.center.schema.ImageRef;

@Mapper
public interface ImageRefMapper {

	void insertImageRef(ImageRef imageRef);
	
	void insertBatchImageRef(List<ImageRef> imageRefs);
	
	List<ImageRef> findImageRefByUUID(String uuid);
	
	List<ImageRef> findImageRefByMD5(String md5);
	
	List<ImageRef> findImageRefsByCondition(Map<String, Object> condition);
	
	Long countImageRefsByCondition(Map<String, Object> condition);
	
	void updateValidStatusByUUID(ImageRef imageRef);
	
	void updateImageInfoByUUID(ImageRef imageRef);
}
