package cn.ccccltd.controllers;

import cn.ccccltd.common.beans.ResultBean;
import cn.ccccltd.services.ProjectInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/home")
@RestController
@CrossOrigin
public class HomeController {

    @Autowired
    ProjectInformationService projectInformationService;

    @RequestMapping(value = "/projectInfo",method = RequestMethod.GET)
    public Long getProjectNumber(){
        return projectInformationService.count();
    }
}
