package cn.ccccltd.controllers;

import cn.ccccltd.beans.LineBean;
import cn.ccccltd.common.beans.ResultBean;
import cn.ccccltd.services.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *  上传日志并分析成图表
 *
 * @author ccccltd
 *
 */
@RequestMapping("/chart")
@RestController
@CrossOrigin
public class ChartController {

	@Autowired
	ChartService chartService;

	@GetMapping("/line")
	public ResultBean<LineBean> line(long uploadRecordId) {
		return new ResultBean<LineBean>(chartService.line(uploadRecordId));
	}

}
