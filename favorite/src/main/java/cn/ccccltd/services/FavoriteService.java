package cn.ccccltd.services;

import cn.ccccltd.beans.Favorite;
import cn.ccccltd.common.utils.UserUtil;
import cn.ccccltd.daos.FavoriteDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class FavoriteService {

    @Autowired
    FavoriteDao dao;

    public Collection<Favorite> getAll(int type) {
        // 校验通过后打印重要的日志
        log.info("getAll start ...");

        List<Favorite> data = dao.findAllByObjType(type);

        log.info("getAll end, data size:" + data.size());

        return data;
    }

    /**
     * 增加配置，需要管理员权限
     *
     * @param favorite
     * @return
     */
    public long add(Favorite favorite) {
        // 参数校验
        notNull(favorite);

        check(favorite.getObjType() > 0);
        check(favorite.getObjId() > 0L);

        // 校验通过后打印重要的日志
        log.info("add favorite:" + favorite);

        long userId = UserUtil.getUserId();

        // 校验重复
        Favorite favoriteNew = dao.findByUserIdAndObjTypeAndObjId(userId, favorite.getObjType(), favorite.getObjId());

        // 如果没有记录，就新增
        if (favoriteNew == null) {
            // 设置用户id
            favorite.setUserId(userId);

            favoriteNew = dao.save(favorite);

            // 修改操作需要打印操作结果
            log.info("add favorite success, id:" + favoriteNew.getId());
        }

        return favoriteNew.getId();
    }

    /**
     * 根据id删除配置项
     * <p>
     * 管理员或者自己创建的才可以删除掉
     *
     * @param id
     * @return
     */
    public boolean delete(long id) {
        Optional<Favorite> favorite = dao.findById(id);

        // 参数校验
        check(favorite.isPresent(), "id.error", id);

        // 判断是否可以删除
        check(canDelete(favorite.get()), "no.permission");

        dao.deleteById(id);

        // 修改操作需要打印操作结果
        log.info("delete favorite success, id:" + id);

        return true;
    }

    /**
     * 自己创建的或者管理员可以删除数据
     * 判断逻辑变化可能性大，抽取一个函数
     *
     * @param favorite
     * @return
     */
    private boolean canDelete(Favorite favorite) {
        return UserUtil.getUserId() == favorite.getUserId() || UserUtil.isAdmin();
    }

}
