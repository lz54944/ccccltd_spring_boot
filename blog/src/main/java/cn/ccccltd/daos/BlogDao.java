package cn.ccccltd.daos;

import cn.ccccltd.beans.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *  Blog类DAO
 * 
 * @author ccccltd
 *
 */
public interface BlogDao extends PagingAndSortingRepository<Blog, Long> {
	Blog findByTitle(String title);

	@Query(value = "select t from Blog t where t.title like %?1% or t.body like %?1%", nativeQuery = false)
	Page<Blog> findAllByKeyword(String keyword, Pageable pageable);
}