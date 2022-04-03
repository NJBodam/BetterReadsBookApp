package com.example.betterreadsdataloader;

import com.example.betterreadsdataloader.author.Author;
import com.example.betterreadsdataloader.author.AuthorRepository;
import com.example.betterreadsdataloader.author.AuthorService;
import com.example.betterreadsdataloader.author.AuthorServiceImpl;
import com.example.betterreadsdataloader.connection.DataStaxAstraProperties;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)  // Configuration for the class property
public class BetterReadsDataLoaderApplication {

    @Autowired
    public AuthorServiceImpl authorService;

    @Value("${datadump.location.author}")
    private String authorDumpLocation;

    @Value("${datadump.location.work}")
    private String worksDumpLocation;



    public static void main(String[] args) {
        SpringApplication.run(BetterReadsDataLoaderApplication.class, args);
    }

    private void initAuthors() {
        Path path = Paths.get(authorDumpLocation);
        Path pathToFile = Paths.get(authorDumpLocation);
        System.err.println(">>>>>>>>>>>>>>>>>>>" + pathToFile.toAbsolutePath());
        try (Stream<String> lines = Files.lines(path)){
            // Read and parse the line
            lines.forEach(line -> {
                String jsonString = line.substring(line.indexOf("{"));
                // Construct Author object
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Author author = new Author();
                    author.setId(jsonObject.optString("key").replace("/authors/", ""));
                    System.err.println(jsonObject.optString("key").replace("/authors/", ""));
                    author.setName(jsonObject.optString("name"));
                    System.err.println(jsonObject.optString("name"));
                    author.setPersonalName(jsonObject.optString("personal_name", "name"));
                    authorService.createAuthor(author);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initWorks() {

    }

    @PostConstruct
    public void start() {
        //System.out.println(authorDumpLocation);
        initAuthors();
    //    initWorks();

    }


    // A method that runs when the application starts


    // This exposes the CQL Session Builder Customizer
    // it used the astraProperties path to create a new CQL Session Buildder Customizer Bean
    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

}
