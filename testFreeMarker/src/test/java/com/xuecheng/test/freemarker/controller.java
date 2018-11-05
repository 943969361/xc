package com.xuecheng.test.freemarker;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/11/1 17:48
 */
@RequestMapping("/freemarker")
@Controller
public class controller {

    @Autowired
    RestTemplate restTemplate;

    @Test
    @RequestMapping("/banner")
    public void testRestTemplate(){
        ResponseEntity<Map> forEntity =
                restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f",
                        Map.class);
        System.out.println(forEntity);
    }

}
