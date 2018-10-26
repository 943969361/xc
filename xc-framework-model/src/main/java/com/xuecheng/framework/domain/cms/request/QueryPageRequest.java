package com.xuecheng.framework.domain.cms.request;

import lombok.Data;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/10/26 14:20
 *
 */
@Data
public class QueryPageRequest {

    //站点id
    private String siteId;
    //页面ID
    private String pageId;
    //页面名称
    private String pageName;
    //别名
    private String pageAliase;
    //模版id
    private String templateId;

}
