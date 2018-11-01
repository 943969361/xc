package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 作者: lin
 * 描述: 实现service的api接口的collection
 * 日期: 2018/10/26 16:11
 *
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private PageService pageService;

    @Override
    @GetMapping("/list/{page}/{size}")

    public QueryResponseResult findList(@PathVariable("page") int page,@PathVariable("size") int
            size,QueryPageRequest queryPageRequest) {
//        //暂时采用测试数据，测试接口是否可以正常运行
//        QueryResult queryResult = new QueryResult();
//        queryResult.setTotal(2);
//        //静态数据列表
//        List list = new ArrayList();
//        CmsPage cmsPage = new CmsPage();
//        cmsPage.setPageName("测试页面");
//        list.add(cmsPage);
//        queryResult.setList(list);
//        QueryResponseResult queryResponseResult = new
//                QueryResponseResult(CommonCode.SUCCESS,queryResult);
//        return queryResponseResult;

        return pageService.findList(page,size,queryPageRequest);
    }

    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return pageService.add(cmsPage);
    }

    // 根据id查找
    @Override
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable String id) {
        return pageService.getById(id);
    }

    // 修改
    @Override
    @PutMapping("/edit/{id}")
    //这里使用put方法，http 方法中put表示更新
    public CmsPageResult edit(@PathVariable String id, @RequestBody CmsPage cmsPage) {
        return pageService.edit(id,cmsPage);
    }

    // 删除
    @Override
    @DeleteMapping("/del/{id}") //使用http的delete方法完成岗位操作
    public ResponseResult del(@PathVariable String id) {
        return pageService.del(id);
    }
}