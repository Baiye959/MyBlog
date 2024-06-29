package com.baiye959.myblog_backend.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Data
@ConfigurationProperties(prefix = "sky.alioss")
public class AliOssProperties {
    public String endpoint;
    public String accessKeyId;
    public String accessKeySecret;
    public String bucketName;
}
