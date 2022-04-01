package com.example.betterreadsdataloader;

import com.example.betterreadsdataloader.connection.DataStaxAstraProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.nio.file.Path;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)  // Configuration for the class property
public class BetterReadsDataLoaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(BetterReadsDataLoaderApplication.class, args);
    }


    // A method that runs when the application starts
    @PostConstruct
    public void start() {

    }


















    // This exposes the CQL Session Builder Customizer
    // it used the astraProperties path to create a new CQL Session Buildder Customizer Bean
    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

}
