package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.generics.repository.MethodRepository;

import java.io.*;
import java.util.*;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/12/7 17:47
 */
@Service
public class MediaFileService {

    @Autowired
    private MethodRepository methodRepository;

    @Autowired
    private MediaFileRepository mediaFileRepository;

    //上传文件根目录
    @Value("${xc-service-manage-media.upload-location}")
    String uploadPath;

    // 文件上传前注册
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String minetype, String fileExt) {

        //检查文件是否上传
        //1、得到文件的路径
        String filePath = getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        //2、查询数据库文件是否存在
        Optional<MediaFile> optional = mediaFileRepository.findById(fileMd5);
        //文件存在直接返回
        if (file.exists() && optional.isPresent()) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        boolean fileFold = createFileFold(fileMd5);
        if (!fileFold) {
            //上传文件目录创建失败
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_CREATEFOLDER_FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }


    /**
     * 根据文件md5得到文件路径
     * 规则：
     * 一级目录：md5的第一个字符
     * 二级目录：md5的第二个字符
     * 三级目录：md5
     * 文件名：md5+文件扩展名
     *
     * @param fileMd5 文件md5值
     * @param fileExt 文件扩展名
     * @return 文件路径
     */
    private String getFilePath(String fileMd5, String fileExt) {
        String filePath = uploadPath + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) +
                "/" + fileMd5 + "/" + fileMd5 + "." + fileExt;
        return filePath;
    }

    //得到文件目录相对路径，路径中去掉根目录
    private String getFileFolderRelativePath(String fileMd5, String fileExt) {
        String filePath = fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" +
                fileMd5 + "/";
        return filePath;
    }

    //得到fileMd5文件所在目录
    private String getFileFolderPath(String fileMd5) {
        String fileFolderPath = uploadPath + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1,
                2) + "/" + fileMd5 + "/";
        return fileFolderPath;
    }

    //创建文件目录
    private boolean createFileFold(String fileMd5) {
        //创建上传文件目录
        String fileFolderPath = getFileFolderPath(fileMd5);
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            //创建文件夹
            boolean mkdirs = fileFolder.mkdirs();
            return mkdirs;
        }
        return true;
    }

    /**
     * 分块检查
     *
     * @param fileMd5
     * @param chunk
     * @param chunkSize
     * @return
     */
    // 分块检查
    public CheckChunkResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize) {

        // 检查分块目录是否存在
        // 得到分块文件的目录
        String checkChunkPath = getCheckChunkPath(fileMd5);
        File checkFile = new File(checkChunkPath + chunk);
        if (checkFile.exists()) {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, true);
        } else {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, false);
        }

    }

    // 获取分块文件的目录
    private String getCheckChunkPath(String fileMd5) {
        String fileChunkFolderPath = getFileFolderPath(fileMd5) + "/" + "chunks" + "/";
        return fileChunkFolderPath;
    }

    /**
     * 块文件上传
     *
     * @param file
     * @param chunk
     * @param fileMd5
     * @return
     */

    public ResponseResult uploadchunk(MultipartFile file, Integer chunk, String fileMd5) {

        if (file == null) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_ISNULL);
        }
        // 找到文件路径
        //创建块文件目录
        boolean fileFold = createChunkFileFolder(fileMd5);
        File chunkFile = new File(getFileFolderPath(fileMd5) + chunk);
        //上传的块文件
        if (!chunkFile.exists()) {
            chunkFile.mkdirs();
        }
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(chunkFile);
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(MediaCode.CHUNK_FILE_UPLOAD_FAIL);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);

    }

    private boolean createChunkFileFolder(String fileMd5) {
        //创建上传文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        if (!chunkFileFolder.exists()) {
            //创建文件夹
            boolean mkdirs = chunkFileFolder.mkdirs();
            return mkdirs;
        }
        return true;
    }

    //得到块文件所在目录
    private String getChunkFileFolderPath(String fileMd5) {
        String fileChunkFolderPath = getFileFolderPath(fileMd5) + "/" + "chunks" + "/";
        return fileChunkFolderPath;
    }

    /**
     * 合并文件
     *
     * @param fileMd5
     * @param fileName
     * @param fileExt
     * @param fileSize
     * @param mimetype
     * @return
     */
    public ResponseResult mergechunks(String fileMd5, String fileName, String fileExt, Long fileSize, String mimetype) {

        // 合并所有分块
        // 获取文件分块路径，读一块写一块
        //获取块文件的路径
        String chunkfileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkfileFolder = new File(chunkfileFolderPath);
        if (!chunkfileFolder.exists()) {
            chunkfileFolder.mkdirs();
        }
        //合并文件路径
        File mergeFile = new File(getFilePath(fileMd5, fileExt));
        //创建合并文件
        //合并文件存在先删除再创建
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        File[] listFiles = chunkfileFolder.listFiles();
        //获取块文件，此列表是已经排好序的列表
        List<File> chunkFiles = getChunkFiles(chunkfileFolder);
        // 返回合并文件
        mergeFile = mergeFile(mergeFile, chunkFiles);
        if (mergeFile == null) {
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }
        // 校验md5是否相等
        boolean checkFileMd5 = checkFileMd5(mergeFile, fileMd5);
        if (!checkFileMd5) {
            ExceptionCast.cast(MediaCode.MD5_VALIDATION_FAILED);
        }
        // 写入mongodb
        //将文件信息保存到数据库
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileName(fileMd5 + "." + fileExt);
        mediaFile.setFileOriginalName(fileName);
        //文件路径保存相对路径
        mediaFile.setFilePath(getFileFolderRelativePath(fileMd5, fileExt));
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        //状态为上传成功
        mediaFile.setFileStatus("301002");
        MediaFile save = mediaFileRepository.save(mediaFile);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    // 合并文件
    private File mergeFile(File mergeFile, List<File> chunkFiles) {

        // 将排序好的文件全部写入新创建的文件
        if (chunkFiles == null && mergeFile == null) {
            // 传入排序文件为空
            return null;
        }
        // 创建输入流和输出流
        try {
            RandomAccessFile random_write = new RandomAccessFile(mergeFile, "rw");
            byte[] b = new byte[1024];
            int len = -1;
            // 遍历文件数组
            for (File chunkFile : chunkFiles) {
                RandomAccessFile random_read = new RandomAccessFile(chunkFile, "r");
                while ((len = random_read.read(b)) != -1) {
                    random_write.write(b, 0, len);
                }
                random_read.close();
            }
            random_write.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mergeFile;
    }

    //获取所有块文件
    private List<File> getChunkFiles(File chunkfileFolder) {

        if (chunkfileFolder == null) {
            ExceptionCast.cast(MediaCode.CHUNK_FILE_UPLOAD_FAIL);
        }
        File[] listFiles = chunkfileFolder.listFiles();
        //将文件数组转成list，并排序
        if (listFiles == null) {
            throw new RuntimeException("块文件数组不存在");
        }
        List<File> chunkFileList = new ArrayList<File>();
        chunkFileList.addAll(Arrays.asList(listFiles));
        Collections.sort(chunkFileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.valueOf(o1.getName()) > Integer.valueOf(o2.getName())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return chunkFileList;
    }

    //校验文件的md5值，合并后的文件
    private boolean checkFileMd5(File mergeFile, String md5) {

        if (mergeFile == null && StringUtils.isEmpty(md5)) {
            return false;
        }
        FileInputStream mergeFileInputstream = null;
        try {
            mergeFileInputstream = new FileInputStream(mergeFile);
            String mergeFileMd5 = DigestUtils.md5Hex(mergeFileInputstream);
            if (mergeFileMd5.equalsIgnoreCase(md5)) {
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}