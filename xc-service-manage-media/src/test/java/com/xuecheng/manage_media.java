package com.xuecheng;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/12/7 14:05
 */
public class manage_media {


    @Test
    public void test() throws IOException {
        // 找到源文件
        File file = new File("F:\\test\\lucene.avi");
        // 存储块文件路径
        String pathNew = "F:\\test\\block\\";
        // 设置每一块的大小
        long size = 1 * 1024 * 1024;
        //计算有多少块向上取整
        System.out.println(file.length());
        long checkUnm = (long) Math.ceil(file.length() * 1.0 / size);
        // 创建缓冲区
        byte[] b = new byte[1024];
        // 读文件类
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        for (int i = 0; i < checkUnm; i++) {
            File newFile = new File(pathNew + i);
            int len = -1;
            RandomAccessFile randomAccessFileWrite = new RandomAccessFile(newFile, "rw");
            while ((len = randomAccessFile.read(b)) != -1) {
                randomAccessFileWrite.write(b, 0, len);
                // 块文件大小等于块的大小
                if (newFile.length() >= size) {
                    break;
                }
            }
            randomAccessFileWrite.close();
        }
        randomAccessFile.close();

    }


    @Test
    public void test2() throws IOException {
        // 获取文件
        File file = new File("F:\\test\\block\\");
        File[] listFiles = file.listFiles();
        // 转数组
        List<File> files = Arrays.asList(listFiles);
        // 排序
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                    return 1;
                }
                return -1;
            }
        });

        // 合并文件路径
        File fileWrite = new File("F:\\test\\lucene_master.avi");
        // 排好序开始写文件
        RandomAccessFile random_write = new RandomAccessFile(fileWrite, "rw");
        // 创建新文件
        boolean newFile = fileWrite.createNewFile();
        // 缓冲区
        byte[] b = new byte[1024];
        for (File file1 : files) {

            RandomAccessFile random_read = new RandomAccessFile(file1, "rw");
            int len = -1;
            while ((len = random_read.read(b)) != -1) {
                random_write.write(b, 0, len);
            }
            random_read.close();
        }
        random_write.close();
    }

}
