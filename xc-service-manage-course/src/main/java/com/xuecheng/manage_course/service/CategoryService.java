package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 作者: lin
 * 描述:
 * 日期: 2018/11/26 10:17
 */
@Service
public class CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    public Page<CategoryNode> findList() {

        PageHelper.startPage(1,10);
        Page<CategoryNode> list = categoryMapper.findList();
        return list;
    }
}
