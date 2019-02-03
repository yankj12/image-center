package com.yan.image.center.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yan.image.center.mapper.ImageMainMapper;
import com.yan.image.center.mapper.ImageRefMapper;
import com.yan.image.center.mapper.ImageTagMapper;
import com.yan.image.center.schema.ImageRef;
import com.yan.image.center.schema.ImageTag;
import com.yan.image.center.vo.ResponseVo;

@Controller
public class ImageTagRestController {
	
	@Autowired
	private ImageMainMapper imageMainMapper;
	
	@Autowired
	private ImageRefMapper imageRefMapper;
	
	@Autowired
	private ImageTagMapper imageTagMapper;
	
	@GetMapping("/imagetag")
	@ResponseBody
	public ResponseVo queryTag(String uuid, String md5) {
		ResponseVo response = new ResponseVo();
		response.setSuccess(false);
		
		if((uuid != null && !"".equals(uuid.trim())) 
				|| (md5 != null && !"".equals(md5.trim()))) {
			ImageRef refTmp = null;
			List<ImageRef> imageRefs = null;
			
			// 根据uuid或者md5查询出图片
			if(md5 != null && !"".equals(md5.trim())) {
				// 先根据md5查是否存在图片
				imageRefs = imageRefMapper.findImageRefByMD5(md5);
			}else {
				// 如果没有传md5，则根据uuid查询图片
				imageRefs = imageRefMapper.findImageRefByUUID(uuid);
			}
			
			if(imageRefs != null && imageRefs.size() > 0) {
				refTmp = imageRefs.get(0);
			}
			
			// 如果没有查询到图片，返回错误
			if(refTmp == null) {
				response.setErrorMsg("uuid和md5没有查询到图片");
			}else {
				uuid = refTmp.getUuid();
				md5 = refTmp.getMd5();
			}
			
			List<ImageTag> imageTags = imageTagMapper.findImageTagByMD5(md5);
			
			response.setSuccess(true);
			response.setResults(imageTags);
			
		}else {
			// uuid 和 md5 二者必选其一
			response.setErrorMsg("uuid和md5二者必须具备一个");
		}
		
		return response;
	}
	
	@PostMapping("/imagetag")
	@ResponseBody
	public String addTag(String uuid, String md5, String tagName) {
		
		if((uuid != null && !"".equals(uuid.trim())) 
				|| (md5 != null && !"".equals(md5.trim()))) {
			ImageRef refTmp = null;
			List<ImageRef> imageRefs = null;
			
			// 根据uuid或者md5查询出图片
			if(md5 != null && !"".equals(md5.trim())) {
				// 先根据md5查是否存在图片
				imageRefs = imageRefMapper.findImageRefByMD5(md5);
			}else {
				// 如果没有传md5，则根据uuid查询图片
				imageRefs = imageRefMapper.findImageRefByUUID(uuid);
			}
			
			if(imageRefs != null && imageRefs.size() > 0) {
				refTmp = imageRefs.get(0);
			}
			
			// 如果没有查询到图片，返回错误
			if(refTmp == null) {
				return "error";
			}else {
				uuid = refTmp.getUuid();
				md5 = refTmp.getMd5();
			}
			
			// 根据uuid或者md5向image_tag标插入映射关系
			// 判断标签是否已经在这个图像中存在（这个最好在前台做）
			// 如果不存在，插入这个标签
			
			Map<String, Object> condition = new HashMap<>();
			condition.put("tagName", tagName);
			condition.put("uuid", uuid);
			condition.put("md5", md5);
			
			Long tagCount = imageTagMapper.countImageTagsByCondition(condition);
			
			if(tagCount <= 0) {
				ImageTag imageTag = new ImageTag();
				imageTag.setMd5(md5);
				imageTag.setUuid(uuid);
				imageTag.setTagName(tagName);
				imageTag.setValidStatus("1");
				imageTag.setInsertTime(new Date());
				imageTag.setUpdateTime(new Date());
				
				imageTagMapper.insertImageTag(imageTag);
			}
			
		}else {
			// uuid 和 md5 二者必选其一
			return "error";
		}
		
		return "success";
	}

}
