package com.yan.image.center.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yan.image.center.mapper.ImageMainMapper;
import com.yan.image.center.schema.ImageMain;

@Controller
public class FileUploadController {
	
	public static final String UPLOAD_FILE_ROOT_DIR = "E:\\uploadfile\\test";
	
	@Value("${image.root.dir}")
	private String imageRootDir;
	
	@Autowired
	private ImageMainMapper imageMainMapper;
	
	// 访问路径为：http://127.0.0.1:8080/file
	@RequestMapping("/file")
	public String file() {
		return "/file";
	}

	// 访问路径为：http://127.0.0.1:8080/file
	@RequestMapping("/ajaxfile")
	public String ajaxfile() {
		return "/ajaxfile";
	}
	
	/**
	 * 文件上传具体实现方法;
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping("/upload")
	@ResponseBody
	public String handleFileUpload(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				/*
				 * 这段代码执行完毕之后，图片上传到了工程的跟路径； 大家自己扩散下思维，如果我们想把图片上传到
				 * d:/files大家是否能实现呢？ 等等;
				 * 这里只是简单一个例子,请自行参考，融入到实际中可能需要大家自己做一些思考，比如： 1、文件路径； 2、文件名；
				 * 3、文件格式; 4、文件大小的限制;
				 */
				String category = "image";
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(new File(UPLOAD_FILE_ROOT_DIR + File.separator + category + File.separator + file.getOriginalFilename())));
				out.write(file.getBytes());
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return "上传失败," + e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				return "上传失败," + e.getMessage();
			}
			return "上传成功";
		} else {
			return "上传失败，因为文件是空的.";
		}
	}
	
	@RequestMapping("/ajaxupload")
	@ResponseBody
	public String ajaxupload(@RequestParam("file") MultipartFile file, String category) {
		// uuid
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		// 计算文件的md5值
		String md5Hex = null;
		try {
			md5Hex = DigestUtils.md5DigestAsHex(file.getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String fileName = file.getOriginalFilename();
		if (fileName.indexOf("\\") != -1) {
			fileName = fileName.substring(fileName.lastIndexOf("\\"));
		}
		
		// suffix
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		// 后缀名都转为小写存储
		suffix = suffix.toLowerCase();
		
		//String category = "image";
		String fileDir = imageRootDir;
		
		// 一张图片可能多人上传，不同人上传的分类可能不同，存在一定冲突
//		if(category != null && !"".equals(category.trim())) {
//			fileDir += File.separator + category;
//		}else {
//			category = "uncategorized";
//			fileDir += File.separator + category;
//		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String dateStr = simpleDateFormat.format(new Date());
		
		fileDir += File.separator + dateStr;
		
		// 图片存储的位置
		String location = fileDir + File.separator + uuid + "." + suffix;
		
		// 组装ImageMain对象
		ImageMain imageMain = new ImageMain();
		
		imageMain.setUuid(uuid);
		imageMain.setMd5(md5Hex);
		imageMain.setLocation(location);
		imageMain.setSuffix(suffix);
		imageMain.setValidStatus("1");
		imageMain.setInsertTime(new Date());
		imageMain.setUpdateTime(new Date());
		
		// 文件写入磁盘
		
		File targetFile = new File(fileDir);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(location);
			out.write(file.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "上传失败";
		}
		
		// 将文件数据写入数据库
		imageMainMapper.insertImageMain(imageMain);
		return "上传成功!";
	}

}
