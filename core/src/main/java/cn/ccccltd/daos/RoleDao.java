package cn.ccccltd.daos;

import cn.ccccltd.beans.Role;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *  角色 DAO
 * 
 * @author ccccltd
 *
 */
public interface RoleDao extends PagingAndSortingRepository<Role, Long> {

}