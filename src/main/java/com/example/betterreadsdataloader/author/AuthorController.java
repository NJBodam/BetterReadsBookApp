package com.example.betterreadsdataloader.author;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/author")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/save")
    public ResponseEntity<String> addAuthor(@RequestBody Admin admin) {
        return authorService.createAuthor(admin);
    }

}

