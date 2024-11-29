package com.sparta.trelloproject.common.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.trelloproject.common.exception.ForbiddenException;
import com.sparta.trelloproject.common.exception.ResponseCode;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
@Transactional
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile multipartFile){
        if (!isMatchingExtension(multipartFile)) {
            throw new ForbiddenException(ResponseCode.INVALID_EXTENTSION);
        }
        return uploadFileToS3(multipartFile);
    }

    public String updateFile(MultipartFile multipartFile, String existFilePath) {
        if (existFilePath != null && !existFilePath.isEmpty()) {
            String existingFileName = existFilePath.substring(
                existFilePath.lastIndexOf("/") + 1);
            amazonS3Client.deleteObject(bucket, existingFileName);
        }
        return uploadFileToS3(multipartFile);
    }

    public void deleteFile(String fileName) {
        amazonS3Client.deleteObject(bucket, fileName);
    }

    private String uploadFileToS3(MultipartFile multipartFile) {
        try {
            String originalFileName = multipartFile.getOriginalFilename();
            String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            String randomFileName = UUID.randomUUID().toString();
            String fullFileName = randomFileName + fileName;

            amazonS3Client.putObject(
                new PutObjectRequest(bucket, fullFileName,
                    multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            );
            return amazonS3Client.getUrl(bucket, fullFileName).toString();
        } catch (IOException e) {
            throw new ForbiddenException(ResponseCode.INVALID_UPLOAD);
        }
    }

    private boolean isMatchingExtension(MultipartFile multipartFile) {
        List<String> extensions = List.of("jpg", "png", "pdf", "csv");
        String originalFileName = multipartFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        return extensions.contains(extension.toLowerCase());
    }
}
