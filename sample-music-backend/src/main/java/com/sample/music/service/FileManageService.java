package com.sample.music.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.sample.music.config.AliOssConfig;
import com.sample.music.common.Result;
import com.sample.music.pojo.vo.FileManageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileManageService {

    // 允许上传的格式
    private static final String[] FILE_TYPE = new String[]{
            ".txt", ".lrc", ".log", ".md",
            ".bmp", ".jpg", ".jpeg", ".gif", ".png",
            ".mp3", ".flac"
    };

    private final OSS ossClient;

    private final AliOssConfig aliOssConfig;

    /**
     * 文件上传接口
     *
     * @param fileContent 文件
     * @return Result<FileManageVO>
     */
    public String uploadByte(byte[] fileContent, String title, String artist, String suffix, String path) {
        String fileName = title + "-" + artist + suffix;
        String filePath = "music/" + path + "/" + fileName;
        try {
            ossClient.putObject(aliOssConfig.getBucketName(), filePath, new ByteArrayInputStream(fileContent));
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }
        return this.aliOssConfig.getUrlPrefix() + filePath;
    }

    /**
     * 文件上传接口
     *
     * @param uploadFile 文件
     * @return Result<FileManageVO>
     */
    public String uploadFile(MultipartFile uploadFile, String path) {
        // 校验文件格式
        boolean isLegal = false;
        for (String type : FILE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }
        if (!isLegal) {
            return "error";
        }
        // 文件新路径
        String fileName = uploadFile.getOriginalFilename();
        String filePath = customFilePath(fileName, path);
        // 上传到阿里云
        try {
            ossClient.putObject(aliOssConfig.getBucketName(), filePath, new ByteArrayInputStream(uploadFile.getBytes()));
        } catch (Exception e) {
            return "error";
        }
        return this.aliOssConfig.getUrlPrefix() + filePath;
    }

    /**
     * 查看文件列表
     * @return List<OSSObjectSummary>
     */
    public List<OSSObjectSummary> list() {
        // 设置最大个数。
        final int maxKeys = 200;
        // 列举文件。
        ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(aliOssConfig.getBucketName()).withMaxKeys(maxKeys));
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        return sums;
    }

    /**
     * 删除文件
     * @param objectName 文件名
     * @return Result<FileManageVO>
     */
    public Result<FileManageVO> delete(String objectName) {
        // 根据BucketName,objectName删除文件
        ossClient.deleteObject(aliOssConfig.getBucketName(), objectName);
        FileManageVO fileManageVO = new FileManageVO();
        fileManageVO.setUrl(objectName);
        fileManageVO.setStatus("removed");
        fileManageVO.setResponse("success");
        return Result.success(fileManageVO);
    }

    /**
     * 下载文件
     * @param os 输出流
     * @param objectName 文件名
     * @throws IOException io异常
     */
    public void exportOssFile(OutputStream os, String objectName) throws IOException {
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(aliOssConfig.getBucketName(), objectName);
        // 读取文件内容。
        BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
        BufferedOutputStream out = new BufferedOutputStream(os);
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        out.flush();
        out.close();
        in.close();
    }

    private String customFilePath(String sourceFileName, String path) {

        // 创建目录路径
        String dirPath = "music/" + path + "/";

        // 获取文件的原始扩展名
        int extensionIndex = sourceFileName.lastIndexOf('.');
        String extension = (extensionIndex != -1) ? sourceFileName.substring(extensionIndex) : "";

        /*// 生成随机数作为文件名的一部分
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000); // 生成1000到9999的随机数

        // 创建新的文件名
        String newFileName = System.currentTimeMillis() + randomNumber + extension;
        */
        // 生成 UUID
        String newFileName = UUID.randomUUID().toString();

        // 如果原始文件名包含扩展名，则添加扩展名
        if (!extension.isEmpty()) {
            newFileName += extension;
        }
        // 返回完整的文件路径
        return dirPath + newFileName;
    }

}
