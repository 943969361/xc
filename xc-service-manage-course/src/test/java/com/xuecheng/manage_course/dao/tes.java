package com.xuecheng.manage_course.dao;

import org.springframework.beans.BeanUtils;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/11/27 09:46
 */
public class tes {

    public static void main(String[] args) {

        user u1 = new user();
        u1.setName("a");
        u1.setAge("5");
        user u2 = new user();
        u2.setName("b");
        //u2.setAge("2");
        BeanUtils.copyProperties(u2,u1);
        System.out.println("u1"+u1);
        System.out.println("u2"+u2);

    }
}
