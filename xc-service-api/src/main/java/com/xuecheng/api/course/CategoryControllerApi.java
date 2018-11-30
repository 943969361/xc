package com.xuecheng.api.course;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



/**
 * 作者: lin
 * 描述:
 * 日期: 2018/11/26 10:11
 */
@Api(value = "课程分类管理",description = "课程分类管理",tags = {"课程分类管理"})
public interface CategoryControllerApi {

    @ApiOperation("查询分类")
    public Page<CategoryNode> findList();

}

