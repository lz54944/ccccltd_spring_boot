package cn.ccccltd.services;

import cn.ccccltd.beans.Blog;
import cn.ccccltd.common.beans.PageResp;
import cn.ccccltd.common.consts.Roles;
import cn.ccccltd.common.utils.UserUtil;
import cn.ccccltd.daos.BlogDao;
import cn.ccccltd.impaladaos.ImpalaBlogDao;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static cn.ccccltd.common.utils.CheckUtil.*;

/**
 * 配置业务处理类
 *
 * @author ccccltd
 */
@Service
@Slf4j
public class BlogService {

    @Autowired
    BlogDao dao;

    @Autowired
    ImpalaBlogDao impalaBlogDao;

    public Collection<Blog> getAll() {
        // 校验通过后打印重要的日志
        log.info("getAll start ...");

        List<Blog> data = Lists.newArrayList(dao.findAll());

        log.info("getAll end, data size:" + data.size());

        return data;
    }

    /**
     *  增加配置，需要管理员权限
     * @param blog
     * @return
     */
    @RequiresRoles(Roles.ADMIN)
    public long add(Blog blog) {
        // 参数校验
        notNull(blog, "param.is.null");
        notEmpty(blog.getTitle(), "name.is.null");
        notEmpty(blog.getBody(), "value.is.null");

        // 校验通过后打印重要的日志
        log.info("add blog:" + blog);

        // 校验重复
        check(null == dao.findByTitle(blog.getTitle()), "name.repeat");

        blog = dao.save(blog);

        // 修改操作需要打印操作结果
        log.info("add blog success, id:" + blog.getId());

        return blog.getId();
    }

    /**
     *  根据id删除配置项
     *
     *  管理员或者自己创建的才可以删除掉
     * @param id
     * @return
     */
    @RequiresRoles(Roles.ADMIN)
    public boolean delete(long id) {

        Optional<Blog> blog = dao.findById(id);

        // 参数校验
        check(blog.isPresent(), "id.error", id);

        // 判断是否可以删除
        check(canDelete(blog.get()), "no.permission");

        dao.deleteById(id);

        // 修改操作需要打印操作结果
        log.info("delete blog success, id:" + id);

        return true;
    }

    /**
     * 自己创建的或者管理员可以删除数据
     * 判断逻辑变化可能性大，抽取一个函数
     *
     * @param blog
     * @return
     */
    private boolean canDelete(Blog blog) {
        return UserUtil.getUser().equals(blog.getCreator()) || UserUtil.isAdmin();
    }

    /**
     *  分页查找
     *
     * @param pageable
     * @param keyword
     * @return
     */
    public PageResp<Blog> listPage(Pageable pageable, String keyword) {
        impalaBlogDao.findAllByKeyword(keyword, pageable);
        if (StringUtils.isEmpty(keyword)) {
            return new PageResp<>(dao.findAll(pageable));
        } else {
            // 也可以用springjpa 的 Specification 来实现查找
            return new PageResp<>(dao.findAllByKeyword(keyword, pageable));
        }
    }

    public Blog getByTitle(String title) {
        return dao.findByTitle(title);
    }

}
