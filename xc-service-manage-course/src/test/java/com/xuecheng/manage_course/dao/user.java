package com.xuecheng.manage_course.dao;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/11/27 09:38
 */
public class user {
    private String name;

    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "user{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
