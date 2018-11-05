package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 作者: lin
 * 描述: 文件服务器
 * 日期: 2018/11/5 14:05
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTest {


    // 存文件
    @Autowired
    private GridFsTemplate gridFsTemplate;

    // 下载文件
    @Autowired
    private GridFSBucket gridFSBucket;


    @Test
    public void  testGridFs() throws FileNotFoundException {

        File file = new File("E:/index_banner.ftl");

        FileInputStream fileInputStream= new FileInputStream(file);
        ObjectId objectId =  gridFsTemplate.store(fileInputStream,"index_banner.ftl","");
        String toString = objectId.toString();
        System.out.println(toString);
    }

    // 下载文件
    @Test
    public void download()throws Exception{

        String fild = "5bdfe3b7fb76e5122c7783d6";
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fild)));
        // 打开一个下载流
        //打开下载流对象
        GridFSDownloadStream gridFSDownloadStream =
                gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsResource，用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        //获取流中的数据
        String s = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");

        System.out.println(s);

    }

    @Test
    public void testDelFile() throws IOException {
        //根据文件id删除fs.files和fs.chunks中的记录
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is("5bdfe3b7fb76e5122c7783d6")));
    }


}
