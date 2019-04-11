package cn.ccccltd.impaladaos;

import cn.ccccltd.impalabeans.ProjectInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProjectInformationDao extends PagingAndSortingRepository<ProjectInformation, Long> {

}
