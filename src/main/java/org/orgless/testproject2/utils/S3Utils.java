package org.orgless.testproject2.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class S3Utils {
    @Value("${aws.region}")
    private String region;

    @Value("${aws.accessKeyId}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    private S3Client s3Client;
    private S3Presigner s3Presigner;

    @PostConstruct
    public void init() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        s3Client = S3Client
                .builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        credentials
                ))
                .region(Region.of(region))
                .build();

        s3Presigner = S3Presigner
                .builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public String getLastModifiedWithPrefix(String s3BucketName, String prefix) {
        List<S3Object> objects = listObjectsWithPrefix(s3BucketName, prefix);
        String s3Key = getLastModified(objects).orElseThrow().key();
        GetObjectRequest request = GetObjectRequest
                .builder()
                .bucket(s3BucketName)
                .key(s3Key)
                .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest
                        .builder()
                        .getObjectRequest(request)
                        .signatureDuration(Duration.ofDays(1L))
                        .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    public List<S3Object> listObjectsWithPrefix(String s3Bucket, String prefix) {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request
                .builder()
                .bucket(s3Bucket)
                .prefix(prefix)
                .build();

        System.out.println("-".repeat(40));
        ListObjectsV2Response response = s3Client.listObjectsV2(listObjectsV2Request);

        response.contents().forEach(System.out::println);
        System.out.println("-".repeat(40));
        return response.contents();
    }

    public Optional<S3Object> getLastModified(Collection<S3Object> objects) {
        return  objects
                .stream()
                .min((a, b) -> b.lastModified().compareTo(a.lastModified()));
    }
}
