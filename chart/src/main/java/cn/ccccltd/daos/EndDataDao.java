package cn.ccccltd.daos;

import cn.ccccltd.beans.EndData;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 用户DAO
 * 
 * @author ccccltd
 *
 */
public interface EndDataDao extends PagingAndSortingRepository<EndData, Long> {

    List<EndData> findAllByRecordId(long uploadRecordId);
}