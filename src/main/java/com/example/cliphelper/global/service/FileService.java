package com.example.cliphelper.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class FileService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile multipartFile) {
        System.out.println("주입받은 amzonS3Client 객체: " + amazonS3);
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            System.out.println("=========파일 저장 중 에러 발생===========");
            throw new RuntimeException(e.getMessage());
        }

        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    public String findFileUrl(String originalFilename) {
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    public void deleteFile(String originalFilename)  {
        amazonS3.deleteObject(bucket, originalFilename);
    }
}
