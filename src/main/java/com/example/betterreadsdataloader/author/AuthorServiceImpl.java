package com.example.betterreadsdataloader.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    @Override
    public String createAuthor(Author author) {
        authorRepository.save(author);
        return "Author created";
    }

}
