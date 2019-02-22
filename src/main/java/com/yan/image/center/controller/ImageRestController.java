package com.yan.image.center.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yan.image.center.mapper.ImageMainMapper;
import com.yan.image.center.mapper.ImageRefMapper;
import com.yan.image.center.mapper.ImageTagMapper;
import com.yan.image.center.schema.ImageMain;
import com.yan.image.center.schema.ImageRef;
import com.yan.image.center.schema.ImageTag;
import com.yan.image.center.vo.DataGridVo;
import com.yan.image.center.vo.ImageVo;
import com.yan.image.center.vo.ResponseVo;

@RestController
public class ImageRestController {
	
	@Autowired
	private ImageMainMapper imageMainMapper;
	
	@Autowired
	private ImageRefMapper imageRefMapper;
	
	@Autowired
	private ImageTagMapper imageTagMapper;
	
	@RequestMapping("/imagesdatagrid")
	@ResponseBody
	public DataGridVo imagesDataGrid(Integer page, Integer rows, String validStatus) {
		DataGridVo dataGrid = new DataGridVo();
		dataGrid.setSuccess(false);
		
		int offset = 0;
		int pageSize = 10;
		
		if(rows > 0){
			pageSize = rows;
		}
		
		if(page > 0){
			offset = (page - 1) * pageSize;
		}
		
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("validStatus", validStatus);
		condition.put("offset", offset);
		condition.put("pageSize", pageSize);
		
		List<ImageRef> imageRefs = imageRefMapper.findImageRefsByCondition(condition);
		Long total = imageRefMapper.countImageRefsByCondition(condition);
		
		dataGrid.setSuccess(true);
		dataGrid.setErrorMsg("");
		dataGrid.setTotal(total.intValue());
		dataGrid.setRows(imageRefs);
		
		return dataGrid;
	}
	
	@RequestMapping("/image")
	@ResponseBody
	public ResponseVo queryImage(String refuuid) {
		ResponseVo responseVo = new ResponseVo();
		
		String md5 = null;
		
		// 先根据ImageRef的uuid找到md5
		List<ImageRef> imageRefs = imageRefMapper.findImageRefByUUID(refuuid);
		
		if(imageRefs != null && imageRefs.size() == 1){
			ImageRef imageRef = imageRefs.get(0);
			md5 = imageRef.getMd5();
			
			List<ImageMain> imageMains = imageMainMapper.findImageMainByMD5(md5);
			List<ImageTag> imageTags = imageTagMapper.findImageTagsByMD5(md5);
			
			String url = null;
			if(imageMains != null && imageMains.size() > 0){
				ImageMain imageMain = imageMains.get(0);
				if(imageMain != null){
					url = imageMain.getUrl();
				}
			}
			
			List<String> tags = null;
			if(imageTags != null && imageTags.size() > 0){
				tags = new ArrayList<String>();
				for(ImageTag imageTag:imageTags){
					tags.add(imageTag.getTagName());
				}
			}
			
			ImageVo imageVo = new ImageVo();
			
			imageVo.setUuid(refuuid);
			imageVo.setMd5(md5);
			imageVo.setDisplayName(imageRef.getDisplayName());
			imageVo.setSuffix(imageRef.getSuffix());
			imageVo.setUserCode(imageRef.getUserCode());
			imageVo.setCategory(imageRef.getCategory());
			imageVo.setUrl(url);
			imageVo.setTags(tags);
			
			
			responseVo.setSuccess(true);
			responseVo.setErrorMsg("");
			responseVo.setResult(imageVo);
		}else{
			responseVo.setSuccess(false);
			responseVo.setErrorMsg("");
			responseVo.setResult(null);
		}
		
		return responseVo;
	}
	
