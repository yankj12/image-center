package com.yan.image.center.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yan.image.center.schema.ImageTag;

@Mapper
public interface ImageTagMapper {

	void insertImageTag(ImageTag imageTag);
	
	void insertBatchImageTag(List<ImageTag> imageTags);
	
	List<ImageTag> findImageTagsByMD5(String md5);
	
	List<ImageTag> findImageTagsByCondition(Map<String, Object> condition);
	
	Long countImageTagsByCondition(Map<String, Object> condition);
	
	void deleteBatchImageTags(List<Long> ids);
	
	void deletImageTagByMd5(String md5);
}
