package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 作者: lin
 * 描述: 继承MongoRepository<实体类名，主键类型>
 * 日期: 2018/10/26 17:18
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

    // 根据站点id查询
    CmsPage findBySiteId(String SiteId);

    CmsPage findByPageNameAndPageWebPathAndSiteId(String pageName,String siteId,String
            pageWebPath);
}
