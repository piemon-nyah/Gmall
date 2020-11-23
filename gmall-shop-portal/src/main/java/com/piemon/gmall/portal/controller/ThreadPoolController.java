package com.piemon.gmall.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: piemon
 * @date: 2020/10/31 0:07
 * @description:
 */
@RestController
public class ThreadPoolController {

    @Qualifier("mainThreadPoolExecutor")
    @Autowired
    ThreadPoolExecutor mainThreadPoolExecutor;

    @GetMapping("/thread/status")
    public Map<String, Object> threadPoolStatue(){

        Map<String,Object> map =new HashMap<>();
        map.put("ActiveCount",mainThreadPoolExecutor.getActiveCount());
        map.put("CorePoolSize",mainThreadPoolExecutor.getCorePoolSize());
        return map;
    }
}
