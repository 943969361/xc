package com.xuecheng.search;


import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/11/14 09:57
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private RestClient restClient;

    @Test
    // 删除索引库
    public void testDeleteIndex() throws IOException {

        // 删除索引对象
        DeleteIndexRequest indexRequest = new DeleteIndexRequest("xc_course");
        // 操作索引的客户端
        IndicesClient indices = client.indices();
        // 执行删除索引
        DeleteIndexResponse delete = indices.delete(indexRequest);
        boolean acknowledged = delete.isAcknowledged();
        System.out.println(acknowledged);

    }

    // 创建索引库
    @Test
    public void testCreatIndex() throws IOException {

        CreateIndexRequest createIndexRequest = new CreateIndexRequest();
        // 设置分片数
        createIndexRequest.settings(Settings.builder().put("number_of_shards",1).put("number_of_replicas",0));
        // 设置映射
        createIndexRequest.mapping("doc","{\n" +
                "\"properties\": {\n" +
                "\"name\": {\n" +
                "\"type\": \"text\",\n" +
                "\"analyzer\":\"ik_max_word\",\n" +
                "\"search_analyzer\":\"ik_smart\"\n" +
                "},\n" +
                "\"description\": {\n" +
                "\"type\": \"text\",\n" +
                "\"analyzer\":\"ik_max_word\",\n" +
                "\"search_analyzer\":\"ik_smart\"\n" +
                "},\n" +
                "\"pic\":{\n" +
                "\"type\":\"text\",\n" +
                "\"index\":false\n" +
                "},\n" +
                "\"studymodel\":{\n" +
                "\"type\":\"text\"\n" +
                "}\n" +
                "}\n" +
                "}", XContentType.JSON);
        IndicesClient indices = client.indices();
        CreateIndexResponse response = indices.create(createIndexRequest);
        boolean acknowledged = response.isAcknowledged();
        System.out.println(acknowledged);
    }

    // 添加文档
    @Test
    public void testAddFile () throws IOException {

        //准备json数据放入map里
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "spring cloud实战");
        jsonMap.put("description", "本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud 基础入门 3.实战Spring Boot 4.注册中心eureka。");
        jsonMap.put("studymodel", "201001");
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
        jsonMap.put("timestamp", dateFormat.format(new Date()));
        jsonMap.put("price", 5.6f);

        // 索引请求对象
        IndexRequest indexRequest = new IndexRequest("xc_course","doc");
        // 指定索引文档内容
        indexRequest.source(jsonMap);
        // 获取返回值
        IndexResponse index = client.index(indexRequest);
        // 返回值
        DocWriteResponse.Result result = index.getResult();
        System.out.println(result);
    }

    // 查询文档
    @Test
    public void testGetFile() throws IOException {

        GetRequest getRequest = new GetRequest("xc_course","doc", "4028e58161bcf7f40161bcf8b77c0000");
        GetResponse documentFields = client.get(getRequest);
        boolean exists = documentFields.isExists();
        Map<String, Object> source = documentFields.getSource();
        System.out.println(source);
    }

    // 跟新文档
    @Test
    public void testUpdateFile() throws IOException {

        UpdateRequest updateRequest = new UpdateRequest("xc_course", "doc","4028e58161bcf7f40161bcf8b77c0000");
        Map<String,String> map = new HashMap<>();
        map.put("name","spring cloud");
        updateRequest.doc(map);
        UpdateResponse update = client.update(updateRequest);
        RestStatus status = update.status();
        System.out.println(status);
    }

    // 删除文档
    @Test
    public void testDeleteFile() throws IOException {

        String id = "4028e58161bcf7f40161bcf8b77c0000";
        DeleteRequest deleteRequest = new DeleteRequest("xc_course","doc",id);
        DeleteResponse delete = client.delete(deleteRequest);
        DocWriteResponse.Result result = delete.getResult();
        System.out.println(result);

    }


}
