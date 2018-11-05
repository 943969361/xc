package com.xuecheng.manage_cms;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

    @Test
    public void  testGridFs() throws FileNotFoundException {

        File file = new File("E:/index_banner.ftl");

        FileInputStream fileInputStream= new FileInputStream(file);
        ObjectId objectId =  gridFsTemplate.store(fileInputStream,"index_banner.ftl","");
        String toString = objectId.toString();
        System.out.println(toString);
    }


}
