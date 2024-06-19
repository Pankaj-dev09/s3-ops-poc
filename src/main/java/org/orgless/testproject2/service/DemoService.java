package org.orgless.testproject2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orgless.testproject2.utils.S3Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class DemoService {
    @Value("${aws.s3Bucket-ums}")
    private String s3BucketUms;

    private final S3Utils s3Utils;

    public Map<String, String> getMap(List<String> userIds) {
        ConcurrentHashMap<String, String> results = new ConcurrentHashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        try {
            List<Future<?>> futures = new ArrayList<>();
//            for (String id : userIds) {
            for(int i = 0 ; i < 250; i++) {
                futures.add(executorService.submit(() -> {
//                    String presignedUrl = s3Utils.getLastModifiedWithPrefix(s3BucketUms, "users/user-" + id);
                    String presignedUrl = s3Utils.listObjectsWithPrefix(s3BucketUms, "").toString();
                    results.put(String.valueOf(new Random().nextInt(Integer.MAX_VALUE)), presignedUrl);
                }));
            }

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Could not execute future: {}", future);
                }
            }
        } finally {
            executorService.shutdown();
        }
        return results;
    }
}
