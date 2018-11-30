package com.xuecheng.manage_course.controller;


import com.github.pagehelper.Page;
import com.xuecheng.api.course.CategoryControllerApi;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/11/26 10:17
 */
@RestController
@RequestMapping("/Category")
public class CategoryController implements  CategoryControllerApi {

    @Autowired
    CategoryService categoryService;

    @Override
    @PostMapping("/find")
    public Page<CategoryNode> findList() {
        return categoryService.findList();
    }

}
