package com.example.betterreadsdataloader.book;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Table(value = "book_by_id")
public class Book {

    // We want the book data to be partitioned based on the book id
    @Id
    @PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED) // I want the element below this line to map to a column and I want that column to be a partition key. Hence, every unique value in this column is going to resolve to a new partition
    private String id;

    @Column("book_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String name;

    @Column("book_description")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String description;


    @Column("published_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate publishedDate;

    @Column("cover_ids")
    // Field type is List, argument type is string
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> coverIds;

    @Column("author_names")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> authorNames;

    @Column("author_id")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> authorIds;

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<String> getCoverIds() {
        return coverIds;
    }

    public void setCoverIds(List<String> coverIds) {
        this.coverIds = coverIds;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public List<String> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<String> authorIds) {
        this.authorIds = authorIds;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", publishedDate=" + publishedDate +
                ", coverIds=" + coverIds +
                ", authorNames=" + authorNames +
                ", authorIds=" + authorIds +
                '}';
    }
}
