package org.orgless.testproject2;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class TestProject2Application {

    public static void main(String[] args) {
        SpringApplication.run(TestProject2Application.class, args);
    }

}
