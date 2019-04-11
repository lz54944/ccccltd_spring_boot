package cn.ccccltd.jpa;

import cn.ccccltd.beans.BaseEntity;
import cn.ccccltd.beans.Favorite;
import cn.ccccltd.consts.ObjType;
import cn.ccccltd.daos.BlogDao;
import cn.ccccltd.daos.FavoriteDao;
import cn.ccccltd.features.Favoritable;
import cn.ccccltd.jms.JMSType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Optional;
import static cn.ccccltd.common.utils.CheckUtil.*;

/**
 * 对象创建事件
 */
@Component
@Slf4j
public class JMSObjectComsumer {

    @JmsListener(destination = JMSType.CREATE)
    public void objectCreatePost(@Payload BaseEntity obj) {
        log.info("create: {}", obj);

        updateCounts(obj);
    }

    @JmsListener(destination = JMSType.DELETE)
    public void deleteObjectPost(@Payload BaseEntity obj) {
        log.info("delete : {}", obj);

        updateCounts(obj);
    }

    /**
     *  现在删除对象的时候，更新各种数据（如收藏数）
     * @param obj
     */
    private void updateCounts(@Payload BaseEntity obj) {
        try {
            // 不用担心冲掉
            JPAThreadLocal.setBackground(true);

            // 如果是新建了收藏对象，把对应的业务对象的数字增加
            if (obj instanceof Favorite) {
                log.info("create favorite, update favorite count.");
                Favorite favorite = (Favorite) obj;

                updateFavoriteCount(favorite.getObjType(), favorite.getObjId());
            }
        } finally {
            JPAThreadLocal.setBackground(false);
        }
    }

    /**
     *  更新对象的收藏数量
     * @param objType
     * @param objId
     */
    public void updateFavoriteCount(int objType, long objId) {

        PagingAndSortingRepository dao = findDao(objType);

        // 找到对应的对象
        // 对象必须实现了可收藏，否则报错，可以把报错完善一下
        Optional<Favoritable> bizObj = dao.findById(objId);

        check(bizObj.isPresent(), "value.is.null");

        // 更新收藏数量字段
        int count = favoriteDao.countByObjTypeAndObjId(objType, objId);

        Favoritable favoritable= bizObj.get();

        favoritable.setFavoriteCount(count);

        dao.save(favoritable);

        log.info("update  obj [type:{}, id:{}] favorite count[{}] success.", objType, objId, count);
    }

    @Autowired
    BlogDao blogDao;

    @Autowired
    FavoriteDao favoriteDao;

    PagingAndSortingRepository<? extends BaseEntity, Long> findDao(int objType) {
        if (objType == ObjType.BLOG) {
            return blogDao;
        }

        throw new IllegalArgumentException("object type error: " + objType);
    }
}
