package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 作者: lin
 * 描述: 页面静态化
 * 日期: 2018/11/5 16:45
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}
