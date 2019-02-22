package com.yan.image.center.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
