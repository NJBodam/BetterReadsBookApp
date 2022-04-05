package com.example.betterreadsdataloader.author;

import com.example.betterreadsdataloader.book.Book;
import com.example.betterreadsdataloader.book.BookRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PersistBookAuthor {

    @Value("${datadump.location.author}")
    private String authorDumpLocation;

    @Value("${datadump.location.work}")
    private String worksDumpLocation;

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    public PersistBookAuthor(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    public void initAuthors(){
        Path path = Paths.get(authorDumpLocation);
       // Path pathToFile = Paths.get(authorDumpLocation);
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
                    author.setPersonalName(jsonObject.optString("personal_name"));
                    // Each record that we are saving is going as a partition
                    // One id (Author Id) is going to be associated with one record
                    // Cassandra takes each author and runs a hash function on it and mapping it to one of the nodes
                    // Cassandra has a default replication factor of 3 so it also creates 2 additional entries in 2 more nodes
                    authorRepository.save(author);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void initBooks() {
        Path path = Paths.get(worksDumpLocation);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        try (Stream<String> lines = Files.lines(path)) {
            // Read and parse the line
            lines.forEach(line -> {
                String jsonString = line.substring(line.indexOf("{"));
                // Construct Author object
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Book book = new Book();
                    book.setId(jsonObject.optString("key").replace("/works/", ""));
                 //   System.err.println(jsonObject.optString("key").replace("/works/", ""));
                    book.setName(jsonObject.optString("title"));
                 //   System.err.println(jsonObject.optString("title"));
                    JSONArray coversJSONArr = jsonObject.optJSONArray("covers");
                    if(coversJSONArr != null) {
                        List<String> coverIds = new ArrayList<>();
                        for (int i = 0; i < coversJSONArr.length(); i++) {
                            coverIds.add(coversJSONArr.getString(i));
                        }
                        book.setCoverIds(coverIds);
                    }
                    //book.setCoverIds((List<String>) jsonObject.optJSONArray("covers"));
                  //  System.err.println(book.getCoverIds().toString());

                    //Setting Author Ids
                    JSONArray authorsJSONArr = jsonObject.optJSONArray("authors");
                    if (authorsJSONArr != null) {
                        List<String> authorIds = new ArrayList<String>();
                        for (int i = 0; i < authorsJSONArr.length(); i++) {
                          //  System.err.println(">>>>>>>WATCH THIS" + authorsJSONArr.getJSONObject(i).getJSONObject("author")
                            //        .getString("key"));
                            String authorId = authorsJSONArr.getJSONObject(i).getJSONObject("author")
                                    .getString("key").replace("/authors/", "");
                            authorIds.add(authorId);
                        }
                        book.setAuthorIds(authorIds);
                      //  System.err.println(authorIds);
                        List<String> authorNames = authorIds.stream().map(authorRepository::findById)
                                .map(optionalAuthor -> {
                                    if(optionalAuthor.isEmpty()) return "Unknown Author";
                                    return optionalAuthor.get().getName();
                                }).collect(Collectors.toList());
                        book.setAuthorNames(authorNames);
                       // System.err.println(authorNames);

                    }

                    JSONObject descriptionObj = jsonObject.optJSONObject("description");
                    if(descriptionObj != null) {
                        book.setDescription(descriptionObj.optString("value"));
                    }
                    book.setPublishedDate(LocalDate.parse(jsonObject.optJSONObject("created").optString("value"), dateFormat));
                    // Each record that we are saving is going as a partition
                    // One id (Author Id) is going to be associated with one record
                    // Cassandra takes each author and runs a hash function on it and mapping it to one of the nodes
                    // Cassandra has a default replication factor of 3 so it also creates 2 additional entries in 2 more nodes
                    System.err.println(">>>>>>>>>>>>" + book);
                    bookRepository.save(book);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
