package com.example.cliphelper.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.cliphelper.global.error.BusinessException;
import com.example.cliphelper.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile multipartFile, String uuid) {
        System.out.println("주입받은 amzonS3Client 객체: " + amazonS3);
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        // final int UUID_LENGTH = 36;
        String filename = uuid + originalFilename;
        try {
            amazonS3.putObject(bucket, filename, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_CANNOT_UPLOAD);
        }

        return amazonS3.getUrl(bucket, filename).toString();
    }

    public String findFileUrl(String filename) {
        return amazonS3.getUrl(bucket, filename).toString();
    }

    public void deleteFile(String filename)  {
        amazonS3.deleteObject(bucket, filename);
    }
}
