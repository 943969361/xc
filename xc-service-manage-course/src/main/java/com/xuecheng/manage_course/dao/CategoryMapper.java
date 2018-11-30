package com.xuecheng.manage_course.dao;


import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;


/**
 * 作者: lin
 * 描述:
 * 日期: 2018/11/26 10:21
 */
@Mapper
public interface CategoryMapper {

    Page<CategoryNode> findList();
}
