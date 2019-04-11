package cn.ccccltd.controllers;

import cn.ccccltd.beans.UploadRecord;
import cn.ccccltd.common.beans.PageReq;
import cn.ccccltd.common.beans.PageResp;
import cn.ccccltd.common.beans.ResultBean;
import cn.ccccltd.services.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *  上传日志并分析成图表
 *
 *
 * 
 * @author ccccltd
 *
 */
@RequestMapping("/upload")
@RestController
@CrossOrigin
public class UploadController {

	@Autowired
    UploadFileService uploadFileService;

	@PostMapping("/")
	public  ResultBean<UploadRecord> upload(@RequestBody() MultipartFile file) {
		return new ResultBean<UploadRecord>(uploadFileService.upload(file));
	}

	@GetMapping(value = "/list")
	public ResultBean<PageResp<UploadRecord>> list(PageReq param) {
		return new ResultBean<>(uploadFileService.listPage(param.toPageable(), param.getKeyword()));
	}

}
