package com.sebczu.elastic.tutorial.application.book;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BookRepository extends ReactiveCrudRepository<Book, String> {

}
