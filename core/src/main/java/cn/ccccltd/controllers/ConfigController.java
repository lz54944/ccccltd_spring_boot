package cn.ccccltd.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.ccccltd.beans.Config;
import cn.ccccltd.common.beans.PageReq;
import cn.ccccltd.common.beans.PageResp;
import cn.ccccltd.common.beans.ResultBean;
import cn.ccccltd.services.ConfigService;

/**
 * 配置相关的controller，支持跨域
 * 
 * @author ccccltd
 *
 */
@RequestMapping("/config")
@RestController
@CrossOrigin
public class ConfigController {

	@Autowired
	ConfigService configService;

	@GetMapping("/all")
	public ResultBean<Collection<Config>> getAll() {
		return new ResultBean<>(configService.getAll());
	}

	@GetMapping(value = "/list")
	public ResultBean<PageResp<Config>> list(PageReq param) {
		return new ResultBean<>(configService.listPage(param.toPageable(), param.getKeyword()));
	}

	/**
	 * 新增配置
	 * 
	 * FIXME 同时支持json格式和表单格式
	 * 
	 * @param config
	 * @return
	 */
	@PostMapping("/add")
	public ResultBean<Long> add(@RequestBody Config config) {
		System.out.println(configService.getClass());
		return new ResultBean<>(configService.add(config));
	}

	@PostMapping("/delete")
	public ResultBean<Boolean> delete(long id) {
		return new ResultBean<>(configService.delete(id));
	}
}
