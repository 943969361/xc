package com.xuecheng.api.config.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

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
public interface CmsPageControllerApi {

    // 页面查询
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) ;
}
