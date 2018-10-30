package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/10/26 17:23
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    // 查询全部
    @Test
    public void testFindAll() {

        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    // 查询分页
    @Test
    public void testFindPage() {

        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    // 添加测试
    @Test
    public void testInsert() {

        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("s01");
        cmsPage.setTemplateId("t01");
        cmsPage.setPageName("测试页面");
        cmsPage.setPageCreateTime(new Date());
        List<CmsPageParam> cmsPageParams = new ArrayList<>();
        CmsPageParam cmsPageParam = new CmsPageParam();
        cmsPageParam.setPageParamName("param1");
        cmsPageParam.setPageParamValue("value1");
        cmsPageParams.add(cmsPageParam);
        cmsPage.setPageParams(cmsPageParams);
        CmsPage page = cmsPageRepository.save(cmsPage);
        System.out.println(page);
    }

    // 根据id查询
    @Test
    public void selectId() {
        // 返回Optional,jdk 1.8特有的属性
        CmsPage cmsPage = cmsPageRepository.findBySiteId("s01");
        if (cmsPage != null) {
            // CmsPage cmsPage = optional.get();
            System.out.println(cmsPage);
        } else {
            System.out.println("==========");
        }
    }

    // 修改
    @Test
    public void testUpdate() {
        // 1.查询
        CmsPage siteId = cmsPageRepository.findBySiteId("s01");
        if (siteId != null) {
            // 2.修改
            siteId.setPageName("赵四");
            // 3.保存
            cmsPageRepository.save(siteId);
            // 4.打印
            System.out.println(siteId);
        } else {
            System.out.println("aaa");
        }

    }

    // 根据站点Id：精确匹配
    //模板Id：精确匹配
    //页面别名：模糊匹配

    @Test
    public void testFindAll2() {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("分类导");
        // 条件匹配器
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        List<CmsPage> list = all.getContent();
        System.out.println(list);
    }


    //自定义条件查询测试
    @Test
    public void testFindAll3() {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageAliase",
               ExampleMatcher.GenericPropertyMatchers.contains());

        CmsPage cmsPage = new CmsPage();
        //cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        cmsPage.setPageAliase("分类导");

        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        Pageable pageable = new PageRequest(0, 10);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println(all);
    }
}