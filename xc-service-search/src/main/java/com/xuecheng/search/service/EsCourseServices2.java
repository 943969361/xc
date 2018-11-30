package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/11/29 15:01
 */
@Service
public class EsCourseServices2 {

    @Value("${xuecheng.course.index}")
    private String index;

    @Value("${xuecheng.course.type}")
    private String type;

    @Value("${xuecheng.course.source_field}")
    private String source_field;

    @Autowired
    RestHighLevelClient RestHighLevelClient;

    // 按关键字搜
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) {

        if(courseSearchParam == null){
            courseSearchParam = new CourseSearchParam();
        }

        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        //设置搜索类型
        searchRequest.types(type);
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //过虑源字段
        String[] split = source_field.split(",");
        searchSourceBuilder.fetchSource(split,new String[]{});
        //创建布尔查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //搜索条件
        //根据关键字搜索
        if(StringUtils.isEmpty(courseSearchParam.getKeyword())){
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(),"name","description", "teachplan")
                    // 匹配度
                    .minimumShouldMatch("70%")
                    // 增加name的匹配度
                    .field("name",10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        if(StringUtils.isNotEmpty(courseSearchParam.getMt())){
            //根据一级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getMt()));
        }
        if(StringUtils.isNotEmpty(courseSearchParam.getSt())){
            //根据二级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("st",courseSearchParam.getSt()));
        }
        if(StringUtils.isNotEmpty(courseSearchParam.getGrade())){
            //根据难度等级
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade",courseSearchParam.getGrade()));
        }
        // 分页
        if(page <= 0){
            page = 1;
        }
        if(size <= 0){
            size = 20;
        }
        int start = (page -1) * size;
        // 设置开始值
        searchSourceBuilder.from(start);
        searchSourceBuilder.size(size);

        //设置boolQueryBuilder到searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        //高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");

        // 设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);

        //请求搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;

        QueryResult<CoursePub> queryResult = new QueryResult();
        List<CoursePub> list = new ArrayList<>();
        try {
            //执行搜索
            SearchResponse response = RestHighLevelClient.search(searchRequest);
            SearchHits hits = response.getHits();
            long totalHits = hits.totalHits;
            queryResult.setTotal(totalHits);
            SearchHit[] hitss = hits.getHits();
            for (SearchHit hit : hitss) {

                CoursePub coursePub = new CoursePub();
                //源文档
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //取出name
                String name = (String) sourceAsMap.get("name");
                coursePub.setName(name);
                //图片
                String pic = (String) sourceAsMap.get("pic");
                coursePub.setPic(pic);
                //价格
                Double price = null;
                try {
                    if(sourceAsMap.get("price")!=null ){
                        price = (Double) sourceAsMap.get("price");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                coursePub.setPrice(price);
                //旧价格
                Double price_old = null;
                try {
                    if(sourceAsMap.get("price_old")!=null ){
                        price_old = (Double) sourceAsMap.get("price_old");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                coursePub.setPrice_old(price_old);
                //将coursePub对象放入list
                list.add(coursePub);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        queryResult.setList(list);
        QueryResponseResult<CoursePub> queryResponseResult = new QueryResponseResult<CoursePub>(CommonCode.SUCCESS,queryResult);

        return queryResponseResult;

    }
}
