package cn.ccccltd.controllers;

import java.util.Arrays;
import java.util.List;

import cn.ccccltd.common.beans.PageReq;
import cn.ccccltd.common.beans.PageResp;
import cn.ccccltd.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.ccccltd.beans.User;
import cn.ccccltd.common.beans.ResultBean;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	/**
	 * 测试数据
	 * 
	 * @param keyword
	 * @return
	 */
	@GetMapping("/search")
	public ResultBean<PageResp<User>> search(@RequestParam String keyword) {
		return new ResultBean<>(userService.list(null, keyword));
	}

	@GetMapping("/list")
	public ResultBean<PageResp<User>> list(PageReq page){
		return new ResultBean<>(userService.list(page.toPageable(), page.getKeyword()));
	}

    /**
     *  修改密码
     * @param id
     * @param password
     * @return
     */
	@PostMapping("updatepwd")
    public  ResultBean<Boolean> updatePwd(long id, String password){
        System.out.println(userService.getClass());//FIXME DELETE
	    userService.updatePwd(id, password.trim());
	    return new ResultBean<>(true);
    }
}
