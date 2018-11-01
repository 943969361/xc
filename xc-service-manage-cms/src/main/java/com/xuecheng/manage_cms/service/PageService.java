package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    // 添加
    public CmsPageResult add( CmsPage cmsPage){
        // 调用dao
        // 根据三个字段查询是否有重复的页面
        CmsPage byByPageNameAndPageWebPathAndSiteId = cmsPageRepository.findByPageNameAndPageWebPathAndSiteId(
                cmsPage.getPageName(),cmsPage.getPageWebPath(),cmsPage.getSiteId());

        // 判断是否重复
        if(byByPageNameAndPageWebPathAndSiteId !=null){
        //校验页面是否存在，已存在则抛出异常
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);
        //添加页面主键由spring data 自动生成
        cmsPageRepository.save(cmsPage);
        //返回结果
        CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        return cmsPageResult;
    }

    // 根据id查询
    public CmsPage getById(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            return  optional.get();
        }
        return null;
    }


    // 修改
    public CmsPageResult edit (String id,CmsPage cmsPage){

        // 1.查询信息
        CmsPage byId = this.getById(id);
        if(byId != null){
            // 查到信息
            // 2.修改
            byId.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            byId.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            byId.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            byId.setPageName(cmsPage.getPageName());
            //更新访问路径
            byId.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            byId.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //执行更新
            // 3.保存
            CmsPage save = cmsPageRepository.save(byId);

            // 4.返回
            if(save != null){
                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS,cmsPage);
                return cmsPageResult;
            }
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }


    // 删除
    public ResponseResult del (String id){

        Boolean flag = false;
        if(id != null){
            CmsPage cmsPage = this.getById(id);
            if(cmsPage != null){
                cmsPageRepository.deleteById(id);
                flag = true;
            }
            if(flag){
                return new  ResponseResult (CommonCode.SUCCESS);
            }
        }
        return new  ResponseResult (CommonCode.FAIL);

    }
}