	@RequestMapping("/editfile")
	@ResponseBody
	public ResponseVo editFileInfo(@RequestParam(value="refuuid",required=false)String refuuid, String userCode, String category, String fileNewName, String tags) {
		ResponseVo responseVo = new ResponseVo();
		
		// 根据refuuid查下image_ref表是否有记录
		// 根据refuuid，更新image_ref表的usercode，category，displayname字段，根据md5更新image_tag表
		
		
		List<ImageRef> imageRefs = imageRefMapper.findImageRefByUUID(refuuid);
		
		if(imageRefs != null && imageRefs.size() == 1){
			ImageRef imageRef = imageRefs.get(0);
			String md5 = imageRef.getMd5();
			
			if(fileNewName != null && !"".equals(fileNewName.trim())){
				// suffix
				String suffix = fileNewName.substring(fileNewName.lastIndexOf(".") + 1);
				// 后缀名都转为小写存储
				suffix = suffix.toLowerCase();
				
				// 不带后缀名得文件名称
				String fileNameWithoutSuffix = fileNewName.substring(0, fileNewName.lastIndexOf("."));
				
				imageRef.setDisplayName(fileNameWithoutSuffix);
				imageRef.setSuffix(suffix);
			}
			
			if(category != null && !"".equals(category.trim())){
				imageRef.setCategory(category);
			}
			
			if(userCode != null && !"".equals(userCode.trim())){
				imageRef.setUserCode(userCode);
			}
			
			imageRef.setUpdateTime(new Date());
			
			// 更新image_ref
			imageRefMapper.updateImageInfoByUUID(imageRef);
			
			
			// 处理标签tags
			// 先简单粗暴地先删后插
			
			// 将页面传入的tag，根据英文逗号分割后转换为List
			List<String> tagList = new ArrayList<String>();
			if(tags != null && !"".equals(tags.trim())){
				
				String[] tagAry = tags.split(",");
				
				if(tagAry != null){
					for(String tag:tagAry){
						
						if(tag != null && !"".equals(tag)){
							tagList.add(tag);
						}
					}
				}
			}
			
			if(tagList != null && tagList.size() > 0){
				
				// 先删后插
				
				// 需要根据md5删除已有的tag
				imageTagMapper.deletImageTagByMd5(md5);
				
				// 新增tag
				List<ImageTag> imageTagsToInsert = new ArrayList<ImageTag>();
				
				for(String tagName:tagList){
					ImageTag imageTag = new ImageTag();
					
					imageTag.setMd5(md5);
					imageTag.setTagName(tagName);
					imageTag.setValidStatus("1");
					imageTag.setInsertTime(new Date());
					imageTag.setUpdateTime(new Date());
					
					imageTagsToInsert.add(imageTag);
				}
				
				if(imageTagsToInsert != null && imageTagsToInsert.size() > 0){
					imageTagMapper.insertBatchImageTag(imageTagsToInsert);
				}
				
			}else{
				// 需要根据md5删除已有的tag
				imageTagMapper.deletImageTagByMd5(md5);
			}
			
			responseVo.setSuccess(true);
			responseVo.setErrorMsg("修改图片信息成功");
			
		}else{
			// 找不到对应文件
			responseVo.setSuccess(false);
			responseVo.setErrorMsg(refuuid + "找不到对应文件");
		}
		
		return responseVo;
	}
	
	@RequestMapping("/deletefile")
	@ResponseBody
	public ResponseVo delete(@RequestParam(value="refuuid",required=false)String refuuid) {
		ResponseVo responseVo = new ResponseVo();
		
		// 根据refuuid查下image_ref表是否有记录
		// 采用逻辑删除的方式，根据refuuid，更新image_ref表的validstatus字段为0
		
		List<ImageRef> imageRefs = imageRefMapper.findImageRefByUUID(refuuid);
		
		if(imageRefs != null && imageRefs.size() == 1){
			ImageRef imageRef = imageRefs.get(0);
			
			imageRef.setValidStatus("0");
			imageRef.setUpdateTime(new Date());
			
			imageRefMapper.updateValidStatusByUUID(imageRef);
			responseVo.setSuccess(true);
			responseVo.setErrorMsg("删除图片成功");
			
		}else{
			// 找不到对应文件
			responseVo.setSuccess(false);
			responseVo.setErrorMsg(refuuid + "找不到对应文件");
		}
		
		return responseVo;
	}
}
