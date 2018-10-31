package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/10/29 09:15
 */
@Service
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    public QueryResponseResult findList(int page,int size, QueryPageRequest queryPageRequest) {

        // 防止空指针
        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }

        // 自定义查询列表
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageAliase",
                ExampleMatcher.GenericPropertyMatchers.contains());

        // 创建cms
        CmsPage cmsPage = new CmsPage();

        // 给查询条件赋值
        if(queryPageRequest.getPageAliase() != null){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        if(queryPageRequest.getPageId() != null){
            cmsPage.setPageId(queryPageRequest.getPageId());
        }
        if(queryPageRequest.getPageName() != null){
            cmsPage.setPageName(queryPageRequest.getPageName());
        }
        if(queryPageRequest.getTemplateId() != null){

            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if(queryPageRequest.getSiteId() != null){

            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }

        if(page <= 0){
            page = 1;
        }
        if(size <= 0){
            size =10;
        }


        // 创建条件实例
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        // 页码减一
        page = page-1;
        Pageable pageable = PageRequest.of(page,size);
        // 自定义分页条件查询
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);

        QueryResult<CmsPage> queryResult = new QueryResult();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);

        // 返回
        return  queryResponseResult;
    }


    public CmsPageResult add( CmsPage cmsPage){
        // 调用dao
        // 根据三个字段查询是否有重复的页面
        CmsPage byByPageNameAndPageWebPathAndSiteId = cmsPageRepository.findByPageNameAndPageWebPathAndSiteId(
                cmsPage.getPageName(),cmsPage.getPageWebPath(),cmsPage.getSiteId());
        // 判断是否重复
        if(byByPageNameAndPageWebPathAndSiteId == null){
            cmsPage.setPageId(null);
            //添加页面主键由spring data 自动生成
            cmsPageRepository.save(cmsPage);
            //返回结果
            CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS,cmsPage);
            return cmsPageResult;
        }else {
            return new CmsPageResult(CommonCode.FAIL,null);
        }
    }
}
