package com.xuecheng.test.freemarker;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/11/1 17:48
 */
@RequestMapping("/freemarker")
@Controller
public class controller {

    @RequestMapping("/test1")
    public String freemarker(Map<String, Object> map){
        map.put("name","程序员");
    //返回模板文件名称
        return "test1";
    }
}
