package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaUploadControllerApi;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/12/7 17:40
 */
@RestController
@RequestMapping("/media/upload")
public class MediaFileController implements MediaUploadControllerApi {

    @Autowired
    private MediaFileService mediaFileService;


    //  文件上传前的操作
    @Override
    public ResponseResult register(String fileMs5, String fileName, Long fileSize, String minetype, String filExt) {
        return mediaFileService.register(fileMs5,fileName,fileSize,minetype,filExt);
    }

    @Override
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        return mediaFileService.checkChunk(fileMd5,chunk,chunkSize);
    }

    @Override
    public ResponseResult uploadchunk(MultipartFile file, Integer chunk, String fileMd5) {
        return mediaFileService.uploadchunk(file,chunk,fileMd5);
    }

    @Override
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        return mediaFileService.mergechunks(fileMd5,fileName,fileExt,fileSize,mimetype);
    }

}
