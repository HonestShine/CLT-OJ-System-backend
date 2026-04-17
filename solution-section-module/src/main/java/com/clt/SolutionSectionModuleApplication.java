package com.clt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SolutionSectionModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SolutionSectionModuleApplication.class, args);
    }
}
