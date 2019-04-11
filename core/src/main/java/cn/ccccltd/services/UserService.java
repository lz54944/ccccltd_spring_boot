package cn.ccccltd.services;

import cn.ccccltd.common.beans.PageResp;
import cn.ccccltd.common.consts.Roles;
import cn.ccccltd.daos.UserDao;
import cn.ccccltd.beans.User;
import cn.ccccltd.tool.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static cn.ccccltd.common.utils.CheckUtil.check;

/**
 * 用户相关处理类
 * 
 * @author ccccltd
 *
 */
@Service
@Slf4j
public class UserService {

	@Autowired
	UserDao userDao;

	public User findUser(String username) {
		return userDao.findByName(username);
	}

	/**
	 * FIXME
	 * @param username
	 * @param password
	 * @return
	 */
	public User login(String username, String password) {
		Subject subject = SecurityUtils.getSubject();

		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		subject.login(token);

		// 登陆成功，取出用户
		User user = (User) subject.getPrincipal();

		return user;
	}


	public void logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
	}

	public PageResp<User> list(Pageable pageable, String keyword) {
		if (StringUtils.isEmpty(keyword)) {
			return new PageResp<>(userDao.findAll(pageable));
		} else {
			// 也可以用springjpa 的 Specification 来实现查找
			return new PageResp<>(userDao.findAllByKeyword(keyword, pageable));
		}
	}

	/**
	 *  修改密码
	 * @param id
	 * @param password
	 */
	//FIXME why not work？？！！
	@RequiresRoles(Roles.ADMIN)
	public void updatePwd(long id, String password) {
        Optional<User> user = userDao.findById(id);

        check(user.isPresent(), "id.error", id);
        check(checkPwd(password), "password.invalid");

        // FIXME
        log.info("modify password, user id: " + id + ", password:" + password);

        // 生成新密码
        String hash = PasswordUtil.renewPassword(password, user.get().getSalt());

        user.get().setPassword(hash);

        userDao.save(user.get());
    }

    //TODO
    private boolean checkPwd(String password) {
	    return password.length() >= 3;
    }
}
