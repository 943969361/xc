package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/12/11 10:15
 */
@Service
public class TestService {

    // 使用transferTo
    public ResponseResult upload(MultipartFile file) {

        if(file == null){
            // 传入文件为空
            ExceptionCast.cast(MediaCode.MD5_VALIDATION_FAILED);
        }
        try {
            file.transferTo(new File("E:\\test"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传问题");
        }

        return new ResponseResult(CommonCode.SUCCESS);
    }
}
