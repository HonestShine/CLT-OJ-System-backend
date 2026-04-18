package com.clt.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.OSSObject;
import com.clt.exception.FileStorageSpaceOutOfRangeException;
import com.clt.exception.FileSuffixException;
import com.clt.exception.NullFileException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOSSUtil {

    private String endpoint;    // 阿里云 OSS 存储服务地址
    private String bucketName;  // 阿里云 OSS 存储空间名称
    private String region;  // 阿里云 OSS 存储空间区域

    private OSS ossClient;  // 阿里云 OSS 客户端

    /**
     * 创建OSSClient实例
     */
    @PostConstruct
    public void init() throws Exception {
        // 从环境变量获取凭证
        EnvironmentVariableCredentialsProvider credentialsProvider =
                CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        //创建OSSClient实例
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
    }

    /**
     * 释放资源
     */
    @PreDestroy
    public void shutdown() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

    /**
     * 上传文件
     */
    public String upload(byte[] content, String originalFilename) throws RuntimeException {
        // 校验文件大小：最大 5MB
        if (content == null || content.length == 0) {
            throw new NullFileException("文件内容不能为空");
        }
        //检查文件格式
        if (originalFilename == null || !originalFilename.matches(".*\\.(jpg|jpeg|png|gif)$")) {
            throw new FileSuffixException("只支持 JPG、PNG、GIF 格式图片");
        }
        //校验文件大小：最大5MB
        final long MAX_CONTENT_LENGTH = 5 * 1024 * 1024;
        if (content.length > MAX_CONTENT_LENGTH) {
            throw new FileStorageSpaceOutOfRangeException("文件大小不能超过5MB");
        }
        //以当天年月作为文件夹保存当月的头像文件
        String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        //生成一个新的不重复的文件名,利用UUID
        String newFileName =UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //组合文件夹名与文件名
        String fileName = dir + "/" + newFileName;
        try {
            ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(content));
        } catch (OSSException | ClientException e) {
            throw new RuntimeException(e);
        }
        return endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
    }

    /**
     * 删除文件
     */
    public void delete(String fileName) {
        try {
            ossClient.deleteObject(bucketName, fileName);
        } catch (OSSException | ClientException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载文件
     */
    public OSSObject download(String fileName) throws RuntimeException {
        try {
            return ossClient.getObject(bucketName, fileName);
        } catch (OSSException | ClientException e) {
            throw new RuntimeException(e);
        }
    }

}
