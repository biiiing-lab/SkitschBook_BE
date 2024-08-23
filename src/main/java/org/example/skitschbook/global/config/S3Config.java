package org.example.skitschbook.global.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3Client.builder()
                .withRegion(Regions.AP_NORTHEAST_1) // todo 리전 설정
                .build();
    }
}
