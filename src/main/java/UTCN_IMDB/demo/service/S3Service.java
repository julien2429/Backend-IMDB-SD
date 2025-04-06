package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.config.CompileTimeException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    // Upload file to S3 bucket
    public String uploadFile(File file) throws CompileTimeException {
        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, file.getName(), file));
            return file.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error uploading file";
        }
    }

    // Download file from S3 bucket
    public S3Object downloadFile(String fileName) {
        return amazonS3.getObject(bucketName, fileName);
    }
}