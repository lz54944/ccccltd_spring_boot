package cn.ccccltd.impaladaos;

import cn.ccccltd.impalabeans.ImpalaBlog;
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
public interface ImpalaBlogDao extends PagingAndSortingRepository<ImpalaBlog, Long> {
	ImpalaBlog findByTitle(String title);

	@Query(value = "select t from ImpalaBlog t where t.title like %?1%", nativeQuery = false)
	Page<ImpalaBlog> findAllByKeyword(String keyword, Pageable pageable);
}