package com.example.betterreadsdataloader.author;

import org.springframework.http.ResponseEntity;

public interface AuthorService {
    ResponseEntity<String> createAuthor(Admin admin);
}
