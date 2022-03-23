package com.sebczu.elastic.tutorial.application.book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

import static java.util.Collections.singletonMap;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ElasticsearchInitialization {

  public static final String BOOK_INDEX = "book";

  private final ReactiveElasticsearchClient client;
  private final BookRepository repository;

//  @PostConstruct
  public void init() {
    client.indices()
      .existsIndex(new GetIndexRequest(BOOK_INDEX))
      .flatMap(exist -> {
        if (exist) {
          return Mono.empty();
        } else {
          return createIndex();
        }
      })
      .doOnSuccess(aVoid -> log.info("success init index"))
      .doOnError(error -> log.error("error", error))
      .block();
  }

  @PostConstruct
  public void intiDocuments() {
    createDocuments()
      .flatMap(repository::save)
      .blockLast();
  }

  private Flux<Book> createDocuments() {
    Book book1 = new Book();
    book1.setId("1");
    book1.setTitle("AAA");
    book1.setContent("aaa bbb ccc ddd");
    Book book2 = new Book();
    book2.setId("2");
    book2.setTitle("BBB");
    book2.setContent("aaa bbb ccc ddd eee fff");
    return Flux.just(book1, book2);
  }

  private Mono<Void> createIndex() {
    Settings settings = Settings.builder()
      .put("index.number_of_shards", "2")
      .put("index.number_of_replicas", "2")
      .build();

    CreateIndexRequest request = new CreateIndexRequest(BOOK_INDEX)
      .settings(settings)
      .source(singletonMap("feature", "reactive-client"));

    return client.indices()
      .createIndex(request)
      .doOnNext(create -> log.info("create index: {}", create))
      .flatMapMany(create -> {
        if (create) {
          return createDocuments();
        }
        return Flux.empty();
      })
      .flatMap(repository::save)
      .then();
  }
}



