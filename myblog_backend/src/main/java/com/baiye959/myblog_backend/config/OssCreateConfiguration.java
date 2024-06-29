package com.baiye959.myblog_backend.config;


import com.baiye959.myblog_backend.utils.AliOssProperties;
import com.baiye959.myblog_backend.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class OssCreateConfiguration {

    /**
     * 创建AliOssUtil对象
     * @return
     */
    @Bean
    //保证只有一个aliOssUtilCreate的bean对象
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtilCreate(AliOssProperties aliOssProperties){
        log.info("开始创建阿里云文件上传工具类对象：{}",aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}
