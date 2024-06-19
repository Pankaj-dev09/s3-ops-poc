package org.orgless.testproject2.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class ExecutorServiceRunner implements CommandLineRunner {
    public static void driver1(String... args) {

        // Provides facilities to execute one thread at a time.
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(
                () -> {
                    IntStream.range(0, 11)
                            .forEach(i -> System.out.print(i + " "));
                    System.out.println("\nTask-1 completed");
                }
        );
        System.out.println("-".repeat(35));

        executorService.execute(
                () -> {
                    IntStream.range(0, 16)
                            .forEach(i -> System.out.print(i + " "));
                    System.out.println("\nTask-2 completed");
                }
        );

        executorService.shutdown();
    }

    private final S3Utils s3Utils;

    @Value("${aws.s3Bucket-ums}")
    private String s3BucketUms;

    public void driver2(String... args) {

        s3Utils.listObjectsWithPrefix(s3BucketUms, "");
    }

    @Override
    public void run(String... args) throws Exception {
//        driver1();
        driver2();
    }
}
