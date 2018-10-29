package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/10/26 14:30
 *
 * 查询列表统一用QueryResponseResult接收，继承ResponseResult
 * 返回
 *     //操作是否成功
 *     boolean success = SUCCESS;
 *     //操作代码
 *     int code = SUCCESS_CODE;
 *     //提示信息
 *     String message;
 * QueryResult
 *     //数据列表
 *     private List<T> list;
 *     //数据总数
 *     private long total;
 *
 */
// swagger 备注显示
@Api(value="cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {

    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
                    @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")})
    // 页面查询
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
}
