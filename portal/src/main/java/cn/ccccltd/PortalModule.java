package cn.ccccltd;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *  门户
 */
@Component
public class PortalModule {

    @PostConstruct
    public  void init(){
        System.out.println("-----------------------------------------------");
        System.out.println("--                                           --");
        System.out.println("--        Portal Module Loaded               --");
        System.out.println("--                                           --");
        System.out.println("-----------------------------------------------");
    }
}
