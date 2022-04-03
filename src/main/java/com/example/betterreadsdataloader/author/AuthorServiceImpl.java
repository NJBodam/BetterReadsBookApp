package com.example.betterreadsdataloader.author;

import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    @Value("${datadump.location.author}")
    private String authorDumpLocation;

    @Value("${datadump.location.work}")
    private String worksDumpLocation;

    private final AuthorRepository authorRepository;

    @Override
    public ResponseEntity<String> createAuthor(Admin admin) {

        if (admin.getName().equals("Bodam") && admin.getPassword().equals("password")) {
            Path path = Paths.get(authorDumpLocation);
            Path pathToFile = Paths.get(authorDumpLocation);
            System.err.println(">>>>>>>>>>>>>>>>>>>" + pathToFile.toAbsolutePath());
            String response;
            try (Stream<String> lines = Files.lines(path)) {
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
                        authorRepository.save(author);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok().body("Author Created Successfully");
    }

}
