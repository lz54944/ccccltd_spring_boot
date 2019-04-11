package cn.ccccltd.services;

import cn.ccccltd.impalabeans.ProjectInformation;
import cn.ccccltd.impaladaos.ProjectInformationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProjectInformationService  {

    @Autowired
    ProjectInformationDao dao;

    //分页获取数据
    public Page<ProjectInformation> pageList(Pageable pageable){
        return dao.findAll(pageable);
    }

    //获取工程总数，暂时先测试一下
    public Long count(){
        return dao.count();
    }
}
